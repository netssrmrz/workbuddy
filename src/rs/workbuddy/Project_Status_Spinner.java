package rs.workbuddy;

public class Project_Status_Spinner
extends rs.android.ui.Db_Spinner
{
	public Project_Status_Spinner(android.content.Context ctx, rs.android.Db db)
	{
		super(ctx);
		rs.android.ui.Db_Adapter values;

		values = new rs.android.ui.Db_Adapter(db, "select id, name from Status_Type order by name asc");
		this.setAdapter(values);
	}
}
