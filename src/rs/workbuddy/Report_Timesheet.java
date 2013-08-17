package rs.workbuddy;

public class Report_Timesheet
extends Workbuddy_Activity
{
  @Override
  public void onCreate(android.os.Bundle state)
	{
		android.widget.TableLayout table_layout;
		
		try
		{
			super.onCreate(state);

			table_layout = new android.widget.TableLayout(this);
			table_layout.setLayoutParams(new android.widget.TableLayout.LayoutParams(
				android.widget.TableLayout.LayoutParams.FILL_PARENT,
				android.widget.TableLayout.LayoutParams.FILL_PARENT));

			this.setContentView(table_layout);
		}
		catch (Exception e)
		{
			rs.android.Util.Show_Error(this, e);
		}
  }
}
