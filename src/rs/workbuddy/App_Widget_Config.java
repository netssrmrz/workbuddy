package rs.workbuddy;

public class App_Widget_Config
extends rs.workbuddy.Workbuddy_Activity_Add
{
  public rs.workbuddy.db.App_Widget app_widget;
	public rs.workbuddy.Event_Type_Spinner type_spinner;
	public rs.workbuddy.Project_Spinner project_spinner;
	
  @Override
	public void onCreate(android.os.Bundle b)
	{
		android.content.Intent i;
		android.os.Bundle extras;

		this.app_widget=new rs.workbuddy.db.App_Widget();
		
		i = this.getIntent();
		extras=i.getExtras();
		this.app_widget.id=(long)extras.getInt(
		  android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID,
		  android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID);
			
		this.setResult(RESULT_CANCELED, null);
		
		this.type_spinner = new Event_Type_Spinner(this);
		this.Add_Field("Type", type_spinner);
		
		this.project_spinner=new Project_Spinner(this, this.db);
		this.Add_Field("Project", project_spinner);
	}
	
	public void Complete_Config()
	{
		android.content.Intent intent;
		
		intent=new android.content.Intent();
		intent.putExtra(
		  android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID,
		  this.app_widget.id);
		this.setResult(RESULT_OK, intent);
		this.finish();
	}
	
	/*@Override
	void On_New_Obj()
	{
		this.event = new Work_Event();
	}*/

	/*@Override
	void On_Load_Obj(Long id)
	{
		this.event = Work_Event.Select(this.db, id);
	}*/

	@Override
	void On_Save_Obj()
	{
		this.app_widget.event_type = this.type_spinner.Get_Selected_Id();

		this.app_widget.project_id = this.project_spinner.Get_Selected_Id();

		this.db.Save(this.app_widget);
		Complete_Config();
	}
}
