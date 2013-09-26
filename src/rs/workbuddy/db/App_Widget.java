package rs.workbuddy.db;

public class App_Widget
{
	public Long id;
  public Long event_type;
	public Long project_id;
	
	public static App_Widget Select_By_Id(rs.android.Db db, Long id)
	{
		return (rs.workbuddy.db.App_Widget)db.SelectObj(rs.workbuddy.db.App_Widget.class, id);
	}
}
