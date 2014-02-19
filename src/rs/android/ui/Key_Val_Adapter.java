package rs.android.ui;

public class Key_Val_Adapter
implements android.widget.SpinnerAdapter
{
	public static final long ID_NA=-1;
	public Long[] ids;
	public String[] items;

	public Key_Val_Adapter(java.util.Map map)
	{
		java.util.ArrayList<Object> items;
		java.util.ArrayList<Long> ids;
		Long id;
		String item;
		
		if (rs.android.Util.NotEmpty(map))
		{
			ids = new java.util.ArrayList<Long>();
			items = new java.util.ArrayList<Object>();
			
			ids.add(ID_NA);
			items.add("n/a");
			
			for (Object key: map.keySet())
			{
				id = rs.android.util.Type.To_Long(key);
				item = rs.android.util.Type.To_String(map.get(key));

				ids.add(id);
				items.add(item);
			}
			
			this.ids = new Long[ids.size()];
			this.items = new String[items.size()];
			ids.toArray(this.ids);
			items.toArray(this.items);
		}
	}

	public void registerDataSetObserver(android.database.DataSetObserver p1)
	{
		// TODO: Implement this method
	}

	public void unregisterDataSetObserver(android.database.DataSetObserver p1)
	{
		// TODO: Implement this method
	}

	public int getCount()
	{
		int res=0;

		if (rs.android.Util.NotEmpty(this.ids))
			res = this.ids.length;
		return res;
	}

	public Object getItem(int i)
	{
		Object res=null;

		if (rs.android.Util.NotEmpty(this.items))
			res = this.items[i];
		return res;
	}

	public long getItemId(int i)
	{
		long res=0;

		if (rs.android.Util.NotEmpty(this.ids))
			res = this.ids[i];
		return res;
	}

	public int Get_Item_Position(Long id)
	{
		int res=0, c;

		if (id != null)
		{
			for (c = 0; c < this.ids.length; c++)
			{
				if (this.ids[c].equals(id))
				{
					res = c;
					break;
				}
			}
		}
		return res;
	}

	public boolean hasStableIds()
	{
		return true;
	}

	public android.view.View getView(int i, android.view.View v, 
	  android.view.ViewGroup p)
	{
		android.widget.TextView res=null;

		res = rs.android.ui.Util.New_Label(p.getContext(), items[i], null);
		return res;
	}

	public int getItemViewType(int p1)
	{
		return 0;
	}

	public int getViewTypeCount()
	{
		return 1;
	}

	public boolean isEmpty()
	{
		boolean res=true;

		if (rs.android.Util.NotEmpty(this.ids))
			res = false;
		return res;
	}

	public android.view.View getDropDownView(int i, 
	  android.view.View v, android.view.ViewGroup p)
	{
		android.widget.TextView res=null;

		res = rs.android.ui.Util.New_Label(p.getContext(), this.items[i], null);
		res.setPadding
		(
			res.getPaddingLeft() + 20,
			res.getPaddingTop() + 10,
			res.getPaddingRight() + 10,
			res.getPaddingBottom() + 15
		);
		return res;
	}
}
