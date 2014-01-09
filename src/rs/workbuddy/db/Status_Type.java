package rs.workbuddy.db;

public class Status_Type
{
	public Long id;
	public String name;
	public Boolean display_home;
	public Integer colour;

	public static Integer Get_Colour(rs.android.Db db, Long id)
	{
		Integer res=0xffbbbbbb;

		if (id!=null)
		  res=(Integer)db.Select_Value(Integer.class, "select colour from Status_Type where id=?", id);
		return res;
	}
	
	public static String Get_Name(rs.android.Db db, Long id)
	{
		String res=null;
		
		if (id!=null)
		  res=(String)db.Select_Value(String.class, "select name from Status_Type where id=?", id);
		return res;
	}
}
