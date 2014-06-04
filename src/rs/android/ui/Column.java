package rs.android.ui;
import android.content.*;
import java.util.*;

public class Column
{
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_LEFT = 2;
	public static final int ALIGN_CENTRE = 3;
	
	public String id;
	public String title;
	public boolean visible;
	public boolean wrap;
	public int align;
	
	public static void Save(android.content.Context ctx, String prefix, java.util.ArrayList<Column> cols)
	{
		if (rs.android.Util.NotEmpty(cols))
		{
			for (Column col: cols)
			{
				col.Save(ctx, prefix);
			}
		}
	}
	
	public void Load(android.content.Context ctx, String prefix)
	{
		String key;
		android.content.SharedPreferences prefs;
		
		key=this.Key(prefix);
		prefs=android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
		if (prefs.contains(key))
		  this.visible = prefs.getBoolean(key, true);
	}
	
	public String Key(String prefix)
	{
		String res=null;
		
		res=prefix+".Column."+this.id+".visible";
		return res;
	}
	
	public static void Load(android.content.Context ctx, String prefix, java.util.ArrayList<Column> cols)
	{
		if (rs.android.Util.NotEmpty(cols))
		{
			for (Column col: cols)
			{
				col.Load(ctx, prefix);
			}
		}
	}
	
	public void Save(android.content.Context ctx, String prefix)
	{
		String key;
		android.content.SharedPreferences.Editor prefs;
		
		key=this.Key(prefix);
		prefs=android.preference.PreferenceManager.getDefaultSharedPreferences(ctx).edit();
		prefs.putBoolean(key, this.visible);
		prefs.apply();
	}
}
