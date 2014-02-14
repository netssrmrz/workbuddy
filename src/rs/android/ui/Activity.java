package rs.android.ui;

public class Activity 
extends 
android.app.Activity
//implements 
//android.app.DatePickerDialog.OnDateSetListener,
//android.app.TimePickerDialog.OnTimeSetListener,
//android.content.DialogInterface.OnClickListener
{
	/*public static final int MENUITEM_VIEW_ACTIVITIES=2000;
	 public static final int MENUITEM_TIMESHEET_REPORT_VIEW=4000;
	 public static final int MENUITEM_SETTINGS=5000;
	 public static final int MENUITEM_LOG_ACTIVITIES=1000;
	 public static final int MENUITEM_VIEW_PROJECTS=3000;*/

	/*public android.view.MenuItem view_activities_menu;
	 public android.view.MenuItem log_activities_menu;
	 public android.view.MenuItem view_timesheet_menu;
	 public android.view.MenuItem settings_menu;
	 public android.view.MenuItem view_projects_menu;*/

	/*public static final int DLG_EVENT_DATE=1;
	 public static final int DLG_EVENT_TIME=2;
	 public static final int DLG_DEL_LAST=3;
	 public static final int DLG_EVENT_DEL=4;*/

	public rs.android.Db db;
	//public android.view.Menu options_menu;
	//public boolean dlg_cancel;
	//public android.app.Dialog curr_dlg;
	//public boolean take_next_date_call, take_next_time_call;

	@Override
	public void onCreate(android.os.Bundle state)
	{
		super.onCreate(state);
		this.db = On_Create_Db();
		On_Create_UI();
	}

	/*@Override
	 public boolean onCreateOptionsMenu(android.view.Menu menu) 
	 {
	 this.options_menu = menu;

	 this.view_activities_menu = menu.add(1, MENUITEM_VIEW_ACTIVITIES, MENUITEM_VIEW_ACTIVITIES, "Activities");
	 this.log_activities_menu = menu.add(1, MENUITEM_LOG_ACTIVITIES, MENUITEM_LOG_ACTIVITIES, "Home");
	 this.view_projects_menu = menu.add(1, MENUITEM_VIEW_PROJECTS, MENUITEM_VIEW_PROJECTS, "Projects");
	 this.view_timesheet_menu = menu.add(1, MENUITEM_TIMESHEET_REPORT_VIEW, MENUITEM_TIMESHEET_REPORT_VIEW, "Timesheet Report");
	 this.settings_menu = menu.add(1, MENUITEM_SETTINGS, MENUITEM_SETTINGS, "Settings");
	 menu.add(1, -2, 99999, "Clear Data");
	 menu.add(1, -3, 99999, "Export Data");
	 menu.add(1, -1, 99999, "View Log");

	 return true;
	 }*/

	/*@Override
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
	 intent = new android.content.Intent(this, Report_Timesheet.class);
	 startActivity(intent);

	 res = true;
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
	 else if (item.getItemId() == -3)
	 {
	 csv_data = this.db.Dump_Table_To_CSV("Work_Event", "notes", String.class, "start_date", java.sql.Date.class);
	 rep_uri = rs.android.Util.Save_File("Work_Event.csv", csv_data);

	 intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
	 intent.setType("text/csv");
	 intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	 intent.putExtra(android.content.Intent.EXTRA_STREAM, rep_uri);
	 startActivity(android.content.Intent.createChooser(intent, "Which application would you like to use?"));

	 res = true;
	 }
	 }
	 catch (Exception e)
	 {
	 rs.android.Util.Show_Error(this, e);
	 }
	 return res;
	 }*/

	@Override
	public void onResume()
	{
		try
		{
			super.onResume();

			if (this.db == null)
			{
				this.db = On_Create_Db();
			}

			On_Resume();

			On_Update_UI();
		}
		catch (Exception e)
		{
			rs.android.ui.Util.Show_Error(this, e);
		}
	}

	@Override
	public void onPause()
	{
		try
		{
			super.onPause();

			On_Pause();
			if (this.db != null)
			{
				this.db.Close();
				this.db = null;
			}
		}
		catch (Exception e)
		{
			rs.android.ui.Util.Show_Error(this, e);
		}
	}

	/*@Override
	 public android.app.Dialog onCreateDialog(int id)
	 {
	 android.app.Dialog res=null;
	 android.app.DatePickerDialog date_dlg;
	 android.app.TimePickerDialog time_dlg;
	 int year, month, day, hour, minute;
	 java.sql.Date now;
	 android.app.AlertDialog.Builder b;

	 this.db.Log("rs.workbuddy.Workbuddy_Activity.onCreateDialog(" + id + ")");
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
	 }*/

	/*public void onClick(android.content.DialogInterface dlg, int which)
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
	 this.take_next_date_call = false;
	 }

	 public void onTimeSet(android.widget.TimePicker v, int hour, int minute)
	 {
	 this.db.Log("rs.workbuddy.Workbudy_Activity.onTimeSet()");
	 if (!this.dlg_cancel && this.take_next_time_call)
	 OnTimeSet(v, hour, minute);
	 this.dlg_cancel = false;
	 this.take_next_time_call = false;
	 }

	 @Override
	 public void onPrepareDialog(int id, android.app.Dialog dialog)
	 {
	 this.db.Log("rs.workbuddy.Workbuddy_Activity.onPrepareDialog(" + id + ")");
	 super.onPrepareDialog(id, dialog);
	 this.curr_dlg = dialog;
	 if (id == DLG_EVENT_DATE)
	 this.take_next_date_call = true;
	 else if (id == DLG_EVENT_TIME)
	 this.take_next_time_call = true;
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
	 }*/

	public void On_Pause()
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

	public void On_Create_UI()
	{

	}

	public rs.android.Db On_Create_Db()
	{
		return null;
	}
}
