package rs.workbuddy;
import java.util.*;

public class Project
{
	public Long id;
	public String name;
	public String notes;
	public Long status_type_id;
	public Long parent_id;

	public static Long[] Select_Home_Ids(rs.android.Db db)
	{
		Long[] res=null;

		res = (Long[])db.Select_Column(Long.class, 
		  "select p.id " +
			"from Project p " +
			"left join Status_Type s on s.id=p.status_type_id " +
			"where s.display_home=1");
		return res;
	}

	public static Long[] Select_Ids(rs.android.Db db)
	{
		return Select_Ids(db, "p.name asc");
	}

	public static Long[] Select_Ids(rs.android.Db db, String order_by)
	{
		Long[] res=null;
		String sql;

		sql = db.Build_SQL_Str("p.id", "project p", null, order_by);
		res = (Long[])db.Select_Column(Long.class, sql);
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
		if (c!=null)
			res=c.intValue();
		return res;
	}
	
	public static Long[] Select_Root_Projects(rs.android.Db db)
	{
		return (Long[])db.Select_Column(Long.class, 
		  "select id from project where parent_id is null order by name asc");
	}
	
	public static Long[] Select_Children(rs.android.Db db, Long parent_id)
	{
		return (Long[])db.Select_Column(Long.class, 
		  "select id from project where parent_id=? order by status_type_id asc, name asc", parent_id);
	}

	public static boolean Is_Family(rs.android.Db db, Long parent_id, Long child_id)
	{
		boolean res=false;
		Long[] children;
		int c;

		children = Project.Select_Children(db, parent_id);
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
		
		children=Select_Children(db, id);
		if (rs.android.Util.NotEmpty(children))
			res=true;
		return res;
	}
	
	public static int Count_Parents(rs.android.Db db, Long id)
	{
		int res=0;
		Long[] ids;

		ids = Select_Parents(db, id);
		if (rs.android.Util.NotEmpty(ids))
			res=ids.length;
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

		status_type_id=(Long)db.Select_Value(Long.class, "select status_type_id from Project where id=?", id);
		res = rs.workbuddy.db.Status_Type.Get_Colour(db, status_type_id);
		if (res == null)
			res = 0xffffffff;
		return res;
	}
}
