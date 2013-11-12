package rs.workbuddy;
import android.widget.*;

public class Workbuddy_Activity_List
extends rs.workbuddy.Workbuddy_Activity
implements android.view.View.OnClickListener
{
	public static final int ACT_RES_REFRESH_UI=1;

	public android.widget.TableLayout table_layout;
	public android.widget.ScrollView main_view;

	public java.util.ArrayList<Long> selected;
	public boolean refresh_data;
	public String ct="rs.workbuddy.Workbuddy_Activity_List";
	public java.lang.Class<?> menuitem_edit_class;
	public java.lang.Class<?> menuitem_add_class;
	public java.lang.Class<?> menuitem_view_class;
	public boolean has_menuitem_delete;
	public boolean has_footer;
	public boolean has_col_select;
	public String title;

	public Workbuddy_Activity_List()
	{
		this.has_col_select = true;
	}

	@Override
	public void onCreate(android.os.Bundle state)
	{
		//String ft=ct + ".onCreate()";
		//android.util.Log.d(ft, "Entry");

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
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		if (this.menuitem_add_class != null)
			menu.findItem(Menus.MENUITEM_ADD).setVisible(true);
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
			title = new android.widget.TextView(this);
			title.setText(this.title);

			table.addView(title, table_layout);
		}
	}

	public void Build_Header_Row(android.widget.TableLayout table)
	{
		android.widget.TableRow row;
		android.widget.TableLayout.LayoutParams table_layout;
		rs.workbuddy.Border_Drawable border;

		table_layout = new android.widget.TableLayout.LayoutParams();
		table_layout.setMargins(0, 0, 0, 0);
		row = new android.widget.TableRow(this);
		row.setPadding(0, 0, 0, 0);

		/*border = new rs.workbuddy.Border_Drawable();
		 border.top = false;
		 border.right = false;
		 border.left = false;
		 border.bottom_paint.setStrokeWidth(3);
		 row.setBackgroundDrawable(border);*/

		row.addView(New_Header_Cell("#"));
		if (this.has_col_select)
		  row.addView(New_Header_Cell(" "));

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
		String ft=ct + ".New_Row()";
		//android.util.Log.d(ft, "Entry");

		table_layout = new android.widget.TableLayout.LayoutParams();
		table_layout.setMargins(0, 0, 0, 0);
		row = new android.widget.TableRow(this);
		row.setPadding(0, 0, 0, 0);

		// add count cell
		cell = new android.widget.TextView(this);
		cell.setText(rs.android.Util.To_String(c + 1));
		//cell.setTextSize(20);
		//cell.setTextColor(0xffbbbbbb);
		cell.setPadding(0, 0, 0, 0);
		row.addView(cell);

		if (this.has_col_select)
		{
			row_layout = new android.widget.TableRow.LayoutParams();
			row_layout.setMargins(0, 0, 0, 0);
			// add select cell
			check = new android.widget.CheckBox(this);
			check.setPadding(0, 0, 0, 0);
			check.setOnClickListener(this);
			check.setTag(ids[c]);
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
		String ft=ct + ".On_Update_UI()";
		android.view.View child;

		//android.util.Log.d(ft, "Entry");
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
				  Update_Row((android.widget.TableRow)child);
			}
		}

		Set_Actions();
	}

	public void Update_Row(android.widget.TableRow row)
	{
		android.widget.CheckBox check;
		Long id;

		if (row.getChildAt(1) instanceof android.widget.CheckBox)
		{
			check = (android.widget.CheckBox)row.getChildAt(1);
			id = (Long)check.getTag();
			if (this.selected.contains(id))
				check.setChecked(true);
			else
				check.setChecked(false);
		}
	}

	public android.widget.TextView New_Cell(String label)
	{
		android.widget.TextView cell;

		cell = new android.widget.TextView(this);
		cell.setText(rs.android.Util.To_String(label, "n/a"));
		//cell.setTextSize(20);
		//cell.setTextColor(0xffffffff);
		cell.setPadding(10, 0, 0, 0);
		return cell;
	}

	public android.widget.TextView New_Header_Cell(String text)
	{
		android.widget.TextView cell;

		cell = new android.widget.TextView(this);
		cell.setText(text);
		cell.setPadding(10, 0, 0, 0);
		//cell.setTextSize(20);
		//cell.setTextColor(0xffeeeeee);
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

	public void On_Build_Header_Row(android.widget.TableRow row)
	{

	}

	public void On_Build_Footer_Row(android.widget.TableRow row)
	{

	}

	public void On_Build_Row(Long id, android.widget.TableRow row)
	{
    android.util.Log.d(ct + ".On_Build_Row()", "Entry");
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
}
