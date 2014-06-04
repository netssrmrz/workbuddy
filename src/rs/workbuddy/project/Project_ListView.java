package rs.workbuddy.project;

public class Project_ListView
extends android.widget.ListView
{
	public Project_ListView(rs.android.Db db, Long id, android.content.Context ctx,
    android.view.View.OnClickListener label_click_listener)
	{
		super(ctx);
		
		rs.workbuddy.project.Project_ListAdapter adapter;
		
		adapter=new rs.workbuddy.project.Project_ListAdapter(db, id, 
      label_click_listener, false);
		this.setAdapter(adapter);
	}
}
