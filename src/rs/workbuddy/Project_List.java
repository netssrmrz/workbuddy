package rs.workbuddy;

public class Project_List
extends rs.workbuddy.Workbuddy_Activity_List
{
	public static final int SORT_NAME=1;
	public static final int SORT_STATUS=2;
	public static final int SORT_PARENT=3;
	
  public String ct="rs.workbuddy.Workbuddy_Activity_List";
	
	public Project_List()
	{
		this.has_menuitem_delete=true;
		
		menuitem_edit_class=Project_Add.class;
		menuitem_add_class=Project_Add.class;

		this.Add_Column("name", "Name");
		this.Add_Column("status", "Status");
		this.Add_Column("parents", "Parents");
		
		this.Add_Sort(SORT_NAME, "Name");
		this.Add_Sort(SORT_STATUS, "Status");
	  this.Add_Sort(SORT_PARENT, "Parent");
	}

	@Override
	public android.view.View On_Get_Col_View(Object obj, String col_id)
	{
		android.view.View res=null;
		android.widget.TextView cell;
		Project p;

		p=(Project)obj;

		if (col_id.equals("name"))
		  res = this.New_Cell(p.name);

		else if (col_id.equals("status"))
		{
		  cell = this.New_Cell(rs.android.Util.To_String(p.Get_Status_Name(this.db), "n/a"));
			cell.setTextColor(rs.workbuddy.db.Status_Type.Get_Colour(this.db, p.status_type_id));
			res=cell;
		}

		else if (col_id.equals("parents"))
		  res = this.New_Cell(Build_Path(this.db, p.parent_id));

		return res;
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
	public Object On_Get_Obj(Long id)
	{
		return Project.Select(this.db, id);
	}
	
	@Override
	public Long[] On_Get_List()
	{
		String order_by=null;
		int active_sort;
		
		active_sort=rs.android.ui.Sort_Option.Load(this, this.getClass().getName());
		
		if (active_sort!=0)
		{
			if (active_sort==SORT_NAME)
				order_by="name asc";

			else if (active_sort==SORT_STATUS)
				order_by="status_type_id asc";

			else if (active_sort==SORT_PARENT)
				order_by="parent_id asc";
		}
    return Project.Select_Ids(this.db, order_by);
	}

	@Override
	public void On_Delete(Long id)
	{
		this.db.Delete("project", "id=?", id);
	}
}
