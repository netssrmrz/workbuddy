package rs.workbuddy;

public class Workbuddy_Activity 
extends 
android.app.Activity
implements 
 android.app.DatePickerDialog.OnDateSetListener,
 android.app.TimePickerDialog.OnTimeSetListener,
 android.content.DialogInterface.OnClickListener
{
	public static final int MENUITEM_VIEW_ACTIVITIES=2000;
	public static final int MENUITEM_TIMESHEET_REPORT_VIEW=4000;
	public static final int MENUITEM_SETTINGS=5000;
	public static final int MENUITEM_LOG_ACTIVITIES=1000;
	public static final int MENUITEM_VIEW_PROJECTS=3000;

	public android.view.MenuItem view_activities_menu;
	public android.view.MenuItem log_activities_menu;
	public android.view.MenuItem view_timesheet_menu;
	public android.view.MenuItem settings_menu;
	public android.view.MenuItem view_projects_menu;

	public static final int DLG_EVENT_DATE=1;
	public static final int DLG_EVENT_TIME=2;
	public static final int DLG_DEL_LAST=3;
	public static final int DLG_EVENT_DEL=4;

  public rs.workbuddy.Db db;
	public android.view.Menu options_menu;
	public boolean dlg_cancel;
	public android.app.Dialog curr_dlg;
	public boolean take_next_date_call, take_next_time_call;

	public void Update_UI()
	{
		try
		{
			this.db.Log("rs.workbuddy.Workbuddy_Activity.Update_UI()");
			On_Update_UI();
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	@Override
	public void onCreate(android.os.Bundle state)
	{
		super.onCreate(state);
		this.db = new Db(this);
	}

  @Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		this.options_menu = menu;

		this.view_activities_menu = menu.add(1, MENUITEM_VIEW_ACTIVITIES, MENUITEM_VIEW_ACTIVITIES, "Activities");
		this.log_activities_menu = menu.add(1, MENUITEM_LOG_ACTIVITIES, MENUITEM_LOG_ACTIVITIES, "Home");
		this.view_projects_menu=menu.add(1, MENUITEM_VIEW_PROJECTS, MENUITEM_VIEW_PROJECTS, "Projects");
		this.view_timesheet_menu = menu.add(1, MENUITEM_TIMESHEET_REPORT_VIEW, MENUITEM_TIMESHEET_REPORT_VIEW, "Timesheet Report");
		this.settings_menu = menu.add(1, MENUITEM_SETTINGS, MENUITEM_SETTINGS, "Settings");
		menu.add(1, -2, 99999, "Clear Data");
		menu.add(1, -3, 99999, "Export Data");
		menu.add(1, -1, 99999, "View Log");

		return true;
	}

  @Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=false;
		android.content.Intent intent;
		String csv_data;
		android.net.Uri rep_uri;

		try
		{
			if (item.getItemId() == MENUITEM_VIEW_ACTIVITIES)
			{
				this.startActivity(new android.content.Intent(this, Event_List.class));
				res = true;
	    }
			else if (item.getItemId() == MENUITEM_VIEW_PROJECTS)
			{
				this.startActivity(new android.content.Intent(this, Project_List.class));
				res = true;
			}
			else if (item.getItemId() == MENUITEM_TIMESHEET_REPORT_VIEW)
			{
				intent=new android.content.Intent(this, Report_Timesheet.class);
				startActivity(intent);

				res=true;
			}
			else if (item.getItemId() == -1) // view log
			{
				this.db.Show_Log();
				res = true;
			}
			else if (item.getItemId() == MENUITEM_SETTINGS)
			{
				intent = new android.content.Intent(this, Settings_Activity.class);
				this.startActivity(intent);
				res = true;
			}
			else if (item.getItemId() == MENUITEM_LOG_ACTIVITIES)
			{
				this.startActivity(new android.content.Intent(this, Main_Activity.class));
				res = true;
			}
			else if (item.getItemId() == -2)
			{
				this.db.Execute_SQL_No_Result("delete from work_event");
				this.Update_UI();
				res = true;
			}
			else if (item.getItemId()==-3)
			{
				csv_data=this.db.Dump_Table_To_CSV("Work_Event", "notes", String.class, "start_date", java.sql.Date.class);
				rep_uri=rs.android.Util.Save_File("Work_Event.csv", csv_data);

				intent=new android.content.Intent(android.content.Intent.ACTION_VIEW);
				intent.setType("text/csv");
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.putExtra(android.content.Intent.EXTRA_STREAM, rep_uri);
				startActivity(android.content.Intent.createChooser(intent, "Which application would you like to use?"));

				res=true;
			}
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
		return res;
	}

	public android.net.Uri Export_Data(rs.android.Db db) 
	throws java.io.IOException
	{
		String state, csv;
		java.sql.Date[] curr_week;
		java.sql.Date start_work, end_work, start_lunch, end_lunch;
		java.io.File sd_dir, csv_file;
		java.io.FileOutputStream csv_stream;
		Object[] work_ids, lunch_ids;
		Work_Event event;
		Long duration;
		Double work_duration, lunch_duration, total_work_duration,
			total_lunch_duration, work_duration_min, lunch_duration_min,
			total_work_duration_min, total_lunch_duration_min;
		android.net.Uri res=null;

		state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state))
		{
			sd_dir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS);
			sd_dir.mkdirs();

			csv = "date, start work, end work, work total hrs, work total min, start lunch, end lunch, lunch total hrs, lunch total min\r\n";
			curr_week = rs.android.Util.Week(null);
			if (rs.android.Util.NotEmpty(curr_week))
			{
				total_work_duration = (double)0;
				total_work_duration_min = (double)0;
				total_lunch_duration = (double)0;
				total_lunch_duration_min = (double)0;
				for (java.sql.Date day: curr_week)
				{
					start_work = null;
					end_work = null;
					work_duration = null;
					work_duration_min = null;
					start_lunch = null;
					end_lunch = null;
					lunch_duration = null;
					lunch_duration_min = null;

					csv = csv + rs.android.Util.To_String(day, null, "ccc dd/MM/yyyy");

					work_ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, day, Work_Event.EVENT_TYPE_WORK);
					if (rs.android.Util.NotEmpty(work_ids))
					{
						event = (Work_Event)this.db.SelectObj(Work_Event.class, (Integer)(work_ids[0]));
						if (event != null && event.start_date != null)
							start_work = event.start_date;

						event = (Work_Event)this.db.SelectObj(Work_Event.class, (Integer)(work_ids[work_ids.length - 1]));
						if (event != null && event.start_date != null)
							end_work = event.Get_Event_End(this.db);

						duration = Work_Event.Get_Events_Duration(this.db, work_ids);
						if (duration != null)
						{
							work_duration = (double)duration / (double)1000 / (double)60 / (double)60;
							total_work_duration += work_duration;
							work_duration_min = (double)duration / (double)1000 / (double)60;
							total_work_duration_min += work_duration_min;
						}
					}

					lunch_ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, day, Work_Event.EVENT_TYPE_LUNCH);
					if (rs.android.Util.NotEmpty(lunch_ids))
					{
						event = (Work_Event)this.db.SelectObj(Work_Event.class, (Integer)(lunch_ids[0]));
						if (event != null && event.start_date != null)
							start_lunch = event.start_date;

						event = (Work_Event)this.db.SelectObj(Work_Event.class, (Integer)(lunch_ids[lunch_ids.length - 1]));
						if (event != null && event.start_date != null)
							end_lunch = event.Get_Event_End(this.db);

						duration = Work_Event.Get_Events_Duration(this.db, lunch_ids);
						if (duration != null)
						{
							lunch_duration = (double)duration / (double)1000 / (double)60 / (double)60;
							total_lunch_duration += lunch_duration;
							lunch_duration_min = (double)duration / (double)1000 / (double)60;
							total_lunch_duration_min += lunch_duration_min;
						}
					}

					if (start_work != null)
						csv = csv + ", " + rs.android.Util.To_String(start_work, null, "h:mm:ss a");
					else
						csv = csv + ",";
					if (end_work != null)
						csv = csv + ", " + rs.android.Util.To_String(end_work, null, "h:mm:ss a");	
					else
						csv = csv + ",";
					if (work_duration != null)
					{
						csv = csv + ", " + rs.android.Util.To_String(work_duration, null, "0.##");
						csv = csv + ", " + rs.android.Util.To_String(work_duration_min, null, "0.##");
					}
					else
						csv = csv + ",,";

					if (start_lunch != null)
						csv = csv + ", " + rs.android.Util.To_String(start_lunch, null, "h:mm:ss a");
					else
						csv = csv + ",";
					if (end_lunch != null)
						csv = csv + ", " + rs.android.Util.To_String(end_lunch, null, "h:mm:ss a"); 
					else
						csv = csv + ",";
					if (lunch_duration != null)
					{
						csv = csv + ", " + rs.android.Util.To_String(lunch_duration, null, "0.##");
						csv = csv + ", " + rs.android.Util.To_String(lunch_duration_min, null, "0.##");
					}
					else
						csv = csv + ",,";

					csv += "\r\n";
				}
				csv += "totals,,," + 
					rs.android.Util.To_String(total_work_duration, null, "0.##") + "," + 
					rs.android.Util.To_String(total_work_duration_min, null, "0.##") + ",,," + 
					rs.android.Util.To_String(total_lunch_duration, null, "0.##") + "," +
					rs.android.Util.To_String(total_lunch_duration_min, null, "0.##") + "\r\n";
			}

			if (rs.android.Util.NotEmpty(csv))
			{
				csv_file = new java.io.File(sd_dir, "timesheet.csv");
				csv_stream = new java.io.FileOutputStream(csv_file);
				csv_stream.write(csv.getBytes());
				csv_stream.close();
				res = android.net.Uri.fromFile(csv_file);
			}
		}
		return res;
	}

	@Override
	public void onResume()
	{
		try
		{
			super.onResume();

			if (this.db == null)
			{
				this.db = new Db(this);
			}

			On_Resume();

			Update_UI();
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();

		OnPause();
		if (this.db != null)
		{
			this.db.Close();
			this.db = null;
		}
	}

	@Override
	public android.app.Dialog onCreateDialog(int id)
	{
		android.app.Dialog res=null;
		android.app.DatePickerDialog date_dlg;
		android.app.TimePickerDialog time_dlg;
		int year, month, day, hour, minute;
		java.sql.Date now;
		android.app.AlertDialog.Builder b;

		this.db.Log("rs.workbuddy.Workbuddy_Activity.onCreateDialog("+id+")");
		now = rs.android.Util.Now();
		if (id == DLG_EVENT_DATE)
		{
			year = rs.android.Util.Date_Get_Year(now);
			month = rs.android.Util.Date_Get_Month(now);
			day = rs.android.Util.Date_Get_Day(now);
			date_dlg = new android.app.DatePickerDialog(this, this, year, month, day);
			date_dlg.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Cancel", this);
			res = date_dlg;
		}
		else if (id == DLG_EVENT_TIME)
		{
			hour = rs.android.Util.Date_Get_Hour(now);
			minute = rs.android.Util.Date_Get_Minute(now);
			time_dlg = new android.app.TimePickerDialog(this, this, hour, minute, false);
			time_dlg.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Cancel", this);
			res = time_dlg;
		}
		else if (id == DLG_DEL_LAST)
		{
			b = new android.app.AlertDialog.Builder(this);
			b.setMessage("Are you sure you want to delete the last activity?");
			b.setTitle("Warning");
			b.setPositiveButton("OK", this);
			b.setNegativeButton("Cancel", this);
			res = b.create();
		}
		else if (id == DLG_EVENT_DEL)
		{
			b = new android.app.AlertDialog.Builder(this);
			b.setMessage("Are you sure?");
			b.setTitle("Warning");
			b.setPositiveButton("OK", this);
			b.setNegativeButton("Cancel", this);
			res = b.create();
		}
		return res;
	}

	public void onClick(android.content.DialogInterface dlg, int which)
	{
		this.db.Log("rs.workbuddy.Workbuddy_Activity.onClick()");
		this.dlg_cancel = false;
		if (which == android.content.DialogInterface.BUTTON_NEGATIVE)
			this.dlg_cancel = true;
		else if (which == android.content.DialogInterface.BUTTON_POSITIVE)
			OnClickPositive(dlg);
	}

	public void onDateSet(android.widget.DatePicker v, int year, int month, int day)
	{
		this.db.Log("rs.workbuddy.Workbuddy_Activity.onDateSet()");
		if (!this.dlg_cancel && this.take_next_date_call)
			OnDateSet(v, year, month, day);
		this.dlg_cancel = false;
		this.take_next_date_call=false;
	}

	public void onTimeSet(android.widget.TimePicker v, int hour, int minute)
	{
		this.db.Log("rs.workbuddy.Workbudy_Activity.onTimeSet()");
		if (!this.dlg_cancel && this.take_next_time_call)
			OnTimeSet(v, hour, minute);
		this.dlg_cancel = false;
		this.take_next_time_call=false;
	}

	@Override
	public void onPrepareDialog(int id, android.app.Dialog dialog)
	{
		this.db.Log("rs.workbuddy.Workbuddy_Activity.onPrepareDialog("+id+")");
		super.onPrepareDialog(id, dialog);
		this.curr_dlg = dialog;
		if (id==DLG_EVENT_DATE)
		  this.take_next_date_call=true;
		else if (id==DLG_EVENT_TIME)
		  this.take_next_time_call=true;
		OnPrepareDialog(id, dialog);
	}

	public void OnPrepareDialog(int id, android.app.Dialog dialog)
	{
    //rs.android.Util.Show_Note(this, "Workbuddy_Activity.OnPrepareDialog()");
	}
	
	public void OnTimeSet(android.widget.TimePicker v, int hour, int minute)
	{
    //rs.android.Util.Show_Note(this, "Workbuddy_Activity.OnTimeSet()");
	}
	
	public void OnDateSet(android.widget.DatePicker v, int year, int month, int day)
	{
		//rs.android.Util.Show_Note(this, "Workbuddy_Activity.OnDateSet()");
	}
	
	public void OnClickPositive(android.content.DialogInterface dlg)
	{
    //rs.android.Util.Show_Note(this, "Workbuddy_Activity.OnClickPositive()");
	}

	public void OnPause()
	{
    //rs.android.Util.Show_Note(this, "Workbuddy_Activity.OnPause()");
	}

	public void On_Resume()
	{
    //rs.android.Util.Show_Note(this, "Workbuddy_Activity.On_Resume()");
	}

	public void On_Update_UI()
	{
    //rs.android.Util.Show_Note(this, "Workbuddy_Activity.On_Update_UI()");
	}
}
