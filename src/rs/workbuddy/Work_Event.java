package rs.workbuddy;

public class Work_Event
{
	public Long id;
  public Long event_type_id;
  public java.sql.Date start_date;
	public String notes;
	public Long project_id;

	public String Get_Project_Name(rs.android.Db db)
	{
		String res=null;
		Project project;
		
		project=Project.Select(db, this.project_id);
		if (project!=null)
		  res=project.name;
		return res;
	}

  public void Save(rs.android.Db db) 
  {
    Work_Event we;
		Long id;

		//db.Log("rs.workbuddy.Work_Event.Save()");
		if (rs.android.Util.NotEmpty(this.id)) // is update
		{
      db.Save(this);
		}
		else // is insert
    {
			id=Work_Event.Select_Prev_Event_Id(db, this.start_date);
			we=Work_Event.Select(db, id);
			
			if (!rs.android.Util.Equals(we.event_type_id, this.event_type_id) && !rs.android.Util.Equals(we.project_id, this.project_id))
			{
				db.Save(this);
			}
		}
  }
	
  public static Long[] Select_Day_Events(rs.android.Db db, java.sql.Date day, Long event_type)
  {
    java.sql.Date day_start, day_end;
    Long[] res=null;

    day_start = (java.sql.Date)rs.android.Util.Round(day, rs.android.Util.ROUND_DATE_DAY);
    day_end = rs.android.Util.Add_Days(day_start, 1);
		res=Select_Timespan_Events(db, day_start, day_end, event_type);
	
    return res;
  }

  public static Long[] Select_Timespan_Events(rs.android.Db db, java.sql.Date start_time, java.sql.Date end_time, Long event_type)
  {
    String sql, where="";
    Long[] res=null;
		Object[] col;

		if (rs.android.Util.NotEmpty(event_type))
			where = "and event_type_id=?";

    sql =
      "select id " +
      "from work_event " +
      "where start_date>=? and start_date<? " + where + " " +
      "order by start_date asc ";
		if (rs.android.Util.NotEmpty(event_type))
      col = db.Select_Column(Long.class, sql, start_time, end_time, event_type);
		else
      col = db.Select_Column(Long.class, sql, start_time, end_time);
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
      res = (java.sql.Date)db.Select_Value(java.sql.Date.class, sql, this.start_date);
			if (res == null)
			{
				now = rs.android.Util.Now();
				if (start_date.getTime() < now.getTime())
					res = now;
			}
    }

    return res;
  }

	public double Get_Event_Duration_Min(rs.android.Db db)
	{
		return (double)Get_Event_Duration(db)/(double)1000/(double)60;
	}
	
	public double Get_Event_Duration_Hr(rs.android.Db db)
	{
		return Get_Event_Duration_Min(db)/(double)60;
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
		id = (Long)db.Select_Value(Long.class, sql);
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

	public static Long[] Select_Ids(rs.android.Db db)
	{
    Long[] res=null;
		String sql;

		sql = "select id from work_event order by start_date desc";
		if (db != null)
		  res = (Long[])db.Select_Column(Long.class, sql);
		return res;
	}
	
	public static Long Select_Prev_Event_Id(rs.android.Db db, java.sql.Date date)
	{
		Long res=null;
		String sql;

		if (rs.android.Util.NotEmpty(db))
		{
			sql = "select id from work_event where start_date<? order by start_date desc";
			res = (Long)db.Select_Value(Long.class, sql, date);
		}
		return res;
	}

	public static Long Select_Next_Event_Id(rs.android.Db db, java.sql.Date date)
	{
		Long res=null;
		String sql;

		if (rs.android.Util.NotEmpty(db))
		{
			sql = "select id from work_event where start_date>? order by start_date asc";
			res = (Long)db.Select_Value(Long.class, sql, date);
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
	
	public Integer Get_Colour(rs.android.Db db)
	{
		Integer res;
		
		res = rs.workbuddy.db.Event_Type.Get_Colour(db, this.event_type_id);
		if (res==null)
			res=0xffffffff;
		return res;
	}
	
	public String Get_Type_Name(rs.android.Db db)
	{
		return rs.workbuddy.db.Event_Type.Get_Name(db, this.event_type_id);
	}
}
