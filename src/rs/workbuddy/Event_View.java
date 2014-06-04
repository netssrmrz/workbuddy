package rs.workbuddy;

public class Event_View
extends rs.workbuddy.Workbuddy_Activity_Add
{
	public android.widget.TextView type_text;
	public android.widget.TextView start_text;
	public android.widget.TextView end_text;
	public android.widget.TextView duration_text;
	public android.widget.TextView project_text;
	public android.widget.TextView notes_text;
	public Work_Event event;
	
	@Override
	public void On_Create_UI(android.view.ViewGroup main_view)
	{
		this.type_text = new android.widget.TextView(this);
		Add_Field("Type", type_text);

		start_text = new android.widget.TextView(this);
		this.Add_Field("Start Time", start_text);

		end_text = new android.widget.TextView(this);
		this.Add_Field("End Time", end_text);

		duration_text = new android.widget.TextView(this);
		this.Add_Field("Duration", duration_text);

		this.project_text = new android.widget.TextView(this);
		this.Add_Field("Project", project_text);

		notes_text = new android.widget.TextView(this);
		this.Add_Field("Notes", notes_text);
	}

	@Override
	public void On_Update_UI()
	{
		if (this.event != null)
		{
			this.type_text.setText(this.event.Get_Type_Name(this.db));

			this.start_text.setText(rs.android.util.Type.To_String(
																this.event.start_date, "n/a", "EEEE dd/MM/yyyy h:mm:ss a"));

			this.end_text.setText(rs.android.util.Type.To_String(
															this.event.Get_Event_End(this.db), "n/a",
															"EEEE dd/MM/yyyy h:mm:ss a"));

			this.duration_text.setText(
				rs.android.util.Type.To_String(this.event.Get_Event_Duration_Hr(this.db), "0", "#,##0.##") + " hr" +
				" (" + rs.android.util.Type.To_String(this.event.Get_Event_Duration_Min(this.db), "0", "#,##0.##") + " min)");

			this.project_text.setText(rs.android.util.Type.To_String(
																	this.event.Get_Project_Name(this.db), "n/a"));

			this.notes_text.setText(this.event.notes);
		}
	}

	@Override
	public void On_Load_Obj(Long id)
	{
		this.event = Work_Event.Select(this.db, id);
	}

	@Override
  public boolean onCreateOptionsMenu(android.view.Menu menu) 
  {
		super.onCreateOptionsMenu(menu);
		menu.findItem(Menus.MENUITEM_DELETE).setVisible(true);
		menu.findItem(Menus.MENUITEM_EDIT).setVisible(true);
		menu.findItem(Menus.MENUITEM_NEXT).setVisible(true);
		menu.findItem(Menus.MENUITEM_PREV).setVisible(true);

		return true;
	}

	@Override
	public void On_Edit()
	{
		android.content.Intent i;
		
		i = new android.content.Intent(this, Event_Add.class);
		i.putExtra("id", this.event.id);
		this.startActivity(i);
		this.finish();		
	}
	
	@Override
	public void On_Next()
	{
		Long id;
		
		id = Work_Event.Select_Next_Event_Id(this.db, this.event.start_date);
		if (id != null)
		{
			this.event = Work_Event.Select(this.db, id);
			this.On_Update_UI();
		}		
	}
	
	@Override
	public void On_Prev()
	{
		Long id;
		
		id = Work_Event.Select_Prev_Event_Id(this.db, this.event.start_date);
		if (id != null)
		{
			this.event = Work_Event.Select(this.db, id);
			this.On_Update_UI();
		}		
	}
	
	@Override
	public void On_Delete()
	{
		if (this.event!=null)
	    Work_Event.Delete(this.db, this.event.id);
		this.finish();
	}
}
