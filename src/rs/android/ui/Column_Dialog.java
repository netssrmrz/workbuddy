package rs.android.ui;
//import rs.workbuddy.*;
//import java.util.*;

public class Column_Dialog
implements 
android.content.DialogInterface.OnClickListener,
android.content.DialogInterface.OnMultiChoiceClickListener
{
	public interface On_Column_Set_Listener
	{
		public void On_Column_Set();
	}

	public android.content.Context ctx;
	public boolean dlg_ok;
	public On_Column_Set_Listener on_column_set_listener;
	public java.util.ArrayList<Column> options;
	public Column selected;

	public Column_Dialog(android.content.Context ctx, On_Column_Set_Listener listener)
	{
		this.ctx = ctx;
		this.dlg_ok = false;
		this.on_column_set_listener = listener;
	}

	public void Show()
	{
		android.app.AlertDialog.Builder builder;
		String[] items=null;
		boolean[] checked=null;
		Column col;
		int c;

		builder = new android.app.AlertDialog.Builder(this.ctx);
		builder.setTitle("Visible Columns");
		if (rs.android.Util.NotEmpty(this.options))
		{
			items = new String[this.options.size()];
			checked=new boolean[this.options.size()];
			for (c = 0; c < this.options.size(); c++)
			{
				col=this.options.get(c);
				items[c] = col.title;
				checked[c]=col.visible;
			}
		}
		builder.setMultiChoiceItems(items, checked, this);
		builder.setPositiveButton("Ok", this);
		builder.setNegativeButton("Cancel", this);
		builder.show();
	}

	public void onClick(android.content.DialogInterface dlg, int which)
	{
		if (this.on_column_set_listener != null && which==android.content.DialogInterface.BUTTON_POSITIVE)
		{
			this.on_column_set_listener.On_Column_Set();
		}
	}
	
	public void onClick(android.content.DialogInterface dlg, int which, boolean checked)
	{
		this.options.get(which).visible=checked;
	}
}
