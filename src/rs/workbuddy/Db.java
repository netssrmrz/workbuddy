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
    db_name="WorkBuddyDb";
    this.db_version=25;
    this.tables = new Table[5];
		
		t=new Table();
		t.name="Work_Event";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_NONE;
		t.create_sql=
		  "CREATE TABLE Work_Event (" +
				"id INTEGER PRIMARY KEY, " +
				"event_type_id INTEGER, " +
				"start_date INTEGER, "+
				"project_id INTEGER, "+
				"notes TEXT"+
				")";
		this.tables[0]=t;
		
		t=new Table();
		t.name="Project";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_NONE;
		t.create_sql=
		  "CREATE TABLE Project (" +
			"id INTEGER PRIMARY KEY, " +
			"name TEXT, " +
			"notes TEXT, " +
			"status_type_id INTEGER, "+
			"parent_id INTEGER)";
		this.tables[1]=t;
		
		t=new Table();
		t.name="Status_Type";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_NONE;
		t.create_sql=
		  "CREATE TABLE Status_Type ("+
			"id INTEGER PRIMARY KEY, "+
			"name TEXT, "+
			"display_home INTEGER, "+
			"colour INTEGER)";
		t.init_sqls=new String[5];
    t.init_sqls[0]="insert into Status_Type (name, display_home) values ('Pending', 0)";
    t.init_sqls[1]="insert into Status_Type (name, display_home) values ('In Progress', 1)";
    t.init_sqls[2]="insert into Status_Type (name, display_home) values ('Completed', 0)";
    t.init_sqls[3]="insert into Status_Type (name, display_home) values ('On Hold', 0)";
    t.init_sqls[4]="insert into Status_Type (name, display_home) values ('Cancelled', 0)";
		this.tables[2]=t;
		
		t=new Table();
		t.name="Event_Type";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_NONE;
		t.create_sql=
		  "CREATE TABLE Event_Type ("+
			"id INTEGER PRIMARY KEY, "+
			"display_home_projects INTEGER, "+
			"name TEXT, "+
			"colour INTEGER)";
		t.init_sqls=new String[3];
    t.init_sqls[0]="insert into Event_Type (name, display_home_projects, colour, template_id) values ('Work', 1, 4294901760)";
    t.init_sqls[1]="insert into Event_Type (name, display_home_projects, colour, template_id) values ('Break', 0, 4294967040)";
    t.init_sqls[2]="insert into Event_Type (name, display_home_projects, colour, temppate_id) values ('Home', 0, 4278255360)";
		this.tables[3]=t;
		
		t=new Table();
		t.name="Log";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_CREATE;
		t.create_sql=
		  "CREATE TABLE Log (" +
			"id INTEGER PRIMARY KEY, " +
			"obj_class TEXT, " +
			"obj_id INTEGER, " +
			"obj_data TEXT, "+
			"operation INTEGER, "+
			"log_date INTEGER)";
		this.tables[4]=t;

		// dropped tables ===========================================================
		/*t=new Table();
		t.name="Log";
		t.update_type=rs.android.Db.Table.UPDATE_TYPE_DROP;
		t.create_sql=
		  "CREATE TABLE Log (" +
			"id INTEGER PRIMARY KEY, " +
			"log_date INTEGER, " +
			"msg TEXT)";
		this.tables[4]=t;*/

    open_helper=new OpenHelper();
    if (open_helper!=null)
      this.conn=open_helper.getWritableDatabase();
  }
}
