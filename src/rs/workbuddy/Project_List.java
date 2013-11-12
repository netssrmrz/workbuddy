package rs.workbuddy;

public class Project_List
extends rs.workbuddy.Workbuddy_Activity_List
{
  public String ct="rs.workbuddy.Workbuddy_Activity_List";
	
	public Project_List()
	{
		this.has_menuitem_delete=true;
		
		menuitem_edit_class=Project_Add.class;
		menuitem_add_class=Project_Add.class;
	}

	@Override
	public void On_Build_Header_Row(android.widget.TableRow row)
	{
		row.addView(New_Header_Cell("Name"));
		row.addView(New_Header_Cell("Status"));
		row.addView(New_Header_Cell("Parents"));
	}

	@Override
	public void On_Build_Row(Long id, android.widget.TableRow row)
	{
		Project p;
	
		p=Project.Select(this.db, id);

		row.addView(New_Cell(p.name));
		row.addView(New_Cell(rs.android.Util.To_String(p.Get_Status_Name(this.db), "n/a")));
		row.addView(New_Cell(Build_Path(this.db, p.parent_id)));
	}
	
	public String Build_Path(rs.android.Db db, Long id)
	{
		String res=null;
		Project p;
		
		if (id!=null)
		{
			p=Project.Select(db, id);
			res=rs.android.Util.AppendStr(Build_Path(db, p.parent_id), p.name, " / ");
		}
		return res;
	}

	@Override
	public Long[] On_Get_List()
	{
    return Project.Select_Ids(this.db);
	}

	@Override
	public void On_Delete(Long id)
	{
		this.db.Delete("project", "id=?", id);
	}
}
