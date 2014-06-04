package rs.workbuddy.project;
//import android.database.*;
import android.view.*;
import android.database.*;

public class Project_ExpandableListAdapter
implements
android.widget.ExpandableListAdapter,
android.view.View.OnClickListener
{
	//public static final Long ID_NA=(long)-1;

	public java.util.ArrayList<Long> ids; 
	//open_ids;
	public rs.android.Db db;
	public android.database.DataSetObserver observer;
	public android.view.View.OnClickListener on_project_selected_listener;

  public Project_ExpandableListAdapter(rs.android.Db db, Long parent_id)
	{
		this.db = db;
		this.ids = new java.util.ArrayList<Long>();
		//this.open_ids = new java.util.ArrayList<Long>();

		this.Add_Sub_Projects(parent_id);
		//this.view_text_size = 0;
		//this.dropdown_text_size = 16;
	}

	public void Add_Sub_Projects(Long parent_id)
	{
		int idx;
		Long[] child_ids;

		child_ids = Project.Select_Children(this.db, parent_id, null, 
			"status_type_id asc, name asc");
		if (rs.android.Util.NotEmpty(child_ids))
		{
			if (parent_id == null)
			{
				this.ids.add(0, Project_SpinnerAdapter.ID_NA);
			}
			this.ids.addAll(java.util.Arrays.asList(child_ids));
		}
	}

	/*public void Set_Projects(Long open_id, boolean include_children)
	 {
	 Long[] parent_ids;

	 android.util.Log.d("Set_Projects", "entry");
	 this.open_ids.clear();
	 parent_ids = Project.Select_Parents(this.db, open_id);
	 if (rs.android.Util.NotEmpty(parent_ids))
	 this.open_ids.addAll(java.util.Arrays.asList(parent_ids));
	 if (include_children && Project.Has_Children(this.db, open_id))
	 this.open_ids.add(open_id);

	 this.ids.clear();
	 this.Add_Sub_Projects(null);
	 if (rs.android.Util.NotEmpty(this.open_ids))
	 {
	 for (Long id: this.open_ids)
	 {
	 this.Add_Sub_Projects(id);
	 }
	 }

	 if (this.observer!=null)
	 this.observer.onChanged();
	 }*/

	@Override
	public void onClick(View widget)
	{
		//android.util.Log.d("onClick", "entry");
		/*if (widget instanceof android.widget.CheckBox)
		 {
		 this.On_Click_Checkbox(widget);
		 //if (this.on_project_clicked_listener!=null)
		 //this.on_project_clicked_listener.onClick(widget);
		 }
		 else if (widget instanceof android.widget.TextView)
		 {
		 if (this.on_project_selected_listener!=null)
		 this.on_project_selected_listener.onClick(widget);
		 }*/
	}

	/*public void On_Click_Checkbox(android.view.View widget)
	 {
	 android.widget.CheckBox checkbox;
	 Integer idx;
	 Long parent_id;

	 android.util.Log.d("On_Click_Checkbox", "entry");
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
	 }*/

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
	public boolean hasStableIds()
	{
		return true;
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
	public int getGroupCount()
	{
		return this.ids.size();
	}

	@Override
	public int getChildrenCount(int grp_idx)
	{
		return 1;
	}

	@Override
	public Object getGroup(int grp_idx)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public Object getChild(int grp_idx, int child_idx)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public long getGroupId(int grp_idx)
	{
		return this.ids.get(grp_idx);
	}

	@Override
	public long getChildId(int grp_idx, int child_idx)
	{
		return 0;
	}

	@Override
	public View getGroupView(int grp_idx, boolean is_expandable, View convert_view, 
	  ViewGroup parent)
	{
		android.widget.TextView v;
		String proj_name;
		Long id;

		id = this.getGroupId(grp_idx);
		proj_name = rs.workbuddy.project.Project.Get_Project_Name(this.db, id);

		v = new android.widget.TextView(parent.getContext());
		v.setText(proj_name);
		v.setPadding(50, 10, 20, 10);
		v.setTextSize(19);
		return v;
	}

	@Override
	public View getChildView(int grp_idx, int child_idx, boolean is_last_child, 
	  View convert_view, ViewGroup parent)
	{
	  android.widget.ExpandableListView v;
		Long id;
		rs.workbuddy.project.Project_ExpandableListAdapter options;

		id = this.getGroupId(grp_idx);
		android.util.Log.d("getChildView", "id: " + id.toString());
		options = new rs.workbuddy.project.Project_ExpandableListAdapter(db, id);
		//android.util.Log.d("getChildView", "options: "+options.);
		v = new android.widget.ExpandableListView(parent.getContext());
		v.setAdapter(options);

		return v;
	}

	@Override
	public boolean isChildSelectable(int grp_idx, int child_idx)
	{
		return true;
	}

	@Override
	public void onGroupExpanded(int grp_idx)
	{
		// TODO: Implement this method
	}

	@Override
	public void onGroupCollapsed(int grp_idx)
	{
		// TODO: Implement this method
	}

	@Override
	public long getCombinedChildId(long grp_id, long child_id)
	{
		return child_id;
	}

	@Override
	public long getCombinedGroupId(long grp_id)
	{
		return grp_id;
	}
}
