package rs.workbuddy;

public class Event_List 
extends rs.workbuddy.Workbuddy_Activity_List
{
	public java.text.SimpleDateFormat date_formatter, time_formatter, short_time_formatter;
	public java.text.DecimalFormat num_formatter;
	public java.sql.Date week_of, max_day, min_day;
		
	public Event_List()
	{
		this.date_formatter=new java.text.SimpleDateFormat("EEE dd/MM/yyyy");
		this.short_time_formatter=new java.text.SimpleDateFormat("h:mm a");	
		this.time_formatter=new java.text.SimpleDateFormat("h:mm:ss a");
		this.num_formatter=new java.text.DecimalFormat("#,##0.##");
		
		this.has_paging=true;
		this.has_menuitem_delete = true;
		this.menuitem_edit_class = Event_Add.class;
		this.menuitem_view_class = Event_View.class;
		this.menuitem_add_class = Event_Add.class;
		this.list_obj_class=Work_Event.class;

		this.Add_Column("date", "Date");
		this.Add_Column("time", "Time");
		this.Add_Column("full_time", "Full Time");
		this.Add_Column("activity", "Activity");
		this.Add_Column("hrs", "Hrs");
		this.Add_Column("mins", "Mins");
    this.Add_Column("project", "Project", true);
		this.Add_Column("proj_or_note", "Project / Notes", true);
	}

	@Override
	public void onCreate(android.os.Bundle state)
	{
		super.onCreate(state);
		
		this.min_day=(java.sql.Date)this.db.Select_Value(java.sql.Date.class, "select start_date from Work_Event order by start_date asc");
		this.max_day=(java.sql.Date)this.db.Select_Value(java.sql.Date.class, "select start_date from Work_Event order by start_date desc");
    this.week_of=this.max_day;
		this.Set_Title();
	}
	
	public void Set_Title()
	{
		java.sql.Date[] week_days;
		
		if (this.week_of==null)
		  this.week_of = rs.android.Util.Now();
		week_days = rs.android.Util.Week(this.week_of);
		if (rs.android.Util.NotEmpty(week_days))
		{
			this.title =
			  rs.android.Util.To_String(week_days[0], "n/a", "MMMM") + ": " + 
			  "Week starting " + rs.android.Util.To_String(week_days[0], "n/a", "EEEE dd/MM/yyyy");
		}
	}
	
	@Override
	public android.view.View On_Get_Col_View(Object obj, String col_id)
	{
		android.view.View res=null;
		double dur_hr, dur_min;
		Work_Event event;

		event = (Work_Event)obj;

		if (col_id.equals("date") && event.start_date!=null)
		  res = this.New_Cell(this.date_formatter.format(event.start_date));

		else if (col_id.equals("time") && event.start_date!=null)
		  res = this.New_Cell(this.short_time_formatter.format(event.start_date));

		else if (col_id.equals("full_time") && event.start_date!=null)
		  res = this.New_Cell(this.time_formatter.format(event.start_date));

		else if (col_id.equals("activity"))
		  res = this.New_Cell(event.Get_Type_Name(this.db));

		else if (col_id.equals("hrs"))
		{
			dur_hr = event.Get_Event_Duration_Hr(this.db);
			res = this.New_Cell(this.num_formatter.format(dur_hr));
		}

		else if (col_id.equals("mins"))
		{
			dur_min = event.Get_Event_Duration_Min(this.db);
			res = this.New_Cell(this.num_formatter.format(dur_min));
		}

		else if (col_id.equals("project"))
		  res=this.New_Cell(event.Get_Project_Name(this.db));
			
		else if (col_id.equals("proj_or_note"))
		{
			res=this.New_Cell_Lines(event.Get_Project_Name(this.db), event.notes);
		}

		return res;
	}

	@Override
	public Object On_Get_Obj(Long id)
	{
		return Work_Event.Select(this.db, id);
	}

	@Override
	public Long[] On_Get_List()
	{
		Long[] res=null;
		java.sql.Date week[];
		String order_by=null;
		
		week=rs.android.Util.Week(this.week_of);
    res = Work_Event.Select_Timespan_Events(this.db, week[0], rs.android.Util.Add_Days(week[6], 1), null, null, order_by);
		return res;
	}

	@Override
	public void On_Delete()
	{
		if (rs.android.Util.NotEmpty(this.selected))
		{
			for (Long id: this.selected)
			{
				Work_Event.Delete(this.db, id);
			}
			this.refresh_data = true;
			this.Update_UI();
		}
	}

	@Override
	public void On_Update_Row(int c)
	{
		rs.android.ui.Border_Drawable border;
		Long id;
		android.view.View row;
		Work_Event event, next_event=null;

		id = this.Get_Row_Id(c);
		if (id != null)
		{
			event = rs.workbuddy.Work_Event.Select(this.db, id);
			if (c < this.table_layout.getChildCount() - 1)
			{
				id = this.Get_Row_Id(c + 1);
				next_event = rs.workbuddy.Work_Event.Select(this.db, id);
			}

			if (next_event != null && rs.android.Util.Date_Get_Day_Of_Week(next_event.start_date) != rs.android.Util.Date_Get_Day_Of_Week(event.start_date))
			{
				border = new rs.android.ui.Border_Drawable();
				border.top = false;
				border.right = false;
				border.left = false;
				border.bottom_paint.setColor(0xff008800);
				
				row = this.table_layout.getChildAt(c);
				row.setBackgroundDrawable(border);
			}
		}
	}

	@Override
	public void On_Next()
	{
	  this.Set_Page(7);
	}

	@Override
	public void On_Prev()
	{
		this.Set_Page(-7);
	}
	
	public void Set_Page(int date_diff)
	{
		java.sql.Date next_week;

		next_week = rs.android.Util.Add_Days(this.week_of, date_diff);
		if ((date_diff>0 && !next_week.after(this.max_day)) || 
		  (date_diff<0 && !next_week.before(this.min_day)))
		{
			this.week_of=next_week;
			this.Set_Title();
		  this.refresh_data = true;
		  this.Update_UI();
		}
	}
}
