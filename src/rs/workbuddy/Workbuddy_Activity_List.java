package rs.workbuddy;
import android.app.*;
import android.widget.*;
import android.view.*;

public class Workbuddy_Activity_List
extends rs.workbuddy.Workbuddy_Activity
implements android.view.View.OnClickListener
{
	public static final int MENUITEM_EDIT=800;
	public static final int MENUITEM_DELETE=900;
	public static final int MENUITEM_ADD=9000;

	public static final int ACT_RES_REFRESH_UI=1;

	public android.widget.TableLayout table_layout;
	public android.widget.ScrollView main_view;
	public java.util.ArrayList<Long> selected;
	
	public android.view.MenuItem edit_menu;
	public android.view.MenuItem del_menu;
	public android.view.MenuItem add_menu;
	
	public boolean refresh_data;

	public java.lang.Class<android.app.Activity> edit_class;
	public java.lang.Class<android.app.Activity> add_class;

	public boolean has_title;
	public boolean has_footer;
	public boolean has_menuitem_edit;
	public boolean has_menuitem_delete;
	public boolean has_menuitem_add;

	public Workbuddy_Activity_List()
	{
		this.has_title = false;
		this.has_footer = false;
		this.refresh_data = true;
	}

	@Override
	public void onCreate(android.os.Bundle state)
	{
		try
		{
			super.onCreate(state);

			this.selected = new java.util.ArrayList<Long>();

			main_view = new android.widget.ScrollView(this);
			main_view.setBackgroundColor(android.R.color.transparent);
			main_view.setLayoutParams(new android.widget.ScrollView.LayoutParams(
																	android.widget.ScrollView.LayoutParams.FILL_PARENT,
																	android.widget.ScrollView.LayoutParams.FILL_PARENT));

			this.setContentView(main_view);
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	public void onClick(android.view.View v)
	{
		Long obj_id;

		obj_id = (long)v.getId();
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

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		boolean res;

		res = super.onCreateOptionsMenu(menu);
		if (this.has_menuitem_add)
		{
			this.add_menu = menu.add(1, MENUITEM_ADD, MENUITEM_ADD, "Add");
			this.add_menu.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		else
		{
			menu.removeItem(MENUITEM_ADD);
			this.add_menu = null;
		}
		return res;
	}

	public void Set_Actions()
	{
		if (this.has_menuitem_edit)
		{
			if (rs.android.Util.NotEmpty(this.selected) && this.edit_menu == null && this.selected.size() == 1)
			{
				this.edit_menu = this.options_menu.add(1, MENUITEM_EDIT, MENUITEM_EDIT, "Edit");
				this.edit_menu.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
			}
			else if ((!rs.android.Util.NotEmpty(this.selected) || this.selected.size() > 1) && this.edit_menu != null)
			{
				this.options_menu.removeItem(MENUITEM_EDIT);
				this.edit_menu = null;
			}
		}

		if (this.has_menuitem_delete)
		{
			if (rs.android.Util.NotEmpty(this.selected) && this.del_menu == null)
			{
				this.del_menu = this.options_menu.add(1, MENUITEM_DELETE, MENUITEM_DELETE, "Delete");
				this.del_menu.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
			}
			else if (!rs.android.Util.NotEmpty(this.selected) && this.del_menu != null)
			{
				this.options_menu.removeItem(MENUITEM_DELETE);
				this.del_menu = null;
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=false;
		android.content.Intent i;

		try
		{
			if (item.getItemId() == MENUITEM_DELETE)
			{
				if (rs.android.Util.NotEmpty(this.selected))
				{
					showDialog(DLG_EVENT_DEL);
				}
				res = true;
			}
			else if (item.getItemId() == MENUITEM_EDIT)
			{
				if (rs.android.Util.NotEmpty(this.selected))
				{
					i = new android.content.Intent(this, this.edit_class);
					i.putExtra("id", this.selected.get(0));
					this.startActivityForResult(i, ACT_RES_REFRESH_UI);
				}
				res = true;
			}
			else if (item.getItemId() == MENUITEM_ADD)
			{
				i = new android.content.Intent(this, this.add_class);
				this.startActivityForResult(i, ACT_RES_REFRESH_UI);
				res = true;
			}
			if (!res)
				res = super.onOptionsItemSelected(item);
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
		return res;
	}

	public void onActivityResult(int req_code, int res_code, android.content.Intent intent)
	{
		if (req_code == ACT_RES_REFRESH_UI)
		{
			this.refresh_data = true;
		}
	}

	@Override
	public void OnClickPositive(android.content.DialogInterface dlg)
	{
		int c;

		for (c = 0; c < this.selected.size(); c++)
		{
			On_Delete(this.selected.get(c));
		}
		this.selected.clear();
		this.refresh_data = true;
		Update_UI();
	}

	public void Add_Header_Row(android.view.ViewGroup parent)
	{
		android.widget.TableRow row;
		rs.workbuddy.Border_Drawable border;

		row = new android.widget.TableRow(this);

		border = new rs.workbuddy.Border_Drawable();
		border.top = false;
		border.right = false;
		border.left = false;
		border.bottom_paint.setStrokeWidth(3);
		row.setBackgroundDrawable(border);

		Add_Header_Cell(row, "#");
		On_Build_Header_Row(row);
		parent.addView(row);
	}

	public void Add_Footer_Row(android.view.ViewGroup parent)
	{
		android.widget.TableRow row;
		rs.workbuddy.Border_Drawable border;

		row = new android.widget.TableRow(this);

		border = new rs.workbuddy.Border_Drawable();
		border.top = false;
		border.right = false;
		border.left = false;
		border.bottom_paint.setStrokeWidth(3);
		row.setBackgroundDrawable(border);

		On_Build_Footer_Row(row);
		parent.addView(row);
	}

	public android.widget.TableRow New_Row(int c, Object obj)
	{
		android.widget.TableRow row;

		row = new android.widget.TableRow(this);
		row.setClickable(true);
		row.setOnClickListener(this);
		row.setId((int)Get_Obj_Id(obj));
		row.setTag(obj);

		Add_Cell(row, rs.android.Util.To_String(c + 1));
		On_Build_Row(obj, row);

		return row;
	}

	@Override
	public void On_Update_UI()
	{
		java.util.List<Object> objs;
		Object obj;
		int c;
		android.widget.TableRow row;
		android.widget.TextView no_data;

		try
		{
			if (this.refresh_data)
			{
				this.table_layout = new android.widget.TableLayout(this);

				this.main_view.removeAllViews();
				this.main_view.addView(this.table_layout);

				if (this.has_title)
				  Add_Title_Row(this.table_layout);
				Add_Header_Row(this.table_layout);

				objs = On_Get_List();
				if (rs.android.Util.NotEmpty(objs))
				{
					for (c = 0; c < objs.size(); c++)
					{
						obj = objs.get(c);
						row = New_Row(c, obj);

						this.table_layout.addView(row);
					}
				}
				else
				{
					no_data = new android.widget.TextView(this);
					no_data.setText("No Data Available");
					this.table_layout.addView(no_data);
				}

			  if (this.has_footer)
				  Add_Footer_Row(this.table_layout);
				this.refresh_data = false;
			}

			if (rs.android.Util.NotEmpty(this.table_layout))
			{
				for (c = 0; c < this.table_layout.getChildCount(); c++)
				{
					Set_Row_Border(c);
				}
			}

			Set_Actions();
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	public void Set_Row_Border(int c)
	{
		rs.workbuddy.Border_Drawable border;
		Long id;
		android.view.View row;

		row = this.table_layout.getChildAt(c);
		id = (long)row.getId();
		if (this.selected.contains(id))
		{
			row.setBackgroundColor(0xff004400);				
		}
		else
		{
			border = new rs.workbuddy.Border_Drawable();
			border.top = false;
			border.right = false;
			border.left = false;
			border.bottom_paint.setColor(0xff444444);

			row.setBackgroundDrawable(border);
		}
	}

	public void Add_Cell(android.widget.TableRow row, String text)
	{
		android.widget.TextView cell;

		cell = new android.widget.TextView(this);
		cell.setText(text);
		cell.setPadding(10, 10, 10, 10);
		cell.setTextSize(20);
		row.addView(cell);
	}

	public void Add_Header_Cell(android.view.ViewGroup row, String text)
	{
		android.widget.TextView cell;

		cell = new android.widget.TextView(this);
		cell.setText(text);
		cell.setPadding(10, 2, 10, 10);
		cell.setTextSize(20);
		cell.setTextColor(0xffeeeeee);
		row.addView(cell);
	}

	public void Add_Footer_Cell(android.view.ViewGroup row, String text)
	{
		android.widget.TextView cell;

		cell = new android.widget.TextView(this);
		cell.setText(text);
		cell.setPadding(10, 2, 10, 10);
		cell.setTextSize(20);
		cell.setTextColor(0xffeeeeee);
		row.addView(cell);
	}

	public void On_Build_Header_Row(android.widget.TableRow row)
	{
    rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.On_Build_Header_Row()");
	}

	public void On_Build_Footer_Row(android.widget.TableRow row)
	{
    rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.On_Build_Footer_Row()");
	}

	public long Get_Obj_Id(Object obj)
	{
		rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.Get_Obj_Id()");
		return 0;
	}

	public void On_Build_Row(Object obj, android.widget.TableRow row)
	{
    rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.On_Build_Row()");
	}

	public java.util.List<Object> On_Get_List()
	{
		rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.On_Get_List()");
		return null;
	}

	public void On_Delete(Long id)
	{
    rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.On_Delete()");
	}

	public void Add_Title_Row(android.view.ViewGroup parent)
	{
		rs.android.Util.Show_Note(this, "Workbuddy_Activity_List.Add_Title_Row()");
	}
}
