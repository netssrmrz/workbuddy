package rs.android.ui;

public class Filter_Dialog
implements 
android.content.DialogInterface.OnClickListener
{
	public interface On_Filter_Set_Listener
	{
		public void On_Filter_Set(Filter_Option which);
	}

	public android.content.Context ctx;
	public boolean dlg_ok;
	public On_Filter_Set_Listener on_filter_set_listener;
	public java.util.ArrayList<Filter_Option> options;
	public Filter_Option selected;

	public Filter_Dialog(android.content.Context ctx, On_Filter_Set_Listener listener)
	{
		this.ctx = ctx;
		this.dlg_ok = false;
		this.on_filter_set_listener = listener;
	}

	public void Add(int id, String label)
	{
		Filter_Option option;

		if (this.options==null)
			this.options=new java.util.ArrayList<Filter_Option>();

		option=new Filter_Option();
		option.id=id;
		option.label=label;
		this.options.add(option);
	}

	public void Show()
	{
		android.app.AlertDialog.Builder builder;
		String[] items=null;
		int c;

		builder=new android.app.AlertDialog.Builder(this.ctx);
		builder.setTitle("Filter by");
		if (rs.android.Util.NotEmpty(this.options))
		{
			items=new String[this.options.size()];
			for (c=0; c<this.options.size(); c++)
			{
				items[c]=this.options.get(c).label;
			}
		}
		builder.setItems(items, this);
		builder.show();
	}

	public void onClick(android.content.DialogInterface dlg, int which)
	{
		if (this.on_filter_set_listener != null)
		{
			this.selected=this.options.get(which);
			this.on_filter_set_listener.On_Filter_Set(this.selected);
		}
	}
}
