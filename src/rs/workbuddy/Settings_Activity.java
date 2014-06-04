package rs.workbuddy;
import android.preference.*;

public class Settings_Activity 
extends android.preference.PreferenceActivity 
implements android.preference.Preference.OnPreferenceChangeListener
{
	public static final String SETTING_KEY_ROUND_TO="round_to";
	public static final String SETTING_KEY_TIMESHEET="timesheet";
	public static final String SETTING_KEY_TIMESHEET2="timesheet2";
	public static final String SETTING_KEY_TIMESHEET_NAME="timesheet_name";
	public static final String SETTING_KEY_TIMESHEET_COMPANY="timesheet_company";
	public static final String SETTING_KEY_TIMESHEET_DEPT="timesheet_dept";
	public static final String SETTING_KEY_TIMESHEET_JOB="timesheet_job";
	public static final String SETTING_KEY_TIMESHEET_MAN="timesheet_man";
	public static final String SETTING_KEY_TIMESHEET_SIG="timesheet_sig";	
	public static final String SETTING_KEY_BACKUP="backup";

  public android.preference.PreferenceScreen ps;
	android.preference.ListPreference round_pref;

  @Override
  public void onCreate(android.os.Bundle savedInstanceState) 
  {
		android.preference.EditTextPreference text_pref;
		
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
		 ps.addPreference(this.hr_rate);*/

		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_TIMESHEET);
		text_pref.setTitle("Timesheet Template File");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_TIMESHEET, null));
		ps.addPreference(text_pref);
		
		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_TIMESHEET2);
		text_pref.setTitle("Timesheet Template File 2");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_TIMESHEET2, null));
		ps.addPreference(text_pref);
		
		this.round_pref = new android.preference.ListPreference(this);
		this.round_pref.setKey(SETTING_KEY_ROUND_TO);
		this.round_pref.setTitle("Round Activity Time");
		this.round_pref.setDefaultValue("15");
		this.round_pref.setOnPreferenceChangeListener(this);
		this.round_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_ROUND_TO, null));
		this.round_pref.setEntries(e);
		this.round_pref.setEntryValues(v);
		ps.addPreference(this.round_pref);

		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_TIMESHEET_NAME);
		text_pref.setTitle("Timesheet Template Contractor Name");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_TIMESHEET_NAME, null));
		ps.addPreference(text_pref);
		
		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_TIMESHEET_COMPANY);
		text_pref.setTitle("Timesheet Template Company Name");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_TIMESHEET_COMPANY, null));
		ps.addPreference(text_pref);
		
		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_TIMESHEET_DEPT);
		text_pref.setTitle("Timesheet Template Company Department");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_TIMESHEET_DEPT, null));
		ps.addPreference(text_pref);
		
		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_TIMESHEET_JOB);
		text_pref.setTitle("Timesheet Template Job Title");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_TIMESHEET_JOB, null));
		ps.addPreference(text_pref);

		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_TIMESHEET_MAN);
		text_pref.setTitle("Timesheet Template Manager's Name");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_TIMESHEET_MAN, null));
		ps.addPreference(text_pref);

		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_TIMESHEET_SIG);
		text_pref.setTitle("Timesheet Template Signature Name");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_TIMESHEET_SIG, null));
		ps.addPreference(text_pref);
		
		text_pref = new android.preference.EditTextPreference(this);
		text_pref.setKey(SETTING_KEY_BACKUP);
		text_pref.setDefaultValue(rs.workbuddy.Db.db_name+"_copy.db");
		text_pref.setTitle("Import / Export Database Filename");
		text_pref.setOnPreferenceChangeListener(this);
		text_pref.setSummary(this.ps.getSharedPreferences().getString(SETTING_KEY_BACKUP, null));
		ps.addPreference(text_pref);
		
		this.setPreferenceScreen(ps);
  }
	
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		
		rs.workbuddy.Menus.Create_Options_Menu(menu);
		menu.findItem(Menus.MENUITEM_EXPORT).setVisible(true);
		menu.findItem(Menus.MENUITEM_IMPORT).setVisible(true);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item)
	{
		boolean res=true;

		if (item.getItemId() == Menus.MENUITEM_EXPORT)
			On_Export();
		else if (item.getItemId()==Menus.MENUITEM_IMPORT)
		  On_Import();
		else
			res=rs.workbuddy.Menus.Options_Item_Selected(item, this);

		return res;
	}
	
	public void On_Export()
	{
		String filename;
		
		filename=Settings_Activity.Get_Backup_Filename(this);
		rs.workbuddy.Db.Backup(filename);
		rs.android.ui.Util.Show_Note(this, "Export to file \""+filename+"\" in Downloads directory complete.");
	}
	
	public void On_Import()
	{
		String filename;
		Db db;

		filename=Settings_Activity.Get_Backup_Filename(this);
		if (rs.workbuddy.Db.Restore(filename, this))
		{
		  rs.android.ui.Util.Show_Note(this, "Import from file \""+filename+
			"\" in Downloads directory complete.");
			
			db=new rs.workbuddy.Db(this);
			rs.android.Log.Save_Restore(db);
			db.Close();
		}
		else
			rs.android.ui.Util.Show_Note(this, "Import file \""+filename+
			"\" not found in Downloads directory.");
	}
	
	public boolean onPreferenceChange(android.preference.Preference p, Object newValue)
	{
		boolean res=true;
		String label;

		label = rs.android.util.Type.To_String(newValue);
		if (p.getKey().equals(SETTING_KEY_ROUND_TO) && !label.equals("None"))
			label = label + " minutes";
		p.setSummary(label);

		return res;
	}

	public static String Get_Timesheet_Filename(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_TIMESHEET, "");
	}

	public static String Get_Timesheet_Filename2(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_TIMESHEET2, "");
	}
	
	public static Long Get_Rounding(android.content.Context ctx)
	{
		String round_str;
		Long res=null;

		round_str = android.preference.PreferenceManager.
		  getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_ROUND_TO, "");

		if (rs.android.Util.NotEmpty(round_str) && !round_str.equals("None"))
			res = rs.android.util.Type.To_Long(round_str) * 60 * 1000;

		return res;
	}

	public static String Get_Timesheet_Name(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_TIMESHEET_NAME, "");
	}
	
	public static String Get_Timesheet_Company(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_TIMESHEET_COMPANY, "");
	}	
	
	public static String Get_Timesheet_Dept(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_TIMESHEET_DEPT, "");
	}	
	
	public static String Get_Timesheet_Job(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_TIMESHEET_JOB, "");
	}	

	public static String Get_Timesheet_Man(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_TIMESHEET_MAN, "");
	}	

	public static String Get_Timesheet_Sig(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_TIMESHEET_SIG, "");
	}	
	
	public static String Get_Backup_Filename(android.content.Context ctx)
	{
		return android.preference.PreferenceManager.
			getDefaultSharedPreferences(ctx).
			getString(SETTING_KEY_BACKUP, "");
	}
}
