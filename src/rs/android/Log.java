package rs.android;
import java.sql.*;

public class Log
{
	public static final int OP_INSERT=1;
	public static final int OP_UPDATE=2;
	public static final int OP_DELETE=3;

	public Long id;
	public java.sql.Date log_date;
  public String obj_class;
	public Long obj_id;
	public String obj_data;
	public Integer operation;

	public static void Save(rs.android.Db db, Long id, Object obj, Class<?> obj_class,
	  int operation)
	{
		Log l;

		if (obj != null)
		{
			l = new Log();
			l.log_date = rs.android.util.Date.Now();
			l.obj_class = obj_class.getName();
			l.obj_id = id;
			if (obj != null)
				l.obj_data = rs.android.Util.Serialise(obj);
			else
			  l.obj_data = null;
			l.operation = operation;

			db.log = false;
			db.Save(l);
			db.log = true;
		}
	}

	public static boolean Has_Changes(rs.android.Db db, Class<?> obj_class, 
	  java.sql.Date since)
	{
		boolean res=true;
		String sql;
		Integer changes;

		if (obj_class != null && db != null && since != null)
		{
			sql = "select count(*) from Log where obj_class=? and log_date>?";
			changes = (Integer)db.Select_Value(Integer.class, sql, obj_class.getName(), since);
			if (rs.android.Util.NotEmpty(changes))
				res = true;
			else
				res=false;
		}
		return res;
	}
}
