package rs.workbuddy;
import android.widget.*;

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
    public void onReceive(android.content.Context ctx, android.content.Intent intent)
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

	public android.widget.Button[] project_buttons;
  public android.widget.Button work_button;
	public android.widget.Button lunch_button;
	public android.widget.Button home_button;
	public android.widget.Button undo_button;
	public android.widget.DatePicker dlg_date;
	public android.widget.TimePicker dlg_time;
	public android.widget.TextView clock_text;
	public android.widget.TextView event_text;
	public rs.android.ui.Guage_View clock;
	public android.widget.LinearLayout main_layout;
	public android.widget.LinearLayout bottom_layout;

	public Long dlg_event_type;
	public Long dlg_project_id;
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
		try
		{
			super.onCreate(state);

			rs.android.Util.ctx = this;

			this.clock = new rs.android.ui.Guage_View(this);
			this.clock.setPadding(0, 10, 0, 0);

			this.work_button = Create_Button("Work", 0xbbff0000);
      this.lunch_button = Create_Button("Break", 0xbbffff00);
			this.home_button = Create_Button("Home", 0xbb00ff00);
			this.undo_button = Create_Button("Undo", null);

			bottom_layout = new android.widget.LinearLayout(this);
			bottom_layout.setPadding(0, 10, 0, 0);
			bottom_layout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			bottom_layout.addView(work_button, new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1));
			bottom_layout.addView(home_button, new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1));
			bottom_layout.addView(lunch_button, new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1));
			bottom_layout.addView(undo_button, new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1));

			clock_text = new android.widget.TextView(this);
			clock_text.setTextColor(0xffbbbbbb);
			clock_text.setTextSize(25);
			clock_text.setTextAlignment(android.widget.TextView.TEXT_ALIGNMENT_CENTER);
			clock_text.setGravity(android.view.Gravity.CENTER);
			clock_text.setPadding(0, 0, 0, 0);

			event_text = new android.widget.TextView(this);
			event_text.setTextColor(0xffbbbbbb);
			event_text.setTextSize(15);
			event_text.setTextAlignment(android.widget.TextView.TEXT_ALIGNMENT_CENTER);
			event_text.setGravity(android.view.Gravity.CENTER);
			event_text.setPadding(0, 0, 0, 0);

			main_layout = new android.widget.LinearLayout(this);
			main_layout.setOrientation(android.widget.LinearLayout.VERTICAL);
			main_layout.addView(clock, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 5f));	
			main_layout.addView(clock_text, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));
			main_layout.addView(event_text, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f));

			this.project_buttons = this.Create_Project_Buttons(this.db);
			this.Layout_Work_Buttons(this.project_buttons);

			this.setContentView(main_layout);
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
  }

	public android.widget.Button Create_Button(String text, Integer colour)
	{
		android.widget.Button res=null;

		this.db.Log("rs.workbuddy.Main_Activity.Create_Button()");
		res = new android.widget.Button(this);
		res.setText(text);
		res.setOnClickListener(this);
		res.setOnLongClickListener(this);
		res.setTextSize(20);
		if (colour != null)
		  Set_Button_Colour(res, colour);
		return res;
	}

	public android.widget.Button[] Create_Project_Buttons(rs.android.Db db)
	{
		android.widget.Button[] res=null;
		java.util.List<Project> projects;
		int c;
		Project project;

		this.db.Log("rs.workbuddy.Main_Activity.Create_Project()");
		projects = Project.Select_All(db);
		if (rs.android.Util.NotEmpty(projects))
		{
			res = new android.widget.Button[projects.size()];
			for (c = 0; c < projects.size(); c++)
			{
				project = projects.get(c);
				res[c] = Create_Button("Work on " + project.name, 0xbbff0000);
				res[c].setTextSize(12);
				res[c].setTag(project.id);
			}
		}

		return res;
	}

	@Override
	public void OnClickPositive(android.content.DialogInterface dlg, int which)
	{
		this.db.Log("rs.workbuddy.Main_Activity.OnClickPositive()");
		Work_Event.Delete_Last(this.db);
		this.Update_UI();
	}

	@Override
	public void OnDateSet(android.widget.DatePicker v, int year, int month, int day)
	{
		this.db.Log("rs.workbuddy.Main_Activity.OnDateSet()");
		this.dlg_date = v;
		this.showDialog(DLG_EVENT_TIME);
	}

	@Override
	public void OnTimeSet(android.widget.TimePicker v, int hour, int minute)
	{
		java.sql.Date date_time;

		try
		{
			this.db.Log("rs.workbuddy.Main_Activity.onTimeSet()");
			this.dlg_time = v;
			date_time = rs.android.Util.New_Date(
				this.dlg_date.getYear(),
				this.dlg_date.getMonth() + 1,
				this.dlg_date.getDayOfMonth(),
				this.dlg_time.getCurrentHour(),
				this.dlg_time.getCurrentMinute(),
				0);
			this.last_event = Work_Event.Save_New(this.db, this.dlg_event_type, date_time, this.dlg_project_id, null);
			this.Update_UI();
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

  public void onClick(android.view.View v)
  {
		Long rounding, project_id;

		try
		{
			this.db.Log("rs.workbuddy.Main_Activity.onClick()");
			rounding = rs.workbuddy.Settings_Activity.Get_Rounding(this);

			if (Is_Work_Button(v))
			{
				project_id = (Long)v.getTag();
				this.last_event = Work_Event.Save_New(db, Work_Event.EVENT_TYPE_WORK, project_id, rounding);
			}
			else if (v == this.lunch_button)
			{
				this.last_event = Work_Event.Save_New(db, Work_Event.EVENT_TYPE_LUNCH, rounding);
			}
			else if (v == this.home_button)
			{
				this.last_event = Work_Event.Save_New(db, Work_Event.EVENT_TYPE_HOME, rounding);
			}
			else if (v == this.undo_button)
			{
				if (this.last_event != null)
				{
					if (this.db.Delete(this.last_event) > 0)
					{
					  this.last_event = null;
					}
					this.db.log=false;
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
			this.db.log=true;
			this.db.Log("rs.workbuddy.Main_Activity.onLongClick()");
			this.dlg_event_type = null;
			this.dlg_project_id = null;
			this.dlg_time = null;
			this.dlg_date = null;

			if (Is_Work_Button(v))
			{
				this.dlg_event_type = Work_Event.EVENT_TYPE_WORK;
				this.dlg_project_id = (Long)v.getTag();
				this.showDialog(DLG_EVENT_DATE);
				res=true;
			}
			else if (v == this.home_button)
			{
				this.dlg_event_type = Work_Event.EVENT_TYPE_HOME;
				this.showDialog(DLG_EVENT_DATE);
				res=true;
			}
			else if (v == this.lunch_button)
			{
				this.dlg_event_type = Work_Event.EVENT_TYPE_LUNCH;
				this.showDialog(DLG_EVENT_DATE);
				res=true;
			}
			else if (v == this.undo_button)
			{
				this.showDialog(DLG_DEL_LAST);
				res=true;
			}
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
		return res;
	}

	public boolean Is_Work_Button(android.view.View v)
	{
		boolean res=false;
		int c;

		this.db.Log("rs.workbuddy.Main_Activity.Is_Work_Button()");
		if (v==this.work_button)
			res=true;
		else if (rs.android.Util.NotEmpty(this.project_buttons))
		{
			for (c = 0; c < this.project_buttons.length; c++)
			{
				if (v == this.project_buttons[c])
				{
					res = true;
					break;
				}
			}
		}
		return res;
	}

	public String Get_Event_Text()
	{
		String date_str, label_str;
		Long duration;
		double hrs, mins;
		java.sql.Date now;
		Work_Event we;

		this.db.Log("rs.workbuddy.Main_Activity.Get_Event_Text()");
		now = rs.android.Util.Now();

		we=Work_Event.Select_Prev_Event(this.db, now);
		date_str = rs.android.Util.To_String(we.start_date, null, "h:mma");
		if (we.event_type.equals(Work_Event.EVENT_TYPE_WORK) && we.project_id!=null)
			label_str="Working on "+we.Get_Project_Name(this.db)+"\nsince "+date_str;
		else
		  label_str = Work_Event.Get_Event_Description(we.event_type) + "\nsince " + date_str;

		//duration = Work_Event.Get_Day_Events_Duration(this.db, now, we.event_type);
		duration = we.Get_Event_Duration(this.db);
		if (rs.android.Util.NotEmpty(duration))
		{
			hrs = (double)duration / (double)1000 / (double)60 / (double)60;
			mins = (double)duration / (double)1000 / (double)60;
			label_str +=
				" for "+rs.android.Util.To_String(hrs, null, "#,###.##") + " hrs " +
				"(" + rs.android.Util.To_String(mins, null, "#,###.##") + " min)";
		}
		return label_str;
	}

  public void Layout_Work_Buttons(android.widget.Button[] buttons)
  {
    android.widget.LinearLayout row=null;
    int rows=0, num_layouts, l, b;
		float layout_weight;

		this.db.Log("rs.workbuddy.Main_Activity.Layout_Work_Buttons()");
    if (rs.android.Util.NotEmpty(buttons))
    {
			num_layouts = (int)java.lang.Math.ceil((double)buttons.length / (double)4);
			layout_weight = 5 / (num_layouts + 1);

			this.main_layout.addView(this.bottom_layout, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, layout_weight));

			for (l = 0; l < num_layouts; l++)
			{
				row = new android.widget.LinearLayout(this);
				row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
				for (b = 0; b < 4 && rows * 4 + b < buttons.length; b++)
				{
					row.addView(buttons[rows * 4 + b], new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1));
				}
				rows++;
				this.main_layout.addView(row, new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, layout_weight));
      }
    }
  }

	@Override
	public void On_Update_UI()
	{
		String label_str;
		//Work_Event we;
		java.sql.Date now;
		Long duration;
		double hrs, amount;
		float hr_rate;
		Long[] ids;
		int c;
		rs.android.ui.Bar b;

		this.db.Log("rs.workbuddy.Main_Activity.On_Update_UI()");
		now = rs.android.Util.Now();

		// update button text
		/*this.work_button.setText("Work");
		this.work_button.setTextSize(20);
		this.lunch_button.setText("Lunch");
		this.lunch_button.setTextSize(20);
		this.home_button.setText("Home");
		this.home_button.setTextSize(20);*/

	  // update duration header
	  label_str = "No activities today.";
		/*duration = Work_Event.Get_Day_Events_Duration(this.db, now, Work_Event.EVENT_TYPE_WORK);
		if (duration != null)
		{
			hrs = (double)duration / (double)1000 / (double)60 / (double)60;
			hr_rate = Settings_Activity.Get_Hourly_Rate(this);
			amount = hr_rate * hrs;

			label_str =
				Settings_Activity.Get_Currency_Symbol(this) +
				rs.android.Util.To_String(amount, null, "#,##0.00") + " @ " +
				Settings_Activity.Get_Currency_Symbol(this) +
				rs.android.Util.To_String(hr_rate, null, "#,##0.##") + "/hr";
		}*/
		label_str=Get_Event_Text();
		this.event_text.setText(label_str);

		// update time header
		label_str = rs.android.Util.To_String(now, "", "h:mm:ss a, EEEE dd/MM/yyyy");
		this.clock_text.setText(label_str);

		// update clock
		this.clock.bars = null;
		b = New_Bar(Work_Event.Select_Prev_Event_Id(this.db, rs.android.Util.Today()));
		this.clock.Add_Bar(b);

		ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, now, null);
		if (rs.android.Util.NotEmpty(ids))
		{
			for (c = 0; c < ids.length; c++)
			{
				b = New_Bar(ids[c]);
				this.clock.Add_Bar(b);
			}
		}
	}

	public int Calc_Event_Bar_Colour(Work_Event we)
	{
		int res=0xffffffff;

		this.db.Log("rs.workbuddy.Main_Activity.Calc_Event_Bar_Colour()");
		if (we != null && we.event_type != null)
		{
			if (we.event_type.equals(Work_Event.EVENT_TYPE_HOME))
				res = 0xff00ff00;
			else if (we.event_type.equals(Work_Event.EVENT_TYPE_LUNCH))
				res = 0xffffff00;
			else if (we.event_type.equals(Work_Event.EVENT_TYPE_WORK))
				res = 0xffff0000;
		}
		return res;
	}

	public rs.android.ui.Bar New_Bar(Long id)
	{
		Work_Event we;
		float sr, lr;
		rs.android.ui.Bar b=null;
		long start_time, duration, today_time, end_time, tomorrow_time;

		if (id != null)
		{
			today_time = rs.android.Util.Today().getTime();
			tomorrow_time = today_time + rs.android.Util.MILLIS_PER_DAY;

			we = rs.workbuddy.Work_Event.Select(this.db, id);
			start_time = we.start_date.getTime();
			duration = we.Get_Event_Duration(this.db);
			end_time = start_time + duration;

			// event stradles day start
			if (start_time < today_time && end_time > today_time)
			{
				start_time = today_time;
				duration = end_time - start_time;
			}
			// event stradles day end
			if (start_time < tomorrow_time && end_time > tomorrow_time)
			{
				duration = tomorrow_time - start_time;
			}

			sr = ((float)start_time - (float)today_time) / (float)rs.android.Util.MILLIS_PER_DAY;
			lr = (float)duration / (float)rs.android.Util.MILLIS_PER_DAY;

			b = new rs.android.ui.Bar(this);
			b.start_angle = 360 * sr;
			b.size_angle = 360 * lr;
			b.colour = Calc_Event_Bar_Colour(we);
		}
		return b;
	}

	@Override
	public void On_Resume()
	{
		android.content.IntentFilter filter;
		UI_TimerTask ui_task;

		this.clock.db = this.db;

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
			this.ui_timer.schedule(ui_task, 0, 5000);
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
