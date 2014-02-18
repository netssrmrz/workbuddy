package rs.android.util;

public class Date
{
	public static final int ROUND_DATE_DAY=1;
	public static final long MILLIS_PER_DAY=1000*60*60*24;
	
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

		res=(java.sql.Date)rs.android.Util.Round(date, ROUND_DATE_DAY);
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
		{
			date=Day_Start(date);
			cal.setTime(date);
		}
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

	
}
