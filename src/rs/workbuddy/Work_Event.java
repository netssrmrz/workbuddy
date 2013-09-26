package rs.workbuddy;
import java.sql.*;
import java.lang.reflect.*;
import java.util.*;

public class Work_Event
{
	public Long id;
  public Long event_type;
  public java.sql.Date start_date;
	public String notes;
	public Long project_id;

	public static Long EVENT_TYPE_WORK=(long)1;
	public static Long EVENT_TYPE_LUNCH=(long)2;
	public static Long EVENT_TYPE_HOME=(long)3;
	public static Long EVENT_TYPE_TRAVEL=(long)4;
	public static Long EVENT_TYPE_MEETING=(long)5;

	public String Get_Project_Name(rs.android.Db db)
	{
		String res=null;
		Project project;
		
		project=Project.Select(db, this.project_id);
		if (project!=null)
		  res=project.name;
		return res;
	}
	
	public String Get_Event_Description()
	{
    return Get_Event_Description(this.event_type);
	}

	public static String Get_Event_Description(Long event_type)
	{
		String res="n/a";

		if (event_type != null)
		{
			if (event_type.equals(EVENT_TYPE_WORK))
				res = "Work";
			else if (event_type.equals(EVENT_TYPE_LUNCH))
				res = "Lunch";
			else if (event_type.equals(EVENT_TYPE_HOME))
				res = "Home";
			else if (event_type.equals(EVENT_TYPE_MEETING))
				res = "Meeting";
			else if (event_type.equals(EVENT_TYPE_TRAVEL))
				res = "Travel";
			else
			  res="unknown";
		}

		return res;
	}

  public void Save(rs.android.Db db) 
  {
    String sql;
    Work_Event we;

		db.Log("rs.workbuddy.Work_Event.Save()");
		if (rs.android.Util.NotEmpty(this.id)) // is update
		{
      db.Save(this);
		}
		else // is insert
    {
			sql =
				"select * " +
				"from work_event " +
				"where start_date<=? " +
				"order by start_date desc";
			we = (Work_Event)db.SelectObj(Work_Event.class, sql, this.start_date);
			if (!(we != null && we.event_type != null && we.event_type.equals(this.event_type)))
			{
				db.Save(this);
			}
		}
  }

	public static Work_Event Save_New(rs.android.Db db, Long event_type, Long rounding)
	{
		return Save_New(db, event_type, rs.android.Util.Now(), null, rounding);
	}

	public static Work_Event Save_New(rs.android.Db db, Long event_type, Long project_id, Long rounding) 
	{
		return Save_New(db, event_type, rs.android.Util.Now(), project_id, rounding);
	}

	public static Work_Event Save_New(rs.android.Db db, Long event_type, int hour, int minute) 
	{
		return Save_New(db, event_type, rs.android.Util.New_Time(hour, minute, 0), null, null);
	}

	public static Work_Event Save_New(
	  rs.android.Db db, 
	  Long event_type, 
		java.sql.Date date_time, 
		Long project_id, 
		Long rounding) 
	{
		Work_Event we;

		db.Log("rs.workbuddy.Work_Event.Save_New()");
		if (rs.android.Util.NotEmpty(rounding))
		{
			date_time = rs.android.Util.Round_Date(date_time, rounding);
		}

		we = new Work_Event();
		we.event_type = event_type;
		we.start_date = date_time;
		we.project_id = project_id;
		we.Save(db);

		return we;
	}

  public static Long[] Select_Day_Events(rs.android.Db db, java.sql.Date day, Long event_type)
  {
    String sql, where="";
    java.sql.Date day_start, day_end;
    Long[] res=null;
		Object[] col;

    day_start = (java.sql.Date)rs.android.Util.Round(day, rs.android.Util.ROUND_DATE_DAY);
    day_end = rs.android.Util.Add_Days(day_start, 1);
		if (rs.android.Util.NotEmpty(event_type))
			where = "and event_type=?";

    sql =
      "select id " +
      "from work_event " +
      "where start_date>=? and start_date<? " + where + " " +
      "order by start_date asc ";
		if (rs.android.Util.NotEmpty(event_type))
      col = db.Select_Column(Long.class, sql, day_start, day_end, event_type);
		else
      col = db.Select_Column(Long.class, sql, day_start, day_end);
		if (rs.android.Util.NotEmpty(col))
		{
			res = (Long[])java.lang.reflect.Array.newInstance(Long.class, col.length);
			System.arraycopy(col, 0, res, 0, col.length);
		}
    return res;
  }

