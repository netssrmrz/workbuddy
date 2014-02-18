package rs.android.ui;
import java.util.*;

public class Date_Dialog
implements 
android.content.DialogInterface.OnClickListener,
android.app.DatePickerDialog.OnDateSetListener
{
  public interface On_Date_Set_Listener
	{
		public void On_Date_Set(java.sql.Date date);
	}
	
	public android.content.Context ctx;
	public boolean dlg_ok;
	public On_Date_Set_Listener on_date_set_listener;

	public Date_Dialog(android.content.Context ctx, On_Date_Set_Listener listener)
	{
		this.ctx = ctx;
		this.dlg_ok = false;
		this.on_date_set_listener=listener;
	}

	public void Show(java.sql.Date init)
	{
		android.app.DatePickerDialog dlg;
		int year, month, day;

		if (init==null)
		  init = rs.android.util.Date.Now();
		year = rs.android.util.Date.Date_Get_Year(init);
		month = rs.android.util.Date.Date_Get_Month(init);
		day = rs.android.util.Date.Date_Get_Day(init);
		dlg = new android.app.DatePickerDialog(this.ctx, this, year, month, day);
		dlg.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Cancel", this);
		dlg.setButton(android.content.DialogInterface.BUTTON_POSITIVE, "Done", this);
		dlg.show();
	}

	public void onClick(android.content.DialogInterface dlg, int which)
	{
		if (which == android.content.DialogInterface.BUTTON_POSITIVE)
			this.dlg_ok = true;
	}

	public void onDateSet(android.widget.DatePicker v, int year, int month, int day)
	{
		java.sql.Date date;
		
		if (this.dlg_ok && this.on_date_set_listener!=null)
		{
			date=rs.android.util.Date.New_Date(year, month+1, day);
			this.on_date_set_listener.On_Date_Set(date);
		}
		this.dlg_ok = false;
	}
}
