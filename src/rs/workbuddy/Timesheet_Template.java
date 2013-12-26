package rs.workbuddy;
import android.net.*;

public class Timesheet_Template
extends rs.android.Zip
implements java.lang.Runnable
{
	public interface On_Finish_Listener
	{
		public void On_Finish(android.net.Uri timesheet_uri);
	}
	
  public android.content.Context ctx;
	public java.sql.Date week_of, week[];
	public Long project_id;
	public rs.android.Db db;
	public String template_name;
	public String timesheet_name;
	public rs.android.ui.Progress_Bar prog_bar;
	public On_Finish_Listener on_finish_listener;

	public void run()
	{
		this.Copy(this.template_name, this.timesheet_name);
	}

  @Override
	public void On_Copy_Entry(rs.android.Zip_Entry entry)
	{
		rs.android.Bookmark[] bookmarks;
		String data, str=null;
		java.lang.StringBuilder buff;
		int pos=0;

		if (this.prog_bar != null)
		{
			this.prog_bar.Inc();
		}
		if (entry.entry.getName().equals("word/document.xml"))
		{
			data = new String(entry.data);

			bookmarks = rs.android.Bookmark.Get_Bookmarks(data);
			if (rs.android.Util.NotEmpty(bookmarks))
			{
				buff = new java.lang.StringBuilder();
				this.week = rs.android.Util.Week(this.week_of);
				for (rs.android.Bookmark b: bookmarks)
				{
					buff.append(data.substring(pos, b.end_pos));

					str = this.Get_Bookmark_Data(b.name);
					if (rs.android.Util.NotEmpty(str))
					{
						str = "<w:r><w:t>" + str + "</w:t></w:r>";
						buff.append(str);						
					}

					pos = b.end_pos;
				}

				buff.append(data.substring(pos));
				data = buff.toString();
				entry.data = data.getBytes();
			}
		}
	}
	
	public class On_Finish
	implements java.lang.Runnable
	{
		public android.net.Uri timesheet_uri;
		
		public void run()
		{
			on_finish_listener.On_Finish(timesheet_uri);
		}
	}
	
	@Override
	public void On_Copy_Finish(android.net.Uri timesheet_uri)
	{
		On_Finish on_finish_fn;
		
		if (this.on_finish_listener!=null && this.prog_bar!=null)
		{
			on_finish_fn=new On_Finish();
			on_finish_fn.timesheet_uri=timesheet_uri;
			this.prog_bar.handler.post(on_finish_fn);
		}
	}

	public String Get_Bookmark_Data(String id)
	{
		String res=null;
		java.sql.Date date;

		//android.util.Log.d("Get_Bookmark_Data()", id);
		if (id.equals("wb_name"))
		{
			res = rs.workbuddy.Settings_Activity.Get_Timesheet_Name(this.ctx);
		}
		else if (id.equals("wb_company"))
		{
			res = rs.workbuddy.Settings_Activity.Get_Timesheet_Company(this.ctx);
		}
		else if (id.equals("wb_department"))
		{
			res = rs.workbuddy.Settings_Activity.Get_Timesheet_Dept(this.ctx);
		}
		else if (id.equals("wb_job_title"))
		{
			res = rs.workbuddy.Settings_Activity.Get_Timesheet_Job(this.ctx);
		}
		else if (id.equals("wb_manager"))
		{
			res = rs.workbuddy.Settings_Activity.Get_Timesheet_Man(this.ctx);
		}
		else if (id.equals("wb_week_ending"))
		{
			res = rs.android.Util.To_String(this.week[6], "n/a", "dd/MM/yyyy");
		}
		else if (id.equals("wb_week_no"))
		{
			res = rs.android.Util.To_String(this.week_of, "n/a", "w");
		}
		else if (id.equals("wb_signature_name"))
		{
			res = rs.workbuddy.Settings_Activity.Get_Timesheet_Sig(this.ctx);
		}
		else if (id.equals("wb_date"))
		{
			date = rs.android.Util.Now();
			res = rs.android.Util.To_String(date, "n/a", "dd/MM/yyyy");
		}
		else if (id.startsWith("wb_sun_"))
		{
	    res = this.Process_Day_Bookmark(this.week[0], id);
		}
		else if (id.startsWith("wb_mon_"))
		{
			res = this.Process_Day_Bookmark(this.week[1], id);
		}
		else if (id.startsWith("wb_tue_"))
		{
			res = this.Process_Day_Bookmark(this.week[2], id);
		}
		else if (id.startsWith("wb_wed_"))
		{
			res = this.Process_Day_Bookmark(this.week[3], id);
		} 
		else if (id.startsWith("wb_thu_"))
		{
			res = this.Process_Day_Bookmark(this.week[4], id);
		}
		else if (id.startsWith("wb_fri_"))
		{
			res = this.Process_Day_Bookmark(this.week[5], id);
		}
		else if (id.startsWith("wb_sat_"))
		{
			res = this.Process_Day_Bookmark(this.week[6], id);
		}
		else if (id.startsWith("wb_week_"))
		{
			res = this.Process_Week_Bookmark(this.week, id);
		}
		return res;
	}

	public String Process_Week_Bookmark(java.sql.Date[] week, String bookmark_id)
	{
		String event_type, res="n/a";
		Long event_type_id, ids[], dur;

		event_type = this.Extract_Bookmark_Event_Type(bookmark_id);
		event_type_id = this.Find_Event_Type(event_type);
		if (event_type_id != null)
		{
			ids = Work_Event.Select_Timespan_Events(this.db, week[0], rs.android.Util.Add_Days(week[6], 1), event_type_id, this.project_id, null);
			if (rs.android.Util.NotEmpty(ids))
			{
				dur = Work_Event.Get_Events_Duration(this.db, ids);
				if (bookmark_id.endsWith("_total_hrs") && dur != null)
				{
					res = rs.android.Util.To_String(Work_Event.Duration_To_Hrs(dur), null, "#,##0.##");
				}
				else if (bookmark_id.endsWith("_total_mins") && dur != null)
				{
					res = rs.android.Util.To_String(Work_Event.Duration_To_Mins(dur), null, "#,##0.##");
				}
			}
		}
		return res;
	}

	public String Process_Day_Bookmark(java.sql.Date day, String bookmark_id)
	{
		String event_type, res="n/a";
		Work_Event event;
		Long event_type_id, ids[], dur;

		if (bookmark_id.endsWith("_date"))
		{
      res=rs.android.Util.To_String(day, "n/a", "dd/MM/yyyy");
		}
		else
		{
			event_type = this.Extract_Bookmark_Event_Type(bookmark_id);
			event_type_id = this.Find_Event_Type(event_type);
			if (event_type_id != null)
			{
				ids = Work_Event.Select_Day_Events(this.db, day, event_type_id, this.project_id);
				if (rs.android.Util.NotEmpty(ids))
				{
					if (bookmark_id.endsWith("_start"))
					{
						event = Work_Event.Select(this.db, ids[0]);
						res = rs.android.Util.To_String(event.start_date, "n/a", "h:mm aa");
					}
					else if (bookmark_id.endsWith("_finish"))
					{
						event = Work_Event.Select(this.db, ids[ids.length - 1]);
						res = rs.android.Util.To_String(event.Get_Event_End(this.db), "n/a", "h:mm aa");
					}
					else if (bookmark_id.endsWith("_total"))
					{
						dur = Work_Event.Get_Events_Duration(this.db, ids);
						if (dur != null)
							res = rs.android.Util.To_String(Work_Event.Duration_To_Hrs(dur), null, "#,##0.##");
					}
					else if (bookmark_id.endsWith("_total_mins"))
					{
						dur = Work_Event.Get_Events_Duration(this.db, ids);
						if (dur != null)
							res = rs.android.Util.To_String(Work_Event.Duration_To_Mins(dur), null, "#,##0.##");
					}
				}
			}
		}
		return res;
	}

	public String Extract_Bookmark_Event_Type(String bookmark_id)
	{
		String[] tokens;
		String res=null;

		tokens = bookmark_id.split("_");
		res = tokens[2];
		return res;
	}

	public Long Find_Event_Type(String bookmark_type)
	{
		Long[] ids;
		String name;
		Long res=null;

		if (rs.android.Util.NotEmpty(bookmark_type))
		{
			ids = Work_Event.Select_Ids(this.db);
			if (rs.android.Util.NotEmpty(ids))
			{
				for (Long id: ids)
				{
					name = rs.workbuddy.db.Event_Type.Get_Name(this.db, id);
					if (rs.android.Util.NotEmpty(name))
					{
						name = name.replace(" ", "");
						name = name.toLowerCase();
						if (name.equals(bookmark_type))
						{
							res = id;
							break;
						}
					}
				}
			}
		}
		return res;
	}
}
