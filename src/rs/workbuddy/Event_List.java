package rs.workbuddy;
import android.widget.*;
import android.content.*;

public class Event_List 
extends rs.workbuddy.Workbuddy_Activity
implements android.view.View.OnClickListener
{
	public static final int MENUITEM_ADD_ACTIVITY=10;
	public static final int MENUITEM_EDIT_ACTIVITY=11;
	public static final int MENUITEM_DELETE_ACTIVITY=12;
	
	public static final int ACT_RES_REFRESH_UI=1;

	public android.widget.TableLayout table_layout;
	public android.widget.ScrollView main_view;
	public java.util.ArrayList<Long> selected;
	public android.view.MenuItem edit_menu, del_menu;
	public boolean no_data;

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
		Long event_id;

		event_id = (long)v.getId();
		if (this.selected.contains(event_id))
		{
			this.selected.remove(event_id);
		}
		else
		{
			this.selected.add(event_id);
		}
		this.no_data = true;
		Update_UI();
	}

	public void Set_Actions()
	{
		if (rs.android.Util.NotEmpty(this.selected))
		{
			if (this.edit_menu == null && this.selected.size() == 1)
			{
				this.edit_menu = this.options_menu.add(1, MENUITEM_EDIT_ACTIVITY, android.view.Menu.NONE, "Edit");
				this.edit_menu.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
			}
			else if (this.edit_menu != null && this.selected.size() > 1)
			{
				this.options_menu.removeItem(MENUITEM_EDIT_ACTIVITY);
				this.edit_menu = null;
			}
			if (this.del_menu == null)
			{
			  this.del_menu = this.options_menu.add(1, MENUITEM_DELETE_ACTIVITY, android.view.Menu.NONE, "Delete");
				this.del_menu.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
			}
		}
		else
		{
			if (this.edit_menu != null)
			{
			  this.options_menu.removeItem(MENUITEM_EDIT_ACTIVITY);
				this.edit_menu = null;
			}
			if (this.del_menu != null)
			{
			  this.options_menu.removeItem(MENUITEM_DELETE_ACTIVITY);
				this.del_menu = null;
			}
		}
	}

  @Override
  public boolean onOptionsItemSelected(android.view.MenuItem item)
  {
    boolean res=false;
		android.content.Intent i;

		res = super.onOptionsItemSelected(item);
		if (!res)
		{
			if (item.getItemId() == MENUITEM_DELETE_ACTIVITY)
			{
				if (rs.android.Util.NotEmpty(this.selected))
				{
					showDialog(DLG_EVENT_DEL);
				}
				res = true;
			}
			else if (item.getItemId() == MENUITEM_EDIT_ACTIVITY)
			{
				if (rs.android.Util.NotEmpty(this.selected))
				{
					i = new android.content.Intent(this, Event_Add.class);
					i.putExtra("event_id", this.selected.get(0));
					this.startActivityForResult(i, ACT_RES_REFRESH_UI);
				}
				res = true;
			}
		}
		return res;
	}

	public void onActivityResult(int req_code, int res_code, android.content.Intent intent)
	{
		//rs.android.Util.Show_Note(this, "activity result");
		if (req_code==ACT_RES_REFRESH_UI)
			Update_UI();
	}
	
	@Override
	public void OnClickPositive(android.content.DialogInterface dlg)
	{
		int c;

		for (c = 0; c < this.selected.size(); c++)
		{
			Work_Event.Delete(this.db, this.selected.get(c));
		}
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
		Add_Header_Cell(row, "Activity");
		Add_Header_Cell(row, "Start");
		Add_Header_Cell(row, "Duration");
		parent.addView(row);
  }

	public android.widget.TableRow New_Row(int c, Work_Event event)
	{
		android.widget.TableRow row;
		String event_type, event_start_date, event_duration;
		long duration;

		row = new android.widget.TableRow(this);
		row.setClickable(true);
		row.setOnClickListener(this);
		row.setId(rs.android.Util.To_Int(event.id));
		row.setTag(event);

		Add_Cell(row, rs.android.Util.To_String(c + 1));

		if (event.event_type.equals(Work_Event.EVENT_TYPE_HOME))
			event_type = "Home";
		else if (event.event_type.equals(Work_Event.EVENT_TYPE_LUNCH))
			event_type = "Lunch";
		else if (event.event_type.equals(Work_Event.EVENT_TYPE_WORK))
			event_type = "Work";
		else 
			event_type = "n/a";
		Add_Cell(row, event_type);

		event_start_date = rs.android.Util.To_String(event.start_date, "n/a", "EEE dd/MM/yyyy h:mm a");
		Add_Cell(row, event_start_date);

		duration = event.Get_Event_Duration(this.db);
		event_duration =
			rs.android.Util.To_String((double)duration / (double)1000 / (double)60 / (double)60, null, "#,##0.##") + "hr " +
			"(" + rs.android.Util.To_String((double)duration / (double)1000 / (double)60, null, "#,##0.##") + "min)";
		Add_Cell(row, event_duration);

		return row;
	}

	@Override
	public void On_Update_UI()
	{
		java.util.ArrayList<Work_Event> events;
		Work_Event event;
		int c;
		android.widget.TableRow row;

		try
		{
			if (!no_data)
			{
				this.table_layout = new android.widget.TableLayout(this);

				this.main_view.removeAllViews();
				this.main_view.addView(this.table_layout);

				Add_Header_Row(this.table_layout);

				events = Work_Event.Select_All(this.db);
				if (rs.android.Util.NotEmpty(events))
				{
					for (c = 0; c < events.size(); c++)
					{
						event = events.get(c);
						row = New_Row(c, event);
						
						this.table_layout.addView(row);
					}
				}
			}

			if (rs.android.Util.NotEmpty(this.table_layout))
			{
				for (c = 0; c < this.table_layout.getChildCount(); c++)
				{
					Set_Row_Border(c);
				}
			}

			Set_Actions();
			this.no_data = false;
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	public void Set_Row_Border(int c)
	{
		rs.workbuddy.Border_Drawable border;
		Long event_id;
		android.widget.TableRow row;
		Work_Event event, next_event=null;

		row = (android.widget.TableRow)this.table_layout.getChildAt(c);
		event_id = (long)row.getId();
		event = (Work_Event)row.getTag();
    if (event != null)
		{
			if (c < this.table_layout.getChildCount() - 1)
				next_event = (Work_Event)this.table_layout.getChildAt(c + 1).getTag();

			if (this.selected.contains(event_id))
			{
				row.setBackgroundColor(0xff004400);				
			}
			else
			{
				border = new rs.workbuddy.Border_Drawable();
				border.top = false;
				border.right = false;
				border.left = false;

				if (next_event != null && rs.android.Util.Date_Get_Day_Of_Week(next_event.start_date) != rs.android.Util.Date_Get_Day_Of_Week(event.start_date))
				{
					border.bottom_paint.setColor(0xff44bb44);
				}
				else
					border.bottom_paint.setColor(0xff444444);
				
				row.setBackgroundDrawable(border);
			}
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
		cell.setTextSize(25);
		cell.setTextColor(0xffeeeeee);
		row.addView(cell);
	}
}
