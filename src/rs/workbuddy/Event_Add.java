package rs.workbuddy;

public class Event_Add
extends rs.workbuddy.Workbuddy_Activity
implements 
android.view.View.OnClickListener,
android.widget.AdapterView.OnItemSelectedListener
{
	public static final int MENUITEM_DONE=20;

	public android.view.MenuItem done_menu;
	public android.widget.Spinner type_spinner;
	public android.widget.Button start_date_button;
	public android.widget.Button start_time_button;
	public Work_Event event;

  @Override
  public void onCreate(android.os.Bundle state)
	{
	  android.widget.LinearLayout main_view, row;
		android.widget.TextView label;
		Array_Adapter types;

		try
		{
			super.onCreate(state);
			//rs.android.Util.Show_Note(this, rs.android.Util.To_String(state.get("event_id")));

			main_view = new android.widget.LinearLayout(this);
			main_view.setOrientation(android.widget.LinearLayout.VERTICAL);

			// event type
			row = new android.widget.LinearLayout(this);
			row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			row.setPadding(15, 15, 0, 0);
			//Border_Drawable.Add_Border(row, 0, 0, 1, 0, 0xff444444);

			label = new android.widget.TextView(this);
			label.setText("Type");
			label.setTextSize(20);
			label.setWidth(200);
			//label.setTextAlignment(android.widget.TextView.TEXT_ALIGNMENT_TEXT_END);
			row.addView(label);

			types = new Array_Adapter(this);
			types.add("n/a");
			types.add("Work");
			types.add("Lunch");
			types.add("Home");
			type_spinner = new android.widget.Spinner(this);
			type_spinner.setAdapter(types);
			type_spinner.setOnItemSelectedListener(this);
			row.addView(type_spinner);

			main_view.addView(row);

			// event start date
			row = new android.widget.LinearLayout(this);
			row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			row.setPadding(15, 15, 0, 0);
			//Border_Drawable.Add_Border(row, 0, 0, 1, 0, 0xff444444);

			label = new android.widget.TextView(this);
			label.setText("Start Date");
			label.setTextSize(20);
			label.setWidth(200);
			row.addView(label);

			start_date_button = new android.widget.Button(this, null, android.R.attr.spinnerStyle);
			start_date_button.setOnClickListener(this);
			start_date_button.setTextSize(20);
			start_date_button.setPadding
			(
			  start_date_button.getPaddingLeft()+10,
				start_date_button.getPaddingTop(),
				start_date_button.getPaddingRight(),
				start_date_button.getPaddingBottom()
			);
			row.addView(start_date_button);

			main_view.addView(row);

			// event start time
			row = new android.widget.LinearLayout(this);
			row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			row.setPadding(15, 15, 0, 0);
			//Border_Drawable.Add_Border(row, 0, 0, 1, 0, 0xff444444);

			label = new android.widget.TextView(this);
			label.setText("Start Time");
			label.setTextSize(20);
			label.setWidth(200);
			row.addView(label);

			start_time_button = new android.widget.Button(this, null, android.R.attr.spinnerStyle);
			start_time_button.setOnClickListener(this);
			start_time_button.setTextSize(20);
			start_time_button.setPadding
			(
			  start_time_button.getPaddingLeft()+10,
				start_time_button.getPaddingTop(),
				start_date_button.getPaddingRight(),
				start_time_button.getPaddingBottom()
			);
			row.addView(start_time_button);

			main_view.addView(row);

			this.setContentView(main_view);
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
  }

	public void onItemSelected(android.widget.AdapterView<?> p1, android.view.View p2, int p3, long p4)
	{
	  int pos;

		pos = this.type_spinner.getSelectedItemPosition();
		if (pos == 1)
			this.event.event_type = Work_Event.EVENT_TYPE_WORK;
		else if (pos == 2)
			this.event.event_type = Work_Event.EVENT_TYPE_LUNCH;
		else if (pos == 3)
			this.event.event_type = Work_Event.EVENT_TYPE_HOME;
		else
			this.event.event_type = null;
	}

	public void onNothingSelected(android.widget.AdapterView<?> p1)
	{
		// TODO: Implement this method
	}

	@Override
  public boolean onCreateOptionsMenu(android.view.Menu menu) 
  {
		boolean res=false;

		res = super.onCreateOptionsMenu(menu);
		this.done_menu = this.options_menu.add(1, MENUITEM_DONE, android.view.Menu.NONE, "Done");
		this.done_menu.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);

		return res;
	}

	@Override
  public boolean onOptionsItemSelected(android.view.MenuItem item)
  {
    boolean res=false, save_res;

		try
		{
			res = super.onOptionsItemSelected(item);
			if (!res)
			{
				if (item.getItemId() == MENUITEM_DONE)
				{
					save_res=this.db.Save(this.event);
					this.finish();
					res = true;
				}
			}
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
		return res;
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
		
		if (id==DLG_EVENT_DATE)
		{
			date_dlg=(android.app.DatePickerDialog)dialog;
			if (this.event.start_date!=null)
			{
			  date_dlg.updateDate(
				rs.android.Util.Date_Get_Year(this.event.start_date),
				rs.android.Util.Date_Get_Month(this.event.start_date),
				rs.android.Util.Date_Get_Day(this.event.start_date));
			}
		}
		else if (id==DLG_EVENT_TIME)
		{
			time_dlg=(android.app.TimePickerDialog)dialog;
			if (this.event.start_date!=null)
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

		this.type_spinner.setSelection(0);
		if (rs.android.Util.NotEmpty(this.event.event_type))
		{
			if (this.event.event_type.equals(Work_Event.EVENT_TYPE_WORK))
				this.type_spinner.setSelection(1);
			else if (this.event.event_type.equals(Work_Event.EVENT_TYPE_LUNCH))
				this.type_spinner.setSelection(2);
			else if (this.event.event_type.equals(Work_Event.EVENT_TYPE_HOME))
				this.type_spinner.setSelection(3);
			else
				this.type_spinner.setSelection(0);
		}

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
	}

	@Override
	public void OnResume()
	{
		long id;
		
		try
		{
			if (this.event == null)
			{
				if (this.getIntent().hasExtra("event_id"))
				{
					id=getIntent().getLongExtra("event_id", 0);
					this.event=(Work_Event)this.db.SelectObj(Work_Event.class, id);
				}
				else
  				this.event = new Work_Event();
			}
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}
}
