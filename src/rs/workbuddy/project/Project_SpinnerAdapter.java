package rs.workbuddy.project;

public class Project_SpinnerAdapter
implements 
android.widget.SpinnerAdapter,
android.view.View.OnClickListener
{
	public static final Long ID_NA=(long)-1;
	public static android.graphics.drawable.PictureDrawable branch_opened_drawable;
	public static android.graphics.drawable.PictureDrawable branch_closed_drawable;
	public static android.graphics.drawable.PictureDrawable branch_pressed_drawable;
	
	public java.util.ArrayList<Long> ids, open_ids;
	public rs.android.Db db;
	public android.database.DataSetObserver observer;
	public android.view.View.OnClickListener 
	on_project_clicked_listener, on_project_selected_listener;
	public float view_text_size, dropdown_text_size;
	android.content.Context ctx;

	@Override
	public void registerDataSetObserver(android.database.DataSetObserver observer)
	{
		this.observer = observer;
	}

	@Override
	public void unregisterDataSetObserver(android.database.DataSetObserver p1)
	{
		this.observer = null;
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
		Object item=null;

		if (rs.android.Util.NotEmpty(this.ids))
			item = rs.workbuddy.project.Project.Select(this.db, this.ids.get(idx));
		return item;
	}

	@Override
	public long getItemId(int idx)
	{
		long res=0;

		if (rs.android.Util.NotEmpty(this.ids))
			res = this.ids.get(idx);
		return res;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public android.view.View getView(int idx, android.view.View p2, android.view.ViewGroup container)
	{
		android.widget.TextView project_name_label;
		String project_name;
		Long project_id;

		if (!this.db.conn.isOpen())
			this.db=new rs.workbuddy.Db(this.ctx);
		project_id = this.ids.get(idx);
		if (project_id == ID_NA)
			project_name = "N/A";
		else
		  project_name = rs.workbuddy.project.Project.Get_Project_Name(this.db, project_id);

		project_name_label = new android.widget.TextView(container.getContext());
		//if (this.view_text_size > 0)
		  //project_name_label.setTextSize(this.view_text_size);
	  project_name_label.setTextSize(20);
		project_name_label.setText(project_name);

		return project_name_label;
	}

	@Override
	public int getItemViewType(int p1)
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
		boolean res=true;

		if (rs.android.Util.NotEmpty(this.ids))
			res = false;
		return res;
	}
	
	public static android.graphics.drawable.StateListDrawable Get_Branch_Drawable()
	{
		android.graphics.drawable.StateListDrawable res=null;

		if (branch_opened_drawable==null)
			branch_opened_drawable=rs.android.ui.Util.Get_Opened_Pic();
		if (branch_closed_drawable==null)
			branch_closed_drawable=rs.android.ui.Util.Get_Closed_Pic();
		if (branch_pressed_drawable==null)
			branch_pressed_drawable=rs.android.ui.Util.Get_Pressed_Pic();

		res = new android.graphics.drawable.StateListDrawable();
		//res.addState(new int[] {-android.R.attr.state_checked}, branch_opened_drawable);
		res.addState(new int[] 
		{
			android.R.attr.state_selected, 
		  android.R.attr.state_focused, 
		  android.R.attr.state_pressed, 
		  android.R.attr.state_activated, 
		  android.R.attr.state_active, 
		  android.R.attr.state_checkable, 
		  android.R.attr.state_empty, 
		  android.R.attr.state_enabled, 
		  android.R.attr.state_expanded, 
		  android.R.attr.state_first, 
		  android.R.attr.state_hovered, 
		  android.R.attr.state_last, 
		  android.R.attr.state_middle, 
		  android.R.attr.state_multiline, 
		  android.R.attr.state_single, 
		  android.R.attr.state_checkable, 
			android.R.attr.state_checked
		}, rs.android.ui.Util.Get_Test_Pic());
		
		return res;
	}
	
	@Override
	public android.view.View getDropDownView(int idx, android.view.View p2, 
	  android.view.ViewGroup container)
	{
		Long project_id;
		
		project_id=this.ids.get(idx);
		return Get_Project_View(container.getContext(), project_id, idx, this.db,
		  this.open_ids.contains(project_id), this);	
	}
		
	public static android.view.View Get_Project_View(android.content.Context ctx,
	Long project_id, Object tag, rs.android.Db db, boolean checked, 
	android.view.View.OnClickListener on_click)
	{
		android.view.View res=null;
		int project_level;
		String project_name;
		Integer project_colour;
		android.widget.TextView project_name_label;
		android.widget.CheckBox project_open_button;
		android.widget.LinearLayout layout;
		android.widget.LinearLayout.LayoutParams params;

		if (project_id == ID_NA)
		{
			project_level = 0;
			project_name = "N/A";
			project_colour = 0xffffffff;
		}
		else
		{
			project_level = rs.workbuddy.project.Project.Count_Parents(db, project_id);
			project_name = rs.workbuddy.project.Project.Get_Project_Name(db, project_id);
			project_colour = Project.Get_Colour(db, project_id);
		}

		project_name_label = new android.widget.TextView(ctx);
		project_name_label.setText(project_name);
		project_name_label.setClickable(true);
		project_name_label.setOnClickListener(on_click);
		project_name_label.setGravity(android.view.Gravity.CENTER_VERTICAL);
		project_name_label.setTag(tag);
		project_name_label.setTextSize(17);
		//project_name_label.setTextColor(project_colour);
		//if (this.dropdown_text_size > 0)
		  //project_name_label.setTextSize(this.dropdown_text_size);
		//rs.workbuddy.Border_Drawable.Add_Border(project_name_label, 0xffff0000);

		project_open_button = new android.widget.CheckBox(ctx);
		project_open_button.setChecked(checked);
		project_open_button.setButtonDrawable(Get_Branch_Drawable());
		if (rs.workbuddy.project.Project.Has_Children(db, project_id))
		{
			project_open_button.setOnClickListener(on_click);
			project_open_button.setTag(tag);
		}
		else
		{
			//project_open_button.setVisibility(android.view.View.INVISIBLE);
		}
		//rs.workbuddy.Border_Drawable.Add_Border(project_open_button, 0xff00ff00);

		layout = new android.widget.LinearLayout(ctx);

		params = new android.widget.LinearLayout.LayoutParams(
		  android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
			android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(project_level * 20, 0, 0, 0);
		layout.addView(project_open_button, params);

		params = new android.widget.LinearLayout.LayoutParams(
		  android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
			android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
		//params.topMargin=10;
		//params.bottomMargin=10;
		layout.addView(project_name_label, params);

		//rs.workbuddy.Border_Drawable.Add_Border(layout, 0xff0000ff);
		res = layout;

		return res;
	}

	public Project_SpinnerAdapter(rs.android.Db db, android.content.Context ctx)
	{
		this.db = db;
		this.ids = new java.util.ArrayList<Long>();
		this.open_ids = new java.util.ArrayList<Long>();
		this.ctx=ctx;

		this.Add_Root_Items();
		this.view_text_size = 0;
		this.dropdown_text_size = 16;
	}

	public void Add_Root_Items()
	{
		Long[] ids;
		String sql;

		sql = "select id from Project where parent_id is null order by name asc";
		ids = (Long[])db.Select_Column(Long.class, sql);

		this.ids.add(0, ID_NA);
		if (rs.android.Util.NotEmpty(ids))
		  this.ids.addAll(java.util.Arrays.asList(ids));
	}

	public void Set_Projects(Long open_id, boolean include_children)
	{
		Long[] parent_ids;

		this.open_ids.clear();
		parent_ids = Project.Select_Parents(this.db, open_id);
		if (rs.android.Util.NotEmpty(parent_ids))
			this.open_ids.addAll(java.util.Arrays.asList(parent_ids));
		if (include_children && Project.Has_Children(this.db, open_id))
			this.open_ids.add(open_id);

		this.ids.clear();
		this.Add_Root_Items();
		if (rs.android.Util.NotEmpty(this.open_ids))
		{
			for (Long id: this.open_ids)
			{
				this.Add_Children(id);
			}
		}
	}

	public void Add_Children(Long parent_id)
	{
		int idx;
		Long[] child_ids;

		if (rs.android.Util.NotEmpty(this.ids))
		{
		  idx = this.ids.indexOf(parent_id);
			if (idx > -1)
			{
			  child_ids = Project.Select_Children(this.db, parent_id, null, 
				  "status_type_id asc, name asc");
				this.ids.addAll(idx + 1, java.util.Arrays.asList(child_ids));
			}
		}
	}

	public void On_Click_Checkbox(android.view.View widget)
	{
		android.widget.CheckBox checkbox;
		Integer idx;
		Long parent_id;

		checkbox = (android.widget.CheckBox)widget;
		idx = (Integer)checkbox.getTag();
		if (idx != null)
		{
			parent_id = this.ids.get(idx);
			if (checkbox.isChecked())
				this.Set_Projects(parent_id, true);
			else
				this.Set_Projects(parent_id, false);
		}
	}

	public void onClick(android.view.View widget)
	{
		if (widget instanceof android.widget.CheckBox)
		{
			this.On_Click_Checkbox(widget);
			if (this.on_project_clicked_listener!=null)
				this.on_project_clicked_listener.onClick(widget);
		}
		else if (widget instanceof android.widget.TextView)
		{
			if (this.on_project_selected_listener!=null)
				this.on_project_selected_listener.onClick(widget);
		}
	}
}
