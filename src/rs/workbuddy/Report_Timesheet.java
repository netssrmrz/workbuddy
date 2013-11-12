package rs.workbuddy;

public class Report_Timesheet
extends Workbuddy_Activity_List
{
  public class Timesheet_Entry
	{
		public java.sql.Date date;
		public Long[] durations;
	}

	public java.sql.Date week_of;

	public Report_Timesheet()
	{
		java.sql.Date[] week_days;

		this.week_of = rs.android.Util.Now();
		week_days = rs.android.Util.Week(this.week_of);
		if (rs.android.Util.NotEmpty(week_days))
		{
			this.title =
			  rs.android.Util.To_String(week_days[0], "n/a", "MMMM") + ": " + 
			  "Week starting " + rs.android.Util.To_String(week_days[0], "n/a", "EEEE dd/MM/yyyy");
		}

		this.has_footer = true;
		this.has_col_select = false;
	}

  @Override
	public void On_Build_Header_Row(android.widget.TableRow row)
	{
		Long[] ids;
		String type_name;

		row.addView(New_Header_Cell("Date"));
		ids = rs.workbuddy.db.Event_Type.Select_Ids(this.db);
		if (rs.android.Util.NotEmpty(ids))
		{
			for (Long id: ids)
			{
				type_name=rs.workbuddy.db.Event_Type.Get_Name(this.db, id);
				
				row.addView(New_Header_Cell(type_name+"\n(Hours)"));
				row.addView(New_Header_Cell(type_name+"\n(Minutes)"));
			}
		}
	}

	@Override
	public void On_Build_Footer_Row(android.widget.TableRow row)
	{
		Long[] ids, week_event_ids;
		Long total_dur;
		String total_dur_str;
		java.sql.Date week_start, week_end;
		Double tot_dur_hr, tot_dur_min;

		row.addView(New_Header_Cell(" "));
		ids = rs.workbuddy.db.Event_Type.Select_Ids(this.db);
		if (rs.android.Util.NotEmpty(ids))
		{
			week_start=rs.android.Util.Week_First_Day(this.week_of);
			week_end=rs.android.Util.Add_Days(week_start, 7);
			for (Long id: ids)
			{
				week_event_ids=Work_Event.Select_Timespan_Events(this.db, week_start, week_end, id);
				total_dur=Work_Event.Get_Events_Duration(this.db, week_event_ids);
				if (total_dur!=null)
				{
					tot_dur_min = (double)total_dur / (double)1000 / (double)60;
					tot_dur_hr = tot_dur_min / (double)60;
				}
				else
				{
					tot_dur_min=null;
				  tot_dur_hr=null;
				}
				total_dur_str=rs.android.Util.To_String(tot_dur_hr, "n/a", "#,##0.##");
				row.addView(New_Header_Cell(total_dur_str));

				total_dur_str=rs.android.Util.To_String(tot_dur_min, "n/a", "#,##0.##");
				row.addView(New_Header_Cell(total_dur_str));
			}
		}
	}

	@Override
	public void On_Build_Row(Long day, android.widget.TableRow row)
	{
		Long[] ids, day_event_ids;
		java.sql.Date date;
		Long dur;
		Double dur_hr, dur_min;

		date = new java.sql.Date(day);
		row.addView(New_Cell(rs.android.Util.To_String(date, "n/a", "EEEE")));

		ids = rs.workbuddy.db.Event_Type.Select_Ids(this.db);
		if (rs.android.Util.NotEmpty(ids))
		{
			for (Long id: ids)
			{
				day_event_ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, date, id);
				dur = Work_Event.Get_Events_Duration(this.db, day_event_ids);
				if (dur != null)
				{
					dur_min = (double)dur / (double)1000 / (double)60;
					dur_hr = dur_min / (double)60;
				}
				else
				{
					dur_min=null;
				  dur_hr = null;
				}
				
				row.addView(New_Cell(rs.android.Util.To_String(dur_hr, "n/a", "#,##0.##")));
				row.addView(New_Cell(rs.android.Util.To_String(dur_min, "n/a", "#,##0.##")));
			}
		}
  }

	@Override
	public Long[] On_Get_List()
	{
		return rs.android.Util.Week_In_Millis(this.week_of);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		super.onCreateOptionsMenu(menu);

		menu.findItem(Menus.MENUITEM_NEXT).setVisible(true);
		menu.findItem(Menus.MENUITEM_PREV).setVisible(true);
		menu.findItem(Menus.MENUITEM_TIMESHEET_SEND).setVisible(true);
		menu.findItem(Menus.MENUITEM_TIMESHEET_VIEW).setVisible(true);

		return true;
	}

	@Override
	public void On_Next()
	{
		this.week_of = rs.android.Util.Add_Days(this.week_of, 7);
		this.refresh_data = true;
		this.Update_UI();
	}

	@Override
	public void On_Prev()
	{
		this.week_of = rs.android.Util.Add_Days(this.week_of, -7);
		this.refresh_data = true;
		this.Update_UI();
	}
	
  @Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=true;

		if (item.getItemId() == Menus.MENUITEM_TIMESHEET_SEND)
			On_Timesheet_Send();
		else if (item.getItemId()==Menus.MENUITEM_TIMESHEET_VIEW)
		  On_Timesheet_View();
		else
			res=rs.workbuddy.Menus.Options_Item_Selected(item, this);

		return res;
	}
	
	public void On_Timesheet_Send()
	{
		
	}
	
	public void On_Timesheet_View()
	{
		
	}
}
