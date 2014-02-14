package rs.workbuddy;
import android.graphics.*;

public class Button_State_Drawable
extends android.graphics.drawable.Drawable
{
  public android.graphics.Paint border_paint;
	public android.graphics.Paint border_focused_paint;
	public android.graphics.Paint border_pressed_paint;
	public android.graphics.Paint border_default_paint;
	public android.graphics.Paint border_corner_paint;
	public android.graphics.Paint border_corner_surface_paint;

	public android.graphics.Paint inner_paint;

	public android.graphics.RectF border_rect;
	public android.graphics.RectF inner_rect;
	public int border_radius;
	public int state;
	public android.content.Context ctx;

	public Button_State_Drawable()
	{
		this.border_radius = 20;
		this.state = 0;

		this.border_rect = new android.graphics.RectF();
		this.inner_rect = new android.graphics.RectF();

		this.border_corner_paint = new android.graphics.Paint();
		this.border_corner_surface_paint = new android.graphics.Paint();
		this.border_default_paint = new android.graphics.Paint();
		//this.border_default_paint.setColor(0xffff0000);
		this.border_focused_paint = new android.graphics.Paint();
		this.border_focused_paint.setColor(0xff00ff00);
		this.border_pressed_paint = new android.graphics.Paint();
		this.border_pressed_paint.setColor(0xff0000ff);
		this.border_paint = this.border_default_paint;
		//this.border_paint=new android.graphics.Paint();
		//this.border_paint.setColor(0xffff0000);

		this.inner_paint = new android.graphics.Paint();
		this.inner_paint.setColor(0xff44ff44);
	}

	@Override
	public boolean isStateful()
	{
		return true;
	}

	public static boolean Has_State(int state, int[] states)
	{
		boolean res=false;

		if (states != null && states.length > 0)
			for (int s: states)
			{
				if (s == state)
				{
					res = true;
					break;
				}
			}
		return res;
	}

	@Override
	public boolean onStateChange(int[] state)
	{
		boolean res=false;

		if (Has_State(android.R.attr.state_pressed, state) && 
				this.state != android.R.attr.state_pressed)
		{
			this.border_paint = this.border_pressed_paint;
			this.state = android.R.attr.state_pressed;
			this.invalidateSelf();
		}
		else if (Has_State(android.R.attr.state_focused, state))
		{
			this.border_paint = this.border_focused_paint;
			this.state = android.R.attr.state_focused;
			this.invalidateSelf();
		}
		else
		{
			this.border_paint = this.border_default_paint;
			this.state = 0;
			this.invalidateSelf();
		}

		return res;
	}

	@Override
	public void onBoundsChange(android.graphics.Rect bounds)
	{
		android.graphics.Rect padding;
		int[] cols;
		float[] cols_pos;
		android.graphics.SweepGradient g;
		android.graphics.RadialGradient rg;
		android.graphics.ComposeShader cs;
		float cx, cy, w, h;
		double br1, br2, br3, bl1, bl2, bl3, 
		  tl1, tl2, tl3, tr1, tr2, tr3, w2, h2;

		try
		{
			this.border_rect.bottom = bounds.bottom;
			this.border_rect.top = bounds.top;
			this.border_rect.left = bounds.left;
			this.border_rect.right = bounds.right;

			cx = this.border_rect.left + this.border_radius;
			cy = this.border_rect.top + this.border_radius;
			//w=this.border_rect.right-this.border_rect.left;
			//h=this.border_rect.bottom-this.border_rect.top;
			//w2=w/(double)2;
			//h2=h/(double)2;

			// white, gray
			cols = new int[7];
			cols[0] = 0xff888888; // dark grey
			cols[1] = 0xff000000; // black
			cols[2] = 0xff888888; // dark grey
			cols[3] = 0xffcccccc; // light grey
			cols[4] = 0xffffffff; // white
			cols[5] = 0xffcccccc; // light grey
			cols[6] = 0xff888888; // dark grey

			cols_pos = new float[7];
			cols_pos[0] = (float)0;
			cols_pos[1] = (float)0.125;
			cols_pos[2] = (float)0.25;
			cols_pos[3] = (float)0.5;
			cols_pos[4] = (float)0.625;
			cols_pos[5] = (float)0.75;
			cols_pos[6] = (float)1;

			g = new android.graphics.SweepGradient(cx, cy, cols, cols_pos);
			rg = new android.graphics.RadialGradient(cx, cy, this.border_radius, 0xffaaaaaa, 0x00aaaaaa, android.graphics.Shader.TileMode.CLAMP);
			cs = new android.graphics.ComposeShader(g, rg, android.graphics.PorterDuff.Mode.SRC_OVER);
			this.border_corner_paint.setShader(cs);
			this.border_corner_paint.setDither(true);
			this.border_corner_paint.setAntiAlias(true);

			this.inner_rect.bottom = this.border_rect.bottom - 10;
			this.inner_rect.top = this.border_rect.top + 10;
			this.inner_rect.left = this.border_rect.left + 10;
			this.inner_rect.right = this.border_rect.right - 10;

			padding = new android.graphics.Rect();
			if (this.getPadding(padding))
			{
				this.border_rect.bottom -= padding.bottom;
				this.border_rect.top += padding.top;
				this.border_rect.left += padding.left;
				this.border_rect.right -= padding.right;
			}
		}
		catch (Exception e)
		{
			rs.android.ui.Util.Show_Error(this.ctx, e);
		}
	}

	public void draw(android.graphics.Canvas c)
	{
		float cx, cy;

		// draw top left corner
		cx = this.border_rect.left + this.border_radius;
		cy = this.border_rect.top + this.border_radius;
		c.drawCircle(cx, cy, this.border_radius, this.border_corner_paint);

		// draw top right corner
		cx = this.border_rect.right - this.border_radius;
		cy = this.border_rect.top + this.border_radius;
		c.drawCircle(cx, cy, this.border_radius, this.border_corner_paint);

		// draw bottom right corner
		// draw bottom left corner
		// draw top
		// draw right
		// draw bottom
		// draw left
		// draw surface
		//c.drawRoundRect(this.border_rect, this.border_radius, this.border_radius, this.border_paint);
		//c.drawRoundRect(this.inner_rect, this.border_radius, this.border_radius, this.inner_paint);
	}

	public void setAlpha(int a)
	{
		// TODO: Implement this method
	}

	public void setColorFilter(android.graphics.ColorFilter cf)
	{
		// TODO: Implement this method
	}

	public int getOpacity()
	{
		// TODO: Implement this method
		return 0;
	}
}
