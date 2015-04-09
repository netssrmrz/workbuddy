package rs.android.ui;

public class Time_Dialog
implements 
android.app.TimePickerDialog.OnTimeSetListener
{
  public interface On_Time_Set_Listener
	{
		public void On_Time_Set(java.sql.Date date);
	}

	public android.content.Context ctx;
	public On_Time_Set_Listener on_time_set_listener;

	public Time_Dialog(android.content.Context ctx, On_Time_Set_Listener listener)
	{
		this.ctx = ctx;
		this.on_time_set_listener=listener;
	}

	public void Show(java.sql.Date init)
	{
		android.app.TimePickerDialog dlg;
		int hour, minute;

		if (init==null)
		  init = rs.android.util.Date.Now();
		hour = rs.android.util.Date.Date_Get_Hour(init);
		minute = rs.android.util.Date.Date_Get_Minute(init);
		dlg = new android.app.TimePickerDialog(this.ctx, this, hour, minute, false);
		dlg.show();
	}

  public void onTimeSet(android.widget.TimePicker v, int hour, int minute)
  {
    java.sql.Date date;

    if (this.on_time_set_listener!=null)
    {
      date=rs.android.util.Date.New_Time(hour, minute, 0);
      this.on_time_set_listener.On_Time_Set(date);
    }
	}
}
