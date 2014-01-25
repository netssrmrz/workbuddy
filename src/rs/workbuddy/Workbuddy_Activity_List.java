package rs.workbuddy;
import java.sql.*;

public class Workbuddy_Activity_List
extends rs.workbuddy.Workbuddy_Activity
implements 
android.view.View.OnClickListener,
rs.android.ui.Column_Dialog.On_Column_Set_Listener,
rs.android.ui.Sort_Dialog.On_Sort_Set_Listener
{
	public static final int ACT_RES_REFRESH_UI=1;

	public android.widget.TableLayout table_layout;
	public android.widget.ScrollView main_view;

	public java.util.ArrayList<Long> selected;
	public java.util.ArrayList<rs.android.ui.Column> cols;
	public java.util.ArrayList<rs.android.ui.Sort_Option> sort_options;
	public boolean refresh_data;
	public String ct="rs.workbuddy.Workbuddy_Activity_List";
	public java.lang.Class<?> menuitem_edit_class;
	public java.lang.Class<?> menuitem_add_class;
	public java.lang.Class<?> menuitem_view_class;
	public java.lang.Class<?> list_obj_class;
	public boolean has_menuitem_delete;
	public boolean has_footer;
	public boolean has_col_select;
	public boolean has_paging;
	public boolean has_tree_layout;
	public String title;
	public java.util.ArrayList<Long> open_ids;
	public int data_start_pos;
	public android.graphics.drawable.PictureDrawable branch_opened_drawable;
	public android.graphics.drawable.PictureDrawable branch_closed_drawable;
	public android.graphics.drawable.PictureDrawable branch_pressed_drawable;
	public java.sql.Date last_refresh_at;

	public Workbuddy_Activity_List()
	{
		this.has_col_select = true;
	}

	@Override
	public void onCreate(android.os.Bundle state)
	{
		super.onCreate(state);

		this.selected = new java.util.ArrayList<Long>();
		this.table_layout = new android.widget.TableLayout(this);

		main_view = new android.widget.ScrollView(this);
		main_view.setBackgroundColor(android.R.color.transparent);
		main_view.setLayoutParams(new android.widget.ScrollView.LayoutParams(
				android.widget.ScrollView.LayoutParams.FILL_PARENT,
				android.widget.ScrollView.LayoutParams.FILL_PARENT));
	  main_view.addView(this.table_layout);
		this.setContentView(main_view);

		this.refresh_data = true;

		this.On_Create_Columns();
		if (rs.android.Util.NotEmpty(this.cols))
			rs.android.ui.Column.Load(this, this.getClass().getName(), this.cols);
	}

	public void On_Pause()
	{
		String key;

		key = this.getClass().getName() + ".open_ids";
    rs.android.Util.Save_Data(this, key, this.open_ids);
	}

	public void On_Resume()
	{
		String key;

		key = this.getClass().getName() + ".open_ids";
		this.open_ids = (java.util.ArrayList<Long>)rs.android.Util.Load_Data(this, key);
		if (this.open_ids == null)
			this.open_ids = new java.util.ArrayList<Long>();
	}

	public void Set_Tree_Layout(boolean has_tree_layout)
	{
		this.open_ids = new java.util.ArrayList<Long>();
		this.has_tree_layout = has_tree_layout;
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		super.onCreateOptionsMenu(menu);

		if (this.menuitem_add_class != null)
			menu.findItem(Menus.MENUITEM_ADD).setVisible(true);

		if (rs.android.Util.NotEmpty(this.cols))
		  menu.findItem(Menus.MENUITEM_COLS).setVisible(true);

		if (rs.android.Util.NotEmpty(this.sort_options))
		  menu.findItem(Menus.MENUITEM_SORT).setVisible(true);

		if (this.has_paging)
		{
			menu.findItem(Menus.MENUITEM_NEXT).setVisible(true);
			menu.findItem(Menus.MENUITEM_PREV).setVisible(true);
		}

		return true;
	}

	public void On_Delete()
	{
		boolean deleted=false;
		
		if (rs.android.Util.NotEmpty(this.selected))
		{
			for (Long id:this.selected)
			{
				deleted=deleted || this.On_Delete(id);
			}
			
			if (deleted)
			{
				this.refresh_data=true;
				this.Update_UI();
			}
		}
	}
	
	public void On_Edit()
	{
		android.content.Intent i;

		if (rs.android.Util.NotEmpty(this.selected))
		{
			i = new android.content.Intent(this, this.menuitem_edit_class);
			i.putExtra("id", this.selected.get(0));
			this.startActivityForResult(i, ACT_RES_REFRESH_UI);
		}
	}

	public void On_Add()
	{
		android.content.Intent i;

		i = new android.content.Intent(this, this.menuitem_add_class);
		this.startActivityForResult(i, ACT_RES_REFRESH_UI);
	}

	public void On_View()
	{
		android.content.Intent i;

		if (rs.android.Util.NotEmpty(this.selected))
		{
			i = new android.content.Intent(this, this.menuitem_view_class);
			i.putExtra("id", this.selected.get(0));
			this.startActivity(i);
		}
	}

	public void onClick(android.view.View v)
	{
		Long obj_id;

		obj_id = (Long)v.getTag();
		if (this.selected.contains(obj_id))
		{
			this.selected.remove(obj_id);
		}
		else
		{
			this.selected.add(obj_id);
		}
		Update_UI();
	}

	public void Set_Actions()
	{
		if (this.menu != null)
		{
			if (this.menuitem_view_class != null)
			{
				if (rs.android.Util.NotEmpty(this.selected) && 
					!this.menu.findItem(Menus.MENUITEM_VIEW).isVisible() && 
					this.selected.size() == 1)
				{
					this.menu.findItem(Menus.MENUITEM_VIEW).setVisible(true);
				}
				else if ((!rs.android.Util.NotEmpty(this.selected) || this.selected.size() > 1) && 
					this.menu.findItem(Menus.MENUITEM_VIEW).isVisible())
				{
					this.menu.findItem(Menus.MENUITEM_VIEW).setVisible(false);
				}
			}

			if (this.menuitem_edit_class != null)
			{
				if (rs.android.Util.NotEmpty(this.selected) && 
					!this.menu.findItem(Menus.MENUITEM_EDIT).isVisible() && 
					this.selected.size() == 1)
				{
					this.menu.findItem(Menus.MENUITEM_EDIT).setVisible(true);
				}
				else if ((!rs.android.Util.NotEmpty(this.selected) || this.selected.size() > 1) && 
					this.menu.findItem(Menus.MENUITEM_EDIT).isVisible())
				{
					this.menu.findItem(Menus.MENUITEM_EDIT).setVisible(false);
				}
			}

			if (this.has_menuitem_delete)
			{
				if (rs.android.Util.NotEmpty(this.selected) && 
					!this.menu.findItem(Menus.MENUITEM_DELETE).isVisible())
				{
					this.menu.findItem(Menus.MENUITEM_DELETE).setVisible(true);
				}
				else if (!rs.android.Util.NotEmpty(this.selected) && 
					this.menu.findItem(Menus.MENUITEM_DELETE).isVisible())
				{
					this.menu.findItem(Menus.MENUITEM_DELETE).setVisible(false);
				}
			}
		}
	}

	public void onActivityResult(int req_code, int res_code, android.content.Intent intent)
	{
		if (req_code == ACT_RES_REFRESH_UI)
		{
			this.refresh_data = true;
			this.selected.clear();
		}
	}

	public void Build_Title_Row(android.widget.TableLayout table)
	{
		android.widget.TextView title;
		android.widget.TableLayout.LayoutParams table_layout;

		if (rs.android.Util.NotEmpty(this.title))
		{
			table_layout = new android.widget.TableLayout.LayoutParams();
			table_layout.setMargins(0, 0, 0, 0);
			title = this.New_Header_Cell(this.title);
			title.setPadding(0, 10, 0, 0);

			table.addView(title, table_layout);
			this.data_start_pos++;
		}
	}

	public void Build_Header_Row(android.widget.TableLayout table)
	{
		android.widget.TableRow row;
		android.widget.TableLayout.LayoutParams table_layout;
		android.widget.TableRow.LayoutParams row_layout;
		android.widget.TextView cell;

		table_layout = new android.widget.TableLayout.LayoutParams();
		table_layout.setMargins(0, 0, 0, 0);
		row = new android.widget.TableRow(this);
		row.setPadding(0, 10, 0, 0);
		//rs.workbuddy.Border_Drawable.Add_Border(row, 0xff00ff00);

		row_layout = new android.widget.TableRow.LayoutParams();
		row_layout.setMargins(0, 0, 0, 0);
		row_layout.gravity = android.view.Gravity.CENTER_VERTICAL | android.view.Gravity.RIGHT;
		cell = New_Header_Cell("#");
		cell.setPadding(0, 0, 10, 0);
		//rs.workbuddy.Border_Drawable.Add_Border(cell, 0xffff0000);
		row.addView(cell, row_layout);

		if (this.has_col_select)
		{
			cell.setPadding(0, 0, 0, 0);
		  row.addView(New_Header_Cell(" "));
		}

		On_Build_Header_Row(row);
		table.addView(row, table_layout);
		this.data_start_pos++;
	}

	public void Build_Footer_Row(android.widget.TableLayout table)
	{
		android.widget.TableRow row;
		//rs.workbuddy.Border_Drawable border;

		if (this.has_footer)
		{
			row = new android.widget.TableRow(this);

			/*border = new rs.workbuddy.Border_Drawable();
			 border.top = false;
			 border.right = false;
			 border.left = false;
			 border.bottom_paint.setStrokeWidth(3);
			 row.setBackgroundDrawable(border);*/

			row.addView(New_Header_Cell(" "));
			if (this.has_col_select)
				row.addView(New_Header_Cell(" "));

			On_Build_Footer_Row(row);
			table.addView(row);
		}
	}

	public android.widget.TableRow Build_Row(Long id)
	{
		android.widget.TableRow.LayoutParams row_layout;
		android.widget.TableRow row;
		android.widget.TextView cell;
		android.widget.CheckBox check;

		row = new android.widget.TableRow(this);
		row.setPadding(0, 0, 0, 0);
		//rs.workbuddy.Border_Drawable.Add_Border(row, 0xff0000ff);

		// add count cell
		row_layout = new android.widget.TableRow.LayoutParams();
		row_layout.setMargins(0, 0, 0, 0);
		row_layout.gravity = android.view.Gravity.CENTER_VERTICAL | android.view.Gravity.RIGHT;
		cell = new android.widget.TextView(this);
		cell.setText("x");
		cell.setTextColor(0xff999999);
		cell.setPadding(0, 0, 10, 0);
		//rs.workbuddy.Border_Drawable.Add_Border(cell, 0xff00ff00);
		row.addView(cell, row_layout);

		if (this.has_col_select)
		{
			cell.setPadding(0, 0, 0, 0);

			row_layout = new android.widget.TableRow.LayoutParams();
			row_layout.setMargins(0, 0, 0, 0);
			row_layout.gravity = android.view.Gravity.CENTER_VERTICAL;
			// add select cell
			check = new android.widget.CheckBox(this);
			check.setPadding(0, 0, 0, 0);
			check.setOnClickListener(this);
			check.setTag(id);
			// rs.workbuddy.Border_Drawable.Add_Border(check, 0xff00ff00);
			row.addView(check, row_layout);
		}

		// add data cells
		Build_Cells(id, row);
    return row;
	}

	@Override
	public void On_Update_UI()
	{
		Long ids[];

		if (this.refresh_data /*&& 
		  rs.android.Log.Has_Changes(this.db, this.list_obj_class, this.last_refresh_at)*/)
		{
			this.data_start_pos = 0;
			this.table_layout.removeAllViews();
			this.Build_Title_Row(this.table_layout);
			this.Build_Header_Row(this.table_layout);

			ids = On_Get_List();
			this.last_refresh_at=rs.android.Util.Now();
			this.Insert_Rows(ids);

			this.Build_Footer_Row(this.table_layout);
			this.refresh_data = false;
		}

		this.Update_Rows();

		this.Set_Actions();
	}

	public void Insert_Rows(Long[] ids)
	{
		int c;
		android.widget.TextView no_data;
		android.widget.TableLayout.LayoutParams table_layout;
		android.widget.TableRow row;

		if (rs.android.Util.NotEmpty(ids))
		{
			for (c = 0; c < ids.length; c++)
			{
				row = this.Build_Row(ids[c]);

				table_layout = new android.widget.TableLayout.LayoutParams();
				table_layout.setMargins(0, 0, 0, 0);
				this.table_layout.addView(row, table_layout);

				if (this.has_tree_layout && this.Is_Open(ids[c]))
				{
					this.Insert_Children(ids[c]);
				}
			}
		}
		else
		{
			no_data = new android.widget.TextView(this);
			no_data.setText("No Data Available");
			this.table_layout.addView(no_data);
		}
	}

	public void Insert_Children(Long id)
	{
		Long ids[];
		android.widget.TableRow row;
		android.widget.TableLayout.LayoutParams table_layout;

		ids = this.On_Get_Children(id);
		if (rs.android.Util.NotEmpty(ids))
		{
			for (Long child_id: ids)
			{
				row = this.Build_Row(child_id);
				table_layout = new android.widget.TableLayout.LayoutParams();
				table_layout.setMargins(0, 0, 0, 0);
				this.table_layout.addView(row, table_layout);

				if (this.Is_Open(child_id))
					this.Insert_Children(child_id);
			}
		}
	}

	public void Update_Rows()
	{
		int c;
		android.view.View child;

		if (rs.android.Util.NotEmpty(this.table_layout))
		{
			for (c = 0; c < this.table_layout.getChildCount(); c++)
			{
				child = this.table_layout.getChildAt(c);
				if (child instanceof android.widget.TableRow)
				  Update_Row(c);
			}
		}
	}

	public void Update_Row(int row_idx)
	{
		android.widget.CheckBox check, open_button;
		android.widget.TextView row_num_view;
		int row_num, data_col=0;
		Long id;
		android.widget.TableRow row;
		android.widget.LinearLayout view;

		row = (android.widget.TableRow)this.table_layout.getChildAt(row_idx);
		if (this.Is_Data_Row(row_idx))
		{
			// set row number
			row_num_view = (android.widget.TextView)row.getChildAt(0);
			row_num = row_idx - this.data_start_pos + 1;
			row_num_view.setText(rs.android.Util.To_String(row_num));
			data_col++;

			// set row selection checkbox
			if (this.has_col_select)
			{
				check = (android.widget.CheckBox)row.getChildAt(1);
				id = (Long)check.getTag();
				if (this.selected.contains(id))
					check.setChecked(true);
				else
					check.setChecked(false);
				data_col++;
			}

			// set branch open/close checkbox
			if (this.has_tree_layout)
			{
				view = (android.widget.LinearLayout)row.getChildAt(data_col);
				open_button = (android.widget.CheckBox)view.getChildAt(0);
				id = (Long)open_button.getTag();
				if (id != null)
					if (this.Is_Open(id))
						open_button.setChecked(true);
					else
						open_button.setChecked(false);
			}
		}

		this.On_Update_Row(row_idx);
	}

	public boolean Is_Data_Row(int idx)
	{
		boolean res=false;

		if (idx >= this.data_start_pos)
			if (!(this.has_footer && idx == this.table_layout.getChildCount() - 1))
			  res = true;
		return res;
	}

	public Long Get_Row_Id(int idx)
	{
		Long res=null;
		android.widget.TableRow row;
		android.widget.CheckBox check;

		row = (android.widget.TableRow)this.table_layout.getChildAt(idx);
		if (row != null && row.getChildAt(1) instanceof android.widget.CheckBox)
		{
			check = (android.widget.CheckBox)row.getChildAt(1);
			res = (Long)check.getTag();
		}
		return res;
	}

	public android.view.View New_Cell_Lines(String... lines)
	{
		android.view.View res=null;
		android.widget.LinearLayout linear;
		android.widget.LinearLayout.LayoutParams layout;
		android.widget.TextView cell;
		java.util.ArrayList<android.widget.TextView> line_views;

		if (rs.android.Util.NotEmpty(lines))
		{
			line_views = new java.util.ArrayList<android.widget.TextView>();
			for (String line: lines)
			{
				if (rs.android.Util.NotEmpty(line))
				{
					cell = this.New_Cell(line);
					if (rs.android.Util.NotEmpty(line_views))
					{
						cell.setTextColor(0xff999999);
					}
					line_views.add(cell);
				}
			}

			if (rs.android.Util.NotEmpty(line_views))
			{
				if (line_views.size() == 1)
				{
          res = line_views.get(0);
				}
				else
				{
					linear = new android.widget.LinearLayout(this);
					linear.setOrientation(android.widget.LinearLayout.VERTICAL);
					linear.setPadding(0, 0, 0, 0);
					//rs.workbuddy.Border_Drawable.Add_Border(res, 0xffff0000);
					for (android.widget.TextView v: line_views)
					{
						layout = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
					  linear.addView(v, layout);
					}
					res = linear;
				}
			}
		}
		if (res == null)
			res = this.New_Cell(null);
		return res;
	}

	public android.widget.TextView New_Cell(String label)
	{
		android.widget.TextView cell;

		cell = new android.widget.TextView(this);
		cell.setText(rs.android.Util.To_String(label, "n/a"));
		cell.setPadding(0, 0, 10, 0);
		//rs.workbuddy.Border_Drawable.Add_Border(cell, 0xff00ff00);
		return cell;
	}

	public android.widget.TextView New_Header_Cell(String text)
	{
		android.widget.TextView cell;

		cell = new android.widget.TextView(this);
		cell.setText(text);
		cell.setPadding(0, 0, 10, 0);
		cell.setTextSize(18);
		//rs.workbuddy.Border_Drawable.Add_Border(cell, 0xffff0000);
		cell.setTextColor(0xffffffff);
		return cell;
	}

	public rs.android.ui.Column Add_Column(String id, String title)
	{
		return this.Add_Column(id, title, false);
	}

	public rs.android.ui.Column Add_Column(String id, String title, boolean wrap)
	{
		rs.android.ui.Column col;

		if (this.cols == null)
			this.cols = new java.util.ArrayList<rs.android.ui.Column>();

		col = new rs.android.ui.Column();
		col.id = id;
		col.title = title;
		col.visible = true;
		col.wrap = wrap;
		this.cols.add(col);
		
		return col;
	}

	public void Add_Sort(int id, String title)
	{
		rs.android.ui.Sort_Option sort;

		if (this.sort_options == null)
			this.sort_options = new java.util.ArrayList<rs.android.ui.Sort_Option>();

		sort = new rs.android.ui.Sort_Option();
		sort.id = id;
		sort.label = title;
		this.sort_options.add(sort);
	}

	public void On_Build_Header_Row(android.widget.TableRow row)
	{
		int c;
		android.view.View cell;

		if (rs.android.Util.NotEmpty(this.cols))
		{
			for (rs.android.ui.Column col: this.cols)
			{
				if (col.visible)
				{
					cell = New_Header_Cell(col.title);
				  row.addView(cell);
					c = row.indexOfChild(cell);

					if (col.wrap)
					{
						this.table_layout.setColumnShrinkable(c, true);
						this.table_layout.setColumnStretchable(c, true);
					}
					else
						this.table_layout.setColumnShrinkable(c, false);
				}
			}
		}
	}

	public void On_Build_Footer_Row(android.widget.TableRow row)
	{
		android.view.View cell_view;
		android.widget.TableRow.LayoutParams layout;

		if (rs.android.Util.NotEmpty(this.cols))
		{
			for (rs.android.ui.Column col: this.cols)
			{
				if (col.visible)
				{
					cell_view = this.On_Get_Col_Footer_View(col.id);
					if (cell_view != null)
					{
						layout = new android.widget.TableRow.LayoutParams();
						layout.gravity = android.view.Gravity.CENTER_VERTICAL;
					  row.addView(cell_view, layout);
					}
				}
			}
		}
	}

	public void Build_Cells(Long id, android.widget.TableRow row)
	{
		Object obj;
		android.view.View cell_view;
		android.widget.TableRow.LayoutParams layout;
		boolean is_first_view=true;

		if (rs.android.Util.NotEmpty(this.cols))
		{
			obj = this.On_Get_Obj(id);

			for (rs.android.ui.Column col: this.cols)
			{
				if (col.visible)
				{
					cell_view = this.On_Get_Col_View(obj, col.id);
					if (cell_view != null)
					{
						//rs.workbuddy.Border_Drawable.Add_Border(cell_view, 0xffff0000);
						if (this.has_tree_layout && is_first_view)
						{
							cell_view = this.Add_Branch_Selector(cell_view, id, obj);
							is_first_view = false;
						}
						
						layout = new android.widget.TableRow.LayoutParams();
						if (col.align==rs.android.ui.Column.ALIGN_RIGHT)
							layout.gravity=android.view.Gravity.CENTER_VERTICAL|android.view.Gravity.RIGHT;
						else if (col.align==rs.android.ui.Column.ALIGN_CENTRE)
							layout.gravity=android.view.Gravity.CENTER;
						else
							layout.gravity = android.view.Gravity.CENTER_VERTICAL|android.view.Gravity.LEFT;
							
					  row.addView(cell_view, layout);
					}
				}
			}
		}
	}

	public android.graphics.drawable.StateListDrawable Get_Branch_Drawable()
	{
		android.graphics.drawable.StateListDrawable res=null;

		if (this.branch_opened_drawable==null)
			this.branch_opened_drawable=rs.android.ui.Util.Get_Opened_Pic();
		if (this.branch_closed_drawable==null)
			this.branch_closed_drawable=rs.android.ui.Util.Get_Closed_Pic();
		if (this.branch_pressed_drawable==null)
			this.branch_pressed_drawable=rs.android.ui.Util.Get_Pressed_Pic();
			
		res = new android.graphics.drawable.StateListDrawable();
		res.addState(new int[] {-android.R.attr.state_checked}, this.branch_opened_drawable);
		res.addState(new int[] {android.R.attr.state_checked}, this.branch_closed_drawable);
		res.addState(new int[] {android.R.attr.state_selected}, this.branch_pressed_drawable);
		
		return res;
	}
	
	public android.widget.LinearLayout Add_Branch_Selector(android.view.View name_label,
	  Long id, Object obj)
	{
		android.widget.LinearLayout res=null, layout;
		android.widget.LinearLayout.LayoutParams params;
		android.content.Context ctx;
		int branch_level;
		android.widget.CheckBox open_button;

		ctx = name_label.getContext();
		branch_level = this.On_Get_Branch_Level(id);

		open_button = new android.widget.CheckBox(ctx);
	  open_button.setButtonDrawable(this.Get_Branch_Drawable());
		open_button.setVisibility(android.view.View.INVISIBLE);
		// rs.workbuddy.Border_Drawable.Add_Border(open_button, 0xff00ff00);

		layout = new android.widget.LinearLayout(ctx);

		params = new android.widget.LinearLayout.LayoutParams(
		  android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
			android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(branch_level * 20, 0, 0, 0);
		layout.addView(open_button, params);

		params = new android.widget.LinearLayout.LayoutParams(
		  android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
			android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(name_label, params);

		if (this.On_Has_Children(id))
		{
			open_button.setVisibility(android.view.View.VISIBLE);
			open_button.setOnClickListener(new On_Branch_Click_Listener());
			open_button.setTag(id);
		}
		//rs.workbuddy.Border_Drawable.Add_Border(layout, 0xff0000ff);
		res = layout;

		return res;
	}

	public class On_Branch_Click_Listener
	implements android.view.View.OnClickListener
	{
		public void onClick(android.view.View widget)
		{
			android.widget.CheckBox checkbox;
			Long id;

			checkbox = (android.widget.CheckBox)widget;
			id = (Long)checkbox.getTag();
			if (id != null)
			{
				if (checkbox.isChecked() && !Is_Open(id))
				{
					open_ids.add(id);
					refresh_data = true;
					Update_UI();
				}
				else if (!checkbox.isChecked() && Is_Open(id))
				{
					open_ids.remove(id);
					refresh_data = true;
					Update_UI();
				}
			}
		}
	}

	public Integer Get_Row_Idx(Long id)
	{
		Integer res=null;
		android.view.View v, row=null;
		int idx;

		v = this.table_layout.findViewWithTag(id);
		if (v != null)
		{
			row = this.Get_Row(v.getParent());
			if (row != null)
			{
				for (idx = 0; idx < this.table_layout.getChildCount(); idx++)
				{
					if (this.table_layout.getChildAt(idx) == row)
						res = idx;
				}
			}
		}
		return res;
	}

	public android.widget.TableRow Get_Row(android.view.ViewParent v)
	{
		android.widget.TableRow res=null;

		if (v instanceof android.widget.TableRow)
			res = (android.widget.TableRow)v;
		else
			res = Get_Row(v.getParent());
		return res;
	}

	public int On_Get_Branch_Level(Long id)
	{
		return 0;
	}

	public boolean On_Has_Children(Long id)
	{
		return false;
	}

	public boolean Is_Open(Long id)
	{
		return this.open_ids.contains(id.longValue());
	}

	public android.view.View On_Get_Col_Footer_View(String col_id)
	{
		return null;
	}

	public android.view.View On_Get_Col_View(Object obj, String col_id)
	{
		return null;
	}

	public Object On_Get_Obj(Long id)
	{
		return null;
	}

	public Long[] On_Get_List()
	{
		rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.On_Get_List()");
		return null;
	}

	public boolean On_Delete(Long id)
	{
    return false;
	}

	public void On_Edit_Cols()
	{
		rs.android.ui.Column_Dialog dlg;

		dlg = new rs.android.ui.Column_Dialog(this, this);
		dlg.options = this.cols;
		dlg.Show();
	}

	public void On_Column_Set()
	{
		rs.android.ui.Column.Save(this, this.getClass().getName(), this.cols);
		this.refresh_data = true;
		this.Update_UI();
	}

	@Override
	public void On_Sort()
	{
		rs.android.ui.Sort_Dialog dlg;

		dlg = new rs.android.ui.Sort_Dialog(this, this);
		dlg.options = this.sort_options;
		dlg.Show();
	}

	public void On_Sort_Set(rs.android.ui.Sort_Option which)
	{
		rs.android.ui.Sort_Option.Save(this, this.getClass().getName(), which.id);
		this.refresh_data = true;
		this.Update_UI();
	}

	public void On_Create_Columns()
	{

	}

	public Long[] On_Get_Children(Long id)
	{
		return null;
	}

	public void On_Update_Row(int c)
	{

	}
}
