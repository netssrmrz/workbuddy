package rs.workbuddy;
import android.content.*;

public class Array_Adapter 
extends android.widget.ArrayAdapter<String>
{
  public Array_Adapter(android.content.Context ctx)
	{
		super(ctx, android.R.layout.simple_spinner_item);
		this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}
	
	@Override
	public android.view.View getView (int position, android.view.View v, android.view.ViewGroup parent)
	{
		android.widget.TextView res=null;
		
		if (v!=null && v instanceof android.widget.TextView)
			res=(android.widget.TextView)v;
		else
		{
			res=(android.widget.TextView)super.getView(position, v, parent);
			res.setTextSize(20);
		}
		
		return res;
	}
}
