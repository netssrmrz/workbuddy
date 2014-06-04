package rs.android.ui;
import android.view.*;
import android.widget.*;
import android.graphics.drawable.*;

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
				layout.addView(v, new android.widget.LinearLayout.LayoutParams(
				  android.widget.LinearLayout.LayoutParams.FILL_PARENT, 0, 1f));
			}
		}
	}
	
	public static final float BRANCH_WIDTH=43;
	public static final float BRANCH_FRAME_WIDTH=11;
	public static final float BRANCH_PIC_FRAME_WIDTH=4; 
	public static final int BRANCH_PIC_COLOR=0xff55bbee;
	
	public static android.graphics.drawable.PictureDrawable Get_Test_Pic()
	{
		android.graphics.drawable.PictureDrawable drawable;
		android.graphics.Picture pic;
		android.graphics.Canvas c;
		android.graphics.Paint paint;

		paint=new android.graphics.Paint();
		paint.setColor(0xffff0000);
		paint.setStrokeWidth(4);
		paint.setStyle(android.graphics.Paint.Style.STROKE);

		pic=new android.graphics.Picture();
		c=pic.beginRecording((int)BRANCH_WIDTH, (int)BRANCH_WIDTH);
		
		c.drawRect(BRANCH_FRAME_WIDTH, BRANCH_FRAME_WIDTH, 
		  BRANCH_WIDTH-BRANCH_FRAME_WIDTH, BRANCH_WIDTH-BRANCH_FRAME_WIDTH, paint);

		pic.endRecording();
		drawable=new android.graphics.drawable.PictureDrawable(pic);

		return drawable;
	}
	
	public static android.graphics.drawable.PictureDrawable Get_Opened_Pic()
	{
		android.graphics.drawable.PictureDrawable pic_drawable;
		android.graphics.Picture pic;
		android.graphics.Canvas c;
		android.graphics.Paint frame_paint, pic_paint;
		
		frame_paint=new android.graphics.Paint();
		frame_paint.setColor(0xff333333);
		frame_paint.setStrokeWidth(1);
		frame_paint.setStyle(android.graphics.Paint.Style.STROKE);
		
		pic_paint=new android.graphics.Paint();
		pic_paint.setColor(BRANCH_PIC_COLOR);
		pic_paint.setStrokeWidth(4);
		pic_paint.setStyle(android.graphics.Paint.Style.STROKE);
		
		pic=new android.graphics.Picture();
		c=pic.beginRecording((int)BRANCH_WIDTH, (int)BRANCH_WIDTH);
		
		// border
		//c.drawRect(BRANCH_FRAME_WIDTH, BRANCH_FRAME_WIDTH, 
		  //BRANCH_WIDTH-BRANCH_FRAME_WIDTH, BRANCH_WIDTH-BRANCH_FRAME_WIDTH, frame_paint);
		
		// draw cross
		c.drawLine(BRANCH_FRAME_WIDTH+BRANCH_PIC_FRAME_WIDTH, BRANCH_WIDTH/2f, 
		  BRANCH_WIDTH-BRANCH_FRAME_WIDTH-BRANCH_PIC_FRAME_WIDTH+1f, BRANCH_WIDTH/2f, 
			pic_paint);
		c.drawLine(BRANCH_WIDTH/2f, BRANCH_FRAME_WIDTH+BRANCH_PIC_FRAME_WIDTH, 
		  BRANCH_WIDTH/2f, BRANCH_WIDTH-BRANCH_FRAME_WIDTH-BRANCH_PIC_FRAME_WIDTH+1, 
			pic_paint);
			
		pic.endRecording();
		pic_drawable=new android.graphics.drawable.PictureDrawable(pic);
		
		return pic_drawable;
	}
	
	public static android.graphics.drawable.PictureDrawable Get_Closed_Pic()
	{
		android.graphics.drawable.PictureDrawable pic_drawable;
		android.graphics.Picture pic;
		android.graphics.Canvas c;
		android.graphics.Paint frame_paint, pic_paint;

		frame_paint=new android.graphics.Paint();
		frame_paint.setColor(0xff333333);
		frame_paint.setStrokeWidth(1);
		frame_paint.setStyle(android.graphics.Paint.Style.STROKE);

		pic_paint=new android.graphics.Paint();
		pic_paint.setColor(BRANCH_PIC_COLOR);
		pic_paint.setStrokeWidth(4);
		pic_paint.setStyle(android.graphics.Paint.Style.STROKE);
		
		pic=new android.graphics.Picture();
		c=pic.beginRecording((int)BRANCH_WIDTH, (int)BRANCH_WIDTH);
		
		// border
		//c.drawRect(BRANCH_FRAME_WIDTH, BRANCH_FRAME_WIDTH, 
		  //BRANCH_WIDTH-BRANCH_FRAME_WIDTH, BRANCH_WIDTH-BRANCH_FRAME_WIDTH, frame_paint);
			
		// draw dash
		c.drawLine(BRANCH_FRAME_WIDTH+BRANCH_PIC_FRAME_WIDTH, BRANCH_WIDTH/2f, 
		  BRANCH_WIDTH-BRANCH_FRAME_WIDTH-BRANCH_PIC_FRAME_WIDTH+1, BRANCH_WIDTH/2f, 
			pic_paint);
			
		pic.endRecording();
		pic_drawable=new android.graphics.drawable.PictureDrawable(pic);

		return pic_drawable;
	}
	
	public static android.graphics.drawable.PictureDrawable Get_Pressed_Pic()
	{
		android.graphics.drawable.PictureDrawable pic_drawable;
		android.graphics.Picture pic;
		android.graphics.Canvas c;
		android.graphics.Paint frame_paint, pic_paint;

		frame_paint=new android.graphics.Paint();
		frame_paint.setColor(0xff333333);
		frame_paint.setStrokeWidth(1);
		frame_paint.setStyle(android.graphics.Paint.Style.STROKE);

		pic_paint=new android.graphics.Paint();
		pic_paint.setColor(BRANCH_PIC_COLOR);
		pic_paint.setStrokeWidth(4);
		pic_paint.setStyle(android.graphics.Paint.Style.STROKE);
		
		pic=new android.graphics.Picture();
		c=pic.beginRecording((int)BRANCH_WIDTH, (int)BRANCH_WIDTH);
		c.drawRect(BRANCH_FRAME_WIDTH, BRANCH_FRAME_WIDTH, BRANCH_WIDTH-BRANCH_FRAME_WIDTH, BRANCH_WIDTH-BRANCH_FRAME_WIDTH, frame_paint);
		pic.endRecording();
		pic_drawable=new android.graphics.drawable.PictureDrawable(pic);

		return pic_drawable;
	}
	
	public static android.graphics.PointF To_Canvas_Pt(android.graphics.RectF world_window, 
		android.graphics.RectF canvas_window, float x, float y)
  {
    android.graphics.PointF res=null;
    float cw, ww;

    res=new android.graphics.PointF();

    cw=canvas_window.right-canvas_window.left;
    ww=world_window.right-world_window.left;
    res.x=((x-world_window.left)*cw/ww)+canvas_window.left;

    cw=canvas_window.bottom-canvas_window.top;
    ww=world_window.top-world_window.bottom;
    res.y=canvas_window.bottom-((y-world_window.bottom)*cw/ww);

    return res;
  }
	
	@SuppressWarnings("unchecked")
  public static void SetWidgets(android.app.Activity activity, java.lang.Class<? extends Object> ids_class) 
  {
    int id;
    android.view.View widget;
    String id_name, tag;
    java.lang.reflect.Field activity_field;
    java.util.Collection<Object> list;

    for (java.lang.reflect.Field field: ids_class.getFields())
    {
      if (field.getType().equals(int.class))
      {
        try 
        {
          id=field.getInt(null);
          id_name=field.getName();
        }
        catch (java.lang.Exception e) 
        {
          id=0;
          id_name=null;
        }

        widget=activity.findViewById(id);
        if (widget!=null)
        {
          tag=(String)widget.getTag();
          if (rs.android.Util.NotEmpty(tag))
            id_name=tag;

          try {activity_field=activity.getClass().getField(id_name);}
          catch (java.lang.Exception e) {activity_field=null;}
          if (activity_field!=null)
          {
            if (rs.android.util.Type.IsGenericList(activity_field, widget.getClass()))
            {
              try {list=(java.util.Collection<Object>)activity_field.get(activity);}
              catch (java.lang.Exception e) {list=null;}
              if (list==null)
              {
                try {list=(java.util.Collection<Object>)activity_field.getType().newInstance();}
                catch (java.lang.Exception e) {list=null;}
                if (list!=null)
                {
                  try {activity_field.set(activity, list);}
                  catch (java.lang.Exception e) {e.printStackTrace();}
                }
              }
              if (list!=null)
                list.add(widget);
            }
            else
            {
              try {activity_field.set(activity, widget);} 
              catch (java.lang.Exception e) {e.printStackTrace();}
            }
          }

          if (activity instanceof android.view.View.OnClickListener)
            if (!(widget instanceof android.widget.ListView))
              widget.setOnClickListener((android.view.View.OnClickListener)activity);
          widget.setOnCreateContextMenuListener(activity);
        }
      }
    }
  }
	
	public static void Show_Obj(android.content.Context ctx, Object obj)
	{
		String obj_msg;

		obj_msg=rs.android.util.Type.Objs_To_String(obj);
		Show_Message(ctx, obj_msg);
	}

	public static void Show_Message(android.content.Context ctx, String msg)
	{
		android.app.AlertDialog dlg;

		//android.widget.Toast.makeText(ctx, msg, android.widget.Toast.LENGTH_LONG).show();
		dlg = new android.app.AlertDialog.Builder(ctx).create();
		dlg.setTitle("Message");
		dlg.setMessage(msg);
		dlg.show();
	}

	public static void Show_Stack(android.content.Context ctx)
	{
		StackTraceElement[] elems;
		String msg;

		elems=Thread.currentThread().getStackTrace();
		if (rs.android.Util.NotEmpty(elems))
		{
			msg="";
			for (StackTraceElement elem: elems)
			{
				msg+=elem.toString()+"\n";
			}
			Show_Message(ctx, msg);
		}
	}

	public static void Show_Note(android.content.Context ctx, String msg)
	{
		android.widget.Toast.makeText(ctx, msg, android.widget.Toast.LENGTH_LONG).show();
	}

	public static void Show_Error(android.content.Context ctx, Exception e)
	{
		java.io.ByteArrayOutputStream buffer;
		java.io.PrintStream stream;

		buffer=new java.io.ByteArrayOutputStream();
		stream=new java.io.PrintStream(buffer);
		e.printStackTrace(stream);

		rs.android.ui.Util.Show_Message(ctx, buffer.toString());
	}
}
