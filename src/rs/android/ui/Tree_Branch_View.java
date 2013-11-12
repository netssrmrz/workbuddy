package rs.android.ui;

public class Tree_Branch_View
extends android.widget.CheckBox
{
  public int level;
	public boolean has_children;
	
  public Tree_Branch_View(android.content.Context ctx)
	{
		super(ctx);
	}

	public void onDraw(android.graphics.Canvas canvas) 
	{
		android.util.Log.d("rs.android.ui.Tree_Branch_View.onDraw()", "Entry");
    if (this.has_children)
			super.onDraw(canvas);
	}

	/*public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		android.util.Log.d("rs.android.ui.Tree_Branch_View.onSizeChanged()", "Entry");
	  //Set_Size(w, h);
	}*/
	
	@Override
	public void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
	{
		int w=40, h=50;
		
		w=w*(this.level+1);
	  this.setMeasuredDimension (w, h);
	}
}
