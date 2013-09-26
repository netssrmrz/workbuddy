package rs.workbuddy;

public class Project_Add
extends 
rs.workbuddy.Workbuddy_Activity_Add
{
	public android.widget.EditText name_text;
	public android.widget.EditText notes_text;
	public android.widget.EditText rate_text;
	public Project project;

  @Override
  public void On_Create_UI(android.view.ViewGroup main_view)
	{
		// project name
		name_text = new android.widget.EditText(this);
		this.Add_Field("Name", name_text);

		// project rate
		rate_text = new android.widget.EditText(this);
		this.Add_Field("Rate", rate_text);

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

		this.rate_text.setText(null);
		if (rs.android.Util.NotEmpty(project.rate))
			this.rate_text.setText(rs.android.Util.To_String(project.rate));
	}

	@Override
	void On_New_Obj()
	{
		this.project = new Project();
	}

	@Override
	void On_Load_Obj(Long id)
	{
		this.project = (Project)this.db.SelectObj(Project.class, id);
	}

	@Override
	void On_Save_Obj()
	{
		project.notes = Get_Text(this.notes_text);
		project.name = Get_Text(this.name_text);
		project.rate = rs.android.Util.ToDouble(Get_Text(this.rate_text));
		this.db.Save(project);
	}
}
