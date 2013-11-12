package rs.android.ui;

public class Db_Spinner
extends android.widget.Spinner
{
	public Db_Spinner(android.content.Context ctx)
	{
		super(ctx);
	}

	public void Set_Selection(Long id)
	{
		this.setSelection(0);
		if (id != null)
		{
			this.setSelection(
			  ((rs.android.ui.Db_Adapter)this.getAdapter()).
				Get_Item_Position(id));
		}
	}

	public Long Get_Selected_Id()
	{
		long id;
		Long res=null;

		id = this.getSelectedItemId();
		if (id == rs.android.ui.Db_Adapter.ID_NA)
			res = null;
		else
		  res = id;
		return res;
	}
}
