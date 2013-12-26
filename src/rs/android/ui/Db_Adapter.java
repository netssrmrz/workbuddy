package rs.android.ui;
import android.database.*;
import android.view.*;

public class Db_Adapter
implements android.widget.SpinnerAdapter
{
  public static final long ID_NA=-1;
  public Long[] ids;
	public String[] items;
	public Float text_size;

  public Db_Adapter(rs.android.Db db, String na_label, String sql, Object ... params)
	{
		android.database.Cursor query_res;
		java.util.ArrayList<Object> items;
		java.util.ArrayList<Long> ids;
		Long id;
		String item;

		query_res = db.Execute_SQL(sql, params);
		if (rs.android.Util.NotEmpty(query_res))
		{
			ids = new java.util.ArrayList<Long>();
			items = new java.util.ArrayList<Object>();
			
			ids.add(ID_NA);
			if (rs.android.Util.NotEmpty(na_label))
				items.add(na_label);
			else
			  items.add("n/a");
			
			while (query_res.moveToNext())
			{
				id = query_res.getLong(0);
				item = query_res.getString(1);

				ids.add(id);
				items.add(item);
			}
			this.ids = new Long[ids.size()];
			this.items = new String[items.size()];
			ids.toArray(this.ids);
			items.toArray(this.items);
		}
		if (query_res != null)
			query_res.close();
	}

	public void registerDataSetObserver(DataSetObserver p1)
	{
		// TODO: Implement this method
	}

	public void unregisterDataSetObserver(DataSetObserver p1)
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
		
		if (id!=null)
		{
			for (c=0; c<this.ids.length; c++)
			{
				if (this.ids[c].equals(id))
				{
					res=c;
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

	public View getView(int i, View v, ViewGroup p)
	{
		android.widget.TextView res=null;

		res = rs.android.ui.Util.New_Label(p.getContext(), items[i], null);
		if (this.text_size!=null)
		  res.setTextSize(this.text_size);
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

	public View getDropDownView(int i, View v, ViewGroup p)
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
		if (this.text_size!=null)
		  res.setTextSize(this.text_size-2);
		return res;
	}
}
