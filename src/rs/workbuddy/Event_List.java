package rs.workbuddy;

public class Event_List 
extends rs.workbuddy.Workbuddy_Activity_List
{
	public Event_List()
	{
		this.has_menuitem_add = true;
		this.has_menuitem_delete = true;
		this.has_menuitem_edit = true;

		edit_class = (java.lang.Class<android.app.Activity>)Event_Add.class;
		add_class = (java.lang.Class<android.app.Activity>)Event_Add.class;
	}

	@Override
	public void On_Build_Header_Row(android.widget.TableRow row)
	{
		Add_Header_Cell(row, "Start");
		Add_Header_Cell(row, "Activity");
		Add_Header_Cell(row, "Duration");
	}

	@Override
	public long Get_Obj_Id(Object obj)
	{
		Work_Event event;

		event = (Work_Event)obj;
		return event.id;
	}

	@Override
	public void On_Build_Row(Object obj, android.widget.TableRow row)
	{
		String event_start_date, event_duration;
		long duration;
		Work_Event event;

		event = (Work_Event)obj;

		event_start_date = rs.android.Util.To_String(event.start_date, "n/a", "EEE dd/MM/yyyy h:mm a");
		Add_Cell(row, event_start_date);

		Add_Cell(row, event.Get_Event_Description());

		duration = event.Get_Event_Duration(this.db);
		event_duration =
			rs.android.Util.To_String((double)duration / (double)1000 / (double)60 / (double)60, null, "#,##0.##") + "hr " +
			"(" + rs.android.Util.To_String((double)duration / (double)1000 / (double)60, null, "#,##0.##") + "min)";
		Add_Cell(row, event_duration);
	}

	@Override
	public java.util.List<Object> On_Get_List()
	{
		if (this.db == null)
			rs.android.Util.Show_Note(this, "no db!");
    return (java.util.List<Object>)Work_Event.Select_All(this.db);
	}

	@Override
	public void On_Delete(Long id)
	{
		Work_Event.Delete(this.db, id);
	}

	@Override
	public void Set_Row_Border(int c)
	{
		rs.workbuddy.Border_Drawable border;
		Long event_id;
		android.view.View row;
		Work_Event event, next_event=null;

		row = this.table_layout.getChildAt(c);
		event_id = (long)row.getId();
		event = (Work_Event)row.getTag();
		if (event != null)
		{
			if (c < this.table_layout.getChildCount() - 1)
				next_event = (Work_Event)this.table_layout.getChildAt(c + 1).getTag();

			if (this.selected.contains(event_id))
			{
				row.setBackgroundColor(0xff004400);				
			}
			else
			{
				border = new rs.workbuddy.Border_Drawable();
				border.top = false;
				border.right = false;
				border.left = false;

				if (next_event != null && rs.android.Util.Date_Get_Day_Of_Week(next_event.start_date) != rs.android.Util.Date_Get_Day_Of_Week(event.start_date))
				{
					border.bottom_paint.setColor(0xff44bb44);
				}
				else
					border.bottom_paint.setColor(0xff444444);

				row.setBackgroundDrawable(border);
			}
		}
	}
}
