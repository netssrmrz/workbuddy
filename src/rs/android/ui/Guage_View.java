package rs.android.ui;
import java.util.*;

public class Guage_View 
extends android.view.View 
{	
	public android.graphics.LinearGradient bk_gradient_2;
	public android.graphics.RadialGradient frame_gradient;
	public android.graphics.SweepGradient bk_gradient_1;
	public android.graphics.Paint face_paint, night_paint; 
	public android.graphics.Paint day_paint;
	public float cx, cy;
	public float r, inner_r;
	public android.graphics.RectF inner_bounds;
	public int width, height;
	public java.util.ArrayList<Bar> bars;
	public boolean draw;
	public rs.android.Db db;

	public Guage_View(android.content.Context context)
	{
		super(context);
		this.draw = false;
	}

	public void Init(int w, int h)
	{
		android.graphics.RadialGradient rad_grad;
		int[] cols=
		{
			android.graphics.Color.GRAY, 
			android.graphics.Color.LTGRAY, 
			android.graphics.Color.GRAY, 
			android.graphics.Color.DKGRAY, 

			android.graphics.Color.GRAY, 
			android.graphics.Color.LTGRAY, 
			android.graphics.Color.GRAY, 
			android.graphics.Color.DKGRAY, 

			android.graphics.Color.GRAY
		};
		float[] pos={(float)0.0, (float)0.125, (float)0.25, (float)0.375, (float)0.5, 
			(float)0.625, (float)0.75, (float)0.875, (float)1.0};
		android.graphics.RectF bounds;

		if (w > 0 && h > 0)
		{
			bounds = new android.graphics.RectF();
			bounds.top = this.getPaddingTop();
			bounds.left = this.getPaddingLeft();
			bounds.bottom = h - this.getPaddingBottom();
			bounds.right = w - this.getPaddingRight();

			this.width = (int)(bounds.right - bounds.left);
			this.height = (int)(bounds.bottom - bounds.top);

			this.cx = (bounds.left + bounds.right) / (float)2;
			this.cy = (bounds.top + bounds.bottom) / (float)2;

			if (this.width < this.height)
				this.r = (float)this.width / 2f;
			else
				this.r = (float)this.height / 2f;

			this.inner_r = this.r * (float)0.75;

			this.bk_gradient_2 = new android.graphics.LinearGradient(
				0, this.cy - this.r, 
				0, this.cy + this.r, 
				android.graphics.Color.BLACK, 
				android.graphics.Color.GRAY, 
				android.graphics.Shader.TileMode.CLAMP);
			this.bk_gradient_1 = new android.graphics.SweepGradient(
				this.cx, 
				this.cy, 
				cols,
				pos
			);
			this.face_paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG);
			this.face_paint.setShader(bk_gradient_1);

			rad_grad = new android.graphics.RadialGradient(
				this.cx, this.cy, this.r, 
				android.graphics.Color.rgb(0, 0, 255), 
				android.graphics.Color.rgb(0, 0, 0), 
				android.graphics.Shader.TileMode.CLAMP);
			this.night_paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG);
			this.night_paint.setColor(android.graphics.Color.BLACK);
			this.night_paint.setStrokeWidth(this.r * (float)0.30);
			this.night_paint.setStyle(android.graphics.Paint.Style.STROKE);
			this.night_paint.setShader(rad_grad);

			rad_grad = new android.graphics.RadialGradient(
				this.cx, this.cy, this.r, 0xff8888ff, 0xff4444ff, 
				android.graphics.Shader.TileMode.CLAMP);
			this.day_paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG);
			this.day_paint.setColor(android.graphics.Color.BLACK);
			this.day_paint.setStrokeWidth(this.r * (float)0.30);
			this.day_paint.setStyle(android.graphics.Paint.Style.STROKE);
			this.day_paint.setShader(rad_grad);

			this.inner_bounds = new android.graphics.RectF();
			this.inner_bounds.top = this.cy - this.inner_r;
			this.inner_bounds.left = this.cx - this.inner_r;
			this.inner_bounds.bottom = this.cy + this.inner_r;
			this.inner_bounds.right = this.cx + this.inner_r;

			if (rs.android.Util.NotEmpty(this.bars))
				for (Bar b: this.bars)
				{
					b.Init(this.inner_bounds);
				}

			this.draw = true;
		}
	}

	public void onDraw(android.graphics.Canvas canvas) 
	{
		if (this.draw)
		{
			// gauge face
			canvas.drawCircle(this.cx, this.cy, this.r, this.face_paint);

			// gauge interior
			canvas.drawArc(this.inner_bounds, 0, 180, false, this.night_paint);
			canvas.drawArc(this.inner_bounds, 180, 180, false, this.day_paint);

			if (rs.android.Util.NotEmpty(this.bars))
			{
				for (Bar bar: this.bars)
				{
					bar.Draw(canvas);
				}
			}
		}
	}

	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		//db.Log("rs.android.ui.Gauge_View.onSizeChanged()");
		try
		{
			Init(w, h);
		}
		catch (Exception e)
		{
			rs.android.ui.Util.Show_Error(this.getContext(), e);
		}
	}

	public void Add_Bar(Bar b)
	{
		if (b != null)
		{
			if (this.bars == null)
				this.bars = new java.util.ArrayList<Bar>();
			this.bars.add(b);
			if (this.inner_bounds != null)
				b.Init(this.inner_bounds);
			this.invalidate();
		}
	}
}
