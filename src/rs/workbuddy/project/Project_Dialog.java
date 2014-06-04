package rs.workbuddy.project;
import android.content.*;

public class Project_Dialog
implements 
android.view.View.OnClickListener,
android.content.DialogInterface.OnClickListener
{
	public interface On_Project_Set_Listener
	{
		public void On_Project_Set(Long project_id);
	}
	
	public android.content.Context ctx;
	public boolean dlg_ok;
	public On_Project_Set_Listener on_project_set_listener;
	public rs.android.Db db;
  public android.app.AlertDialog dlg;
  //public rs.workbuddy.project.Project_ListView project_list_view;

	public Project_Dialog(android.content.Context ctx, 
	  On_Project_Set_Listener listener, rs.workbuddy.Db db)
	{
		this.ctx = ctx;
		this.dlg_ok = false;
		this.on_project_set_listener = listener;
		this.db=db;
	}

	public void Show()
	{
		android.app.AlertDialog.Builder builder;
    rs.workbuddy.project.Project_ListView project_list_view;
  
		project_list_view=new rs.workbuddy.project.Project_ListView(this.db, null, 
      this.ctx, this);
		
		builder = new android.app.AlertDialog.Builder(this.ctx);
		builder.setTitle("Select a Project");
		builder.setView(project_list_view);
		builder.setNegativeButton("Cancel", this);
		this.dlg=builder.show();
	}

  @Override
  public void onClick(DialogInterface p1, int p2)
  {
    this.dlg.dismiss();
  }
  
  @Override
  public void onClick(android.view.View label)
  {
    Long id;

    id=(Long)label.getTag();
    if (this.on_project_set_listener!=null)
      this.on_project_set_listener.On_Project_Set(id);
    this.dlg.dismiss();
  }
}
