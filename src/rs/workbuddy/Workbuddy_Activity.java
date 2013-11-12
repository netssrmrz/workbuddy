package rs.workbuddy;

public class Workbuddy_Activity 
extends 
android.app.Activity
implements android.content.DialogInterface.OnClickListener
{
  public rs.workbuddy.Db db;
	public android.view.Menu menu;

	public void Update_UI()
	{
		On_Update_UI();
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
		//android.util.Log.d("rs.workbuddy.Workbuddy_Activity.onCreateOptiinsMenu()", "Entry");
		this.menu=menu;
		rs.workbuddy.Menus.Create_Options_Menu(menu);
		return true;
	}

  @Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=true;
		android.app.AlertDialog.Builder b;
		android.app.AlertDialog dlg;
		
		if (item.getItemId() == Menus.MENUITEM_EDIT)
			On_Edit();
		else if (item.getItemId()==Menus.MENUITEM_DONE)
		  On_Done();
		else if (item.getItemId()==Menus.MENUITEM_ADD)
		  On_Add();
		else if (item.getItemId()==Menus.MENUITEM_NEXT)
		  On_Next();
		else if (item.getItemId()==Menus.MENUITEM_PREV)
		  On_Prev();
		else if (item.getItemId()==Menus.MENUITEM_VIEW)
		  On_View();
		else if (item.getItemId()==Menus.MENUITEM_DELETE)
		{
			b = new android.app.AlertDialog.Builder(this);
			b.setMessage("Are you sure?");
			b.setTitle("Warning");
			b.setPositiveButton("OK", this);
			b.setNegativeButton("Cancel", this);
			dlg = b.create();
			dlg.show();
		}
		else
			res=rs.workbuddy.Menus.Options_Item_Selected(item, this);
			
		return res;
	}

	/*public android.net.Uri Export_Data(rs.android.Db db) 
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
	 }*/

	@Override
	public void onResume()
	{
		super.onResume();

		if (this.db == null)
		{
			this.db = new Db(this);
		}

		On_Resume();

		Update_UI();
	}

	@Override
	public void onPause()
	{
		super.onPause();

		On_Pause();
		if (this.db != null)
		{
			this.db.Close();
			this.db = null;
		}
	}

	public void onClick(android.content.DialogInterface dlg, int which)
	{
		if (which == android.content.DialogInterface.BUTTON_POSITIVE)
			On_Delete();
	}

	public void On_Delete()
	{
		
	}
	
	public void On_Done()
	{
		
	}
	
	public void On_Edit()
	{
		
	}
	
	public void On_Add()
	{
		
	}
	
	public void On_Next()
	{
		
	}
	
	public void On_Prev()
	{
		
	}
	
	public void On_Pause()
	{
    
	}

	public void On_Resume()
	{
    
	}

	public void On_Update_UI()
	{
    
	}
	
	public void On_View()
	{
		
	}
}
