package rs.android;

import java.io.IOException;
import java.util.Arrays;
import android.content.*;
import java.lang.reflect.*;
import java.io.*;

public class Util
{
  public static android.content.Context ctx=null;
  
  public static final int ROUND_DATE_DAY=1;
	public static final long MILLIS_PER_DAY=1000*60*60*24;
	
	public static void Dump_Dir(java.io.File dir)
	{
		String[] filenames;
		
		if (dir!=null && dir.isDirectory())
		{
		  filenames=dir.list();
		  if (NotEmpty(filenames))
		  {
			  for (String filename: filenames)
				{
					android.util.Log.d("Dump_Dir", filename);
				}
		  }
		}
	}
	
	public static Class<?> Class_For_Name(String name)
	{
		Class<?> res=null;
		
		try {res=Class.forName(name);}
		catch (java.lang.ClassNotFoundException e) {res=null;}
		
		return res;
	}
	
	public static void Copy_File(String in_filepath, String out_filepath)
	{
		java.io.FileInputStream in_stream;
		java.io.OutputStream out_stream;
		byte[] buffer = new byte[1024];
		int length;

		try
		{
			in_stream = new java.io.FileInputStream(in_filepath);
			out_stream = new java.io.FileOutputStream(out_filepath);
			
			while ((length = in_stream.read(buffer)) > 0)
			{
				out_stream.write(buffer, 0, length);
			}

			out_stream.flush();
			out_stream.close();
			in_stream.close();			
		}
		catch (Exception e)
		{
			android.util.Log.d("rs.android.Util.Copy_File()", e.toString());
		}
	}
	
