package rs.workbuddy;

public class Report_Timesheet
extends Workbuddy_Activity_List
{
  public class Timesheet_Entry
	{
		public java.sql.Date date;
		public Long work_duration;
		public Long lunch_duration;
	}

	public static final int MENUITEM_NEXT=700;
	public static final int MENUITEM_PREV=600;
	public static final int MENUITEM_TIMESHEET_REPORT_SEND=5000;
	public static final int MENUITEM_TIMESHEET_REPORT_EXPORT=6000;
	
	public android.view.MenuItem next_menu, prev_menu;
	public android.view.MenuItem send_timesheet_menu;
	public android.view.MenuItem export_timesheet_menu;
	public java.sql.Date week_of;
	
	public Report_Timesheet()
	{
		super();
		this.has_footer=true;
		this.has_title=true;
		this.week_of=rs.android.Util.Now();
	}
	
  @Override
	public void On_Build_Header_Row(android.widget.TableRow row)
	{
		Add_Header_Cell(row, "Date");
		Add_Header_Cell(row, "Work\nHrs (Min)");
		Add_Header_Cell(row, "Lunch\nHrs (Min)");
		Add_Header_Cell(row, Settings_Activity.Get_Currency_Symbol(this));
	}
	
	@Override	public void Add_Title_Row(android.view.ViewGroup parent)
	{
    android.widget.TextView title;
		String title_str=null;
		java.sql.Date[] week_days;
		
		week_days=rs.android.Util.Week(this.week_of);
		if (rs.android.Util.NotEmpty(week_days))
		{
			title_str=
			  rs.android.Util.To_String(week_days[0], "n/a", "MMMM")+": "+ 
			  "Week starting "+rs.android.Util.To_String(week_days[0], "n/a", "EEEE dd/MM/yyyy");
		}
		title=new android.widget.TextView(this);
		title.setText(title_str);
		title.setTextSize(20);
		title.setPadding(10, 10, 10, 10);
		parent.addView(title);
	}
	
	public long Get_Obj_Id(Object obj)
	{
		Timesheet_Entry e;

		e = (Timesheet_Entry)obj;
		return e.date.getTime();
	}

	@Override
	public void On_Build_Row(Object obj, android.widget.TableRow row)
	{
		Timesheet_Entry entry;
		Double work_duration_hrs=null, work_duration_min=null,
		  lunch_duration_hrs=null, lunch_duration_min=null;
		String work_duration_str="n/a", lunch_duration_str="n/a",
		  earned_str="n/a";

		entry = (Timesheet_Entry)obj;
		if (entry.work_duration != null)
		{
			work_duration_hrs = (double)entry.work_duration / (double)1000 / (double)60 / (double)60;
			work_duration_min = (double)entry.work_duration / (double)1000 / (double)60;
			work_duration_str = 
			  rs.android.Util.To_String(work_duration_hrs, "n/a", "#,##0.##")+" ("+ 
			  rs.android.Util.To_String(work_duration_min, "n/a", "#,##0.##")+")";
			earned_str=rs.android.Util.To_String(work_duration_hrs*Settings_Activity.Get_Hourly_Rate(this), "n/a", "#,##0.00");
		}
		if (entry.lunch_duration != null)
		{
			lunch_duration_hrs = (double)entry.lunch_duration / (double)1000 / (double)60 / (double)60;
			lunch_duration_min = (double)entry.lunch_duration / (double)1000 / (double)60;
			lunch_duration_str = 
			  rs.android.Util.To_String(lunch_duration_hrs, "n/a", "#,##0.##")+" ("+ 
			  rs.android.Util.To_String(lunch_duration_min, "n/a", "#,##0.##")+")";
		}

    Add_Cell(row, rs.android.Util.To_String(entry.date, "n/a", "EEEE"));
		Add_Cell(row, work_duration_str);
		Add_Cell(row, lunch_duration_str);
		Add_Cell(row, earned_str);
	}

	@Override
	public java.util.List<Object> On_Get_List()
	{
		java.sql.Date[] curr_week_days;
		java.util.ArrayList<Timesheet_Entry> entries=null;
		Timesheet_Entry entry;
		Long[] work_ids, lunch_ids;
		Work_Event event;

		curr_week_days = rs.android.Util.Week(this.week_of);
		if (rs.android.Util.NotEmpty(curr_week_days))
		{
			entries = new java.util.ArrayList<Timesheet_Entry>();
			for (java.sql.Date day: curr_week_days)
			{
				entry = new Timesheet_Entry();

				entry.date = day;

				work_ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, day, Work_Event.EVENT_TYPE_WORK);
				if (rs.android.Util.NotEmpty(work_ids))
				{
					/*event = (Work_Event)this.db.SelectObj(Work_Event.class, work_ids[0]);
					 if (event != null && event.start_date != null)
					 start_work = event.start_date;

					 event = (Work_Event)this.db.SelectObj(Work_Event.class, (Integer)(work_ids[work_ids.length - 1]));
					 if (event != null && event.start_date != null)
					 end_work = event.Get_Event_End(this.db);*/

					entry.work_duration = Work_Event.Get_Events_Duration(this.db, work_ids);
				}

				lunch_ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, day, Work_Event.EVENT_TYPE_LUNCH);
				if (rs.android.Util.NotEmpty(lunch_ids))
				{
					/*event = (Work_Event)this.db.SelectObj(Work_Event.class, (Integer)(lunch_ids[0]));
					 if (event != null && event.start_date != null)
					 start_lunch = event.start_date;

					 event = (Work_Event)this.db.SelectObj(Work_Event.class, (Integer)(lunch_ids[lunch_ids.length - 1]));
					 if (event != null && event.start_date != null)
					 end_lunch = event.Get_Event_End(this.db);*/

					entry.lunch_duration = Work_Event.Get_Events_Duration(this.db, lunch_ids);
				}

				entries.add(entry);
			}
		}
		return (java.util.ArrayList<Object>)entries;
	}
	
	public void On_Build_Footer_Row(android.widget.TableRow row)
	{
		int c;
		android.view.View table_row;
		Timesheet_Entry e;
		Long work_total=(long)0, lunch_total=(long)0;
		double total_hr, total_min;
		
		for (c=0; c<this.table_layout.getChildCount(); c++)
		{
			table_row=this.table_layout.getChildAt(c);
			e=(Timesheet_Entry)table_row.getTag();
			if (e!=null)
			{
				if (e.work_duration!=null)
				  work_total+=e.work_duration;
				if (e.lunch_duration!=null)
				  lunch_total+=e.lunch_duration;
			}
		}
		
    Add_Footer_Cell(row, ""); // no. col
		Add_Footer_Cell(row, "Totals"); // day
		
		total_hr=(double)work_total/(double)1000/(double)60/(double)60;
	  total_min=(double)work_total/(double)1000/(double)60;
	  Add_Footer_Cell(row, rs.android.Util.To_String(total_hr, "n/a", "#,##0.##")+
		  " ("+rs.android.Util.To_String(total_min, "n/a", "#,##0.##")+")");
		
		total_hr=(double)lunch_total/(double)1000/(double)60/(double)60;
	  total_min=(double)lunch_total/(double)1000/(double)60;
		Add_Footer_Cell(row, rs.android.Util.To_String(total_hr, "n/a", "#,##0.##")+
		  " ("+rs.android.Util.To_String(total_min, "n/a", "#,##0.##")+")");
			
		total_hr=(double)work_total/(double)1000/(double)60/(double)60*Settings_Activity.Get_Hourly_Rate(this);
		Add_Footer_Cell(row, rs.android.Util.To_String(total_hr, null, "#,##0.00"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		
		this.next_menu = menu.add(1, MENUITEM_NEXT, MENUITEM_NEXT, ">");
		this.prev_menu = menu.add(1, MENUITEM_PREV, MENUITEM_PREV, "<");
		this.export_timesheet_menu = menu.add(1, MENUITEM_TIMESHEET_REPORT_EXPORT, MENUITEM_TIMESHEET_REPORT_EXPORT, "Export Timesheet Report");
		this.send_timesheet_menu = menu.add(1, MENUITEM_TIMESHEET_REPORT_SEND, MENUITEM_TIMESHEET_REPORT_SEND, "Send Timesheet Report");
		
		this.next_menu.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		this.prev_menu.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
	
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=false;
		android.content.Intent i;

		super.onOptionsItemSelected(item);
		
		try
		{
			res = super.onOptionsItemSelected(item);
			if (!res)
			{
				if (item.getItemId() == MENUITEM_NEXT)
				{
					this.week_of=rs.android.Util.Add_Days(this.week_of, 7);
					this.refresh_data=true;
					this.Update_UI();
					res = true;
				}
				else if (item.getItemId() == MENUITEM_PREV)
				{
					this.week_of=rs.android.Util.Add_Days(this.week_of, -7);
					this.refresh_data=true;
					this.Update_UI();
					res = true;
				}
			}
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
		return res;
	}
}
