package rs.android.ui;
import android.content.*;
import android.graphics.*;

public class Bar
{
	public double value;
	public float start_angle;
	public float size_angle;
	//public int[] colours;
	//public float[] colour_pos;
	public android.graphics.Paint paint;
	public float width;
	public int colour;
	public android.graphics.RectF bounds;
	public android.content.Context ctx;

	public Bar(android.content.Context ctx)
	{
		this.ctx=ctx;
	}

	public void Init(android.graphics.RectF bounds)
	{
		this.bounds=bounds;
		this.width=(this.bounds.right-this.bounds.left)*0.1f;
		
		this.paint=new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG|android.graphics.Paint.DITHER_FLAG);
		this.paint.setColor(this.colour);
		this.paint.setStrokeWidth(this.width);
		this.paint.setStyle(android.graphics.Paint.Style.STROKE);
		this.paint.setMaskFilter(new android.graphics.BlurMaskFilter(20, android.graphics.BlurMaskFilter.Blur.SOLID));
		this.Prep_New_Shaders();
	}

	public void Draw(android.graphics.Canvas c)
	{
		if (this.bounds==null)
			rs.android.ui.Util.Show_Note(this.ctx, "bounds is null");
		else
		if (this.size_angle>0)
			c.drawArc(this.bounds, start_angle+90, size_angle, false, this.paint);
	}

	public void Prep_New_Shaders()
	{
		float cx, cy, start_angle;
		float[] colour_pos;
		int[] colours;
		int r, g, b;

		cx=(this.bounds.right+this.bounds.left)/2f;
		cy=(this.bounds.bottom+this.bounds.top)/2f;
		start_angle=this.start_angle+90;
		
		r=(int)((float)android.graphics.Color.red(this.colour)/2f);
		b=(int)((float)android.graphics.Color.blue(this.colour)/2f);
		g=(int)((float)android.graphics.Color.green(this.colour)/2f);
		colours=new int[2];
		colours[1]=this.colour;
		colours[0]=android.graphics.Color.rgb(r, g, b);
		
		colour_pos=new float[2];
		colour_pos[0]=start_angle/360f;
		colour_pos[1]=(start_angle+this.size_angle)/360f;
		
		this.paint.setShader(new android.graphics.SweepGradient(cx, cy, colours, colour_pos));
	}
}
