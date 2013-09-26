package rs.workbuddy;
import java.lang.reflect.*;

public class Db 
extends rs.android.Db
{
  Db(android.content.Context context)
  {
    OpenHelper open_helper;
		Table t;

    this.context=context;
    this.db_name="WorkBuddyDb";
    this.db_version=12;
    this.tables = new Table[4];
		
		t=new Table();
		t.name="Work_Event";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_NONE;
		t.create_sql=
		  "CREATE TABLE Work_Event (" +
				"id INTEGER PRIMARY KEY, " +
				"event_type INTEGER, " +
				"start_date INTEGER, "+
				"project_id INTEGER, "+
				"notes TEXT"+
				")";
		this.tables[2]=t;
				
		t=new Table();
		t.name="Log";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_NONE;
		t.create_sql=
		  "CREATE TABLE Log (" +
			"id INTEGER PRIMARY KEY, " +
			"log_date INTEGER, " +
			"msg TEXT)";
		this.tables[0]=t;
		
		t=new Table();
		t.name="Project";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_NONE;
		t.create_sql=
		  "CREATE TABLE Project (" +
			"id INTEGER PRIMARY KEY, " +
			"name TEXT, " +
			"notes TEXT, " +
			// location
			// employer
			"rate NUMERIC)";
		this.tables[1]=t;
		
		t=new Table();
		t.name="App_Widget";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_CREATE;
		t.create_sql=
		  "CREATE TABLE App_Widget (" +
			"id INTEGER PRIMARY KEY, " +
			"event_type INTEGER, " +
			"project_id INTEGER "+
			")";
		this.tables[3]=t;
		
		// lat, long
		// event type
		// project id
		// range
		// delay
			
    open_helper=new OpenHelper();
    if (open_helper!=null)
      this.conn=open_helper.getWritableDatabase();
  }
}
