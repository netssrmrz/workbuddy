package rs.workbuddy;
import java.io.*;
import android.widget.*;
import android.net.*;
import rs.android.ui.*;

public class Report_Timesheet
extends Workbuddy_Activity_List
implements Timesheet_Template.On_Finish_Listener,
android.widget.AdapterView.OnItemSelectedListener,
rs.workbuddy.Template_Dialog.On_Template_Set_Listener
{
  public class Timesheet_Entry
	{
		public java.sql.Date date;
		public Long[] durations;
	}

	public android.widget.TextView prog_label;
	public rs.android.ui.Progress_Bar prog_bar;
	public java.sql.Date week_of, min_day, max_day;
	public boolean is_send;
	public boolean is_view;
	public Long project_id;
	public rs.workbuddy.Project_Adapter projects;

	public Report_Timesheet()
	{

	}

	@Override
	public void onCreate(android.os.Bundle state)
	{
		super.onCreate(state);

		this.has_footer = true;
		this.has_col_select = false;
		this.has_paging = true;

		this.min_day = (java.sql.Date)this.db.Select_Value(java.sql.Date.class, "select start_date from Work_Event order by start_date asc");
		this.max_day = (java.sql.Date)this.db.Select_Value(java.sql.Date.class, "select start_date from Work_Event order by start_date desc");
    this.week_of = this.max_day;
		this.Set_Title();
	}

	@Override
	public void On_Create_Columns()
	{
		Long[] ids;
		String type_name;
		rs.android.ui.Column col;

		this.Add_Column("date", "Date");
		ids = rs.workbuddy.db.Event_Type.Select_Ids(this.db);
		if (rs.android.Util.NotEmpty(ids))
		{
			for (Long id: ids)
			{
				type_name = rs.workbuddy.db.Event_Type.Get_Name(this.db, id);
				col = this.Add_Column("mins_" + id, type_name + "\n(Minutes)");
				col.align = rs.android.ui.Column.ALIGN_RIGHT;
				col = this.Add_Column("hrs_" + id, type_name + "\n(Hours)");
				col.align = rs.android.ui.Column.ALIGN_RIGHT;
			}
		}
	}

	public void Set_Title()
	{
		java.sql.Date[] week_days;

		if (this.week_of == null)
		  this.week_of = rs.android.util.Date.Now();
		week_days = rs.android.util.Date.Week(this.week_of);
		if (rs.android.Util.NotEmpty(week_days))
		{
			this.title =
			  rs.android.Util.To_String(week_days[0], "n/a", "MMMM") + ": " + 
			  "Week starting " + rs.android.Util.To_String(week_days[0], "n/a", "EEEE dd/MM/yyyy");
		}
	}

	@Override
	public android.view.View On_Get_Col_Footer_View(String col_id)
	{
		Long[] week_event_ids;
		Long total_dur, event_type_id;
		String total_dur_str;
		java.sql.Date week_start, week_end;
		Double tot_dur_hr, tot_dur_min;
		android.widget.TextView cell=null;

		if (col_id.equals("date"))
		  cell = New_Footer_Cell("Totals");

		else if (col_id.startsWith("mins"))
		{
			week_start = rs.android.util.Date.Week_First_Day(this.week_of);
			android.util.Log.d("On_Get_Col_Footer_View", "week start: "+rs.android.Util.To_String(week_start, null, "EEEE dd/MM/yyyy h.mm.ss a"));
			week_end = rs.android.util.Date.Add_Days(week_start, 7);
			android.util.Log.d("On_Get_Col_Footer_View", "week end: "+rs.android.Util.To_String(week_end, null, "EEEE dd/MM/yyyy h.mm.ss a"));
			event_type_id = rs.android.Util.To_Long(col_id.substring(5));
			week_event_ids = Work_Event.Select_Timespan_Events(this.db, week_start, week_end, event_type_id, this.project_id, null);
			total_dur = Work_Event.Get_Events_Duration(this.db, week_event_ids);
			if (total_dur != null)
				tot_dur_min = (double)total_dur / (double)1000 / (double)60;
			else
				tot_dur_min = null;

			total_dur_str = rs.android.Util.To_String(tot_dur_min, "n/a", "#,##0.##");
			cell = New_Footer_Cell(total_dur_str);
		}

		else if (col_id.startsWith("hrs"))
		{
			week_start = rs.android.util.Date.Week_First_Day(this.week_of);
			week_end = rs.android.util.Date.Add_Days(week_start, 7);
			event_type_id = rs.android.Util.To_Long(col_id.substring(4));
			week_event_ids = Work_Event.Select_Timespan_Events(this.db, week_start, week_end, event_type_id, this.project_id, null);
			total_dur = Work_Event.Get_Events_Duration(this.db, week_event_ids);
			if (total_dur != null)
				tot_dur_hr = (double)total_dur / (double)1000 / (double)60 / (double)60;
			else
				tot_dur_hr = null;

			total_dur_str = rs.android.Util.To_String(tot_dur_hr, "n/a", "#,##0.##");
			cell = New_Footer_Cell(total_dur_str);
		}

		return cell;
	}

	@Override
	public android.view.View On_Get_Col_View(Object obj, String col_id)
	{
		Double dur_hr, dur_min;
		java.sql.Date date;
		android.widget.TextView cell=null;
		Long[] day_event_ids;
		Long event_type_id, dur;

		date = new java.sql.Date((Long)obj);

		if (col_id.equals("date"))
		{
			cell = this.New_Cell(rs.android.Util.To_String(date, "n/a", "EEEE"));
			//cell.setTextSize(18);
		}

		else if (col_id.startsWith("mins"))
		{
			event_type_id = rs.android.Util.To_Long(col_id.substring(5));
			day_event_ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, date, event_type_id, this.project_id);
			dur = Work_Event.Get_Events_Duration(this.db, day_event_ids);
			if (dur != null)
				dur_min = (double)dur / (double)1000 / (double)60;
			else
				dur_min = null;

			cell = New_Cell(rs.android.Util.To_String(dur_min, null, "#,##0.##"));
		}

		else if (col_id.startsWith("hrs"))
		{
			event_type_id = rs.android.Util.To_Long(col_id.substring(4));
			day_event_ids = rs.workbuddy.Work_Event.Select_Day_Events(this.db, date, event_type_id, this.project_id);
			dur = Work_Event.Get_Events_Duration(this.db, day_event_ids);
			if (dur != null)
				dur_hr = (double)dur / (double)1000 / (double)60 / (double)60;
			else
				dur_hr = null;

			cell = New_Cell(rs.android.Util.To_String(dur_hr, null, "#,##0.##"));
		}

		return cell;
	}

	@Override
	public Object On_Get_Obj(Long id)
	{
		return id;
	}

	@Override
	public Long[] On_Get_List()
	{
		return rs.android.util.Date.Week_In_Millis(this.week_of);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		rs.workbuddy.Project_Spinner proj_spinner;
		android.view.MenuItem menu_item;

		super.onCreateOptionsMenu(menu);

		menu.findItem(Menus.MENUITEM_TIMESHEET_SEND).setVisible(true);
		menu.findItem(Menus.MENUITEM_TIMESHEET_VIEW).setVisible(true);

		proj_spinner = new Project_Spinner(this, this.db);
		proj_spinner.setOnItemSelectedListener(this);
		proj_spinner.Get_Adapter().view_text_size = 15;
		menu_item = menu.findItem(Menus.MENUITEM_FILTER_PROJ);
		menu_item.setActionView(proj_spinner);
		menu_item.setVisible(true);

		return true;
	}

	public void onItemSelected(AdapterView<?> parent, 
	  android.view.View view, int position, long itemId)
	{
		if (itemId == rs.android.ui.Db_Adapter.ID_NA)
			this.project_id = null;
		else
			this.project_id = itemId;

		this.refresh_data = true;
		this.Update_UI();
	}

	public void onNothingSelected(AdapterView<?> parent)
	{

	}

	@Override
	public void On_Next()
	{
	  this.Set_Page(7);
	}

	@Override
	public void On_Prev()
	{
		this.Set_Page(-7);
	}

	public void Set_Page(int date_diff)
	{
		java.sql.Date next_week;

		next_week = rs.android.util.Date.Add_Days(this.week_of, date_diff);
		if ((date_diff > 0 && !next_week.after(this.max_day)) || 
		  (date_diff < 0 && !next_week.before(this.min_day)))
		{
			this.week_of = next_week;
			this.Set_Title();
		  this.refresh_data = true;
		  this.Update_UI();
		}
	}

  @Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=true;

		if (item.getItemId() == Menus.MENUITEM_TIMESHEET_SEND)
			On_Timesheet_Send();
		else if (item.getItemId() == Menus.MENUITEM_TIMESHEET_VIEW)
		  On_Timesheet_View();
		else
		  res = super.onOptionsItemSelected(item);

		return res;
	}

	public void On_Edit_Cols()
	{
		rs.android.ui.Column_Dialog dlg;

		dlg = new rs.android.ui.Column_Dialog(this, this);
		dlg.options = this.cols;
		dlg.Show();
	}

	public void On_Column_Set()
	{
		rs.android.ui.Column.Save(this, this.getClass().getName(), this.cols);
		this.refresh_data = true;
		this.Update_UI();
	}

	public void On_Timesheet_Send()
	{
    rs.workbuddy.Template_Dialog dlg;

		this.is_send = true;
    dlg = new rs.workbuddy.Template_Dialog(this, this);
    dlg.Show();
	}

	public void On_Timesheet_View()
	{ 
    rs.workbuddy.Template_Dialog dlg;

    this.is_view = true;
    dlg = new rs.workbuddy.Template_Dialog(this, this);
    dlg.Show();
	}

  public void On_Template_Set(String filename)
  {
    this.Build_Timesheet(this.week_of, filename);
  }

	public void Build_Timesheet(java.sql.Date week_of, String template_name)
	{
		java.lang.Thread thread;
		rs.workbuddy.Timesheet_Template zip;

		if (rs.android.Util.NotEmpty(week_of) && rs.android.Util.NotEmpty(template_name))
		{
			if (rs.android.util.File.Exists(template_name))
			{
				prog_label = new android.widget.TextView(this);
				prog_label.setText("Creating timesheet from template \"" + template_name + "\"...");
				prog_label.setPadding(20, 20, 20, 0);
				prog_label.setTextSize(15);
				this.table_layout.addView(prog_label, 0);

				this.prog_bar = new rs.android.ui.Progress_Bar(this);
				this.prog_bar.setIndeterminate(false);
				this.prog_bar.setMax(rs.android.Zip.Count_Entries(template_name));
				this.prog_bar.setProgress(0);
				this.prog_bar.setPadding(20, 0, 20, 20);
				this.table_layout.addView(this.prog_bar, 1);

				zip = new rs.workbuddy.Timesheet_Template();
				zip.ctx = this;
				zip.week_of = this.week_of;
				zip.db = this.db;
				zip.template_name = template_name;
				zip.timesheet_name = "timesheet.docx";
				zip.prog_bar = this.prog_bar;
				zip.on_finish_listener = this;
				zip.project_id = this.project_id;

				thread = new java.lang.Thread(zip);
				thread.start();
			}
			else
				rs.android.ui.Util.Show_Note(this, "Template not found.");
		}
	}

	public void On_Finish(android.net.Uri timesheet_uri)
	{
		android.content.Intent intent=null;
		String mime;
		android.webkit.MimeTypeMap type_map;
		android.content.pm.PackageManager pm;

		this.table_layout.removeView(this.prog_bar);
		this.table_layout.removeView(this.prog_label);

		if (this.is_send)
		{
			this.is_send = false;
			intent = new android.content.Intent(android.content.Intent.ACTION_SEND, timesheet_uri);
			intent.putExtra(android.content.Intent.EXTRA_STREAM, timesheet_uri);
		}
		if (this.is_view)
		{
			this.is_view = false;
			intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, timesheet_uri);
		}

		if (intent != null)
		{
			pm = this.getPackageManager();
			type_map = android.webkit.MimeTypeMap.getSingleton();
			if (type_map.hasExtension("docx"))
			  mime = type_map.getMimeTypeFromExtension("docx");
			else
			//mime="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
				mime = "application/msword";

			intent.setType(mime);
			//intent.setData(timesheet_uri);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

			intent = android.content.Intent.createChooser(intent, "Select");
			this.startActivity(intent);
		}
	}
}
