package rs.workbuddy.project;
import android.view.*;

public class Project_ListAdapter
implements 
android.widget.ListAdapter,
android.widget.SpinnerAdapter,
android.view.View.OnClickListener
{
	public static final Long ID_NA=(long)-1;
  
	public java.util.ArrayList<Long> ids, open_ids;
	public rs.android.Db db;
	public java.util.ArrayList<android.database.DataSetObserver> observers;
  public android.view.View.OnClickListener label_click_listener;
	public boolean is_spinner, has_na, is_small_spinner;

	public Project_ListAdapter(rs.android.Db db, Long project_id,
  android.view.View.OnClickListener label_click_listener, boolean has_na)
	{
		this.has_na=has_na;
		this.observers = new java.util.ArrayList<android.database.DataSetObserver>();
		this.Set_Project(db, project_id, true);
		this.label_click_listener=label_click_listener;
	}
	
	public void Init()
	{
		if (this.ids==null)
		  this.ids = new java.util.ArrayList<Long>();
		else
			this.ids.clear();
			
		if (this.open_ids==null)
		  this.open_ids = new java.util.ArrayList<Long>();
		else
		  this.open_ids.clear();
	}
	
	public void Add_NA()
	{
		if (this.has_na)
		  ids.add(0, Project_SpinnerAdapter.ID_NA);
	}
	
	public void Set_Project(rs.android.Db db, Long id, boolean include_children)
	{
		Long[] parent_ids;

		this.Set_Db(db);
		this.Init();
		this.Open_Project(db, null);
		parent_ids = Project.Select_Parents(this.db, id);
		if (rs.android.Util.NotEmpty(parent_ids))
		{
			for (Long parent_id: parent_ids)
				this.Open_Project(db, parent_id);
				
			if (include_children && Project.Has_Children(this.db, id))
				this.Open_Project(db, id);
		}
		this.Add_NA();
	}
	
	// adds children to existing project
	public void Open_Project(rs.android.Db db, Long parent_id)
	{
		Long[] child_ids;
		int pos=0;

		if (!this.open_ids.contains(parent_id))
		{
			child_ids = Project.Select_Children(db, parent_id, null, 
				"status_type_id asc, name asc");
			if (rs.android.Util.NotEmpty(child_ids))
			{
				if (this.ids.contains(parent_id))
					pos=this.Get_Item_Position(parent_id)+1;
				ids.addAll(pos, java.util.Arrays.asList(child_ids));
				
				this.open_ids.add(parent_id);
				this.Notify_Observers();
			}
		}
	}

	public void Close_Project(Long parent_id)
	{
		Long[] child_ids;

		if (this.open_ids.contains(parent_id))
		{
			child_ids = Project.Select_Children(db, parent_id, null, null);
			if (rs.android.Util.NotEmpty(child_ids))
			{
				for (Long id: child_ids)
				{
					this.Close_Project(id);
					this.ids.remove(id);
				}
					
				this.Notify_Observers();
			}
			this.open_ids.remove(parent_id);
		}
	}

	@Override
	public void registerDataSetObserver(android.database.DataSetObserver observer)
	{
		this.observers.add(observer);
	}

	@Override
	public void unregisterDataSetObserver(android.database.DataSetObserver observer)
	{
		this.observers.remove(observer);
	}

	@Override
	public int getCount()
	{
		int res=0;

		if (rs.android.Util.NotEmpty(this.ids))
			res = this.ids.size();
		return res;
	}

	@Override
	public Object getItem(int idx)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public long getItemId(int idx)
	{
		long res=0;

		if (rs.android.Util.NotEmpty(this.ids))
			res = this.ids.get(idx);
		return res;
	}

  public int Get_Item_Position(Long id)
  {
    int res=-1;
    
    res=this.ids.indexOf(id);
    return res;
  }
  
	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public android.view.View getView(int idx, android.view.View view, 
	  android.view.ViewGroup parent_view)
	{
		android.view.View res=null;

		if (!this.is_spinner)
			res = this.Get_Normal_View(idx, parent_view);
		else
			res = this.Get_Spinner_View(idx, parent_view);

		return res;
	}
	
	@Override
	public View getDropDownView(int idx, android.view.View view, 
	  android.view.ViewGroup parent_view)
	{
		return this.Get_Normal_View(idx, parent_view);
	}
	
	public String Get_Project_Name(Long p_id)
	{
		String p_name=null;
		
		if (p_id.equals(Project_ListAdapter.ID_NA))
			p_name="n/a";
		else
		  p_name = rs.workbuddy.project.Project.Get_Project_Name(this.db, p_id);
			
		if (rs.android.Util.NotEmpty(p_name) && p_name.length()>40)
			p_name=p_name.substring(0, 40)+"...";
			
		return p_name;
	}
	
	public android.view.View Get_Spinner_View(int idx, 
	  android.view.ViewGroup parent_view)
	{
		android.widget.TextView label;
		String p_name;
		Long p_id;
		
		p_id=this.getItemId(idx);
		p_name=this.Get_Project_Name(p_id);
		
		label = new android.widget.TextView(parent_view.getContext());
		label.setText(p_name);
		if (this.is_small_spinner)
			label.setTextSize(15);
		else
		  label.setTextSize(20);
		
		return label;
	}
	
	public android.view.View Get_Normal_View(int idx, 
	  android.view.ViewGroup parent_view)
	{
		android.widget.TextView label;
		android.widget.ImageView handle;
		android.widget.LinearLayout layout;
		android.widget.LinearLayout.LayoutParams layout_params;
		Long p_id;
		String p_name;

		p_id=this.getItemId(idx);
		p_name=this.Get_Project_Name(p_id);

		handle = new android.widget.ImageView(parent_view.getContext());
		handle.setTag(p_id);
		if (!rs.workbuddy.project.Project.Has_Children(this.db, p_id))
		  handle.setVisibility(android.view.View.INVISIBLE);
		this.Set_Handle_Image(handle);
    handle.setOnClickListener(this);
		//rs.android.ui.Border_Drawable.Add_Border(handle, 0xffff0000);

		label = new android.widget.TextView(parent_view.getContext());
		label.setText(p_name);
		label.setClickable(true);
		label.setGravity(android.view.Gravity.CENTER_VERTICAL);
		label.setOnClickListener(this.label_click_listener);
    label.setTag(p_id);
		label.setPadding(0,0,20,0);
		//rs.android.ui.Border_Drawable.Add_Border(label, 0xffff0000);
		
		layout = new android.widget.LinearLayout(parent_view.getContext());
		layout_params=new android.widget.LinearLayout.LayoutParams(
		  android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
			android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		layout_params.leftMargin=rs.workbuddy.project.Project.Count_Parents
		  (this.db, p_id)*20;
    layout_params.topMargin=0;
    layout_params.bottomMargin=0;
    layout_params.rightMargin=0;
    layout_params.gravity=android.view.Gravity.CENTER;
		layout.addView(handle, layout_params);
		layout_params=new android.widget.LinearLayout.LayoutParams(
		  android.widget.LinearLayout.LayoutParams.FILL_PARENT,
			android.widget.LinearLayout.LayoutParams.FILL_PARENT);
		layout.addView(label, layout_params);
		//rs.android.ui.Border_Drawable.Add_Border(layout, 0xffff0000);
		
		return layout;
	}
	
	@Override
	public void onClick(android.view.View handle)
	{
		Long id;
    
		id = (Long)handle.getTag();
    
    //android.util.Log.d("onClick()", 
      //rs.workbuddy.project.Project.Get_Project_Name(this.db, id));

		if (!this.open_ids.contains(id))
			this.Open_Project(this.db, id);
		else
			this.Close_Project(id);
	}
  
  public void Set_Handle_Image(android.view.View handle)
  {
    Long id;

		id = (Long)handle.getTag();
    
    if (!this.open_ids.contains(id))
      handle.setBackgroundDrawable(rs.android.ui.Util.Get_Opened_Pic());
    else
      handle.setBackgroundDrawable(rs.android.ui.Util.Get_Closed_Pic());
  }

	public void Notify_Observers()
	{
		if (rs.android.Util.NotEmpty(this.observers))
			for (android.database.DataSetObserver observer: this.observers)
				observer.onChanged();
	}

	/*public android.view.View xGet_Open_View(int idx,
	  android.view.ViewGroup parent_view)
	{
		android.widget.ListView list;
		rs.workbuddy.project.Project_ListAdapter data;

		data = new rs.workbuddy.project.Project_ListAdapter(this.db, this.getItemId(idx),
      this.label_click_listener);

		list = new android.widget.ListView(parent_view.getContext());
		list.setAdapter(data);

		return list;
	}*/

	@Override
	public int getItemViewType(int idx)
	{
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public boolean isEmpty()
	{
		return !rs.android.Util.NotEmpty(this.ids);
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return true;
	}

	@Override
	public boolean isEnabled(int idx)
	{
		return true;
	}
	
  public void Set_Db(rs.android.Db db)
	{
		if (this.db==null || this.db.conn==null || !this.db.conn.isOpen())
		  this.db=db;
	}
}
