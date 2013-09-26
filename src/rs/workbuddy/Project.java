package rs.workbuddy;

public class Project
{
	public Long id;
	public String name;
	public String notes;
	// location
	// employer
	public Double rate;
	//public Boolean work_shortcut;
	
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

		res = (Project)db.SelectObj(Project.class, id);
		return res;
	}
	
	public static String Get_Project_Name(rs.android.Db db, Long id)
	{
		String res=null;
		
		if (id!=null)
		  res=(String)db.Select_Value("select name from Project where id=?", Long.class, id);
		return res;
	}
}
