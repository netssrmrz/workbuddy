package rs.android.util;

public class Type
{
	public static Class<?> Class_For_Name(String name)
	{
		Class<?> res=null;

		try {res=Class.forName(name);}
		catch (java.lang.ClassNotFoundException e) {res=null;}

		return res;
	}
	
	public static Object GetObjFieldValue(Object obj, String field_name)
  {
    Object res=null;
    java.lang.reflect.Method method;
    java.lang.reflect.Field field;
    Class<? extends Object> class_type;

    if (obj!=null && rs.android.Util.NotEmpty(field_name))
    {
      class_type=obj.getClass();

      field=FindClassField(class_type, field_name);
      if (rs.android.Util.NotEmpty(field))
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
        if (rs.android.Util.NotEmpty(method))
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

    if (obj!=null && rs.android.Util.NotEmpty(field_name))
    {
      class_type=obj.getClass();

      field=FindClassField(class_type, field_name);
      if (rs.android.Util.NotEmpty(field))
      {
				try {field.set(obj, val); res=true;}
				catch (Exception e) { res=false; }
      }
      else
      {
        method=FindClassMethod(class_type, "set"+field_name);
        if (rs.android.Util.NotEmpty(method))
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
	
	public static java.lang.reflect.Method FindClassMethod(Class<? extends Object> obj_class, String name)
  {
    java.lang.reflect.Method res=null;
    java.lang.reflect.Method[] methods=null;
    int c;

    if (rs.android.Util.NotEmpty(obj_class) && rs.android.Util.NotEmpty(name))
    {
      name=name.toLowerCase();
      methods=obj_class.getMethods();
      if (rs.android.Util.NotEmpty(methods))
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

    if (rs.android.Util.NotEmpty(obj_class) && rs.android.Util.NotEmpty(name))
    {
      name=name.toLowerCase();
      fields=obj_class.getFields();
      if (rs.android.Util.NotEmpty(fields))
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
	
	public static java.lang.String To_String(Object obj)
  {
    return To_String(obj, "", null);
  }

  public static java.lang.String To_String(Object obj, String def)
  {
    return To_String(obj, def, null);
  }

  public static java.lang.String To_String(Object obj, java.lang.String def, String format)
  {
    String res=null, fields_str, true_str, false_str;
    java.text.SimpleDateFormat date_format;
    java.text.DecimalFormat num_format;
    Object field_val;
    String[] format_vals;

    res=def;
    if (obj!=null)
    {
      if (obj instanceof String && rs.android.Util.NotEmpty(obj))
        res=obj.toString().trim();
      else if (obj instanceof java.sql.Timestamp)
      {
        if (!rs.android.Util.NotEmpty(format))
          format="dd/MM/yyyy HH:mm:ss";
        date_format=new java.text.SimpleDateFormat(format);
        res=date_format.format(obj);
      }
      else if (obj instanceof java.sql.Date)
      {
        if (!rs.android.Util.NotEmpty(format))
          format="dd/MM/yyyy HH:mm:ss";
        date_format=new java.text.SimpleDateFormat(format);
        res=date_format.format(obj);
      }
      else if (obj instanceof java.lang.Double)
      {
        if (!rs.android.Util.NotEmpty(format))
          format="#,##0.##";
        num_format=new java.text.DecimalFormat(format);
        res=num_format.format(obj);
      }
      else if (obj instanceof Float)
      {
        if (!rs.android.Util.NotEmpty(format))
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
					res=rs.android.Util.AppendStr(res, rs.android.util.Type.To_String(list_obj), ", ");
        }
      }
      else if (obj instanceof java.lang.Boolean)
      {
        true_str="true";
        false_str="false";
        if (rs.android.Util.NotEmpty(format))
        {
          format_vals=format.split(",");
          if (format_vals.length>0 && rs.android.Util.NotEmpty(format_vals[0]))
            true_str=rs.android.Util.Trim(format_vals[0]);
          if (format_vals.length>1 && rs.android.Util.NotEmpty(format_vals[1]))
            false_str=rs.android.Util.Trim(format_vals[1]);
        }

        if (((java.lang.Boolean)obj).booleanValue())
          res=true_str;
        else
          res=false_str;
      }
      else
        res=obj.toString();
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
          str=rs.android.Util.Remove_Other_Chars("1234567890.", (String)obj);
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
          str=rs.android.Util.Remove_Other_Chars("1234567890.", To_String(obj));
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
          str=rs.android.Util.Remove_Other_Chars("1234567890.-", (String)obj);
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
          str=rs.android.Util.Remove_Other_Chars("1234567890.", To_String(obj));
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
        res=rs.android.Util.AppendStr(res, field.getName()+": "+
				  rs.android.util.Type.To_String(field_val, "null"), ", ", null);
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
      }
    }
    else
    {
      res=Obj_To_String(obj);
    }
		return res;
  }
}
