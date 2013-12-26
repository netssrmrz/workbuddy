package rs.workbuddy;

public class Project_Add
extends 
rs.workbuddy.Workbuddy_Activity_Add
{
	public android.widget.EditText name_text;
	public android.widget.EditText notes_text;
	public rs.workbuddy.Project_Spinner parent_spinner;
	public rs.workbuddy.Project_Status_Spinner status_spinner;
	public Project project;

  @Override
  public void On_Create_UI(android.view.ViewGroup main_view)
	{
		// project name
		name_text = new android.widget.EditText(this);
		this.Add_Field("Name", name_text);

		parent_spinner = new rs.workbuddy.Project_Spinner(this, this.db);
		((Project_Adapter)this.parent_spinner.getAdapter()).view_text_size=20;
		this.Add_Field("Parent", parent_spinner);
		
		status_spinner=new rs.workbuddy.Project_Status_Spinner(this, this.db);
		this.Add_Field("Status", status_spinner);

		// project notes
		notes_text = new android.widget.EditText(this);
		this.Add_Field("Notes", notes_text);
  }

	@Override
	public void On_Update_UI()
	{
    this.notes_text.setText(null);
		if (rs.android.Util.NotEmpty(project.notes))
			this.notes_text.setText(project.notes);

		this.name_text.setText(null);
		if (rs.android.Util.NotEmpty(project.name))
			this.name_text.setText(project.name);

		this.parent_spinner.Set_Selection(this.db, project.parent_id);
		
		this.status_spinner.Set_Selection(project.status_type_id);
	}

	@Override
	void On_New_Obj()
	{
		this.project = new Project();
	}

	@Override
	void On_Load_Obj(Long id)
	{
		this.project = Project.Select(this.db, id);
	}

	@Override
	void On_Save_Obj()
	{
		project.notes = Get_Text(this.notes_text);
		project.name = Get_Text(this.name_text);
		project.parent_id=this.parent_spinner.Get_Selected_Id();
		project.status_type_id = this.status_spinner.Get_Selected_Id();
		this.db.Save(project);
	}
}
