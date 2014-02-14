package rs.workbuddy;

public class Event_Add
extends rs.workbuddy.Workbuddy_Activity_Add
{
	public rs.workbuddy.Event_Type_Spinner type_spinner;
	public rs.android.ui.Date_Button start_date_button;
	public rs.android.ui.Time_Button start_time_button;
	public rs.workbuddy.Project_Spinner project_spinner;
	public android.widget.EditText notes_text;
	public Work_Event event;

  @Override
  public void On_Create_UI(android.view.ViewGroup main_view)
	{
		this.finish=true;

		// event type
		this.type_spinner = new Event_Type_Spinner(this, this.db);
		this.Add_Field("Type", type_spinner);

		// event start date
		start_date_button = new rs.android.ui.Date_Button(this);
		this.Add_Field("Start Date", start_date_button);

		// event start time
		start_time_button = new rs.android.ui.Time_Button(this);
		this.Add_Field("Start Time", start_time_button);

		this.project_spinner=new Project_Spinner(this, this.db);
		//((Project_Adapter)this.project_spinner.getAdapter()).view_text_size=25;
		this.Add_Field("Project", project_spinner);

		// event notes
		notes_text = new android.widget.EditText(this);
		this.Add_Field("Notes", notes_text);
  }

	@Override
	public void On_Update_UI()
	{
		this.type_spinner.Set_Selection(this.event.event_type_id);

		this.start_date_button.Set_Date(this.event.start_date);

		this.start_time_button.Set_Time(this.event.start_date);

		this.project_spinner.Set_Selection(this.db, this.event.project_id);

    this.notes_text.setText(null);
		if (rs.android.Util.NotEmpty(this.event.notes))
		{
			this.notes_text.setText(this.event.notes);
		}
	}

	@Override
	void On_New_Obj()
	{
		this.event = new Work_Event();
	}

	@Override
	void On_Load_Obj(Long id)
	{
		this.event = Work_Event.Select(this.db, id);
	}

	@Override
	void On_Save_Obj()
	{
		this.event.event_type_id = this.type_spinner.Get_Selected_Id();

		this.event.project_id = this.project_spinner.Get_Selected_Id();

		this.event.notes = Get_Text(this.notes_text);
		
		this.event.start_date=rs.android.Util.Date_Set_Time(
		  this.start_date_button.date, this.start_time_button.time);

		this.db.Save(this.event);
	}
}