	public static android.net.Uri Save_File(String name, String data)
	{
		String state;
		java.io.File sd_dir, csv_file;
		java.io.FileOutputStream csv_stream;
		android.net.Uri res=null;
		
		state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state))
		{
			sd_dir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS);
			sd_dir.mkdirs();
			if (rs.android.Util.NotEmpty(data))
			{
				csv_file = new java.io.File(sd_dir, name);
				try
				{
					csv_stream = new java.io.FileOutputStream(csv_file);
					csv_stream.write(data.getBytes());
					csv_stream.close();
				}
				catch (Exception e)
				{
					csv_file=null;
				}
				
				if (csv_file!=null)
				  res = android.net.Uri.fromFile(csv_file);
			}
		}
		return res;
	}
	
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
        field_val=GetObjFieldValue(obj, field_name);
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
  
  public static android.graphics.PointF To_Canvas_Pt(android.graphics.RectF world_window, 
      android.graphics.RectF canvas_window, float x, float y)
  {
    android.graphics.PointF res=null;
    float cw, ww;
    
    res=new android.graphics.PointF();
    
    cw=canvas_window.right-canvas_window.left;
    ww=world_window.right-world_window.left;
    res.x=((x-world_window.left)*cw/ww)+canvas_window.left;
    
    cw=canvas_window.bottom-canvas_window.top;
    ww=world_window.top-world_window.bottom;
    res.y=canvas_window.bottom-((y-world_window.bottom)*cw/ww);
    
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
        field_val=GetObjFieldValue(obj, field_name);
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
      if (value instanceof java.sql.Date && round.intValue()==ROUND_DATE_DAY)
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
	
  /**
   * 
   * Used to test whether strings, arrays, JDBC result sets, and lists are empty, or if a given 
   * JDBC connection is open.
   *
   * @param obj Object to test.
   * 
   * @return Boolean indicating "true" if the corresponding object is not empty and "false" if the 
   * object is empty or null.
   * 
   * @author Esteban Ramirez
   * 
   */
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
      //else if (obj instanceof Db && NotEmpty(((Db)obj).conn))
        //res=true;
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
  
  @SuppressWarnings("unchecked")
  public static void SetWidgets(android.app.Activity activity, java.lang.Class<? extends Object> ids_class) 
  {
    int id;
    android.view.View widget;
    String id_name, tag;
    java.lang.reflect.Field activity_field;
    java.util.Collection<Object> list;
    
    for (java.lang.reflect.Field field: ids_class.getFields())
    {
      if (field.getType().equals(int.class))
      {
        try 
        {
          id=field.getInt(null);
          id_name=field.getName();
        }
        catch (java.lang.Exception e) 
        {
          id=0;
          id_name=null;
        }

        widget=activity.findViewById(id);
        if (widget!=null)
        {
          tag=(String)widget.getTag();
          if (rs.android.Util.NotEmpty(tag))
            id_name=tag;
          
          try {activity_field=activity.getClass().getField(id_name);}
          catch (java.lang.Exception e) {activity_field=null;}
          if (activity_field!=null)
          {
            if (rs.android.Util.IsGenericList(activity_field, widget.getClass()))
            {
              try {list=(java.util.Collection<Object>)activity_field.get(activity);}
              catch (java.lang.Exception e) {list=null;}
              if (list==null)
              {
                try {list=(java.util.Collection<Object>)activity_field.getType().newInstance();}
                catch (java.lang.Exception e) {list=null;}
                if (list!=null)
                {
                  try {activity_field.set(activity, list);}
                  catch (java.lang.Exception e) {e.printStackTrace();}
                }
              }
              if (list!=null)
                list.add(widget);
            }
            else
            {
              try {activity_field.set(activity, widget);} 
              catch (java.lang.Exception e) {e.printStackTrace();}
            }
          }
          
          if (activity instanceof android.view.View.OnClickListener)
            if (!(widget instanceof android.widget.ListView))
              widget.setOnClickListener((android.view.View.OnClickListener)activity);
          widget.setOnCreateContextMenuListener(activity);
        }
      }
    }
  }
  
  /**
   * 
   * Used to extract the value of a given objects field by name. This function will use a
   * case-insensitive search to execute the first getter method it finds matching the given
   * name. 
   * 
   * @param obj Object whose field value will be retrieved.
   * 
   * @param field_name String of getter method to use.
   * 
   * @return Object representing the value of the specified field.
   * 
   * @author Esteban Ramirez
   *
   */
  public static Object GetObjFieldValue(Object obj, String field_name)
  {
    Object res=null;
    java.lang.reflect.Method method;
    java.lang.reflect.Field field;
    Class<? extends Object> class_type;

    if (obj!=null && NotEmpty(field_name))
    {
      class_type=obj.getClass();
      
      field=FindClassField(class_type, field_name);
      if (NotEmpty(field))
      {
        try
        {
          res=field.get(obj);
        } 
        catch (java.lang.Exception e)
        {
          res=null;
          e.printStackTrace();
        } 
      }
      else
      {
        method=FindClassMethod(class_type, "get"+field_name);
        if (NotEmpty(method))
        {
          try
          {
            res=method.invoke(obj, (Object[])null);
          } 
          catch (java.lang.Exception e)
          {
            res=null;
            e.printStackTrace();
          } 
        }
      }
    }
    return res;
  }

  public static boolean SetObjFieldValue(Object obj, 
	  String field_name, Object val) 
  {
    java.lang.reflect.Method method;
    java.lang.reflect.Field field;
    Class<? extends Object> class_type;
    Object[] params;
		boolean res=false;

    if (obj!=null && NotEmpty(field_name))
    {
      class_type=obj.getClass();
      
      field=FindClassField(class_type, field_name);
      if (NotEmpty(field))
      {
				try {field.set(obj, val); res=true;}
				catch (Exception e) { res=false; }
      }
      else
      {
        method=FindClassMethod(class_type, "set"+field_name);
        if (NotEmpty(method))
        {
          params=new Object[1];
          params[0]=val;
          try {method.invoke(obj, params); res=true;}
					catch (Exception e) {res=false;}
        }
      }
    }
		return res;
  }

  /**
   * 
   * Used to find class methods by name whilst ignoring string case.
   * 
   * @param obj_class Class to be searched.
   * 
   * @param name String name of method to search for.
   * 
   * @return First method with a matching name. Note that parameters are ignored. 
   * 
   * @author Esteban Ramirez
   *
   */
  public static java.lang.reflect.Method FindClassMethod(Class<? extends Object> obj_class, String name)
  {
    java.lang.reflect.Method res=null;
    java.lang.reflect.Method[] methods=null;
    int c;
    
    if (NotEmpty(obj_class) && NotEmpty(name))
    {
      name=name.toLowerCase();
      methods=obj_class.getMethods();
      if (NotEmpty(methods))
      {
        for (c=0; c<methods.length; c++)
        {
          if (methods[c].getName().toLowerCase().equals(name))
          {
            res=methods[c];
            break;
          }
        }
      }
    }
    return res;
  }
  
  public static java.lang.reflect.Field FindClassField(Class<? extends Object> obj_class, String name)
  {
    java.lang.reflect.Field res=null;
    java.lang.reflect.Field[] fields=null;
    int c;
    
    if (NotEmpty(obj_class) && NotEmpty(name))
    {
      name=name.toLowerCase();
      fields=obj_class.getFields();
      if (NotEmpty(fields))
      {
        for (c=0; c<fields.length; c++)
        {
          if (fields[c].getName().toLowerCase().equals(name))
          {
            res=fields[c];
            break;
          }
        }
      }
    }
    return res;
  }  

  /**
   * 
   * Used to convert an object of any type into a string without worrying if the object is a null.
   * Same as ToString(Object, java.lang.String) but assumes the default string will be an
   * empty string.
   * 
   * @param obj Object to convert into a string.
   * 
   * @return String representing converted object.
   * 
   * @author Esteban Ramirez
   * @throws IOException 
   *
   */
  public static java.lang.String To_String(Object obj)
  {
    return To_String(obj, "", null);
  }

  public static java.lang.String To_String(Object obj, String def)
  {
    return To_String(obj, def, null);
  }

  /**
   * 
   * Used to convert an object of any type into a string without worrying if the object is a null.
   * Will also convert date, time, and byte arrays to strings.
   * 
   * @param obj Object to convert into a string.
   * 
   * @param def Default string to return if the object is null.
   * 
   * @return String representing converted object.
   * 
   * @author Esteban Ramirez
   * @throws IOException 
   *
   */
  public static java.lang.String To_String(Object obj, java.lang.String def, String format)
  {
    String res=null, fields_str, true_str, false_str;
    java.text.SimpleDateFormat date_format;
    java.text.DecimalFormat num_format;
    Object field_val;
    String[] format_vals;
    //java.io.InputStream is;
    //java.sql.Clob clob;
    //long size;
    //byte[] clob_data;
    
    res=def;
    if (obj!=null)
    {
      if (obj instanceof String && NotEmpty(obj))
        res=obj.toString().trim();
      else if (obj instanceof java.sql.Timestamp)
      {
        if (!NotEmpty(format))
          format="dd/MM/yyyy HH:mm:ss";
        date_format=new java.text.SimpleDateFormat(format);
        res=date_format.format(obj);
      }
      else if (obj instanceof java.sql.Date)
      {
        if (!NotEmpty(format))
          format="dd/MM/yyyy HH:mm:ss";
        date_format=new java.text.SimpleDateFormat(format);
        res=date_format.format(obj);
      }
      else if (obj instanceof java.lang.Double)
      {
        if (!NotEmpty(format))
          format="#,##0.##";
        num_format=new java.text.DecimalFormat(format);
        res=num_format.format(obj);
      }
      else if (obj instanceof Float)
      {
        if (!NotEmpty(format))
          format="#,##0.##";
        num_format=new java.text.DecimalFormat(format);
        res=num_format.format(obj);
      }
      else if (obj instanceof java.lang.Long)
        res=obj.toString();
      else if (obj instanceof byte[])
        res=new String((byte[])obj);
      else if (obj instanceof java.math.BigDecimal)
        res=obj.toString();
      else if (obj instanceof java.util.Collection<?>)
      {
        for (Object list_obj: (java.util.Collection<?>)obj)
        {
					res=rs.android.Util.AppendStr(res, rs.android.Util.To_String(list_obj), ", ");
          /*fields_str=null;
          for (java.lang.reflect.Field field: list_obj.getClass().getFields())
          {
            try {field_val=field.get(list_obj);}
            catch (java.lang.Exception e) {field_val=null;}
            fields_str=AppendStr(fields_str, field.getName()+": "+To_String(field_val, "null", null), ", ", null);
          }
          res+=fields_str+"\n";*/
        }
      }
      else if (obj instanceof java.lang.Boolean)
      {
        true_str="true";
        false_str="false";
        if (NotEmpty(format))
        {
          format_vals=format.split(",");
          if (format_vals.length>0 && NotEmpty(format_vals[0]))
            true_str=Trim(format_vals[0]);
          if (format_vals.length>1 && NotEmpty(format_vals[1]))
            false_str=Trim(format_vals[1]);
        }
        
        if (((java.lang.Boolean)obj).booleanValue())
          res=true_str;
        else
          res=false_str;
      }
      /*else if (obj instanceof java.sql.Clob)
      {
        clob=(java.sql.Clob)obj;
        size=clob.length();
        if (size>0)
        {
          clob_data=new byte[(int)size];
          is=clob.getAsciiStream();
          is.read(clob_data);
          
          res=new String(clob_data);
        }
      }*/
      else
        res=obj.toString();
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
  public static java.lang.Integer To_Int(Object obj)
  {
    java.lang.Integer res=null;
    
    if (obj!=null)
    {
      if (obj instanceof java.lang.String)
      {
        res=java.lang.Integer.parseInt((String)obj);
      }
      else if (obj instanceof java.lang.Integer)
        res=(java.lang.Integer)obj;
      else if (obj instanceof java.lang.Double)
        res=((java.lang.Double)obj).intValue();
      //else if (obj instanceof java.sql.Timestamp)
        //res=((java.sql.Timestamp)obj).getTime();
      //else if (obj instanceof java.util.Date)
        //res=(java.lang.Integer)obj;
      else
        res=java.lang.Integer.parseInt(To_String(obj));
    }
    return res;
  }

  public static java.lang.Long To_Long(Object obj)
  {
    java.lang.Long res=null;
    
    if (obj!=null)
    {
      if (obj instanceof java.lang.String)
      {
        res=java.lang.Long.parseLong((String)obj);
      }
      else if (obj instanceof java.lang.Long)
        res=(java.lang.Long)obj;
      else if (obj instanceof java.lang.Integer)
        res=((java.lang.Integer)obj).longValue();
      else if (obj instanceof java.lang.Double)
        res=((java.lang.Double)obj).longValue();
      //else if (obj instanceof java.sql.Timestamp)
        //res=((java.sql.Timestamp)obj).getTime();
      //else if (obj instanceof java.util.Date)
        //res=(java.lang.Integer)obj;
      else
        res=java.lang.Long.parseLong(To_String(obj));
    }
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
  
  public static java.lang.Double ToDouble(Object obj)
  {
    java.lang.Double res=null;
    String str;
    
    if (obj!=null)
    {
      try
      {
        if (obj instanceof java.lang.String)
        {
          str=Remove_Other_Chars("1234567890.-", (String)obj);
          res=java.lang.Double.parseDouble(str);
        }
        else if (obj instanceof java.lang.Integer)
          res=((java.lang.Integer)obj).doubleValue();
        else if (obj instanceof java.lang.Double)
          res=(java.lang.Double)obj;
        //else if (obj instanceof java.sql.Timestamp)
          //res=((java.sql.Timestamp)obj).getTime();
        //else if (obj instanceof java.util.Date)
          //res=(java.lang.Integer)obj;
        else
        {
          str=Remove_Other_Chars("1234567890.", To_String(obj));
          res=java.lang.Double.parseDouble(str);
        }
      }
      catch (NumberFormatException e)
      {
        res=null;
      }
    }
    return res;
  }
  
  public static java.lang.Float To_Float(Object obj)
  {
    java.lang.Float res=null;
    String str;
    
    if (obj!=null)
    {
      try
      {
        if (obj instanceof java.lang.String)
        {
          str=Remove_Other_Chars("1234567890.", (String)obj);
          res=java.lang.Float.parseFloat(str);
        }
        else if (obj instanceof java.lang.Long)
          res=((java.lang.Long)obj).floatValue();
        else if (obj instanceof java.lang.Integer)
          res=((java.lang.Integer)obj).floatValue();
        else if (obj instanceof java.lang.Double)
          res=((java.lang.Double)obj).floatValue();
        //else if (obj instanceof java.sql.Timestamp)
          //res=((java.sql.Timestamp)obj).getTime();
        //else if (obj instanceof java.util.Date)
        //res=(java.lang.Integer)obj;
        else if (obj instanceof java.sql.Date)
          res=new Float(((java.sql.Date)obj).getTime());
        else
        {
          str=Remove_Other_Chars("1234567890.", To_String(obj));
          res=java.lang.Float.parseFloat(str);
        }
      }
      catch (NumberFormatException e)
      {
        res=null;
      }
    }
    return res;
  }
  
  public static boolean IsGenericList(java.lang.reflect.Field field, Class<? extends Object> list_class)
  {
    boolean res=false;
    java.lang.reflect.ParameterizedType gen_type;
    java.lang.reflect.Type list_type;
    
    if (field!=null)
    {
      if (field.getGenericType() instanceof java.lang.reflect.ParameterizedType)
      {
        gen_type = (java.lang.reflect.ParameterizedType)field.getGenericType();
        if (list_class!=null)
        {
          list_type=gen_type.getActualTypeArguments()[0];
          if (list_type.equals(list_class))
            res=true;
        }
        else
          res=true;
      }
    }
    return res;
  }
  
  public static java.util.ArrayList<Object> NewGenericList(Class<? extends Object> list_class)
  {
    java.util.ArrayList<Object> res=null;
    
    res=new java.util.ArrayList<Object>();
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
          obj_field_val=GetObjFieldValue(obj, field_name);
          if (match_type.equals("ends_with") && To_String(obj_field_val, "").endsWith(To_String(field_val)))
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

  // Date Functions ==================================================================================================================

	public static java.sql.Date Round_Date(java.sql.Date date, Long millis)
	{
		java.sql.Date res=date;
		long round_millis, round_date;
		
		if (millis!=null)
		{
		  round_millis=(long)(((float)date.getTime()+((float)millis/2f))/(float)millis);
		  round_date=round_millis*millis;
		  res=new java.sql.Date(round_date);
		}
		return res;
	}
	
	public static java.sql.Date Day_Start(java.sql.Date date)
	{
		java.sql.Date res=null;
		
		res=(java.sql.Date)Round(date, ROUND_DATE_DAY);
		return res;
	}
	
	public static java.sql.Date[] Month(java.sql.Date date)
	{
		java.sql.Date[] res=null;
		java.util.Calendar cal;
		int week_day, c;
		
		cal=java.util.Calendar.getInstance();
		if (date!=null)
			cal.setTime(date);
		week_day=cal.get(java.util.Calendar.DAY_OF_WEEK);
		cal.add(java.util.Calendar.DATE, 1-week_day);
		
		res=new java.sql.Date[7];
		for (c=0; c<7; c++)
		{
		  res[c]=new java.sql.Date(cal.getTimeInMillis());
		  cal.add(java.util.Calendar.DATE, 1);
		}
		
		return res;
	}

	public static java.sql.Date Week_First_Day(java.sql.Date date)
	{
		java.sql.Date res=null;
		java.util.Calendar cal;
		int week_day;

		cal=java.util.Calendar.getInstance();
		if (date!=null)
			cal.setTime(date);
		week_day=cal.get(java.util.Calendar.DAY_OF_WEEK);
		cal.add(java.util.Calendar.DATE, 1-week_day);
		res=new java.sql.Date(cal.getTimeInMillis());

		return res;
	}
	
	public static java.sql.Date[] Week(java.sql.Date date)
	{
		java.sql.Date first_day, res[]=null;
		java.util.Calendar cal;
		int c;

		cal=java.util.Calendar.getInstance();
		if (date!=null)
		{
		  first_day=Week_First_Day(date);
			cal.setTime(first_day);
		}

		res=new java.sql.Date[7];
		for (c=0; c<7; c++)
		{
		  res[c]=new java.sql.Date(cal.getTimeInMillis());
		  cal.add(java.util.Calendar.DATE, 1);
		}

		return res;
	}


	public static Long[] Week_In_Millis(java.sql.Date date)
	{
		java.sql.Date first_day;
		Long[] res=null;
		java.util.Calendar cal;
		int c;

		cal=java.util.Calendar.getInstance();
		if (date!=null)
		{
		  first_day=Week_First_Day(date);
			cal.setTime(first_day);
		}

		res=new Long[7];
		for (c=0; c<7; c++)
		{
		  res[c]=cal.getTimeInMillis();
		  cal.add(java.util.Calendar.DATE, 1);
		}

		return res;
	}
	
  public static java.sql.Date Now()
  {
    return new java.sql.Date((new java.util.Date()).getTime());
  }

  public static java.sql.Date Add_Months(java.sql.Date date, int months)
  {
    java.sql.Date res=date;
    java.util.Calendar cal;

    cal=java.util.Calendar.getInstance();
    cal.setTime(date);
    cal.add(java.util.Calendar.MONTH, months);
    res=new java.sql.Date(cal.getTimeInMillis());
    return res;
  }

  public static java.sql.Date Add_Hours(java.sql.Date date, int hours)
  {
    java.sql.Date res=date;
    java.util.Calendar cal;

    cal=java.util.Calendar.getInstance();
    cal.setTime(date);
    cal.add(java.util.Calendar.HOUR_OF_DAY, hours);
    res=new java.sql.Date(cal.getTimeInMillis());
    return res;
  }

  public static java.sql.Date Add_Days(java.sql.Date date, int days)
  {
    java.sql.Date res=date;
    java.util.Calendar cal;

    cal=java.util.Calendar.getInstance();
    cal.setTime(date);
    cal.add(java.util.Calendar.DAY_OF_MONTH, days);
    res=new java.sql.Date(cal.getTimeInMillis());
    return res;
  }

  public static java.sql.Date Today()
  {
    java.sql.Date res=null, now;

    now=Now();
		res=New_Date(Date_Get_Year(now), Date_Get_Month(now)+1, Date_Get_Day(now));
    return res;
  }
  
  public static java.sql.Date New_Date(int year, int month, int day, int hour, int min, int sec)
  {
    java.sql.Date res=null;
    java.util.Calendar date;

    date=new java.util.GregorianCalendar();
    date.clear();
    date.set(year, month-1, day, hour, min, sec);
    res=new java.sql.Date(date.getTimeInMillis());
    return res;
  }
  
  public static java.sql.Date New_Date(int year, int month, int day)
  {
    java.sql.Date res=null;
    java.util.Calendar date;
    
    date=new java.util.GregorianCalendar();
    date.clear();
    date.set(year, month-1, day);
    res=new java.sql.Date(date.getTimeInMillis());
    return res;
  }

  public static java.sql.Date New_Time(int hr, int min, int sec)
  {
    java.sql.Date res=null;
    java.util.Calendar date;

    date=new java.util.GregorianCalendar();
    date.clear();
    date.set(java.util.Calendar.SECOND, sec);
		date.set(java.util.Calendar.MINUTE, min);
		date.set(java.util.Calendar.HOUR_OF_DAY, hr);
    res=new java.sql.Date(date.getTimeInMillis());
    return res;
  }
  
  public static int Date_Get_Year(java.sql.Date date)
  {
    int res=0;
    java.util.Calendar cal;
    
    if (date!=null)
    {
      cal=java.util.Calendar.getInstance();
      cal.clear();
      cal.setTime(date);
      res=cal.get(java.util.Calendar.YEAR);
    }
    return res;
  }
  
  public static int Date_Get_Hour(java.sql.Date date)
  {
    int res=0;
    java.util.Calendar cal;

    if (date!=null)
    {
      cal=java.util.Calendar.getInstance();
      cal.clear();
      cal.setTime(date);
      res=cal.get(java.util.Calendar.HOUR_OF_DAY);
    }
    return res;
  }

  public static int Date_Get_Minute(java.sql.Date date)
  {
    int res=0;
    java.util.Calendar cal;

    if (date!=null)
    {
      cal=java.util.Calendar.getInstance();
      cal.clear();
      cal.setTime(date);
      res=cal.get(java.util.Calendar.MINUTE);
    }
    return res;
  }
  
  public static int Date_Get_Month(java.sql.Date date)
  {
    int res=0;  
    java.util.Calendar cal;
    
    if (date!=null)
    {
      cal=java.util.Calendar.getInstance();
      cal.clear();
      cal.setTime(date);
      res=cal.get(java.util.Calendar.MONTH);
    }
    return res;
  }
  
  public static int Date_Get_Day(java.sql.Date date)
  {
    int res=0;
    java.util.Calendar cal;
    
    if (date!=null)
    {
      cal=java.util.Calendar.getInstance();
      cal.clear();
      cal.setTime(date);
      res=cal.get(java.util.Calendar.DAY_OF_MONTH);
    }
    return res;
  }

	public static int Date_Get_Day_Of_Week(java.sql.Date date)
  {
    int res=0;
    java.util.Calendar cal;

    if (date!=null)
    {
      cal=java.util.Calendar.getInstance();
      cal.clear();
      cal.setTime(date);
      res=cal.get(java.util.Calendar.DAY_OF_WEEK);
    }
    return res;
  }
	
	public static java.sql.Date Date_Set_Time(java.sql.Date date, java.sql.Date time)
	{
		java.sql.Date res=null;
    java.util.Calendar cal;

    if (date!=null && time!=null)
    {
      cal=java.util.Calendar.getInstance();
      cal.clear();
      cal.setTime(date);
      cal.set(java.util.Calendar.HOUR_OF_DAY, Date_Get_Hour(time));
			cal.set(java.util.Calendar.MINUTE, Date_Get_Minute(time));
			res=new java.sql.Date(cal.getTimeInMillis());
    }
		
		return res;
	}
	
  // String Functions ==================================================================================================================
  
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

    stra=To_String(obja, def);
    
    strb=To_String(objb, def);
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
          val=GetObjFieldValue(obj, field_name);
        else
          val=obj;
        res=AppendStr(res, val, separator, null, envelope, envelope, false);
      }
    }
    return res;
  }
	
  // Misc. Functions ==================================================================================================================
  
  public static void Err_To_Log(Exception ex)
  {
    String tag="rs.android.Util.Err_To_Log()";

    if (ex!=null)
      android.util.Log.d(tag, ex.getMessage());
  }

  public static String Obj_To_String(Object obj)
  {
    String res=null;
    Object field_val;

    if (obj!=null)
    {
      for (java.lang.reflect.Field field: rs.android.Db.Get_Fields(obj.getClass()))
      {
        try {field_val=field.get(obj);}
        catch (java.lang.Exception e) {field_val=null;}
        res=rs.android.Util.AppendStr(res, field.getName()+": "+rs.android.Util.To_String(field_val, "null"), ", ", null);
      }
    }
    return res;
  }
	
  public static String Objs_To_String(Object obj)
  {
    String res=null;
    int c;
    Object[] objs;
    
    if (obj instanceof java.util.Collection<?>)
    {
      objs=((java.util.Collection<?>)obj).toArray();
      for (c=0; c<objs.length; c++)
      {
        res=Obj_To_String(objs[c]);
        //android.util.Log.d(tag, fields_str);
      }
    }
    else
    {
      res=Obj_To_String(obj);
      //android.util.Log.d(tag, fields_str);
    }
		return res;
  }
  
	public static void Show_Obj(android.content.Context ctx, Object obj)
	{
		String obj_msg;
		
		obj_msg=Objs_To_String(obj);
		Show_Message(ctx, obj_msg);
	}
	
	public static void Show_Message(android.content.Context ctx, String msg)
	{
		android.app.AlertDialog dlg;
		
		//android.widget.Toast.makeText(ctx, msg, android.widget.Toast.LENGTH_LONG).show();
		dlg = new android.app.AlertDialog.Builder(ctx).create();
		dlg.setTitle("Message");
		dlg.setMessage(msg);
		dlg.show();
	}

	public static void Show_Stack(android.content.Context ctx)
	{
		StackTraceElement[] elems;
		String msg;
		
		elems=Thread.currentThread().getStackTrace();
		if (NotEmpty(elems))
		{
			msg="";
			for (StackTraceElement elem: elems)
			{
				msg+=elem.toString()+"\n";
			}
			Show_Message(ctx, msg);
		}
	}
	
	public static void Show_Note(android.content.Context ctx, String msg)
	{
		android.widget.Toast.makeText(ctx, msg, android.widget.Toast.LENGTH_LONG).show();
	}
	
	public static void Show_Error(android.content.Context ctx, Exception e)
	{
		java.io.ByteArrayOutputStream buffer;
		java.io.PrintStream stream;

		buffer=new java.io.ByteArrayOutputStream();
		stream=new java.io.PrintStream(buffer);
		e.printStackTrace(stream);

		rs.android.Util.Show_Message(ctx, buffer.toString());
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
