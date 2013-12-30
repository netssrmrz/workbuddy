package rs.workbuddy;

public class Project_Spinner
extends android.widget.Spinner
implements
android.view.View.OnClickListener
{
	public rs.android.Db db;

	public Project_Spinner(android.content.Context ctx, rs.android.Db db)
	{
		super(ctx);
		rs.workbuddy.Project_Adapter projects;

		this.db = db;
		projects = new rs.workbuddy.Project_Adapter(db);
		projects.on_project_clicked_listener = this;
		projects.on_project_selected_listener = this;
		this.setAdapter(projects);
	}

	public void Set_Selection(rs.android.Db db, Long id)
	{
		rs.workbuddy.Project_Adapter projects;

		projects = (rs.workbuddy.Project_Adapter)this.getAdapter();
		projects.Set_Projects(id, false);
		this.Set_Selection(id);
	}

	public void Set_Selection(Long id)
	{
		int idx;
		rs.workbuddy.Project_Adapter projects;

		this.setSelection(0);
		projects = (rs.workbuddy.Project_Adapter)this.getAdapter();
		if (id != null && rs.android.Util.NotEmpty(projects.ids))
		{
			idx = projects.ids.indexOf(id);
			if (idx >= 0)
			  this.setSelection(idx);
		}
	}

	public Long Get_Selected_Id()
	{
		long id;
		Long res=null;

		id = this.getSelectedItemId();
		if (id == rs.workbuddy.Project_Adapter.ID_NA)
			res = null;
		else
		  res = id;
		return res;
	}

	public void onClick(android.view.View widget)
	{
		Integer idx;
		Long sel_id;

		if (widget instanceof android.widget.CheckBox)
		{
			sel_id = this.getSelectedItemId();
			this.setAdapter(this.getAdapter());
			this.Set_Selection(sel_id);
		}
		else if (widget instanceof android.widget.TextView)
		{
			idx = (Integer)widget.getTag();
			this.setSelection(idx);
			this.onDetachedFromWindow();
		}
	}
	
	public Project_Adapter Get_Adapter()
	{
		return (Project_Adapter)this.getAdapter();
	}
}
