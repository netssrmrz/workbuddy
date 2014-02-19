package rs.android.ui;

public class Time_Button
extends android.widget.Button
implements 
android.view.View.OnClickListener,
rs.android.ui.Time_Dialog.On_Time_Set_Listener
{
	public java.sql.Date time;

  public Time_Button(android.app.Activity ctx)
	{
		super(ctx, null, android.R.attr.spinnerStyle);
		this.setOnClickListener(this);
	}

	public void onClick(android.view.View v)
  {
		rs.android.ui.Time_Dialog dlg;
		
		try
		{
			dlg=new rs.android.ui.Time_Dialog(this.getContext(), this);
			dlg.Show(this.time);
		}
		catch (Exception e)
		{
			rs.android.ui.Util.Show_Error(this.getContext(), e);
		}
	}

	public void Set_Time(java.sql.Date time)
	{
		String time_str;

		this.time = time;
		this.setText("n/a");
		if (this.time != null)
		{  
			time_str = rs.android.util.Type.To_String(this.time, "n/a", "h:mm:ss a");
			this.setText(time_str);
		}
	}

	public void On_Time_Set(java.sql.Date date)
	{
    this.Set_Time(date);
	}
	
	public void OnClickPositive(android.content.DialogInterface dlg)
	{
    //rs.android.Util.Show_Note(this, "Workbuddy_Activity.OnClickPositive()");
	}
}
