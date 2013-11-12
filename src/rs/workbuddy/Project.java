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
		
		res=(Long[])db.Select_Column(Long.class, 
		  "select p.id "+
			"from Project p "+
			"left join Status_Type s on s.id=p.status_type_id "+
			"where s.display_home=1");
		return res;
	}

	public static Long[] Select_Ids(rs.android.Db db)
	{
		Long[] res=null;

		res=(Long[])db.Select_Column(Long.class, "select p.id from Project p order by p.name");
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

		if (id!=null)
		  res = (Project)db.SelectObj(Project.class, id);
		return res;
	}
	
	public static String Get_Project_Name(rs.android.Db db, Long id)
	{
		String res=null;
		
		if (id!=null)
		  res=(String)db.Select_Value(String.class, "select name from Project where id=?", id);
		return res;
	}

	public String Get_Status_Name(rs.android.Db db)
	{
		return rs.workbuddy.db.Status_Type.Get_Name(db, this.status_type_id);
	}
}
