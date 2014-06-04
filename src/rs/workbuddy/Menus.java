package rs.workbuddy;
import android.content.*;

public class Menus
{
	public static final int MENUITEM_HOME=20;
	public static final int MENUITEM_EVENTS=30;
	public static final int MENUITEM_PROJECTS=40;
	public static final int MENUITEM_TIMESHEET=50;
	public static final int MENUITEM_TIMESHEET_VIEW=51;
	public static final int MENUITEM_TIMESHEET_SEND=52;
	public static final int MENUITEM_SETTINGS=60;

	public static final int MENUITEM_FILTER_PROJ=12;
	public static final int MENUITEM_NEXT=2;
	public static final int MENUITEM_PREV=1;
	public static final int MENUITEM_ADD=3;
	public static final int MENUITEM_VIEW=4;
	public static final int MENUITEM_EDIT=5;
	public static final int MENUITEM_DELETE=7;
	public static final int MENUITEM_DONE=8;
	public static final int MENUITEM_FILTER=9;
	public static final int MENUITEM_SORT=10;
	public static final int MENUITEM_COLS=11;
	public static final int MENUITEM_EXPORT=12;
	public static final int MENUITEM_SEARCH=13;
	public static final int MENUITEM_IMPORT=14; 
	
	public static final int MENUITEM_PROJECTLIST_MOVE=6;
	
	public String ct="rs.workbuddy.Menus";

	public static void Create_Options_Menu(android.view.Menu menu)
	{
		android.view.MenuItem item;
		//android.util.Log.d("rs.workbuddy.Menus.Menus()", "Entry");

		menu.add(1, MENUITEM_HOME, MENUITEM_HOME, "Home");
		menu.add(1, MENUITEM_EVENTS, MENUITEM_EVENTS, "Activities");
		menu.add(1, MENUITEM_PROJECTS, MENUITEM_PROJECTS, "Projects");
		menu.add(1, MENUITEM_TIMESHEET, MENUITEM_TIMESHEET, "Timesheet");
		menu.add(1, MENUITEM_SETTINGS, MENUITEM_SETTINGS, "Settings");
		
		item=menu.add(1, MENUITEM_TIMESHEET_VIEW, MENUITEM_TIMESHEET_VIEW, "View Timesheet");
		item.setVisible(false);
		
		item=menu.add(1, MENUITEM_TIMESHEET_SEND, MENUITEM_TIMESHEET_SEND, "Send Timesheet");
		item.setVisible(false);

		item = menu.add(1, MENUITEM_NEXT, MENUITEM_NEXT, ">");
		item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		item.setVisible(false);
	  
		item = menu.add(1, MENUITEM_PREV, MENUITEM_PREV, "<");
		item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		item.setVisible(false);
	  
		item = menu.add(1, MENUITEM_ADD, MENUITEM_ADD, "Add");
		item.setVisible(false);
		item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		item = menu.add(1, MENUITEM_VIEW, MENUITEM_VIEW, "View");
		item.setVisible(false);
		item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		
	  item = menu.add(1, MENUITEM_EDIT, MENUITEM_EDIT, "Edit");
		item.setVisible(false);
		item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		
	  item = menu.add(1, MENUITEM_DELETE, MENUITEM_DELETE, "Delete");
		item.setVisible(false);
	  
		item = menu.add(1, MENUITEM_DONE, MENUITEM_DONE, "Done");
		item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		item.setVisible(false);
		
		item = menu.add(1, MENUITEM_COLS, MENUITEM_COLS, "Edit Columns");
		item.setVisible(false);
		
		item = menu.add(1, MENUITEM_SORT, MENUITEM_SORT, "Sort");
		item.setVisible(false);
		
		item = menu.add(1, MENUITEM_FILTER, MENUITEM_FILTER, "Filter");
		item.setVisible(false);
		
		item = menu.add(1, MENUITEM_FILTER_PROJ, MENUITEM_FILTER_PROJ, "Project");
		item.setVisible(false);
		item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		item=menu.add(1, MENUITEM_EXPORT, MENUITEM_EXPORT, "Export");
		item.setVisible(false);
		
		item=menu.add(1, MENUITEM_IMPORT, MENUITEM_IMPORT, "Import");
		item.setVisible(false);
		
		item=menu.add(1, MENUITEM_PROJECTLIST_MOVE, MENUITEM_PROJECTLIST_MOVE, "Move");
		item.setVisible(false);
	}

	public static boolean Options_Item_Selected(android.view.MenuItem item, android.content.Context ctx)
	{
		boolean res=true;

		if (item.getItemId() == MENUITEM_HOME)
			ctx.startActivity(new android.content.Intent(ctx, Main_Activity.class));
		else if (item.getItemId() == MENUITEM_PROJECTS)
			ctx.startActivity(new android.content.Intent(ctx, rs.workbuddy.project.Project_List.class));
		else if (item.getItemId()==MENUITEM_EVENTS)
		  ctx.startActivity(new android.content.Intent(ctx, Event_List.class));
		else if (item.getItemId()==MENUITEM_TIMESHEET)
		  ctx.startActivity(new android.content.Intent(ctx, Report_Timesheet.class));
		else if (item.getItemId()==MENUITEM_SETTINGS)
		  ctx.startActivity(new android.content.Intent(ctx, Settings_Activity.class));
		else
		  res=false;
	 
		 /*else if (item.getItemId() == -3)
		 {
		 csv_data = this.db.Dump_Table_To_CSV("Work_Event", "notes", String.class, "start_date", java.sql.Date.class);
		 rep_uri = rs.android.Util.Save_File("Work_Event.csv", csv_data);

		 intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
		 intent.setType("text/csv");
		 intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		 intent.putExtra(android.content.Intent.EXTRA_STREAM, rep_uri);
		 startActivity(android.content.Intent.createChooser(intent, "Which application would you like to use?"));

		 res = true;
		 }*/

		return res;
	}
}
