package rs.android.ui;

public class Filter_Option
{
	public int id;
	public String label;
	
  public static String Key(String prefix)
	{
		String res=null;

		res=prefix+".Filter_Option.active";
		return res;
	}

	public static int Load(android.content.Context ctx)
	{
		int res=0;
		
		res=rs.android.ui.Sort_Option.Load(ctx, ctx.getClass().getName());
		return res;
	}
	
	public static int Load(android.content.Context ctx, String prefix)
	{
		String key;
		int res=0;

		key=Key(prefix);
		res = android.preference.PreferenceManager.
		  getDefaultSharedPreferences(ctx).getInt(key, 0);
		return res;
	}
	
	public static void Save(android.content.Context ctx, String prefix, int id)
	{
		String key;
		android.content.SharedPreferences.Editor prefs;

		key=Key(prefix);
		prefs=android.preference.PreferenceManager.getDefaultSharedPreferences(ctx).edit();
		prefs.putInt(key, id);
		prefs.apply();
	}
}
