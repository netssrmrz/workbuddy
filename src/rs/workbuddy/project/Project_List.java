package rs.workbuddy.project;

public class Project_List
extends rs.workbuddy.Workbuddy_Activity_List
implements rs.workbuddy.project.Project_Dialog.On_Project_Set_Listener
{
	public static final int SORT_NAME=1;
	public static final int SORT_STATUS=2;
	public static final int SORT_PARENT=3;
	
	public static final int FILTER_ALL=1;
	public static final int FILTER_ACTIVE=2;
	public static final int FILTER_INACTIVE=3;
	
  public String ct="rs.workbuddy.Workbuddy_Activity_List";
	
	public Project_List()
	{
		rs.android.ui.Column col;
		
		this.has_menuitem_delete=true;
		
		menuitem_edit_class=Project_Add.class;
		menuitem_add_class=Project_Add.class;
		this.list_obj_class=Project.class;

		this.Add_Column("name", "Name", true, true);
		this.Add_Column("status", "Status", false, true);
		this.Add_Column("parents", "Parents", true, false);
		col=this.Add_Column("events", "Activities", false, false);
		col.align=rs.android.ui.Column.ALIGN_RIGHT;
		
		this.Add_Sort(SORT_NAME, "Project Name");
		this.Add_Sort(SORT_STATUS, "Project Status");
	  this.Add_Sort(SORT_PARENT, "Project Parent (Hierarchical View)");
		
		this.Add_Filter(FILTER_ALL, "All Projects");
		this.Add_Filter(FILTER_ACTIVE, "Active Projects");
		this.Add_Filter(FILTER_INACTIVE, "Inactive Projects");
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
	
  public String On_Build_Sort()
  {
    String order_by=null;
    int active_sort;

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
		return order_by;
  }
	
	public String On_Build_Filter()
	{
		String where=null, id_list;
		int filter_id;
		Object[] ids;
		
		filter_id=rs.android.ui.Filter_Option.Load(this, this.getClass().getName());
		if (filter_id!=0)
		{
			if (filter_id==FILTER_ACTIVE)
			{
				ids=this.db.Select_Column(Long.class, "select id from Status_Type "+
				  "where name in ('Pending','In Progress','On Hold')");
				id_list=rs.android.Util.Build_Str_List(ids, null, ", ", null);
				where="status_type_id in ("+id_list+")";
			}
			else if (filter_id==FILTER_INACTIVE)
			{
				ids=this.db.Select_Column(Long.class, "select id from Status_Type "+
				  "where name in ('Completed','Cancelled')");
				id_list=rs.android.Util.Build_Str_List(ids, null, ", ", null);
				where="status_type_id in ("+id_list+")";
			}
		}
		return where;
	}
  
	@Override
	public Long[] On_Get_List()
	{
		String order_by=null, where=null;
		Long[] res=null;
		
		where=this.On_Build_Filter();
    order_by=this.On_Build_Sort();
    res = Project.Select_Ids(this.db, where, order_by);
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
		String where=null;
		Long[] res=null;
		
		where=this.On_Build_Filter();
		res = Project.Select_Children(this.db, id, where, "status_type_id asc, name asc");
		return res;
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
	
  @Override
	public void On_Add()
	{
		android.content.Intent i;

		if (rs.android.Util.NotEmpty(this.selected))
		{
			i = new android.content.Intent(this, this.menuitem_edit_class);
			i.putExtra("parent_id", this.selected.get(0));
			this.startActivityForResult(i, ACT_RES_REFRESH_UI);
		}
		else
			super.On_Add();
	}
	
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		menu.findItem(rs.workbuddy.Menus.MENUITEM_PROJECTLIST_MOVE).setVisible(true);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=true;

		if (item.getItemId() == rs.workbuddy.Menus.MENUITEM_PROJECTLIST_MOVE)
			this.On_Move();
		else
		  res = super.onOptionsItemSelected(item);

		return res;
	}
	
	public void On_Move()
	{
		rs.workbuddy.project.Project_Dialog dlg;

		if (this.selected.size()>0)
		{
		  dlg = new rs.workbuddy.project.Project_Dialog(this, this, this.db);
		  dlg.Show();
		}
	}
	
	public void On_Project_Set(Long id)
	{
		rs.workbuddy.project.Project p;
		
		if (rs.android.Util.NotEmpty(this.selected))
		{
			for (Long sel_id: selected)
			{
				p=rs.workbuddy.project.Project.Select(this.db, sel_id);
				p.parent_id=id;
				this.db.Save(p);
			}
			this.selected.clear();
			this.refresh_data = true;
			this.Update_UI();
		}
	}
}
