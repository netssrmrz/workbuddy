package rs.workbuddy.db;

public class Event_Type
{
  public Long id;
	public String name;
	public Boolean display_home_projects;
	public Integer colour;
	public String template_id;
	
	public static Integer Get_Colour(rs.android.Db db, Long id)
	{
		Integer res=null;
		
		if (id!=null)
		  res=(Integer)db.Select_Value(Integer.class, "select colour from Event_Type where id=?", id);
		return res;
	}
	
	public static String Get_Name(rs.android.Db db, Long id)
	{
		String res=null;

		if (id!=null)
		  res=(String)db.Select_Value(String.class, "select name from Event_Type where id=?", id);
		return res;		
	}
	
	public static Long[] Select_Ids(rs.android.Db db)
	{
		Long[] res=null;
		String sql;
		
		sql="select id from Event_Type order by name asc";
		res=(Long[])db.Select_Column(Long.class, sql);
		return res;
	}

	public static Long[] Select_Home_Ids(rs.android.Db db)
	{
		Long[] res=null;

		res=(Long[])db.Select_Column(Long.class, 
																 "select id "+
																 "from Event_Type "+
																 "where display_home_projects=1");
		return res;
	}
}
