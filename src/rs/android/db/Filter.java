package rs.android.db;

public class Filter
{
  public java.util.ArrayList<Long> sub_filter_ids;
	public java.util.ArrayList<Object> sub_filter_vals;
  
  public static Filter Add_Sub_Filter(Long id)
  {
    return null;
  }
  
  public static Filter Add_Param_Val(Object val)
  {
    return null;
  }
  
  public String Build_SQL()
  {
    return null;
  }
  
  public Object[] Build_Params()
  {
    return null;
  }
	
	public static boolean Has_Params(Filter filter)
	{
		return false;
	}
}
