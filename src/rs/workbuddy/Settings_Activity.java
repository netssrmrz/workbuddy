package rs.workbuddy;

public class Settings_Activity 
extends android.preference.PreferenceActivity 
implements android.preference.Preference.OnPreferenceChangeListener
{
	public static final String SETTING_KEY_ROUND_TO="round_to";

  public android.preference.PreferenceScreen ps;
	android.preference.ListPreference round_pref;

  @Override
  public void onCreate(android.os.Bundle savedInstanceState) 
  {
		String[] e=
		{
			"No Rounding",
			"5 minutes",
			"10 minutes",
			"15 minutes", 
			"30 minutes",
			"45 minutes",
			"1 hour"
		};
		String[] v={"None", "5", "10", "15", "30", "45", "60"};

		super.onCreate(savedInstanceState);

		android.preference.PreferenceManager pm;

		pm = this.getPreferenceManager();
		ps = pm.createPreferenceScreen(this);

		/*this.hr_rate = new android.preference.EditTextPreference(this);
		 this.hr_rate.setKey(SETTING_KEY_HOURLY_RATE);
		 this.hr_rate.setTitle("Hourly Rate");
		 this.hr_rate.setDefaultValue(50);
		 this.hr_rate.getEditText().setRawInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
		 this.hr_rate.setOnPreferenceChangeListener(this);
		 this.hr_rate.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_HOURLY_RATE, null));
		 ps.addPreference(this.hr_rate);

		 this.curr_pref = new android.preference.EditTextPreference(this);
		 this.curr_pref.setKey(SETTING_KEY_CURRENCY_SYMBOL);
		 this.curr_pref.setTitle("Currency Symbol");
		 this.curr_pref.setDefaultValue("$");
		 this.curr_pref.setOnPreferenceChangeListener(this);
		 this.curr_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_CURRENCY_SYMBOL, null));
		 ps.addPreference(this.curr_pref);*/

		this.round_pref = new android.preference.ListPreference(this);
		this.round_pref.setKey(SETTING_KEY_ROUND_TO);
		this.round_pref.setTitle("Round Activity Time");
		this.round_pref.setDefaultValue("15");
		this.round_pref.setOnPreferenceChangeListener(this);
		this.round_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_ROUND_TO, null));
		this.round_pref.setEntries(e);
		this.round_pref.setEntryValues(v);
		ps.addPreference(this.round_pref);

		this.setPreferenceScreen(ps);
  }

	public boolean onPreferenceChange(android.preference.Preference p, Object newValue)
	{
		boolean res=true;
		String label;

		label = rs.android.Util.To_String(newValue);
		if (p.getKey().equals(SETTING_KEY_ROUND_TO) && !label.equals("None"))
			label = label + " minutes";
		p.setSummary(label);

		return res;
	}

	/*public static float Get_Hourly_Rate(android.content.Context ctx)
	 {
	 String hr_rate_str;
	 float res=0;

	 hr_rate_str = android.preference.PreferenceManager.
	 getDefaultSharedPreferences(ctx).
	 getString(SETTING_KEY_HOURLY_RATE, null);
	 res = rs.android.Util.To_Float(hr_rate_str);
	 return res;
	 }

	 public static String Get_Currency_Symbol(android.content.Context ctx)
	 {
	 return android.preference.PreferenceManager.
	 getDefaultSharedPreferences(ctx).
	 getString(SETTING_KEY_CURRENCY_SYMBOL, "");
	 }*/

	public static Long Get_Rounding(android.content.Context ctx)
	{
		String round_str;
		Long res=null;

		round_str = android.preference.PreferenceManager.
		  getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_ROUND_TO, "");

		if (rs.android.Util.NotEmpty(round_str) && !round_str.equals("None"))
			res = rs.android.Util.To_Long(round_str) * 60 * 1000;

		return res;
	}
}
