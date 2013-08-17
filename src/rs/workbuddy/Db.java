package rs.workbuddy;
import java.lang.reflect.*;

public class Db 
extends rs.android.Db
{
  Db(android.content.Context context)
  {
    OpenHelper open_helper;
    String[][] tables=
    {
      { "Work_Event", "CREATE TABLE work_event (" +
				"id INTEGER PRIMARY KEY, " +
				"event_type TEXT, " +
				"start_date INTEGER)" }
    };

    this.context=context;
    this.db_name="WorkBuddyDb";
    this.db_version=1;
    this.tables=tables;
    open_helper=new OpenHelper();
    if (open_helper!=null)
      this.conn=open_helper.getWritableDatabase();
  }

  public void Insert_Test_Data() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    java.sql.Date date;

		this.Log("Insert_Test_Data()");
		this.Execute_SQL_No_Result("delete from work_event");
    date=rs.android.Util.New_Date(2013, 7, 18, 9, 30, 0); Work_Event.Save_New(this, "work", date);
		date=rs.android.Util.New_Date(2013, 7, 18, 13, 30, 0); Work_Event.Save_New(this, "lunch", date);
		date=rs.android.Util.New_Date(2013, 7, 18, 14, 0, 0); Work_Event.Save_New(this, "work", date);
		date=rs.android.Util.New_Date(2013, 7, 18, 18, 0, 0); Work_Event.Save_New(this, "home", date);	
		date=rs.android.Util.New_Date(2013, 7, 19, 9, 30, 0); Work_Event.Save_New(this, "work", date);
		date=rs.android.Util.New_Date(2013, 7, 19, 13, 30, 0); Work_Event.Save_New(this, "lunch", date);
		date=rs.android.Util.New_Date(2013, 7, 19, 14, 0, 0); Work_Event.Save_New(this, "work", date);
		date=rs.android.Util.New_Date(2013, 7, 19, 18, 0, 0); Work_Event.Save_New(this, "home", date);	
    date=rs.android.Util.New_Date(2013, 7, 20, 9, 30, 0); Work_Event.Save_New(this, "work", date);
		date=rs.android.Util.New_Date(2013, 7, 20, 13, 30, 0); Work_Event.Save_New(this, "lunch", date);
		date=rs.android.Util.New_Date(2013, 7, 20, 14, 0, 0); Work_Event.Save_New(this, "work", date);
		date=rs.android.Util.New_Date(2013, 7, 20, 18, 0, 0); Work_Event.Save_New(this, "home", date);	
	}
}
