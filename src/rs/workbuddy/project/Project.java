package rs.workbuddy.project;
import java.util.*;

public class Project
implements java.io.Serializable
{
	public Long id;
	public String name;
	public String notes;
	public Long status_type_id;
	public Long parent_id;

	public static Long[] Select_Home_Ids(rs.android.Db db)
	{
		Long[] res=null;
		String sql;

		sql =
			"select p.id " +
			"from project p " +
			  "left join " +
			    "(SELECT project_id, max(start_date) last_date " +
			    "FROM work_event " +
			    "group by project_id " +
			    "order by start_date desc) d on d.project_id=p.id " +
			  "left join status_type s on s.id=p.status_type_id " +
			"where s.display_home=1 " +
			"order by last_date desc ";

		res = (Long[])db.Select_Column(Long.class, sql);
		if (rs.android.Util.NotEmpty(res) && res.length>8)
		{
			res=java.util.Arrays.copyOf(res, 8);
		}
		
		return res;
	}

	public static Long[] Select_Ids(rs.android.Db db)
	{
		return Select_Ids(db, null, "p.name asc");
	}
	
	public static Long[] Select_Ids(rs.android.Db db, String where, String order_by)
	{
		Long[] res=null;
		String sql;

		sql = db.Build_SQL_Str("p.id", "project p", where, order_by);
		res = (Long[])db.Select_Column(Long.class, sql);
		return res;
	}
	
	public static Long[] Select_Ids(rs.android.Db db, String where, String order_by, Object ... params)
	{
		Long[] res=null;
		String sql;

		sql = db.Build_SQL_Str("p.id", "project p", where, order_by);
		res = (Long[])db.Select_Column(Long.class, sql, params);
		return res;
	}

	public static java.util.List<Project> Select_All(rs.android.Db db)
	{
		java.util.ArrayList<Project> res=null;
		String sql;

		sql = "select * from project order by name asc";
		if (db != null)
		  res = (java.util.ArrayList<Project>)db.Select_Objs(Project.class, sql);
		return res;
	}

	public static Project Select(rs.android.Db db, Long id)
	{
		Project res=null;

		if (id != null)
		  res = (Project)db.SelectObj(Project.class, id);
		return res;
	}

	public static String Get_Project_Name(rs.android.Db db, Long id)
	{
		String res=null;

		if (id != null)
		  res = (String)db.Select_Value(String.class, "select name from Project where id=?", id);
		return res;
	}

	public String Get_Status_Name(rs.android.Db db)
	{
		return rs.workbuddy.db.Status_Type.Get_Name(db, this.status_type_id);
	}

	public static int Count_Children(rs.android.Db db, Long parent_id)
	{
    Integer c=null;
		int res=0;

		c = (Integer)db.Select_Value(Integer.class, 
		  "select count(*) from project where parent_id=?", parent_id);
		if (c != null)
			res = c.intValue();
		return res;
	}

	public static Long[] Select_Root_Projects(rs.android.Db db, String where, 
	  String order_by)
	{
		String sql;
		Long[] res=null;
		
		where=rs.android.Util.AppendStr(where, "(parent_id is null)", " and ");
		sql = db.Build_SQL_Str("p.id", "project p", where, order_by);
		res = (Long[])db.Select_Column(Long.class, sql);
		return res;
	}

	public static Long[] Select_Children(rs.android.Db db, Long parent_id, 
	  String where, String order_by)
	{
		String sql;
		Long[] res=null;
		
		if (parent_id!=null)
		  where=rs.android.Util.AppendStr(where, "(parent_id=?)", " and ");
		else
			where=rs.android.Util.AppendStr(where, "(parent_id is null)", " and ");
			
		sql = db.Build_SQL_Str("p.id", "project p", where, order_by);
		
		if (parent_id!=null)
		  res = (Long[])db.Select_Column(Long.class, sql, parent_id);
		else
			res = (Long[])db.Select_Column(Long.class, sql);
			
		return res;
	}

	public static boolean Is_Family(rs.android.Db db, Long parent_id, Long child_id)
	{
		boolean res=false;
		Long[] children;
		int c;

		children = Project.Select_Children(db, parent_id, null, null);
		if (rs.android.Util.NotEmpty(children))
		{
			if (java.util.Arrays.asList(children).contains(child_id))
				res = true;
			else
			{
				for (c = 0; c < children.length; c++)
				{
					if (Is_Family(db, children[c], child_id))
					{
						res = true;
						break;
					}
				}
			}
		}
		return res;
	}

	public static boolean Has_Children(rs.android.Db db, Long id)
	{
		boolean res=false;
		Long[] children;

		children = Select_Children(db, id, null, null);
		if (rs.android.Util.NotEmpty(children))
			res = true;
		return res;
	}

	public static int Count_Parents(rs.android.Db db, Long id)
	{
		int res=0;
		Long[] ids;

		ids = Select_Parents(db, id);
		if (rs.android.Util.NotEmpty(ids))
			res = ids.length;
		return res;
	}

	public static Long[] Select_Parents(rs.android.Db db, Long id)
	{
		Long[] res=null;
		String sql;
		Long parent_id;
		java.util.ArrayList<Long> ids;

		if (db != null && rs.android.Util.NotEmpty(id))
		{
			sql = "select parent_id from Project where id=?";
			ids = new java.util.ArrayList<Long>();
			do
			{
				parent_id = (Long)db.Select_Value(Long.class, sql, id);
				if (parent_id != null)
				{
					ids.add(0, parent_id);
					id = parent_id;
				}
			}
			while (parent_id != null);

			if (rs.android.Util.NotEmpty(ids))
			{
				res = ids.toArray(new Long[ids.size()]);
			}
		}
		return res;
	}

	public static Integer Get_Colour(rs.android.Db db, Long id)
	{
		Integer res;
		Long status_type_id;

		status_type_id = (Long)db.Select_Value(Long.class, "select status_type_id from Project where id=?", id);
		res = rs.workbuddy.db.Status_Type.Get_Colour(db, status_type_id);
		if (res == null)
			res = 0xffffffff;
		return res;
	}

	public Integer Get_Event_Count(rs.android.Db db)
	{
		return Count_Events(db, this.id);
	}

	public static int Count_Events(rs.android.Db db, Long id)
	{
		return (Integer)db.Select_Value(Integer.class, "select count(*) from Work_Event where project_id=?", id);
	}

	public static int Delete(rs.android.Db db, Long id)
	{
		return db.Delete(id, Project.class);
	}

	public static Long Select_Last_Used(rs.android.Db db, java.sql.Date date)
	{
		Long res=null;
		String sql;

		sql =
		  "SELECT e.project_id " +
			"FROM Work_Event e " +
			"where e.start_date<? and e.project_id is not null " +
			"order by e.start_date desc";
		res = (Long)db.Select_Value(Long.class, sql, date);
		return res;
	}
}
