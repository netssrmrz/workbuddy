package rs.android;

import java.io.IOException;
import java.util.Arrays;
import android.content.*;
import java.lang.reflect.*;
import java.io.*;

public class Util
{
  public static android.content.Context ctx=null;
  
  public static Object Add(Object val, Object inc)
  {
    Object res=val;
    
    if (val!=null & inc!=null)
    {
      if (val instanceof java.sql.Date)
      {
        if (inc instanceof Integer)
          res=((java.sql.Date)val).getTime()+((Integer)inc).longValue();
        if (inc instanceof Float)
          res=((java.sql.Date)val).getTime()+((Float)inc).longValue();
        if (inc instanceof Long)
          res=((java.sql.Date)val).getTime()+((Long)inc).longValue();
        if (res!=null)
          res=new java.sql.Date((Long)res);
      }
    }
    return res;
  }

  public static Object Min(java.util.List<?> objs, String field_name, Integer round, Object def)
  {
    Object res=def, field_val;
    
    if (NotEmpty(objs) && NotEmpty(field_name))
    {
      for (Object obj: objs)
      {
        field_val=rs.android.util.Type.GetObjFieldValue(obj, field_name);
        if (res==null)
          res=field_val;
        else if (field_val instanceof java.sql.Date && ((java.sql.Date)res).after((java.sql.Date)field_val))
          res=field_val;
        else if (field_val instanceof Double && (Double)res>(Double)field_val)
          res=field_val;
      }
      res=Round(res, round);
    }
    return res;
  }
      
  public static Object Max(java.util.List<?> objs, String field_name, Integer round, Object init_max)
  {
    Object res=null, field_val;
    
    if (init_max!=null)
      res=init_max;
    if (NotEmpty(objs) && NotEmpty(field_name))
    {
      for (Object obj: objs)
      {
        field_val=rs.android.util.Type.GetObjFieldValue(obj, field_name);
        if (res==null)
          res=field_val;
        else if (field_val instanceof java.sql.Date && ((java.sql.Date)res).before((java.sql.Date)field_val))
          res=field_val;
        else if (field_val instanceof Double && (Double)res<(Double)field_val)
          res=field_val;
      }
      res=Round(res, round);
    }
    return res;
  }
  
  public static Object Round(Object value, Integer round)
  {
    Object res=value;
    java.util.Calendar to, from;
    
    if (value!=null && round!=null)
    {
      if (value instanceof java.sql.Date &&
			  round.intValue()==rs.android.util.Date.ROUND_DATE_DAY)
      {
        from=java.util.Calendar.getInstance();
        from.setTime((java.sql.Date)value);
        
        to=java.util.Calendar.getInstance();
        to.clear();
        to.set(from.get(java.util.Calendar.YEAR), from.get(java.util.Calendar.MONTH), from.get(java.util.Calendar.DAY_OF_MONTH));
        res=new java.sql.Date(to.getTimeInMillis());
      }
    }
    return res;
  }
  
	public static boolean Equals(Object a, Object b)
	{
		boolean res=false;
		
		if (a==null && b==null)
			res=true;
		else if (a!=null && b!=null)
		  res=a.equals(b);
		return res;
	}
	
