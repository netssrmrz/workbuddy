package rs.workbuddy;

public class Workbuddy_Activity 
extends 
android.app.Activity
implements android.content.DialogInterface.OnClickListener
{
  public rs.workbuddy.Db db;
	public android.view.Menu menu;

	public void Update_UI()
	{
		On_Update_UI();
	}

	@Override
	public void onCreate(android.os.Bundle state)
	{
		super.onCreate(state);
		this.db = new Db(this);
	}

  @Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		//android.util.Log.d("rs.workbuddy.Workbuddy_Activity.onCreateOptiinsMenu()", "Entry");
		this.menu=menu;
		rs.workbuddy.Menus.Create_Options_Menu(menu);
		return true;
	}

  @Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=true;
		android.app.AlertDialog.Builder b;
		android.app.AlertDialog dlg;
		
		//android.util.Log.d("Workbuddy_Activity.onOptionsItemSelected()", "Entry");
		if (item.getItemId() == Menus.MENUITEM_EDIT)
			On_Edit();
		else if (item.getItemId()==Menus.MENUITEM_DONE)
		  On_Done();
		else if (item.getItemId()==Menus.MENUITEM_ADD)
		  On_Add();
		else if (item.getItemId()==Menus.MENUITEM_NEXT)
		  On_Next();
		else if (item.getItemId()==Menus.MENUITEM_PREV)
		  On_Prev();
		else if (item.getItemId()==Menus.MENUITEM_VIEW)
		  On_View();
		else if (item.getItemId()==Menus.MENUITEM_COLS)
		  On_Edit_Cols();
		else if (item.getItemId()==Menus.MENUITEM_SORT)
			On_Sort();
		else if (item.getItemId()==Menus.MENUITEM_DELETE)
		{
			b = new android.app.AlertDialog.Builder(this);
			b.setMessage("Are you sure?");
			b.setTitle("Warning");
			b.setPositiveButton("OK", this);
			b.setNegativeButton("Cancel", this);
			dlg = b.create();
			dlg.show();
		}
		else
			res=rs.workbuddy.Menus.Options_Item_Selected(item, this);
			
		return res;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		if (this.db == null)
			this.db = new Db(this);

		On_Resume();

		Update_UI();
	}

	@Override
	public void onPause()
	{
		On_Pause();
		if (this.db != null)
		{
			this.db.Close();
			this.db = null;
		}
		super.onPause();
	}

	public void onClick(android.content.DialogInterface dlg, int which)
	{
		if (which == android.content.DialogInterface.BUTTON_POSITIVE)
			On_Delete();
	}

	public void On_Delete()
	{
		
	}
	
	public void On_Done()
	{
		
	}
	
	public void On_Edit()
	{
		
	}
	
	public void On_Add()
	{
		
	}
	
	public void On_Next()
	{
		
	}
	
	public void On_Prev()
	{
		
	}
	
	public void On_Pause()
	{
    
	}

	public void On_Resume()
	{
    
	}

	public void On_Update_UI()
	{
    
	}
	
	public void On_View()
	{
		
	}
	
	public void On_Edit_Cols()
	{
		
	}
	
	public void On_Sort()
	{
		
	}
}
