package rs.workbuddy;

public class Event_Type_Spinner
extends rs.android.ui.Db_Spinner
{
  public Event_Type_Spinner(android.content.Context ctx, rs.android.Db db)
	{
		super(ctx);
		rs.android.ui.Db_Adapter types;
		
		types=new rs.android.ui.Db_Adapter(db, null, "select id, name from Event_Type order by name asc");
		this.setAdapter(types);
	}
}
