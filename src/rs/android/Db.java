package rs.android;
import java.lang.reflect.*;

public class Db
{
  public android.database.sqlite.SQLiteDatabase conn;
  public android.content.Context context;
  public String db_name;
  public int db_version;
  public String[][] tables;
  public boolean log;
	public String log_text;
  public java.util.HashMap<String, Object> cache;

  public static final String log_tag="rs.android.Db";

  // Db Admin ==============================================================================================

  public Db()
  {    
    this.log=false;
    this.cache=new java.util.HashMap<String, Object>();
  }

  public Db(android.app.Activity activity, String db_name)
  {
    String db_file_name;

    this.context=activity;
    this.db_name=db_name;

    if (DbExists() || CreateFromTemplate())
    {
      db_file_name=GetDbPath();
      this.conn=android.database.sqlite.SQLiteDatabase.openDatabase(db_file_name, null, android.database.sqlite.SQLiteDatabase.OPEN_READWRITE);
    }
  }

  public boolean CreateFromTemplate()
  {
    java.io.FileOutputStream db_stream;
    java.io.InputStream db_template_file;
    boolean res=false;

    try {db_template_file=context.getAssets().open(db_name);}
    catch (java.io.IOException e) {e.printStackTrace(); db_template_file=null;}
    if (Util.NotEmpty(db_template_file))
    {
      try {db_stream=new java.io.FileOutputStream(GetDbPath());}
      //try {db_stream=this.activity.(GetDbPath());}
      catch (java.io.FileNotFoundException e) 
      {
        e.printStackTrace(); 
        db_stream=null;
      }
      if (db_stream!=null)
      {
        while (Util.NotEmpty(db_template_file)) 
        {
          try {db_stream.write(db_template_file.read());}
          catch (java.io.IOException e) {e.printStackTrace(); break;}
        }
        try {db_stream.close();}
        catch (java.io.IOException e) {e.printStackTrace();}
        res=true;
      }
      try {db_template_file.close();}
      catch (java.io.IOException e) {e.printStackTrace();}
    }
    return res;
  }

  public boolean DbExists()
  {
    boolean res=false;
    String db_file_name;
    java.io.File db_file;

    db_file_name=GetDbPath();
    if (Util.NotEmpty(db_file_name))
    {
      db_file=new java.io.File(db_file_name);
      if (db_file.exists())
        res=true;
    }
    return res;
  }

  public String GetDbPath()
  {
    String res=null;

    if (Util.NotEmpty(db_name))
    {
      //res=activity.getDatabasePath(db_name).getPath();
      res=context.getFilesDir().getPath()+"/"+db_name;
    }
    return res;
  }

  /*public void Open() 
	 {
	 String db_file_name;

	 db_file_name=GetDbPath();
	 if (DbExists() && Util.NotEmpty(db_file_name))
	 {
	 this.conn=android.database.sqlite.SQLiteDatabase.openDatabase(db_file_name, null, android.database.sqlite.SQLiteDatabase.OPEN_READWRITE);
	 }
	 }*/

  public void Close()
  {
    if (this.conn!=null)
      this.conn.close();
  }
	
	public void Log(String msg)
	{
		if (this.log)
		{
			if (this.log_text==null)
				this.log_text="";
			this.log_text+=rs.android.Util.Now().toString()+": "+msg+"\r\n";
		}
	}

  // Query Support ========================================================================================

  public String DeriveTableName(Class<? extends Object> obj_class)
  {
    String res=null;

    if (obj_class!=null)
      res=obj_class.getSimpleName().toLowerCase();
    return res;
  }

