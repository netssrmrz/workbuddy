package rs.workbuddy;

public class Project_Spinner
extends android.widget.Spinner
{
	public Project_Spinner(
	  android.content.Context ctx,
		rs.android.Db db)
	{
		super(ctx);
		rs.android.ui.Db_Adapter projects;

		projects = new rs.android.ui.Db_Adapter(db, "select id, name from project order by name asc");
		this.setAdapter(projects);
	}

	public void Set_Selection(Long project_id)
	{
		this.setSelection(0);
		if (project_id != null)
		{
			this.setSelection(
			  ((rs.android.ui.Db_Adapter)this.getAdapter()).
				Get_Item_Position(project_id));
		}
	}

	public Long Get_Selected_Id()
	{
		long project_id;
		Long res=null;

		project_id = this.getSelectedItemId();
		if (project_id == rs.android.ui.Db_Adapter.ID_NA)
			res = null;
		else
		  res = project_id;
		return res;
	}
}
