package rs.workbuddy;
import android.widget.*;
import java.util.*;
import rs.android.ui.*;

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
	public boolean has_menuitem_delete;
	public boolean has_footer;
	public boolean has_col_select;
	public boolean has_paging;
	public boolean has_tree_layout;
	public String title;

	public Workbuddy_Activity_List()
	{
		this.has_col_select = true;
		this.has_tree_layout=false;
	}

	@Override
	public void onCreate(android.os.Bundle state)
	{
		super.onCreate(state);

		this.selected = new java.util.ArrayList<Long>();

		this.table_layout = new android.widget.TableLayout(this);
		//this.table_layout.setShrinkAllColumns(true);

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

	public void Build_Row(Long[] ids, int c, android.widget.TableLayout table)
	{
		android.widget.TableLayout.LayoutParams table_layout;
		android.widget.TableRow.LayoutParams row_layout;
		android.widget.TableRow row;
		android.widget.TextView cell;
		android.widget.CheckBox check;

		table_layout = new android.widget.TableLayout.LayoutParams();
		table_layout.setMargins(0, 0, 0, 0);
		row = new android.widget.TableRow(this);
		row.setPadding(0, 0, 0, 0);
		//rs.workbuddy.Border_Drawable.Add_Border(row, 0xff0000ff);

		// add count cell
		row_layout = new android.widget.TableRow.LayoutParams();
		row_layout.setMargins(0, 0, 0, 0);
		row_layout.gravity = android.view.Gravity.CENTER_VERTICAL | android.view.Gravity.RIGHT;
		cell = new android.widget.TextView(this);
		cell.setText(rs.android.Util.To_String(c + 1));
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
			check.setTag(ids[c]);
			//rs.workbuddy.Border_Drawable.Add_Border(check, 0xff00ff00);
			row.addView(check, row_layout);
		}

		// add data cells
		On_Build_Row(ids[c], row);

		table.addView(row, table_layout);
	}

	@Override
	public void On_Update_UI()
	{
		Long ids[];
		int c;
		android.widget.TextView no_data;
		android.view.View child;

		if (this.refresh_data)
		{
			this.table_layout.removeAllViews();
			this.Build_Title_Row(this.table_layout);
			this.Build_Header_Row(this.table_layout);

			ids = On_Get_List();
			if (rs.android.Util.NotEmpty(ids))
			{
				for (c = 0; c < ids.length; c++)
				{
					Build_Row(ids, c, this.table_layout);
				}
			}
			else
			{
				no_data = new android.widget.TextView(this);
				no_data.setText("No Data Available");
				this.table_layout.addView(no_data);
			}

			this.Build_Footer_Row(this.table_layout);
			this.refresh_data = false;
		}

		if (rs.android.Util.NotEmpty(this.table_layout))
		{
			for (c = 0; c < this.table_layout.getChildCount(); c++)
			{
				child = this.table_layout.getChildAt(c);
				if (child instanceof android.widget.TableRow)
				  Update_Row(c);
			}
		}

		Set_Actions();
	}

	public void Update_Row(int row_idx)
	{
		android.widget.CheckBox check;
		Long id;
		android.widget.TableRow row;

		row = (android.widget.TableRow)this.table_layout.getChildAt(row_idx);
		if (row.getChildAt(1) instanceof android.widget.CheckBox)
		{
			check = (android.widget.CheckBox)row.getChildAt(1);
			id = (Long)check.getTag();
			if (this.selected.contains(id))
				check.setChecked(true);
			else
				check.setChecked(false);
			this.On_Update_Row(row_idx);
		}
	}

	public Long Get_Row_Id(int idx)
	{
		Long res=null;
		android.widget.TableRow row;
		android.widget.CheckBox check;

		row = (android.widget.TableRow)this.table_layout.getChildAt(idx);
		if (row.getChildAt(1) instanceof android.widget.CheckBox)
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
		//cell.setTextSize(20);
		//cell.setTextColor(0xffffffff);
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

	/*public void Add_Footer_Cell(android.view.ViewGroup row, String text)
	 {
	 android.widget.TextView cell;

	 cell = new android.widget.TextView(this);
	 cell.setText(text);
	 cell.setPadding(10, 2, 10, 10);
	 cell.setTextSize(20);
	 cell.setTextColor(0xffeeeeee);
	 row.addView(cell);
	 }*/

	public void Add_Column(String id, String title)
	{
		this.Add_Column(id, title, false);
	}

	public void Add_Column(String id, String title, boolean wrap)
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

	public void On_Update_Row(int c)
	{

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
					//android.util.Log.d("On_Build_Header_Row", col.title+": "+c);
					
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
	
	public void On_Build_Row(Long id, android.widget.TableRow row)
	{
		Object obj;
		android.view.View cell_view;
		android.widget.TableRow.LayoutParams layout;

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
						layout = new android.widget.TableRow.LayoutParams();
						layout.gravity = android.view.Gravity.CENTER_VERTICAL;
					  row.addView(cell_view, layout);
					}
				}
			}
		}
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

	public void On_Delete(Long id)
	{
    rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.On_Delete()");
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
}
