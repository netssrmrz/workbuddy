package rs.android.ui;

public class Date_Button
extends android.widget.Button
implements 
android.view.View.OnClickListener,
rs.android.ui.Date_Dialog.On_Date_Set_Listener
{
	public java.sql.Date date;

  public Date_Button(android.app.Activity ctx)
	{
		super(ctx, null, android.R.attr.spinnerStyle);
		this.setOnClickListener(this);
	}

	public void onClick(android.view.View v)
  {
		rs.android.ui.Date_Dialog dlg;
		
		try
		{
			dlg=new rs.android.ui.Date_Dialog(this.getContext(), this);
			dlg.Show(this.date);
		}
		catch (Exception e)
		{
			rs.android.ui.Util.Show_Error(this.getContext(), e);
		}
	}

	public void On_Date_Set(java.sql.Date date)
	{
		this.Set_Date(date);
	}

	public void Set_Date(java.sql.Date date)
	{
		String date_str;
		
		this.date=date;
		this.setText("n/a");
		if (date != null)
		{
			date_str = rs.android.util.Type.To_String(date, "n/a", "EEEE dd/MM/yyyy");
			this.setText(date_str);
		}		
	}
}