  void Put_Content_value(android.content.ContentValues values, String field_name, Object field_value)
  {
    if (values!=null)
    {
      if (field_value instanceof Boolean) values.put(field_name, (Boolean)field_value);
      else if (field_value instanceof Byte) values.put(field_name, (Byte)field_value);
      else if (field_value instanceof byte[]) values.put(field_name, (byte[])field_value);
      else if (field_value instanceof Double) values.put(field_name, (Double)field_value);
      else if (field_value instanceof Float) values.put(field_name, (Float)field_value);
      else if (field_value instanceof Integer) values.put(field_name, (Integer)field_value);
      else if (field_value instanceof Long) values.put(field_name, (Long)field_value);
      else if (field_value instanceof Short) values.put(field_name, (Short)field_value);
      else if (field_value instanceof String) values.put(field_name, (String)field_value);
      else if (field_value instanceof java.util.Date) values.put(field_name, ((java.util.Date)field_value).getTime());
      else values.put(field_name, rs.android.Util.To_String(field_value));
    }
  }

  public android.content.ContentValues BuildSaveParams(Object obj)
  {
    String field_name;
    android.content.ContentValues values=null;
    Object field_value;

    if (obj!=null)
    {
      values=new android.content.ContentValues();
      for (java.lang.reflect.Field field : obj.getClass().getFields())
      {
        if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))
        {
          field_name=field.getName();
          field_value=rs.android.Util.GetObjFieldValue(obj, field_name);
          if (field_value!=null)
          {
            Put_Content_value(values, field_name, field_value);
          }
        }
      }
    }
    return values;
  }

  public Object NewObjFromCursor(Class<? extends Object> obj_class, android.database.Cursor query_res)
  {
    Object res=null, field_val;
    int c;
    String field_name, cache_key, class_name;
    java.lang.reflect.Field field;
    Class<? extends Object> field_class;

    if (Util.NotEmpty(query_res) && obj_class!=null)
    {
      try {res=obj_class.newInstance();}
      catch (java.lang.Exception e) {res=null;}
      if (res!=null)
      {
        class_name=obj_class.getName();
        for (c=0; c<query_res.getColumnCount(); c++)
        {
          field_name=query_res.getColumnName(c).toLowerCase();

          cache_key="NewObjFromCursor."+class_name+"."+field_name;
          if (this.cache.containsKey(cache_key))
            field=(java.lang.reflect.Field)this.cache.get(cache_key);
          else
          {
            field=Util.FindClassField(obj_class, field_name);
            this.cache.put(cache_key, field);
          }

          if (field!=null)
          {
            field_class=field.getType();
            field_val=Get_Value_As(field_class, query_res, c);

            try {field.set(res, field_val);} 
            catch (java.lang.Exception e) {e.printStackTrace();}
          }
        }
      }
    }
    return res;
  }

  public static Object Get_Value_As(Class<? extends Object> obj_class, android.database.Cursor query_res, int col)
  {
    Object res=null;
    int int_res;

    if (obj_class!=null && Util.NotEmpty(query_res) && col>-1)
    {
      if (obj_class==byte[].class) 
        res=query_res.getBlob(col);
      else if (obj_class==Boolean.class) 
      {
        int_res=query_res.getInt(col);
        if (int_res==0)
          res=false;
        else
          res=true;
      }
      else if (obj_class==Double.class) 
        res=query_res.getDouble(col);
      else if (obj_class==Float.class) 
        res=query_res.getFloat(col);
      else if (obj_class==Integer.class) 
        res=query_res.getInt(col);
      else if (obj_class==Long.class) 
        res=query_res.getLong(col);
      else if (obj_class==Short.class) 
        res=query_res.getShort(col);
      else if (obj_class==String.class) 
        res=query_res.getString(col);
      else if (obj_class==java.util.Date.class) 
        res=new java.util.Date(query_res.getLong(col));
      else if (obj_class==java.sql.Date.class) 
        res=new java.sql.Date(query_res.getLong(col));
      else 
        res=null;
    }
    return res;
  }

  public static String ToParamStr(Object obj)
  {
    String res=null;
    Class<? extends Object> obj_class;

    if (obj!=null)
    {
      obj_class=obj.getClass();
      if (obj_class!=null)
      {
        //if (obj_class==byte[].class) res=query_res.getBlob(col);
        if (obj_class==Double.class) 
          res=Util.To_String(obj);
        else if (obj_class==Float.class) 
          res=Util.To_String(obj);
        else if (obj_class==Integer.class) 
          res=Util.To_String(obj);
        else if (obj_class==Long.class) 
          res=Util.To_String(obj);
        else if (obj_class==Short.class) 
          res=Util.To_String(obj);
        else if (obj_class==String.class) 
          res=Util.To_String(obj);
        else if (obj_class==java.util.Date.class) 
          res=Util.To_String(((java.util.Date)obj).getTime());
        else if (obj_class==java.sql.Date.class) 
          res=Util.To_String(((java.sql.Date)obj).getTime());
        else 
          res=null;
      }
    }
    return res;
  }

  public String[] BuildSQLParams(Object ... params)
  {
    String[] sqlite_params=null;
    int c;

    if (Util.NotEmpty(params))
    {
      sqlite_params=new String[params.length];
      for (c=0; c<params.length; c++)
        sqlite_params[c]=ToParamStr(params[c]);
    }
    return sqlite_params;
  }

  public android.database.Cursor Execute_SQL(String sql, Object ... params)
  {
    android.database.Cursor query_res=null;
    String[] sqlite_params=null;

    if (Util.NotEmpty(sql))
    {
      if (log)
        android.util.Log.d("Execute_SQL", SQL_To_Str(sql, params));
      sqlite_params=BuildSQLParams(params);
      query_res=this.conn.rawQuery(sql, sqlite_params);
    }
    return query_res;
  }

  public void Execute_SQL_No_Result(String sql, Object ... params)
  {
    String[] sqlite_params=null;

    if (Util.NotEmpty(sql))
    {
      if (log)
        android.util.Log.d("Execute_SQL_No_Result", SQL_To_Str(sql, params));
      sqlite_params=BuildSQLParams(params);
      if (Util.NotEmpty(sqlite_params))
        this.conn.execSQL(sql, sqlite_params);
      else
        this.conn.execSQL(sql);
    }
  }

  public String SQL_To_Str(String sql, Object ... params)
  {
    String res=null;
    int c, pos;

    if (Util.NotEmpty(sql))
    {
      res=sql;
      if (Util.NotEmpty(params))
      {
        for (c=0; c<params.length; c++)
        {
          pos=res.indexOf('?');
          if (pos!=-1)
          {
            res=
              res.substring(0, pos)+ToParamStr(params[c])+
              res.substring(pos+1);
          }
        }
      }
      res=Util.AppendStr(res, Get_Stack_Trace(), "\r\n");
    }
    return res;
  }

  public String Get_Stack_Trace()
  {
    StackTraceElement[] stack;
    String res=null;

    stack=Thread.currentThread().getStackTrace();
    for (StackTraceElement elem: stack)
    {
      if (elem.getClassName().startsWith("rs.") &&
          !elem.getMethodName().equals("Get_Stack_Trace") && 
          !elem.getMethodName().equals("SQL_To_Str"))
        res=Util.AppendStr(res, elem.toString(), "\r\n");
    }
    return res;
  }

  public int Delete(String from, String where, Object ... params)
  {
    String[] sqlite_params=null;
		int res=0;

		this.Log("rs.android.Db.Delete(from, where, params)");
    if (Util.NotEmpty(from))
    {
			this.Log("...has from: "+from);
      sqlite_params=BuildSQLParams(params);
      res=this.conn.delete(from, where, sqlite_params);
    }
		return res;
  }

  // Select ===============================================================================================

  public Object[] Select_Column(Class<? extends Object> val_class, String sql, Object ... params)
  {
    return Select_Column(null, val_class, sql, params);
  }

  public Object[] Select_Column(Integer top, Class<? extends Object> val_class, String sql, Object ... params)
  {
    Object[] res=null;
    Object val;
    android.database.Cursor query_res;
    java.util.ArrayList<Object> col;
    int c=0;

    if (top==null || top>0)
    {
      query_res=Execute_SQL(sql, params);
      if (Util.NotEmpty(query_res))
      {
        col=new java.util.ArrayList<Object>();
        while (query_res.moveToNext())
        {
          val=Get_Value_As(val_class, query_res, 0);
          col.add(val);
          if (top!=null)
          {
            c++;
            if (c==top)
              break;
          }
        }
        res=col.toArray();
      }
      if (query_res!=null)
        query_res.close();
    }
    return res;
  }

  public Object Select_Value(String sql, Class<? extends Object> val_class, Object ... params)
  {
    Object res=null;
    android.database.Cursor query_res;

		this.Log("rs.android.Db.Select_Value()");
    query_res=Execute_SQL(sql, params);
    if (Util.NotEmpty(query_res))
    {
      query_res.moveToNext();
      res=Get_Value_As(val_class, query_res, 0);
      if (this.log)
        android.util.Log.d("Select_Value", "result: "+rs.android.Util.To_String(res));
    }
    if (query_res!=null)
      query_res.close();
    return res;
  }

  public Object[] Select_Values(String sql, Object ... params)
  {
    Object[] res=null;
    android.database.Cursor query_res;
    int c;

    query_res=Execute_SQL(sql, params);
    if (Util.NotEmpty(query_res))
    {
      query_res.moveToNext();
      res=new Object[query_res.getColumnCount()];
      for (c=0; c<res.length; c++)
      {
        res[c]=Get_Value_As(String.class, query_res, c);
      }
      if (this.log)
        android.util.Log.d("Select_Value", "result: "+rs.android.Util.Build_Str_List(res, null, ", ", null));
    }
    if (query_res!=null)
      query_res.close();
    return res;
  }

  public Object SelectObj(Class<? extends Object> obj_class, long id)
  {
    String sql, table_name;
    Object res=null;

    table_name=DeriveTableName(obj_class);
    sql="select * from "+table_name+" where id=?";
    res=SelectObj(obj_class, sql, id);
    return res;
  }

  public Object SelectObj(Class<? extends Object> obj_class, String sql, Object ... params)
  {
    Object res=null;
    android.database.Cursor query_res;

    query_res=Execute_SQL(sql, params);
    if (Util.NotEmpty(query_res))
    {
      query_res.moveToNext();
      res=NewObjFromCursor(obj_class, query_res);
    }
    if (query_res!=null)
      query_res.close();
    return res;
  }

  public java.util.ArrayList<?> SelectObjs(Class<? extends Object> obj_class, String sql, Object ... params)
  {
    return SelectObjs((Integer)null, obj_class, sql, params);
  }

  public java.util.ArrayList<?> SelectObjs(Integer top, Class<? extends Object> obj_class, String sql, Object ... params)
  {
    java.util.ArrayList<Object> res=null;
    android.database.Cursor query_res;
    Object obj;
    int c=0;

    query_res=Execute_SQL(sql, params);
    if (Util.NotEmpty(query_res))
    {
      res=Util.NewGenericList(obj_class);
      while (query_res.moveToNext())
      {
        obj=NewObjFromCursor(obj_class, query_res);
        res.add(obj);
        if (top!=null)
        {
          c++;
          if (c==top)
            break;
        }
      }
    }
    if (query_res!=null)
      query_res.close();

    return res;
  }

  // Write ===============================================================================================

  public int Insert(String table_name, Object ... params)
  {
    int res=0, c;
    android.content.ContentValues values;
    long insert_res;

    if (rs.android.Util.NotEmpty(table_name) && rs.android.Util.NotEmpty(params))
    {
      values=new android.content.ContentValues();
      for (c=0; c<params.length; c+=2)
      {
        Put_Content_value(values, (String)params[c], params[c+1]);
      }
      insert_res=this.conn.insert(table_name, null, values);
      if (insert_res!=-1)
        res=(Integer)this.Select_Value("select last_insert_rowid()", Integer.class);
    }
    return res;
  }

  public boolean Save(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    Object id;
		boolean res=false;

		this.Log("rs.android.Db.Save(Object)");
    if (obj!=null)
    {
			this.Log("...has object");
      if (obj instanceof java.util.List<?>)
      {
        for (Object list_obj: (java.util.List<?>)obj)
        {
          Save(list_obj);
        }
      }
      else
      {
				this.Log("...is not list");
        id=rs.android.Util.GetObjFieldValue(obj, "id");
        if (id==null)
				{
          if (this.Insert(obj)>0)
						res=true;
				}
        else
				{
          if (this.Update(obj))
						res=true;
				}
      }
    }
		return res;
  }

  public long Insert(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    String table_name;
    android.content.ContentValues values;
    long id, res=0;
		Boolean table_avail;
		boolean set_res=false;

		this.Log("rs.android.Db.Insert(Object)");
    if (obj!=null && rs.android.Util.NotEmpty(this.conn))
    {
			this.Log("...has object and database.");
      values=BuildSaveParams(obj);
			table_avail=TableAvail(obj);
			this.Log("...param values: "+values.toString());
			this.Log("...table_avail: "+table_avail.toString());
      if (rs.android.Util.NotEmpty(values) && table_avail)
      {
				this.Log("...has parameters and table.");
        table_name=DeriveTableName(obj.getClass());
        id=this.conn.insert(table_name, null, values);
        //id=(Integer)this.Select_Value("select last_insert_rowid()", Integer.class);
				if (id!=-1)
				{
          set_res=Util.SetObjFieldValue(obj, "id", id);
					if (!set_res)
						this.Log("...error setting object id.");
					
					res=id;
					this.Log("...insert successful (row id: "+id+")");
				}
				else
				{
				  Util.SetObjFieldValue(obj, "id", null);
					this.Log("...insert failed.");
				}
      }
    }
		return res;
  }

  public boolean Update(Object obj)
  {
    String table_name;
    android.content.ContentValues values;
    Long id;
    String[] id_param;
		boolean res=false;
		int update_res;

    this.Log("rs.android.Db.Update(Object)");
    if (obj!=null && rs.android.Util.NotEmpty(this.conn))
    {
      table_name=DeriveTableName(obj.getClass());
      values=BuildSaveParams(obj);
      if (rs.android.Util.NotEmpty(values))
      {
        id=(Long)Util.GetObjFieldValue(obj, "id");
        id_param=new String[1];
        id_param[0]=Util.To_String(id);
        update_res=this.conn.update(table_name, values, "id=?", id_param);
				if (update_res>0)
					res=true;
      }
    }
		return res;
  }

  public int Delete(Object obj)
  {
    int res=0;
    String table_name;
    Long id;

		this.Log("rs.android.Db.Delete(Object)");
    if (obj!=null && rs.android.Util.NotEmpty(this.conn))
    {
			this.Log("...object and connection available");
      table_name=DeriveTableName(obj.getClass());
      id=(Long)Util.GetObjFieldValue(obj, "id");
      if (rs.android.Util.NotEmpty(table_name) && id!=null)
      {
				this.Log("...table name and id field available");
        res=this.Delete(table_name, "id=?", id);
      }
			else
			{
				if (id==null)
					this.Log("...no object id.");
				if (!rs.android.Util.NotEmpty(table_name))
					this.Log("...no table name.");
			}
    }
    return res;
  }

  // Misc ===============================================================================================

  public boolean TableAvail(Object obj)
  {
    boolean res=false;
    String table_name, sql, res_table_name;

		this.Log("rs.android.Db.TableAvail()");
    table_name=DeriveTableName(obj.getClass());
		this.Log("...table_name: "+table_name);
    sql="SELECT name FROM sqlite_master WHERE type='table' and name=?";
    res_table_name=(String)this.Select_Value(sql, String.class, table_name);
    if (Util.NotEmpty(res_table_name) && res_table_name.equals(table_name))
    {
			this.Log("...has equal table name.");
      res=true;
    }
    else if (obj instanceof Table_Admin)
    {
			this.Log("...has table creation function.");
      ((Table_Admin)obj).Create_Table(this);
      res=true;
    }
    return res;
  }

  public static void DumpCursorToLog(android.database.Cursor cursor, Object ... field_details)
  {
    int c, d;
    String fields_str=null, field_val, field_name, field_id;
    Class<?> field_class;
    long field_val_long;
    java.sql.Date field_val_date;

    if (rs.android.Util.NotEmpty(cursor))
    {
      for (c=0; c<cursor.getColumnCount(); c++)
      {
        field_val=cursor.getString(c);
        field_name=cursor.getColumnName(c);
        for (d=0; d<field_details.length; d+=2)
        {
          field_id=field_details[d].toString();
          field_class=(Class<?>)field_details[d+1];
          if (field_id.equals(field_name))
          {
            if (field_class==java.sql.Date.class) 
            {
              field_val_long=rs.android.Util.To_Long(field_val);
              field_val_date=new java.sql.Date(field_val_long);
              field_val=Util.To_String(field_val_date);
            }
          }
        }
        fields_str=rs.android.Util.AppendStr(fields_str, field_name+": "+field_val, ", ", null);
      }
      android.util.Log.d("DumpCursorToLog", fields_str);
    }
  }

  public static String Dump_Cursor_To_CSV(android.database.Cursor cursor, Object ... field_details)
  {
    int c, d;
    String fields_str=null, field_val, field_name, field_id, fields_title=null, res=null;
    Class<?> field_class;
    long field_val_long;
    java.sql.Date field_val_date;
    java.lang.StringBuilder t_res=null;

    if (rs.android.Util.NotEmpty(cursor))
    {
      t_res=new java.lang.StringBuilder(1024);
      for (c=0; c<cursor.getColumnCount(); c++)
      {
        field_name=cursor.getColumnName(c);
        fields_title=rs.android.Util.AppendStr(fields_title, field_name, ", ", null);
      }
      t_res.append(fields_title+"\r\n");

      while (cursor.moveToNext())
      {
        fields_str=null;
        for (c=0; c<cursor.getColumnCount(); c++)
        {
          field_val=cursor.getString(c);
          field_name=cursor.getColumnName(c);
          for (d=0; d<field_details.length; d+=2)
          {
            field_id=field_details[d].toString();
            field_class=(Class<?>)field_details[d+1];
            if (field_id.equals(field_name) && field_val!=null)
            {
              if (field_class==java.sql.Date.class) 
              {
                field_val_long=rs.android.Util.To_Long(field_val);
                field_val_date=new java.sql.Date(field_val_long);
                field_val=Util.To_String(field_val_date);
              }
            }
          }
          if (rs.android.Util.NotEmpty(field_val))
            field_val=field_val.replace('"', ' ');
          fields_str=rs.android.Util.AppendStr(fields_str, field_val, ", ", null);
        }
        t_res.append(fields_str+"\r\n");
      }
      res=t_res.toString();
		}
		return res;
  }
	
  public String Dump_Table_To_CSV(String table_name, Object ... field_details)
  {
    android.database.Cursor cursor;
    int c, d;
    String fields_str=null, field_val, field_name, field_id, fields_title=null, res=null;
    Class<?> field_class;
    long field_val_long;
    java.sql.Date field_val_date;
    java.lang.StringBuilder t_res=null;

    cursor=Execute_SQL("select * from "+table_name);
    if (rs.android.Util.NotEmpty(cursor))
    {
      t_res=new java.lang.StringBuilder(1024);
      for (c=0; c<cursor.getColumnCount(); c++)
      {
        field_name=cursor.getColumnName(c);
        fields_title=rs.android.Util.AppendStr(fields_title, field_name, ", ", null);
      }
      t_res.append(fields_title+"\r\n");

      while (cursor.moveToNext())
      {
        fields_str=null;
        for (c=0; c<cursor.getColumnCount(); c++)
        {
          field_val=cursor.getString(c);
          field_name=cursor.getColumnName(c);
          for (d=0; d<field_details.length; d+=2)
          {
            field_id=field_details[d].toString();
            field_class=(Class<?>)field_details[d+1];
            if (field_id.equals(field_name))
            {
              if (field_class==java.sql.Date.class) 
              {
                field_val_long=rs.android.Util.To_Long(field_val);
                field_val_date=new java.sql.Date(field_val_long);
                field_val=Util.To_String(field_val_date);
              }
            }
          }
          if (rs.android.Util.NotEmpty(field_val))
            field_val=field_val.replace('"', ' ');
          fields_str=rs.android.Util.AppendStr(fields_str, field_val, ", ", null);
        }
        t_res.append(fields_str+"\r\n");
      }
      res=t_res.toString();
    }
    cursor.close();
    return res;
  }

  public void DumpTableToLog(String table_name, Object ... field_details)
  {
    android.database.Cursor query_res;

    query_res=Execute_SQL("select * from "+table_name);
    while (query_res.moveToNext())
    {
      DumpCursorToLog(query_res, field_details);
    }
    query_res.close();
  }

  public void DumpObjsToLog(Class<? extends Object> obj_class)
  {
    String table_name;
    java.util.ArrayList<?> objs;

    table_name=this.DeriveTableName(obj_class);
    if (rs.android.Util.NotEmpty(table_name)) 
    {
      objs=this.SelectObjs(obj_class, "select * from "+table_name);
      rs.android.Util.Dump_To_Log(objs);
    }
  }
	
	public String Dump_Cursor_To_Dialog(android.content.Context ctx, String sql, Object ... field_details)
	{
		String res=null;
		android.database.Cursor sql_res;
		android.app.AlertDialog alertDialog;

		sql_res=this.Execute_SQL(sql);
    if (rs.android.Util.NotEmpty(sql_res))
		  res=rs.android.Db.Dump_Cursor_To_CSV(sql_res, field_details);
    else
      res="no data.";
		sql_res.close();

		alertDialog = new android.app.AlertDialog.Builder(ctx).create();
		alertDialog.setTitle("SQL Result");
		alertDialog.setMessage(res);
		alertDialog.show();

		return res;
	}
	
  public static String AppendFilter(String where, String filter)
  {
    String res=null;

    res=rs.android.Util.AppendStr(where, filter, " and ");
    return res;
  }

  public static String BuildClassFromCursor(android.database.Cursor query)
  {
    String res=null, field_name;
    int c;

    res="public class C\n{\n";
    for (c=0; c<query.getColumnCount(); c++) 
    {
      field_name=query.getColumnName(c);
      res+="  String "+field_name+";\n";
    }
    res+="}";
    return res;
  }

  public String Build_SQL_Str(String select, String from, String where)
  {
    return this.Build_SQL_Str(select, from, where, null);
  }

  public String Build_SQL_Str(String select, String from, String where, String order_by)
  {
    String res=null;

    if (rs.android.Util.NotEmpty(select) && rs.android.Util.NotEmpty(from))
    {
      res="select "+select+" from "+from;
      res=rs.android.Util.AppendStr(res, where, " where ");
      res=rs.android.Util.AppendStr(res, order_by, " order by ");
    }
    return res;
  }

  public boolean Rows_Exist(String from, String where)
  {
    boolean res=false;
    String sql;
    Integer count;

    sql=this.Build_SQL_Str("count(*)", from, where);
    count=(Integer)this.Select_Value(sql, Integer.class);
    if (rs.android.Util.NotEmpty(count) && count>0)
      res=true;
    return res;
  }

  public class OpenHelper 
  extends android.database.sqlite.SQLiteOpenHelper
  {
    public OpenHelper()
    {
      super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db)
    {
      for (String[] table: tables)
      {
        if (rs.android.Util.NotEmpty(table[1]))
          db.execSQL(table[1]);
      }
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion)
    {
      for (String[] table: tables)
      {
        // if table exists
				// read all data
				db.execSQL("DROP TABLE IF EXISTS "+table[0]);
				// create new table
				// copy old data to new table
        // else
				// create new table
      }
      onCreate(db);
    }    
  }
}