  public static boolean NotEmpty(Object obj) 
  {
    boolean res=false;
    int avail;
		String str;
    
    if (obj!=null)
    {
      res=true;
      if (obj instanceof String && ((String)obj).trim().length()<=0)
        res=false;
			else if (obj instanceof android.text.Editable && ((android.text.Editable)obj).toString().trim().length()<=0)
			  res=false;
      else if (obj instanceof android.database.Cursor && ((android.database.Cursor)obj).getCount()<=0) 
        res=false;
      else if (obj instanceof android.database.sqlite.SQLiteDatabase && !((android.database.sqlite.SQLiteDatabase)obj).isOpen())
        res=false;
      else if (obj instanceof Db && !NotEmpty(((Db)obj).conn))
        res=false;
			//else if (obj instanceof int[] && ((int[])obj).length<=0)
			  //res=false;
      else if (obj.getClass().isArray() && ((Object[])obj).length<=0)
        res=false;
      else if (obj instanceof android.content.ContentValues && ((android.content.ContentValues)obj).size()<=0)
        res=false;
      else if (obj instanceof java.lang.Integer && ((java.lang.Integer)obj).intValue()==0)
        res=false;
      else if (obj instanceof java.lang.Double && ((java.lang.Double)obj).doubleValue()==0.0)
        res=false;
      else if (obj instanceof java.sql.Date && ((java.sql.Date)obj).getTime()==0)
        res=false;
      else if (obj instanceof java.util.Collection<?> && ((java.util.Collection<?>)obj).size()==0)
        res=false;
      else if (obj instanceof java.util.List<?> && ((java.util.List<?>)obj).size()==0)
        res=false;
			else if (obj instanceof android.view.ViewGroup && ((android.view.ViewGroup)obj).getChildCount()==0)
			  res=false;
      else if (obj instanceof java.io.InputStream)
      {
        try {avail=((java.io.InputStream)obj).available();}
        catch (java.io.IOException e) {e.printStackTrace(); avail=0;}
        if (avail==0)
          res=false;
      }
    }
    
    return res;
  }
  
  public static String Trim(String str)
  {
    String res=null;
    
    if (NotEmpty(str))
      res=str.trim();
    return res;
  }
  
  public static String Remove_Other_Chars(String ok_chars, String str)
  {
    String res=null;
    int c;
    char ch;
    StringBuilder sb;
    
    if (NotEmpty(str) && NotEmpty(ok_chars))
    {
      sb=new StringBuilder();
      for (c=0; c<str.length(); c++)
      {
        ch=str.charAt(c);
        if (ok_chars.indexOf(ch)!=-1)
        {
          sb.append(ch);
        }
      }
      res=sb.toString();
    }
    return res;
  }
  
  public static Object List_Contains(java.util.Collection<?> list, String field_name, String match_type, Object field_val)
  {
    Object res=null, obj_field_val;
    
    if (NotEmpty(list))
    {
      for (Object obj: list)
      {
        if (obj!=null)
        {
          obj_field_val=rs.android.util.Type.GetObjFieldValue(obj, field_name);
          if (match_type.equals("ends_with") && 
					  rs.android.util.Type.To_String(obj_field_val, "").
						  endsWith(rs.android.util.Type.To_String(field_val)))
          {
            res=obj;
            break;
          }
          else if (obj_field_val.equals(field_val))
          {
            res=obj;
            break;
          }
        }
      }
    }
    return res;
  }
  
  public static String AppendStr(Object obja, Object objb, String div)
  {
    return AppendStr(obja, objb, div, null, null, null, false);
  }
  
  public static String AppendStr(Object obja, Object objb, String div, String def)
  {
    return AppendStr(obja, objb, div, def, null, null, false);
  }
  
  public static String AppendStr(Object obja, Object objb, String div, String def, String env)
  {
    return AppendStr(obja, objb, div, def, env, env, false);
  }
  
  public static String AppendStr(Object obja, Object objb, String div, String def, String open_env, String close_env, boolean append_null)
  {
    java.lang.String res=null, stra, strb;

    stra=rs.android.util.Type.To_String(obja, def);
    
    strb=rs.android.util.Type.To_String(objb, def);
    if (NotEmpty(strb) && NotEmpty(open_env) && NotEmpty(close_env))
      strb=open_env+strb+close_env;

    if (NotEmpty(strb) && NotEmpty(stra))
      res=stra+div+strb;
    else if (!NotEmpty(strb) && NotEmpty(stra))
		{
			if (!NotEmpty(div) && append_null)
				res=stra+div;
			else
        res=stra;
		}
    else if (NotEmpty(strb) && !NotEmpty(stra))
		{
			if (!NotEmpty(div) && append_null)
				res=div+strb;
			else
        res=strb;
		}	
    return res;
  }
  
