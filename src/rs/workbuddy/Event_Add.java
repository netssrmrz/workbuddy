package rs.workbuddy;
import android.widget.*;
import android.content.*;

public class Event_Add
extends rs.workbuddy.Workbuddy_Activity_Add
implements 
android.view.View.OnClickListener
{
	public rs.workbuddy.Event_Type_Spinner type_spinner;
	public android.widget.Button start_date_button;
	public android.widget.Button start_time_button;
	public rs.workbuddy.Project_Spinner project_spinner;
	public android.widget.EditText notes_text;
	public Work_Event event;

  @Override
  public void On_Create_UI(android.view.ViewGroup main_view)
	{
		//this.finish=false;

		// event type
		this.type_spinner = new Event_Type_Spinner(this);
		this.Add_Field("Type", type_spinner);

		// event start date
		start_date_button = new android.widget.Button(this, null, android.R.attr.spinnerStyle);
		start_date_button.setOnClickListener(this);
		this.Add_Field("Start Date", start_date_button);

		// event start time
		start_time_button = new android.widget.Button(this, null, android.R.attr.spinnerStyle);
		start_time_button.setOnClickListener(this);
		this.Add_Field("Start Time", start_time_button);

		this.project_spinner=new Project_Spinner(this, this.db);
		this.Add_Field("Project", project_spinner);

		// event notes
		notes_text = new android.widget.EditText(this);
		this.Add_Field("Notes", notes_text);
  }

	public void onClick(android.view.View v)
  {
		try
		{
			if (v == this.start_date_button)
				this.showDialog(DLG_EVENT_DATE);
			else if (v == this.start_time_button)
			  this.showDialog(DLG_EVENT_TIME);
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	@Override
  public void onPrepareDialog(int id, android.app.Dialog dialog)
  {
		android.app.DatePickerDialog date_dlg;
		android.app.TimePickerDialog time_dlg;

		if (id == DLG_EVENT_DATE)
		{
			date_dlg = (android.app.DatePickerDialog)dialog;
			if (this.event.start_date != null)
			{
			  date_dlg.updateDate(
					rs.android.Util.Date_Get_Year(this.event.start_date),
					rs.android.Util.Date_Get_Month(this.event.start_date),
					rs.android.Util.Date_Get_Day(this.event.start_date));
			}
		}
		else if (id == DLG_EVENT_TIME)
		{
			time_dlg = (android.app.TimePickerDialog)dialog;
			if (this.event.start_date != null)
			{
				time_dlg.updateTime(
				  rs.android.Util.Date_Get_Hour(this.event.start_date),
					rs.android.Util.Date_Get_Minute(this.event.start_date));
			}
		}
  }

	@Override
	public void OnDateSet(android.widget.DatePicker v, int year, int month, int day)
	{
		java.util.Calendar cal;

		try
		{
			cal = java.util.Calendar.getInstance();
			if (this.event.start_date != null)
			{
				cal.setTime(this.event.start_date);
			}
			cal.set(java.util.Calendar.YEAR, year);
			cal.set(java.util.Calendar.MONDAY, month);
			cal.set(java.util.Calendar.DAY_OF_MONTH, day);
			this.event.start_date = new java.sql.Date(cal.getTimeInMillis());
			Update_UI();
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	@Override
	public void OnTimeSet(android.widget.TimePicker v, int hour, int minute)
	{
		java.util.Calendar cal;

		try
		{
			cal = java.util.Calendar.getInstance();
			if (this.event.start_date != null)
			{
				cal.setTime(this.event.start_date);
			}
			cal.set(java.util.Calendar.HOUR_OF_DAY, hour);
			cal.set(java.util.Calendar.MINUTE, minute);
			cal.set(java.util.Calendar.SECOND, 0);
			cal.set(java.util.Calendar.MILLISECOND, 0);
			this.event.start_date = new java.sql.Date(cal.getTimeInMillis());
			Update_UI();
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	@Override
	public void On_Update_UI()
	{
		String date_str;

		this.type_spinner.Set_Selection(this.event.event_type);

		this.start_date_button.setText("n/a");
		if (this.event.start_date != null)
		{
			date_str = rs.android.Util.To_String(this.event.start_date, "n/a", "EEEE dd/MM/yyyy");
			this.start_date_button.setText(date_str);
		}

		this.start_time_button.setText("n/a");
		if (this.event.start_date != null)
		{
			date_str = rs.android.Util.To_String(this.event.start_date, "n/a", "h:mm:ss a");
			this.start_time_button.setText(date_str);
		}

		this.project_spinner.Set_Selection(this.event.project_id);

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
		this.event.event_type = this.type_spinner.Get_Selected_Id();

		this.event.project_id = this.project_spinner.Get_Selected_Id();

		this.event.notes = Get_Text(this.notes_text);

		this.db.Save(this.event);
	}
}