	public static Long Get_Day_Events_Duration(rs.android.Db db, java.sql.Date day, Long event_type)
	{
		Long[] ids;
		Long res;

		ids = Select_Day_Events(db, day, event_type);
		res = Get_Events_Duration(db, ids);
		return res;
	}

  public static Long Get_Events_Duration(rs.android.Db db, Object[] ids)
  {
    Long res=null, total, duration;
    int c;
    Work_Event e;

    if (rs.android.Util.NotEmpty(ids))
    {
      total = (long)0;
      for (c = 0; c < ids.length; c++)
      {
        e = (Work_Event)db.SelectObj(Work_Event.class, (Long)(ids[c]));
        duration = e.Get_Event_Duration(db);
        if (duration != null)
          total += duration;
      }
      res = total;
    }
    return res;
  }

  public java.sql.Date Get_Event_End(rs.android.Db db)
  {
    java.sql.Date res=null, now;
    String sql;

    if (this.start_date != null)
    {
      sql =
        "select start_date " +
        "from work_event " +
        "where start_date>? " +
				"order by start_date asc";
      res = (java.sql.Date)db.Select_Value(sql, java.sql.Date.class, this.start_date);
			if (res == null)
			{
				now = rs.android.Util.Now();
				if (start_date.getTime() < now.getTime())
					res = now;
			}
    }

    return res;
  }

  public long Get_Event_Duration(rs.android.Db db)
  {
    long res=0;
    java.sql.Date end_date;

    end_date = this.Get_Event_End(db);
    if (end_date != null && this.start_date != null)
    {
      res = end_date.getTime() - start_date.getTime();
    }
    return res;
  }

	public static boolean Delete_Last(rs.android.Db db)
	{
		String sql;
		Long id;
		boolean res=false;

		sql = "select id from work_event order by start_date desc";
		id = (Long)db.Select_Value(sql, Long.class);
		if (id != null && db.Delete("work_event", "id=?", id) > 0)
			res = true;
		return res;
	}

	public static boolean Delete(rs.android.Db db, Long id)
	{
		boolean res=false;

		if (id != null && db.Delete("work_event", "id=?", id) > 0)
			res = true;
		return res;
	}

	public static Work_Event Select(rs.android.Db db, Long id)
	{
		Work_Event res=null;

		res = (Work_Event)db.SelectObj(Work_Event.class, id);
		return res;
	}

	public static java.util.ArrayList<Work_Event> Select_All(rs.android.Db db)
	{
    java.util.ArrayList<Work_Event> res=null;
		String sql;

		sql = "select * from work_event order by start_date desc";
		if (db != null)
		  res = (java.util.ArrayList<Work_Event>)db.Select_Objs(Work_Event.class, sql);
		return res;
	}

	public static Long Select_Prev_Event_Id(rs.android.Db db, java.sql.Date date)
	{
		Long res=null;
		String sql;

		if (rs.android.Util.NotEmpty(db))
		{
			sql = "select id from work_event where start_date<? order by start_date desc";
			res = (Long)db.Select_Value(sql, Long.class, date);
		}
		return res;
	}

	public static Work_Event Select_Prev_Event(rs.android.Db db, java.sql.Date date)
	{
		Long id;
	  Work_Event res=null;

		if (rs.android.Util.NotEmpty(db))
		{
			id = Select_Prev_Event_Id(db, date);
			res = (Work_Event)db.SelectObj(Work_Event.class, id);
		}
		return res;
	}
}
