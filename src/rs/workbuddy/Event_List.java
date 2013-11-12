package rs.workbuddy;

public class Event_List 
extends rs.workbuddy.Workbuddy_Activity_List
{
	public Event_List()
	{
		this.has_menuitem_delete = true;
		this.menuitem_edit_class = Event_Add.class;
		this.menuitem_view_class = Event_View.class;
		this.menuitem_add_class = Event_Add.class;
	}

	@Override
	public void On_Build_Header_Row(android.widget.TableRow row)
	{
		row.addView(New_Header_Cell("Date"));
		row.addView(New_Header_Cell("Time"));
		row.addView(New_Header_Cell("Activity"));
		row.addView(New_Header_Cell("Hrs"));
		row.addView(New_Header_Cell("Mins"));
		row.addView(New_Header_Cell("Project"));
	}

	@Override
	public void On_Build_Row(Long id, android.widget.TableRow row)
	{
		String event_date, event_duration;
		double dur_hr, dur_min;
		Work_Event event;

		event = Work_Event.Select(this.db, id);

		event_date = rs.android.Util.To_String(event.start_date, "n/a", "EEE dd/MM/yyyy");
		row.addView(New_Cell(event_date));
		
		event_date=rs.android.Util.To_String(event.start_date, "n/a", "h:mm a");
		row.addView(New_Cell(event_date));

		row.addView(New_Cell(event.Get_Type_Name(this.db)));

		dur_hr = event.Get_Event_Duration_Hr(this.db);
		dur_min=event.Get_Event_Duration_Min(this.db);
		event_duration = rs.android.Util.To_String(dur_hr, null, "#,##0.##");
		row.addView(New_Cell(event_duration));
		
		event_duration=rs.android.Util.To_String(dur_min, null, "#,##0.##");
		row.addView(New_Cell(event_duration));
		
		row.addView(New_Cell(event.Get_Project_Name(this.db)));
	}

	@Override
	public Long[] On_Get_List()
	{
    return Work_Event.Select_Ids(this.db);
	}

	@Override
	public void On_Delete(Long id)
	{
		Work_Event.Delete(this.db, id);
	}

	/*@Override
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
	}*/
}
