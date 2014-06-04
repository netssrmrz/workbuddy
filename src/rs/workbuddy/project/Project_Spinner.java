package rs.workbuddy.project;

public class Project_Spinner
extends android.widget.Spinner
implements android.view.View.OnClickListener
{
	public Project_Spinner(android.content.Context ctx, rs.android.Db db)
	{
		super(ctx);
		
		rs.workbuddy.project.Project_ListAdapter adapter;

		adapter = new rs.workbuddy.project.Project_ListAdapter(db, null, this, true);
		adapter.is_spinner=true;
		this.setAdapter(adapter);
	}

	public void Set_Selection(rs.android.Db db, Long id)
	{
		int idx;
		rs.workbuddy.project.Project_ListAdapter adapter;

		adapter = (rs.workbuddy.project.Project_ListAdapter)this.getAdapter();
		adapter.Set_Project(db, id, true);
		
		idx=adapter.Get_Item_Position(id);
		if (idx > 0)
		  this.setSelection(idx);
		else
		  this.setSelection(0);
	}

	public Long Get_Selected_Id()
	{
		long id;
		Long res=null;

		id = this.getSelectedItemId();
		if (id == rs.workbuddy.project.Project_ListAdapter.ID_NA)
			res = null;
		else
		  res = id;
		return res;
	}

	public void onClick(android.view.View widget)
	{
		Long id;
		rs.workbuddy.project.Project_ListAdapter adapter;
		int idx;

		adapter=(rs.workbuddy.project.Project_ListAdapter)this.getAdapter();
		id = (Long)widget.getTag();
		idx=adapter.Get_Item_Position(id);
		this.setSelection(idx);
		this.onDetachedFromWindow();
	}
}
