package rs.workbuddy;

public class Main_Activity 
extends rs.workbuddy.Workbuddy_Activity
implements 
android.view.View.OnClickListener,
android.view.View.OnLongClickListener,
android.app.DatePickerDialog.OnDateSetListener,
android.app.TimePickerDialog.OnTimeSetListener,
android.content.DialogInterface.OnClickListener
{
	public class UI_Receiver
  extends android.content.BroadcastReceiver
  {
    @Override
    public void onReceive(android.content.Context context, android.content.Intent intent)
    {
      Update_UI();
    }
  }

	public class UI_TimerTask extends java.util.TimerTask
	{
		public android.content.Context ctx;

		public void run()
		{
			android.content.Intent intent;

			intent = new android.content.Intent();
			intent.setAction("UI_UPDATE_ACTION");
			ctx.sendBroadcast(intent);   
		}
	}

  public android.widget.Button work_button;
	public android.widget.Button lunch_button;
	public android.widget.Button home_button;
	public android.widget.Button undo_button;
	public android.widget.DatePicker date;
	public android.widget.TimePicker time;
	public android.widget.TextView work_text;

	public String dlg_event_type;
	public Work_Event last_event;
	public UI_Receiver ui_receiver;
	public java.util.Timer ui_timer;
	
	public static void Set_Button_Colour(android.widget.Button button, int colour)
	{
		android.graphics.PorterDuffColorFilter filter;

		filter = new android.graphics.PorterDuffColorFilter(colour, android.graphics.PorterDuff.Mode.SRC_ATOP);
		button.getBackground().setColorFilter(filter);
	}

  @Override
  public void onCreate(android.os.Bundle state)
	{
		android.widget.LinearLayout.LayoutParams button_layout;
	  android.widget.LinearLayout main_layout;
	  android.widget.LinearLayout top_layout;
	  android.widget.LinearLayout bottom_layout;

		try
		{
			super.onCreate(state);

			rs.android.Util.ctx = this;

			button_layout = new android.widget.LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, 
				android.view.ViewGroup.LayoutParams.FILL_PARENT, 
				(float)0.5);

			work_button = new android.widget.Button(this);
			work_button.setText("Work");
			work_button.setLayoutParams(button_layout);
			work_button.setOnClickListener(this);
			work_button.setOnLongClickListener(this);
			Set_Button_Colour(work_button, 0xbbff0000);

			lunch_button = new android.widget.Button(this);
			lunch_button.setText("Lunch");
			lunch_button.setLayoutParams(button_layout);
			lunch_button.setOnClickListener(this);
			lunch_button.setOnLongClickListener(this);
			Set_Button_Colour(lunch_button, 0xbbffff00);

			home_button = new android.widget.Button(this);
			home_button.setText("Home");
			home_button.setLayoutParams(button_layout);
			home_button.setOnClickListener(this);
			home_button.setOnLongClickListener(this);
			Set_Button_Colour(home_button, 0xbb00ff00);

			undo_button = new android.widget.Button(this);
			undo_button.setText("Undo");
			undo_button.setLayoutParams(button_layout);
			undo_button.setOnClickListener(this);
			undo_button.setOnLongClickListener(this);

			top_layout = new android.widget.LinearLayout(this);
			top_layout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			top_layout.setLayoutParams(
				new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.FILL_PARENT,
					android.widget.LinearLayout.LayoutParams.FILL_PARENT,
					(float)0.5));
			top_layout.addView(work_button);
			top_layout.addView(lunch_button);

			bottom_layout = new android.widget.LinearLayout(this);
			bottom_layout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			bottom_layout.setLayoutParams(
				new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.FILL_PARENT,
					android.widget.LinearLayout.LayoutParams.FILL_PARENT,
					(float)0.5));
			bottom_layout.addView(home_button);
			bottom_layout.addView(undo_button);

			work_text = new android.widget.TextView(this);
			work_text.setTextColor(0xffbbbbbb);
			work_text.setTextSize(25);
			work_text.setTextAlignment(android.widget.TextView.TEXT_ALIGNMENT_CENTER);
			work_text.setGravity(android.view.Gravity.CENTER);
			work_text.setPadding(0, 10, 0, 10);

			main_layout = new android.widget.LinearLayout(this);
			main_layout.setOrientation(android.widget.LinearLayout.VERTICAL);
			main_layout.addView(work_text);
			main_layout.addView(top_layout);
			main_layout.addView(bottom_layout);

			this.setContentView(main_layout);
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
  }

	@Override
	public void OnClickPositive(android.content.DialogInterface dlg, int which)
	{
		Work_Event.Delete_Last(this.db);
		this.Update_UI();
	}
	
	@Override
	public void OnDateSet(android.widget.DatePicker v, int year, int month, int day)
	{
		this.date = v;
		this.showDialog(DLG_EVENT_TIME);
	}

	@Override
	public void OnTimeSet(android.widget.TimePicker v, int hour, int minute)
	{
		java.sql.Date date_time;

		try
		{
			this.time = v;
			date_time = rs.android.Util.New_Date(
				this.date.getYear(),
				this.date.getMonth() + 1,
				this.date.getDayOfMonth(),
				this.time.getCurrentHour(),
				this.time.getCurrentMinute(),
				0);
			this.last_event = Work_Event.Save_New(this.db, this.dlg_event_type, date_time);
			this.Update_UI();
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

  public void onClick(android.view.View v)
  {
		try
		{
			this.db.Log("rs.workbuddy.Main_Activity.onClick()");
			if (v == this.work_button)
			{
				this.last_event = Work_Event.Save_New(db, Work_Event.EVENT_TYPE_WORK);
			}
			else if (v == this.lunch_button)
			{
				this.last_event = Work_Event.Save_New(db, Work_Event.EVENT_TYPE_LUNCH);
			}
			else if (v == this.home_button)
			{
				this.last_event = Work_Event.Save_New(db, Work_Event.EVENT_TYPE_HOME);
			}
			else if (v == this.undo_button)
			{
				if (this.last_event != null)
				{
					if (this.db.Delete(this.last_event) > 0)
						this.db.Log("...Last event deleted.");
					this.last_event = null;
				}
			}

			this.Update_UI();
		}
		catch (Exception e)
		{
		  rs.android.Util.Show_Error(this, e);
		}
  }

	public boolean onLongClick(android.view.View v)
	{
		boolean res=false;

		try
		{
			this.dlg_event_type = null;
			if (v == this.work_button)
				this.dlg_event_type = Work_Event.EVENT_TYPE_WORK;
			else if (v == this.home_button)
				this.dlg_event_type = Work_Event.EVENT_TYPE_HOME;
			else if (v == this.lunch_button)
				this.dlg_event_type = Work_Event.EVENT_TYPE_LUNCH;
			else if (v == this.undo_button)
			{
				this.showDialog(DLG_DEL_LAST);
			}

			if (this.dlg_event_type != null)
			{
				this.showDialog(DLG_EVENT_DATE);
				res = true;
			}
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
		return res;
	}

	@Override
	public void On_Update_UI()
	{
		String sql, date_str, work_str, time_str;
		Work_Event we;
		Object[] work_ids;
		java.sql.Date now;
		Long duration;
		double work_hrs, work_mins;

		now = rs.android.Util.Now();

		// update button text
		this.work_button.setText("Work");
		this.lunch_button.setText("Lunch");
		this.home_button.setText("Home");
		sql =
		  "select * " +
			"from work_event " +
			"where start_date<=? " +
			"order by start_date desc ";
	  we = (Work_Event)db.SelectObj(Work_Event.class, sql, now);
		if (we != null)
		{
			if (rs.android.Util.NotEmpty(we.event_type) && we.event_type.equals("work"))
			{
				date_str = rs.android.Util.To_String(we.start_date, null, "h:mm a");
				this.work_button.setText("Work since " + date_str);
			}
			else if (rs.android.Util.NotEmpty(we.event_type) && we.event_type.equals("lunch"))
			{
				date_str = rs.android.Util.To_String(we.start_date, null, "h:mm a");
				this.lunch_button.setText("Lunch since " + date_str);
			}
			else if (rs.android.Util.NotEmpty(we.event_type) && we.event_type.equals("home"))
			{
				date_str = rs.android.Util.To_String(we.start_date, null, "h:mm a");
				this.home_button.setText("Home since " + date_str);
			}
		}

	  // update duration header
		//work_str="Today: "+rs.android.Util.To_String(rs.android.Util.Now(), null, "EEE dd/MM/
	  work_str = "No activities today.";
		work_ids = rs.workbuddy.Work_Event.Get_Day_Events(this.db, now, "work");
		if (rs.android.Util.NotEmpty(work_ids))
		{
			this.work_text.setText("length " + work_ids.length);
			duration = Work_Event.Get_Events_Duration(this.db, work_ids);
			if (duration != null)
			{
				work_hrs = (double)duration / (double)1000 / (double)60 / (double)60;
				work_mins = (double)duration / (double)1000 / (double)60;
				work_str =
				  "Work: " + rs.android.Util.To_String(work_hrs, null, "#,###.##") + " hours " +
					"(" + rs.android.Util.To_String(work_mins, null, "#,###.##") + " minutes)";
			}
		}

		// update time header
		work_str = rs.android.Util.To_String(now, "", "h:mm:ss a, EEEE dd/MM/yyyy\n") + work_str;
		this.work_text.setText(work_str);
	}

	@Override
	public void OnResume()
	{
		android.content.IntentFilter filter;
		UI_TimerTask ui_task;

		if (this.ui_receiver == null)
		{
			this.ui_receiver = new UI_Receiver();
			filter = new android.content.IntentFilter();
			filter.addAction("UI_UPDATE_ACTION");
			this.registerReceiver(this.ui_receiver, filter);
		}

		if (this.ui_timer == null)
		{
			ui_task = new UI_TimerTask();
			ui_task.ctx = this;

			this.ui_timer = new java.util.Timer();
			this.ui_timer.schedule(ui_task, 0, 2000);
		}
	}

	@Override
	public void OnPause()
	{
		if (this.ui_timer != null)
		{
			this.ui_timer.cancel();
			this.ui_timer = null;
		}

		if (this.ui_receiver != null)
		{
		  this.unregisterReceiver(this.ui_receiver);
		  this.ui_receiver = null;
		}
	}
}
