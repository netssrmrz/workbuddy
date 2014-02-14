package rs.android.ui;

public class Border_Drawable
extends android.graphics.drawable.Drawable
{
  public android.graphics.Paint top_paint;
	public android.graphics.Paint left_paint;
	public android.graphics.Paint bottom_paint;
	public android.graphics.Paint right_paint;
	public android.graphics.Paint bk_paint;

	//public int top_width, left_width, bottom_width, right_width;
	public android.graphics.Rect bounds_rect;
	public boolean top, left, bottom, right;
	public float top_padding, left_padding, bottom_padding, right_padding;
	public android.content.Context ctx;
	public int gap;

	public Border_Drawable()
	{
		this.top=true;
		this.right=true;
		this.bottom=true;
		this.left=true;

		this.top_paint = new android.graphics.Paint();
		this.top_paint.setColor(0xffffffff);
		this.top_paint.setStrokeWidth(1);

		this.left_paint = new android.graphics.Paint();
		this.left_paint.setColor(0xffffffff);
		this.left_paint.setStrokeWidth(1);

		this.bottom_paint = new android.graphics.Paint();
		this.bottom_paint.setColor(0xffffffff);
		this.bottom_paint.setStrokeWidth(1);

		this.right_paint = new android.graphics.Paint();
		this.right_paint.setColor(0xffffffff);
		this.right_paint.setStrokeWidth(1);

		this.bk_paint=new android.graphics.Paint();
		this.bk_paint.setColor(0xff000000);
		this.bk_paint.setStyle(android.graphics.Paint.Style.FILL);

		this.gap=0;
	}

	@Override
	public void onBoundsChange(android.graphics.Rect bounds)
	{
		if (bounds.left<0)
			bounds.left=0;
		this.bounds_rect = bounds;
	}

	public void draw(android.graphics.Canvas c)
	{
		c.drawPaint(this.bk_paint);
		if (this.top)
			c.drawLine(
				this.bounds_rect.left, this.bounds_rect.top+this.top_padding,
				this.bounds_rect.right, this.bounds_rect.top+this.top_padding,
				this.top_paint);
		if (this.right)
			c.drawLine(
				this.bounds_rect.right-1-this.right_padding, this.bounds_rect.top,
				this.bounds_rect.right-1-this.right_padding, this.bounds_rect.bottom,
				this.right_paint);
		if (this.bottom)
			c.drawLine(
				this.bounds_rect.right, this.bounds_rect.bottom-1-this.bottom_padding,
				this.bounds_rect.left, this.bounds_rect.bottom-1-this.bottom_padding,
				this.bottom_paint);
		if (this.left)
			c.drawLine(
				this.bounds_rect.left+this.left_padding, this.bounds_rect.bottom,
				this.bounds_rect.left+this.left_padding, this.bounds_rect.top,
				this.left_paint);
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

	public static void Add_Border_Top(android.view.View v, int colour, float width,
	  float top_padding)
	{
		Add_Border(v, width, 0, 0, 0, 
		  top_padding, 0, 0, 0, colour, null);
	}

	public static void Add_Border(android.view.View v, int colour)
	{
		Add_Border(v, 1, 1, 1, 1, 0, 0, 0, 0, colour, 0xff000000);
	}

	public static void Add_Border(android.view.View v, int colour, Integer bk_colour)
	{
		Add_Border(v, 1, 1, 1, 1, 0, 0, 0, 0, colour, bk_colour);
	}

	public static void Add_Border(
	  android.view.View v,
	  float top_width, float right_width, float bottom_width, float left_width, 
		float top_padding, float right_padding, float bottom_padding, float left_padding,
		int colour, Integer bk_paint)
	{
		Border_Drawable b;

		b=new Border_Drawable();
		if (top_width>0)
		{
			b.top_paint.setStrokeWidth(top_width);
			b.top=true;
			b.top_padding=top_padding;
		}
		else
		  b.top=false;

		if (right_width>0)
		{
			b.right_paint.setStrokeWidth(right_width);
			b.right=true;
			b.right_padding=right_padding;
		}
		else
		  b.right=false;

		if (bottom_width>0)
		{
			b.bottom_paint.setStrokeWidth(bottom_width);
			b.bottom=true;
			b.bottom_padding=bottom_padding;
		}
		else
		  b.bottom=false;

		if (left_width>0)
		{
			b.left_paint.setStrokeWidth(left_width);
			b.left=true;
			b.left_padding=left_padding;
		}
		else
		  b.left=false;

		b.Set_Color(colour, bk_paint);
		v.setBackgroundDrawable(b);
	}

	public void Set_Stroke_Width(float w)
	{
		this.bottom_paint.setStrokeWidth(w);
		this.right_paint.setStrokeWidth(w);
		this.top_paint.setStrokeWidth(w);
		this.left_paint.setStrokeWidth(w);
	}

	public void Set_Color(int c, Integer bk_paint)
	{
		this.top_paint.setColor(c);
		this.right_paint.setColor(c);
		this.bottom_paint.setColor(c);
		this.left_paint.setColor(c);
		if (bk_paint!=null)
		  this.bk_paint.setColor(bk_paint.intValue());
	}
}
