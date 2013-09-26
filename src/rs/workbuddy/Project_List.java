package rs.workbuddy;

public class Project_List
extends rs.workbuddy.Workbuddy_Activity_List
{
	public Project_List()
	{
		this.has_menuitem_add=true;
		this.has_menuitem_delete=true;
		this.has_menuitem_edit=true;

		edit_class=(java.lang.Class<android.app.Activity>)Project_Add.class;
		add_class=(java.lang.Class<android.app.Activity>)Project_Add.class;
	}

	@Override
	public void On_Build_Header_Row(android.widget.TableRow row)
	{
		Add_Header_Cell(row, "Name");
		Add_Header_Cell(row, "Rate");
	}

	@Override
	public long Get_Obj_Id(Object obj)
	{
		return ((Project)obj).id;
	}

	@Override
	public void On_Build_Row(Object obj, android.widget.TableRow row)
	{
		Project p;

		p=(Project)obj;

		Add_Cell(row, p.name);
		Add_Cell(row, rs.android.Util.To_String(p.rate, null, "#,##0.00"));
	}

	@Override
	public java.util.List<Object> On_Get_List()
	{
    return (java.util.List<Object>)Project.Select_All(this.db);
	}

	@Override
	public void On_Delete(Long id)
	{
		this.db.Delete("project", "id=?", id);
	}
}
