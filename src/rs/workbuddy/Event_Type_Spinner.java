package rs.workbuddy;
import android.content.*;

public class Event_Type_Spinner
extends android.widget.Spinner
{
  public Event_Type_Spinner(android.content.Context ctx)
	{
		super(ctx);
		Array_Adapter types;
		
		types = new Array_Adapter(ctx);
		types.add("n/a");
		types.add("Work");
		types.add("Lunch");
		types.add("Home");
		this.setAdapter(types);
	}
	
	public void Set_Selection(Long event_type)
	{
		this.setSelection(0);
		if (rs.android.Util.NotEmpty(event_type))
		{
			if (event_type.equals(Work_Event.EVENT_TYPE_WORK))
				this.setSelection(1);
			else if (event_type.equals(Work_Event.EVENT_TYPE_LUNCH))
				this.setSelection(2);
			else if (event_type.equals(Work_Event.EVENT_TYPE_HOME))
				this.setSelection(3);
			else
				this.setSelection(0);
		}
	}
	
	public Long Get_Selected_Id()
	{
		int pos;
		Long event_type=null;

		pos = this.getSelectedItemPosition();
		if (pos == 1)
			event_type = Work_Event.EVENT_TYPE_WORK;
		else if (pos == 2)
			event_type = Work_Event.EVENT_TYPE_LUNCH;
		else if (pos == 3)
			event_type = Work_Event.EVENT_TYPE_HOME;
		return event_type;
	}
}