  public static String Extract_Str(String prefix, String suffix, String src)
  {
    String res=null;
    int start=-1, end=-1;

    if (NotEmpty(prefix))
      start=Find_Str(prefix, src, false, 0);
    if (start==-1)
      start=0;
    else
      start++;

    if (NotEmpty(suffix))
      end=Find_Str(suffix, src, true, start);
    if (end==-1)
      end=src.length();

    if (start>-1 && end>-1)
      res=src.substring(start, end);
    return res;
  }

  public static int Find_Str(String target_str, String in_str, boolean ret_start, int start_at)
  {
    int pos=0, c, start_pos=0, res=-1;
    char ch;

    if (NotEmpty(target_str) && NotEmpty(in_str))
    {
      for (c=start_at; c<in_str.length(); c++)
      {
        ch=in_str.charAt(c);
        if (ch!='\r' && ch!=' ' && ch!='\n' && ch!='\t')
        {
          if (target_str.charAt(pos)==ch)
          {
            if (pos==0 && ret_start)
              start_pos=c;
            pos++;
            if (pos==target_str.length())
            {
              if (ret_start)
                res=start_pos;
              else
                res=c;
              break;
            }
          }
          else
            pos=0;
        }
      }
    }
    return res;
  }

  public static String Build_Str_List(Object[] objs, String field_name, String separator, String envelope)
  {
    return Build_Str_List(Arrays.asList(objs), field_name, separator, envelope);
  }

  public static String Build_Str_List(java.util.List<?> objs, String field_name, String separator, String envelope)
  {
    String res=null;
    Object val;

    if (NotEmpty(objs))
    {
      if (!NotEmpty(separator))
        separator=", ";
      for (Object obj: objs)
      {
        if (NotEmpty(field_name))
          val=rs.android.util.Type.GetObjFieldValue(obj, field_name);
        else
          val=obj;
        res=AppendStr(res, val, separator, null, envelope, envelope, false);
      }
    }
    return res;
  }
	
  public static void Err_To_Log(Exception ex)
  {
    String tag="rs.android.Util.Err_To_Log()";

    if (ex!=null)
      android.util.Log.d(tag, ex.getMessage());
  }

	public static String Serialise(Object obj)
	{
		java.io.ObjectOutputStream so;
		java.io.ByteArrayOutputStream bo;
		String res=null;

		if (obj != null)
		{
			try
			{
				bo = new java.io.ByteArrayOutputStream();
				so = new java.io.ObjectOutputStream(bo);
				so.writeObject(obj);
				so.flush();
				res=android.util.Base64.encodeToString(bo.toByteArray(), 
				  android.util.Base64.DEFAULT);
			}
			catch (java.io.IOException e)
			{
				android.util.Log.d("rs.android.Util.Serialise()", e.toString());
				res = null;
			}
		}
		return res;
	}
	
	public static void Save_Data(android.content.Context ctx, String key, Object data)
	{
		android.content.SharedPreferences.Editor prefs;
		String data_str=null;

		data_str=Util.Serialise(data);
		if (data_str != null)
		{
			prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx).edit();
			prefs.putString(key, data_str);
			prefs.apply();
	  }
	}
	
	public static Object Deserialise(String data_str)
	{
		byte b[];
    Object res=null;
		java.io.ByteArrayInputStream bi;
		java.io.ObjectInputStream si;
		
		if (data_str != null)
		{
			try 
			{
				b = android.util.Base64.decode(data_str, android.util.Base64.DEFAULT);
				bi = new java.io.ByteArrayInputStream(b);
				si = new java.io.ObjectInputStream(bi);
				res = si.readObject();
			} 
			catch (Exception e) 
			{
				res = null;
			}
		}
		return res;
	}
	
	public static Object Load_Data(android.content.Context ctx, String key)
	{
		Object res=null;
		String data_str=null;

		data_str = android.preference.PreferenceManager.
		  getDefaultSharedPreferences(ctx).getString(key, null);
    res=Util.Deserialise(data_str);
		
		return res;
	}
}
