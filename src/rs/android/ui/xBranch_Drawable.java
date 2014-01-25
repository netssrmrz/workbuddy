package rs.android.ui;

public class xBranch_Drawable
extends android.graphics.drawable.StateListDrawable
{
	android.graphics.Paint paint;
	
	public xBranch_Drawable()
	{
		int[] state;
		
		state=new int[1];
		
		state[0]=android.R.attr.state_checked;
		this.addState(state, this.Get_Checked_Pic());
	}
	
	public android.graphics.drawable.PictureDrawable Get_Checked_Pic()
	{
		android.graphics.drawable.PictureDrawable pic_draw;
		android.graphics.Picture pic;
		android.graphics.Canvas c;
		
		pic=new android.graphics.Picture();
		c=pic.beginRecording(20, 20);
		c.drawLine(0, 10, 20, 10, this.paint);
		pic.endRecording();
		
		pic_draw=new android.graphics.drawable.PictureDrawable(pic);
		return pic_draw;
	}
}
