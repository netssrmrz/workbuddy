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
		rs.android.ui.Column col;
		
		this.has_menuitem_delete=true;
		
		menuitem_edit_class=Project_Add.class;
		menuitem_add_class=Project_Add.class;
		this.list_obj_class=Project.class;

		this.Add_Column("name", "Name");
		this.Add_Column("status", "Status");
		this.Add_Column("parents", "Parents");
		col=this.Add_Column("events", "Activities");
		col.align=rs.android.ui.Column.ALIGN_RIGHT;
		
		this.Add_Sort(SORT_NAME, "Name");
		this.Add_Sort(SORT_STATUS, "Status");
	  this.Add_Sort(SORT_PARENT, "Parent");
	}
	
	@Override
	public void onCreate(android.os.Bundle state)
	{
		int active_sort;
		
		super.onCreate(state);
		
		active_sort=rs.android.ui.Sort_Option.Load(this, this.getClass().getName());
		if (active_sort==SORT_PARENT)
			this.Set_Tree_Layout(true);
		else
			this.Set_Tree_Layout(false);
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
		  cell = this.New_Cell(rs.android.util.Type.To_String(p.Get_Status_Name(this.db), "n/a"));
			cell.setTextColor(rs.workbuddy.db.Status_Type.Get_Colour(this.db, p.status_type_id));
			res=cell;
		}

		else if (col_id.equals("parents"))
		  res = this.New_Cell(Build_Path(this.db, p.parent_id));
		
		else if (col_id.equals("events"))
			res=this.New_Cell(rs.android.util.Type.To_String(p.Get_Event_Count(this.db), "n/a", "#,##0"));

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
		Long[] res=null;
		
		active_sort=rs.android.ui.Sort_Option.Load(this, this.getClass().getName());
		
		if (active_sort!=0)
		{
			if (active_sort==SORT_NAME)
			{
				order_by="name asc";
			}

			else if (active_sort==SORT_STATUS)
			{
				order_by="status_type_id asc";
			}
		}
		
		if (this.has_tree_layout)
			res = Project.Select_Root_Projects(this.db);
		else
      res = Project.Select_Ids(this.db, order_by);
		
		return res;
	}

	@Override
	public boolean On_Delete(Long id)
	{
		boolean res=false;
		
		if (Project.Delete(this.db, id)>0)
			res=true;
		return res;
	}

	@Override
	public int On_Get_Branch_Level(Long id)
	{
		return Project.Count_Parents(this.db, id);
	}

	@Override
	public boolean On_Has_Children(Long id)
	{
		return Project.Has_Children(this.db, id);
	}
	
	@Override
	public Long[] On_Get_Children(Long id)
	{
		return Project.Select_Children(this.db, id);
	}
	
	@Override
	public void On_Sort_Set(rs.android.ui.Sort_Option which)
	{
		if (which.id==SORT_PARENT)
		  this.Set_Tree_Layout(true);
		else
		  this.Set_Tree_Layout(false);
		super.On_Sort_Set(which);
	}
	
	@Override
	public String On_Get_Delete_Msg()
	{
		String res=null;
		int total=0;
		
		if (rs.android.Util.NotEmpty(this.selected))
		{
			for (Long id: this.selected)
			  total=total+Project.Count_Events(this.db, id);
			if (total>0)
		    res=rs.android.util.Type.To_String(total, null, "#,##0") + " related activities will also be deleted.";
		}
		return res;
	}
}
