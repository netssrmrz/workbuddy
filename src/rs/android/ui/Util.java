package rs.android.ui;
import android.view.*;
import android.widget.*;

public class Util
{
  public float wr, hr;

  public Util(android.content.Context ctx)
  {
    android.util.DisplayMetrics dm;

    dm=ctx.getResources().getDisplayMetrics();
    this.hr=(float)dm.heightPixels/(float)100;
    this.wr=(float)dm.widthPixels/(float)100;
  }

  public float To_Canvas_X(float value)
  {
    float res=0;

    res=value*this.wr;
    return res;
  }

  public float To_Canvas_Y(float value)
  {
    float res=0;

    res=value*this.hr;
    return res;
  }

  public static void Add_Bottom_Border(android.content.Context ctx, android.view.View widget, boolean clear)
  {
    android.graphics.drawable.Drawable[] bk_contents;
    android.graphics.drawable.GradientDrawable bk_border;
    android.graphics.drawable.LayerDrawable bk;
    int c, w, h;

    if (widget.getBackground() instanceof android.graphics.drawable.LayerDrawable && !clear)
    {
      bk=(android.graphics.drawable.LayerDrawable)widget.getBackground();
      bk_contents=new android.graphics.drawable.Drawable[bk.getNumberOfLayers()+1];
      for (c=0; c<bk.getNumberOfLayers(); c++)
        bk_contents[c]=bk.getDrawable(c);
    }
    else
    {
      bk_contents=new android.graphics.drawable.Drawable[1];
    }

    w=widget.getWidth();
    h=widget.getHeight();
    bk_border=new android.graphics.drawable.GradientDrawable();
    bk_border.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
    bk_border.setStroke(2, android.graphics.Color.BLUE);
    bk_contents[bk_contents.length-1]=bk_border;

    bk=new android.graphics.drawable.LayerDrawable(bk_contents);
    bk.setBounds(20, 20, 30, 30);
    widget.setBackgroundDrawable(bk);
  }

  public static float Calc_Text_Size()
  {
    float res=0;

    return res;
  }

  public static int Calc_Padding_Size()
  {
    int res=0;

    return res;
  }

  public static boolean Is_Landscape_Mode(android.content.Context context)
  {
    boolean res=false;
    android.util.DisplayMetrics dm;

    dm=context.getResources().getDisplayMetrics();
    if (dm.widthPixels>dm.heightPixels)
      res=true;
    return res;
  }
	
	public static void Add_View(android.view.ViewGroup parent, android.view.View child, float weight)
	{
		android.widget.LinearLayout.LayoutParams params;
		
		params=new android.widget.LinearLayout.LayoutParams(
		  android.view.ViewGroup.LayoutParams.FILL_PARENT,
			android.view.ViewGroup.LayoutParams.FILL_PARENT,
			weight);
		parent.addView(child, params);
	}
	
	public static android.widget.TextView New_Label(android.content.Context ctx, String text, Integer width)
	{
		android.widget.TextView label;
		
		label = new android.widget.TextView(ctx);
		label.setText(text);
		label.setTextSize(20);
		if (width!=null)
		  label.setWidth(width);
		return label;
	}
	
	public static void Add_Views(android.view.ViewGroup layout, android.view.View[] views)
	{
		if (rs.android.Util.NotEmpty(views) && layout!=null)
		{
			for (android.view.View v: views)
			{
				layout.addView(v);
			}
		}
	}
}
