package rs.workbuddy;
import java.sql.*;
import java.lang.reflect.*;
import java.util.*;

public class Work_Event
{
	public Long id;
  public String event_type;
  public java.sql.Date start_date;
	
	public static final String EVENT_TYPE_WORK="work";
	public static final String EVENT_TYPE_LUNCH="lunch";
	public static final String EVENT_TYPE_HOME="home";

  public void Save(rs.android.Db db) 
	throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    String sql;
    Work_Event we;

		db.Log("rs.workbuddy.Work_Event.Save()");
    sql=
      "select * "+
      "from work_event "+
      "where start_date<=? "+
			"order by start_date desc";
    we=(Work_Event)db.SelectObj(Work_Event.class, sql, this.start_date);
    if (!(we!=null && we.event_type!=null && we.event_type.equals(this.event_type)))
		{
      db.Save(this);
			db.Log("...work event saved (id: "+this.id+").");
		}
  }
	
	public static Work_Event Save_New(rs.android.Db db, String event_type) 
	throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		return Save_New(db, event_type, rs.android.Util.Now());
	}

	public static Work_Event Save_New(rs.android.Db db, String event_type, int hour, int minute) 
	throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		return Save_New(db, event_type, rs.android.Util.New_Time(hour, minute, 0));
	}
	
	public static Work_Event Save_New(rs.android.Db db, String event_type, java.sql.Date date_time) 
	throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Work_Event we;

		we=new Work_Event();
		we.event_type=event_type;
		we.start_date=date_time;
		we.Save(db);

		return we;
	}
	
	public static java.sql.Date Get_Day_Start(rs.android.Db db, java.sql.Date day, String event_type)
	{
		String sql;
		java.sql.Date res=null, day_start, day_end;
		
		db.Log("rs.android.Util.Get_Day_Start()");
		sql=
		  "select start_date "+
		  "from work_event "+
		  "where start_date>=? and start_date<? and event_type=? "+
		  "order by start_date asc";
	  day_start=(java.sql.Date)rs.android.Util.Round(day, rs.android.Util.ROUND_DATE_DAY);
		day_end=rs.android.Util.Add_Days(day_start, 1);
		res=(java.sql.Date)db.Select_Value(sql, java.sql.Date.class, day_start, day_end, event_type);
		
		return res;
	}
  
  public static java.sql.Date Get_Day_End(rs.android.Db db, java.sql.Date day, String event_type)
  {
    String sql;
    java.sql.Date res=null, day_start, day_end, event_start,
      event_end;

    day_start=(java.sql.Date)rs.android.Util.Round(day, rs.android.Util.ROUND_DATE_DAY);
    day_end=rs.android.Util.Add_Days(day_start, 1);
    
    // find last
    db.Log("rs.android.Util.Get_Day_End()");
    sql=
      "select start_date "+
      "from work_event "+
      "where start_date>=? and start_date<? and event_type=? "+
      "order by start_date desc";
    event_start=(java.sql.Date)db.Select_Value(sql, java.sql.Date.class, day_start, day_end, event_type);

    sql=
      "select start_date "+
      "from work_event "+
      "where start_date>=? and start_date<? and event_type<>? "+
      "order by start_date asc";
    event_end=(java.sql.Date)db.Select_Value(sql, java.sql.Date.class, day_start, day_end, event_type);

    return res;
	}
  
  public static Object[] Get_Day_Events(rs.android.Db db, java.sql.Date day, String event_type)
  {
    String sql;
    java.sql.Date day_start, day_end;
    Object[] res=null;
    
    db.Log("rs.android.Work_Event.Get_Day_Events(day: "+rs.android.Util.To_String(day)+", event_type: "+event_type+")");
    day_start=(java.sql.Date)rs.android.Util.Round(day, rs.android.Util.ROUND_DATE_DAY);
    db.Log("…Has day_start "+rs.android.Util.To_String(day_start));
    day_end=rs.android.Util.Add_Days(day_start, 1);
    db.Log("…Has day_end "+rs.android.Util.To_String(day_end));
    
    sql=
      "select id "+
      "from work_event "+
      "where start_date>=? and start_date<? and event_type=? "+
      "order by start_date asc ";
    res=db.Select_Column(Integer.class, sql, day_start, day_end, event_type);
    if (rs.android.Util.NotEmpty(res))
      db.Log("…Has "+event_type+" events "+res.length);
		else
		  db.Log("…No events found.");
    return res;
  }
  
  public static Long Get_Events_Duration(rs.android.Db db, Object[] ids)
  {
    Long res=null, total, duration;
    int c;
    Work_Event e;
    
    if (rs.android.Util.NotEmpty(ids))
    {
      total=(long)0;
      for (c=0; c<ids.length; c++)
      {
        e=(Work_Event)db.SelectObj(Work_Event.class, (Integer)(ids[c]));
        duration=e.Get_Event_Duration(db);
        if (duration!=null)
          total+=duration;
      }
      res=total;
    }
    return res;
  }
  
  public java.sql.Date Get_Event_End(rs.android.Db db)
  {
    java.sql.Date res=null, now;
    String sql;
    
    if (this.start_date!=null)
    {
      sql=
        "select start_date "+
        "from work_event "+
        "where start_date>? ";
      res=(java.sql.Date)db.Select_Value(sql, java.sql.Date.class, this.start_date);
			if (res==null)
			{
				now=rs.android.Util.Now();
				if (start_date.getTime()<now.getTime())
					res=now;
			}
    }
    
    return res;
  }
  
  public long Get_Event_Duration(rs.android.Db db)
  {
    long res=0;
    java.sql.Date end_date;
    
    end_date=this.Get_Event_End(db);
    if (end_date!=null && this.start_date!=null)
    {
      res=end_date.getTime()-start_date.getTime();
    }
    return res;
  }
	
	public static boolean Delete_Last(rs.android.Db db)
	{
		String sql;
		Long id;
		boolean res=false;
		
		sql="select id from work_event order by start_date desc";
		id=(Long)db.Select_Value(sql, Long.class);
		if (id!=null && db.Delete("work_event", "id=?", id)>0)
			res=true;
		return res;
	}
	
	public static boolean Delete(rs.android.Db db, Long id)
	{
		boolean res=false;

		if (id!=null && db.Delete("work_event", "id=?", id)>0)
			res=true;
		return res;
	}
	
	public static java.util.ArrayList<Work_Event> Select_All(rs.android.Db db)
	{
    java.util.ArrayList<Work_Event> res=null;
		String sql;
		
		sql="select * from work_event order by start_date desc";
		if (db!=null)
		  res=(java.util.ArrayList<Work_Event>)db.SelectObjs(Work_Event.class, sql);
		return res;
	}
}
