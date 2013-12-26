package rs.android.ui;

public class Sort_Dialog
implements 
android.content.DialogInterface.OnClickListener
{
	public interface On_Sort_Set_Listener
	{
		public void On_Sort_Set(Sort_Option which);
	}

	public android.content.Context ctx;
	public boolean dlg_ok;
	public On_Sort_Set_Listener on_sort_set_listener;
	public java.util.ArrayList<Sort_Option> options;
	public Sort_Option selected;

	public Sort_Dialog(android.content.Context ctx, On_Sort_Set_Listener listener)
	{
		this.ctx = ctx;
		this.dlg_ok = false;
		this.on_sort_set_listener = listener;
	}

	public void Add(int id, String label)
	{
		Sort_Option option;
		
		if (this.options==null)
			this.options=new java.util.ArrayList<Sort_Option>();
			
		option=new Sort_Option();
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
		builder.setTitle("Sort by");
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
		if (this.on_sort_set_listener != null)
		{
			this.selected=this.options.get(which);
			this.on_sort_set_listener.On_Sort_Set(this.selected);
		}
	}
}
