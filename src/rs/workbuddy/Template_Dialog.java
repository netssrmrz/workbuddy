package rs.workbuddy;

public class Template_Dialog
implements 
android.content.DialogInterface.OnClickListener
{
	public interface On_Template_Set_Listener
	{
		public void On_Template_Set(String filename);
	}
	
	public android.content.Context ctx;
	public boolean dlg_ok;
	public On_Template_Set_Listener on_template_set_listener;
	public String[] options;
	public String filename1, filename2;
	public String selected;

	public Template_Dialog(android.content.Context ctx, On_Template_Set_Listener listener)
	{
		this.ctx = ctx;
		this.dlg_ok = false;
		this.on_template_set_listener = listener;
	}

	public void Show()
	{
		android.app.AlertDialog.Builder builder;

    this.filename1=rs.workbuddy.Settings_Activity.Get_Timesheet_Filename(this.ctx);
    this.filename2=rs.workbuddy.Settings_Activity.Get_Timesheet_Filename2(this.ctx);
    
    this.options=new String[2];
    this.options[0]="Template 1: "+this.filename1;
    this.options[1]="Template 2: "+this.filename2;
    
		builder = new android.app.AlertDialog.Builder(this.ctx);
		builder.setTitle("Timesheet Templates");
		builder.setItems(this.options, this);
		builder.show();
	}

  public void onClick(android.content.DialogInterface dlg, int which)
  {
    if (this.on_template_set_listener != null)
    {
			if (which==0)
				this.selected=this.filename1;
			else
        this.selected=this.filename2;
      this.on_template_set_listener.On_Template_Set(this.selected);
    }
	}
}
