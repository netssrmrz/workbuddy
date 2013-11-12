package rs.workbuddy.db;

public class Status_Type
{
	public Long id;
	public String name;
	public Boolean display_home;
	
	public static String Get_Name(rs.android.Db db, Long id)
	{
		String res=null;
		
		if (id!=null)
		  res=(String)db.Select_Value(String.class, "select name from Status_Type where id=?", id);
		return res;
	}
}
