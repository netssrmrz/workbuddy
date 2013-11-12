package rs.workbuddy;

public class Workbuddy_Activity_Add
extends 
rs.workbuddy.Workbuddy_Activity
{
	public android.view.MenuItem done_menu;
	public android.widget.LinearLayout main_view;
	public boolean finish;

  @Override
  public void onCreate(android.os.Bundle state)
	{
		try
		{
			super.onCreate(state);

			this.finish = true;
			main_view = new android.widget.LinearLayout(this);
			main_view.setOrientation(android.widget.LinearLayout.VERTICAL);

      On_Create_UI(main_view);

			this.setContentView(main_view);
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
  }

  @Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		menu.findItem(Menus.MENUITEM_DONE).setVisible(true);
		return true;
	}
	
	@Override
	public void On_Done()
	{
		On_Save_Obj();
		if (this.finish)
			this.finish();
	}

	public static String Get_Text(android.widget.EditText v)
	{
		String res=null;
		android.text.Editable text;

		if (v != null)
		{
			text = v.getText();
			if (rs.android.Util.NotEmpty(text))
			{
				res = text.toString();
			}
		}
		return res;
	}

	public void Add_Field(String title, android.view.View widget)
	{
		android.widget.LinearLayout row;
		android.widget.TextView label;

		row = new android.widget.LinearLayout(this);
		row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
		row.setPadding(15, 15, 0, 0);

		label = rs.android.ui.Util.New_Label(this, title, 200);
		row.addView(label);

		if (widget instanceof android.widget.TextView)
		  ((android.widget.TextView)widget).setTextSize(20);
		row.addView(widget);

		main_view.addView(row);
	}

	@Override
	public void On_Resume()
	{
		long id;

		try
		{
			if (this.getIntent().hasExtra("id"))
			{
				id = getIntent().getLongExtra("id", 0);
				On_Load_Obj(id);
			}
			else
				On_New_Obj();
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
	}

	void On_New_Obj()
	{
	}

	void On_Load_Obj(Long id)
	{
	}

	void On_Save_Obj()
	{
	}

	void On_Create_UI(android.view.ViewGroup view)
	{
	}
}
