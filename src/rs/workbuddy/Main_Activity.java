package rs.workbuddy;
//import android.widget.*;
//import android.widget.*;
//import rs.workbuddy.db.*;
//import android.view.*;
//import java.sql.*;

public class Main_Activity 
extends rs.workbuddy.Workbuddy_Activity
implements 
android.view.View.OnClickListener,
android.view.View.OnLongClickListener,
rs.android.ui.Date_Dialog.On_Date_Set_Listener,
rs.android.ui.Time_Dialog.On_Time_Set_Listener
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

	public static final int BUTTONS_PER_ROW=4;

	public android.widget.TextView clock_text;
	public android.widget.TextView event_text;
	public rs.android.ui.Guage_View clock;
	public android.widget.LinearLayout main_layout;
	public android.widget.LinearLayout event_type_layout;
	public android.widget.LinearLayout project_layout;

	public UI_Receiver ui_receiver;
	public java.util.Timer ui_timer;
	public Work_Event dlg_event;
  public boolean has_type_buttons;
  public java.sql.Date last_update_time;

	public static void Set_Button_Colour(android.widget.Button button, Integer colour)
	{
		android.graphics.PorterDuffColorFilter filter;

		if (colour != null)
		{
			filter = new android.graphics.PorterDuffColorFilter(colour, android.graphics.PorterDuff.Mode.SRC_ATOP);
			button.getBackground().setColorFilter(filter);
		}
	}

  @Override
  public void onCreate(android.os.Bundle state)
	{
		super.onCreate(state);
		
		rs.android.Util.ctx = this;

		main_layout = new android.widget.LinearLayout(this);

		if (rs.android.ui.Util.Is_Landscape_Mode(this))
		{
			main_layout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
			main_layout.addView(this.Create_Clock(), 
				new android.widget.LinearLayout.LayoutParams(
					0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 50f));
			main_layout.addView(this.Create_Buttons(), 
				new android.widget.LinearLayout.LayoutParams(
					0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 50f));
		}
		else
		{
			main_layout.setOrientation(android.widget.LinearLayout.VERTICAL);
			main_layout.addView(this.Create_Clock(), 
				new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 75f));
			main_layout.addView(this.Create_Buttons(), 
				new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 25f));
		}

		this.setContentView(main_layout);
  }

	public android.widget.LinearLayout Create_Buttons()
	{
		android.widget.LinearLayout button_frame;

		this.event_type_layout = new android.widget.LinearLayout(this);
		this.event_type_layout.setOrientation(android.widget.LinearLayout.VERTICAL);
		this.event_type_layout.setPadding(0, 0, 0, 0);
		//rs.android.ui.Border_Drawable.Add_Border(this.event_type_layout, 0xff00ff00);

		this.project_layout = new android.widget.LinearLayout(this);
		this.project_layout.setOrientation(android.widget.LinearLayout.VERTICAL);
		this.project_layout.setPadding(0, 0, 0, 0);
		//rs.android.ui.Border_Drawable.Add_Border(this.project_layout, 0xff00ff00);

		button_frame = new android.widget.LinearLayout(this);
		button_frame.setOrientation(android.widget.LinearLayout.VERTICAL);
		button_frame.addView(this.event_type_layout,
			new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 20f));
		button_frame.addView(this.project_layout,
			new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 80f));
		//rs.android.ui.Border_Drawable.Add_Border(button_frame, 0xffff0000);

		return button_frame;
	}

	public android.widget.LinearLayout Create_Clock()
	{
		android.widget.LinearLayout clock_frame;

		this.clock = new rs.android.ui.Guage_View(this);
		this.clock.setPadding(0, 10, 0, 0);

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

		clock_frame = new android.widget.LinearLayout(this);
		clock_frame.setOrientation(android.widget.LinearLayout.VERTICAL);
		clock_frame.addView(clock,
			new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 85f));
		clock_frame.addView(clock_text,
			new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 7f));
		clock_frame.addView(event_text,
			new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 8f));

		return clock_frame;
	}

  public void onClick(android.view.View v)
  {
		Long rounding=null;
		Work_Event event;

		//android.util.Log.d("workbuddy", "onClick()");
		rounding = rs.workbuddy.Settings_Activity.Get_Rounding(this);

		event = (Work_Event)v.getTag();
		event.id = null;
		event.notes = null;
		event.start_date = rs.android.util.Date.Round_Date(rs.android.util.Date.Now(), rounding);
		event.Set_Default_Project(this.db);
		event.Save(this.db);

		this.Update_UI();
  }

	public boolean onLongClick(android.view.View v)
	{
		boolean res=true;
		rs.android.ui.Date_Dialog date_dlg;

		this.dlg_event = (Work_Event)v.getTag();
		date_dlg = new rs.android.ui.Date_Dialog(this, this);
		date_dlg.Show(null);
		return res;
	}

	public void On_Date_Set(java.sql.Date date)
	{
		rs.android.ui.Time_Dialog time_dlg;

		this.dlg_event.start_date = date;
		time_dlg = new rs.android.ui.Time_Dialog(this, this);
		time_dlg.Show(date);
	}

	public void On_Time_Set(java.sql.Date time)
	{
		this.dlg_event.start_date = rs.android.util.Date.Date_Set_Time(this.dlg_event.start_date, time);
		this.dlg_event.id = null;
		this.dlg_event.notes = null;
		this.dlg_event.Set_Default_Project(this.db);
		this.dlg_event.Save(this.db);

		this.Update_UI();
	}

	public String Get_Prev_Event_Description(rs.android.Db db, java.sql.Date date)
	{
		String label_str, event_type_name;
		double hrs, mins;
		Work_Event we;

		label_str = "No Activities.";

		we = Work_Event.Select_Prev_Event(db, date);
		if (we != null)
		{
			event_type_name = we.Get_Type_Name(db);
			if (!rs.android.Util.NotEmpty(event_type_name))
				event_type_name = "Activity";
			label_str = event_type_name + " since " +
				rs.android.Util.To_String(we.start_date, null, "h:mma");
			if (we.project_id != null)
				label_str = rs.android.Util.AppendStr(label_str, we.Get_Project_Name(db), "\n");

			hrs = we.Get_Event_Duration_Hr(db);
			mins = we.Get_Event_Duration_Min(db);
			if (rs.android.Util.NotEmpty(hrs))
			{
				label_str +=
					" for " + rs.android.Util.To_String(hrs, null, "#,###.##") + " hrs " +
					"(" + rs.android.Util.To_String(mins, null, "#,###.##") + " min)";
			}
		}
		return label_str;
	}

	public android.widget.Button New_Project_Button(rs.android.Db db, Long project_id, Long event_type_id)
	{
		android.widget.Button res=null;
		Work_Event event;

		//this.db.Log("rs.workbuddy.Main_Activity.Create_Button()");
		res = this.New_Type_Button(db, event_type_id);

		event = (Work_Event)res.getTag();
		event.project_id = project_id;

		res.setText(res.getText() + ": " + rs.workbuddy.Project.Get_Project_Name(db, project_id));
		res.setTextSize(15);

		return res;
	}

	public android.widget.Button New_Type_Button(rs.android.Db db, Long event_type_id)
	{
		android.widget.Button res=null;
		Work_Event event;

		//this.db.Log("rs.workbuddy.Main_Activity.Create_Button()");
		event = new Work_Event();
		event.event_type_id = event_type_id;

		res = new android.widget.Button(this);
		res.setText(rs.workbuddy.db.Event_Type.Get_Name(db, event_type_id));
		res.setOnClickListener(this);
		res.setOnLongClickListener(this);
		res.setTextSize(20);
		res.setTag(event);
	  Set_Button_Colour(res, rs.workbuddy.db.Event_Type.Get_Colour(db, event_type_id));
		return res;
	}

  public android.view.ViewGroup[] Build_Type_Rows(rs.android.Db db)
	{
		android.view.ViewGroup[] res=null;
		Long[] ids;
		int no_rows, c;

		ids = rs.workbuddy.db.Event_Type.Select_Ids(db);
		if (rs.android.Util.NotEmpty(ids))
		{
			no_rows = (ids.length / BUTTONS_PER_ROW) + 1;

			res = new android.view.ViewGroup[no_rows];
			for (c = 0; c < res.length; c++)
				res[c] = new android.widget.LinearLayout(this);

			for (c = 0; c < ids.length; c++)
				res[c / BUTTONS_PER_ROW].addView(this.New_Type_Button(db, ids[c]), 
					new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1));
		}
		return res;
	}

	public android.view.ViewGroup[] Build_Project_Rows(rs.android.Db db)
	{
		android.view.ViewGroup[] res=null, curr_type_rows, temp;
		Long[] ids;
		int c;

		ids = rs.workbuddy.db.Event_Type.Select_Home_Ids(db);
		if (rs.android.Util.NotEmpty(ids))
		{
			for (c = 0; c < ids.length; c++)
			{
				curr_type_rows = this.Build_Project_Rows(db, ids[c]);
				if (res == null)
					res = curr_type_rows;
				else
				{
				  temp = new android.view.ViewGroup[res.length + curr_type_rows.length];
					System.arraycopy(res, 0, temp, 0, res.length);
					System.arraycopy(curr_type_rows, 0, temp, res.length, curr_type_rows.length);
					res = temp;
				}
			}
		}
		return res;
	}

  public android.view.ViewGroup[] Build_Project_Rows(rs.android.Db db, Long event_type_id)
	{
		android.view.ViewGroup[] res=null;
		Long[] ids;
		int no_rows, c;

		ids = rs.workbuddy.Project.Select_Home_Ids(db);
		if (rs.android.Util.NotEmpty(ids))
		{
			no_rows = ((ids.length-1) / BUTTONS_PER_ROW) + 1;

			res = new android.view.ViewGroup[no_rows];
			for (c = 0; c < res.length; c++)
			{
				res[c] = new android.widget.LinearLayout(this);
				//rs.android.ui.Border_Drawable.Add_Border(res[c], 0xff0000ff);
			}

			for (c = 0; c < ids.length; c++)
				res[c / BUTTONS_PER_ROW].addView(this.New_Project_Button(db, ids[c], event_type_id), 
					new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1));
		}
		return res;
	}

	/*public void Do_Stuff()
	 {
	 rs.workbuddy.db.Status_Type s;

	 s=(rs.workbuddy.db.Status_Type)this.db.SelectObj(rs.workbuddy.db.Status_Type.class, 
	 "select * from status_type where name='Pending'");
	 s.colour=0xffffff00;
	 this.db.Save(s);

	 s=(rs.workbuddy.db.Status_Type)this.db.SelectObj(rs.workbuddy.db.Status_Type.class, 
	 "select * from status_type where name='In Progress'");
	 s.colour=0xff00ff00;
	 this.db.Save(s);

	 s=(rs.workbuddy.db.Status_Type)this.db.SelectObj(rs.workbuddy.db.Status_Type.class, 
	 "select * from status_type where name='Completed'");
	 s.colour=0xffff0000;
	 this.db.Save(s);

	 s=(rs.workbuddy.db.Status_Type)this.db.SelectObj(rs.workbuddy.db.Status_Type.class, 
	 "select * from status_type where name='On Hold'");
	 s.colour=0xffffff00;
	 this.db.Save(s);

	 s=(rs.workbuddy.db.Status_Type)this.db.SelectObj(rs.workbuddy.db.Status_Type.class, 
	 "select * from status_type where name='Cancelled'");
	 s.colour=0xffff0000;
	 this.db.Save(s);
	 }*/

	@Override
	public void On_Update_UI()
	{
		String label_str;
		java.sql.Date now;
		Long[] ids;
		int c;
		rs.android.ui.Bar b;

		//this.Do_Stuff();
		//this.db.Log("rs.workbuddy.Main_Activity.On_Update_UI()");
		now = rs.android.util.Date.Now();

		// set event type buttons
		if (!this.has_type_buttons)
		{
			this.event_type_layout.removeAllViews();
			rs.android.ui.Util.Add_Views(this.event_type_layout, this.Build_Type_Rows(this.db));
			this.has_type_buttons = true;
		}

		// set project buttons
    if (rs.android.Log.Has_Changes(this.db, Project.class, this.last_update_time))
    {
		  this.project_layout.removeAllViews();
		  rs.android.ui.Util.Add_Views(this.project_layout, this.Build_Project_Rows(this.db));
    }
    this.last_update_time = rs.android.util.Date.Now();

	  // update duration header
		this.event_text.setText(this.Get_Prev_Event_Description(this.db, now));

		// update time header
		label_str = rs.android.Util.To_String(now, "", "h:mm:ss a, EEEE dd/MM/yyyy");
		this.clock_text.setText(label_str);

		// update clock
		this.clock.bars = null;
		b = New_Bar(Work_Event.Select_Prev_Event_Id(this.db, rs.android.util.Date.Today()));
		this.clock.Add_Bar(b);

		ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, now, null, null);
		if (rs.android.Util.NotEmpty(ids))
		{
			for (c = 0; c < ids.length; c++)
			{
				b = New_Bar(ids[c]);
				this.clock.Add_Bar(b);
			}
		}
	}

	public rs.android.ui.Bar New_Bar(Long id)
	{
		Work_Event we;
		float sr, lr;
		rs.android.ui.Bar b=null;
		long start_time, duration, today_time, end_time, tomorrow_time;

		if (id != null)
		{
			today_time = rs.android.util.Date.Today().getTime();
			tomorrow_time = today_time + rs.android.util.Date.MILLIS_PER_DAY;

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

			sr = ((float)start_time - (float)today_time) / (float)rs.android.util.Date.MILLIS_PER_DAY;
			lr = (float)duration / (float)rs.android.util.Date.MILLIS_PER_DAY;

			b = new rs.android.ui.Bar(this);
			b.start_angle = 360 * sr;
			b.size_angle = 360 * lr;
			b.colour = we.Get_Colour(this.db);
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
			this.ui_timer.schedule(ui_task, 0, 1000);
		}
	}

	@Override
	public void On_Pause()
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
