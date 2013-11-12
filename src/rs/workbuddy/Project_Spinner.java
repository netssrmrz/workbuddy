package rs.workbuddy;

public class Project_Spinner
extends rs.android.ui.Db_Spinner
{
	public Project_Spinner(android.content.Context ctx, rs.android.Db db)
	{
		super(ctx);
		rs.android.ui.Db_Adapter projects;

		projects = new rs.android.ui.Db_Adapter(db, "select id, name from project order by name asc");
		this.setAdapter(projects);
	}
}
