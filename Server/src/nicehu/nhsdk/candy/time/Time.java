package nicehu.nhsdk.candy.time;

import java.util.Calendar;

import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.str.ParseU;

public class Time
{
	public static final long YEAR = 365 * 24 * 60 * 60 * 1000L;
	public static final long MONTH = 30 * 24 * 60 * 60 * 1000L;
	public static final long WEEK = 7 * 24 * 60 * 60 * 1000L;
	public static final long DAY = 24 * 60 * 60 * 1000L;
	public static final long HOUR = 60 * 60 * 1000L;
	public static final long MINUTE = 60 * 1000L;
	public static final long SECOND = 1000L;
	public static final long MillSECOND = 1L;

	private long time;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int millisecond;

	public Time()
	{
		Calendar calendar = Calendar.getInstance(TimeZoneU.getTimeZone());
		this.time = calendar.getTimeInMillis();
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH) + 1;
		this.day = calendar.get(Calendar.DAY_OF_MONTH);
		this.hour = calendar.get(Calendar.HOUR_OF_DAY);
		this.minute = calendar.get(Calendar.MINUTE);
		this.second = calendar.get(Calendar.SECOND);
		this.millisecond = calendar.get(Calendar.MILLISECOND);
	}

	public Time(long time)
	{
		Calendar calendar = Calendar.getInstance(TimeZoneU.getTimeZone());
		calendar.setTimeInMillis(time);
		this.time = calendar.getTimeInMillis();
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH) + 1;
		this.day = calendar.get(Calendar.DAY_OF_MONTH);
		this.hour = calendar.get(Calendar.HOUR_OF_DAY);
		this.minute = calendar.get(Calendar.MINUTE);
		this.second = calendar.get(Calendar.SECOND);
		this.millisecond = calendar.get(Calendar.MILLISECOND);
	}

	public Time(String str)
	{
		if (Empty.is(str))
		{
			return;
		}

		String[] cells = new String[7];
		String[] cells2 = str.split("\\:|\\-|\\ |\\.");
		for (int i = 0; i < cells2.length; ++i)
		{
			cells[i] = cells2[i];
		}
		if (cells2.length <= 7)
		{
			for (int i = cells2.length; i < 7; ++i)
			{
				cells[i] = "0";
			}
		}
		Calendar calendar = Calendar.getInstance(TimeZoneU.getTimeZone());
		calendar.set(Calendar.YEAR, ParseU.pInt(cells[0]));
		calendar.set(Calendar.MONTH, ParseU.pInt(cells[1]) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, ParseU.pInt(cells[2]));
		calendar.set(Calendar.HOUR_OF_DAY, ParseU.pInt(cells[3]));
		calendar.set(Calendar.MINUTE, ParseU.pInt(cells[4]));
		calendar.set(Calendar.SECOND, ParseU.pInt(cells[5]));
		calendar.set(Calendar.MILLISECOND, ParseU.pInt(cells[6]));

		this.time = calendar.getTimeInMillis();
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH) + 1;
		this.day = calendar.get(Calendar.DAY_OF_MONTH);
		this.hour = calendar.get(Calendar.HOUR_OF_DAY);
		this.minute = calendar.get(Calendar.MINUTE);
		this.second = calendar.get(Calendar.SECOND);
		this.millisecond = calendar.get(Calendar.MILLISECOND);
	}

	public String getStr()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.year + "-" + this.month + "-" + this.day + " " + this.hour + ":" + this.minute + ":" + this.second);
		return sb.toString();
	}

	public String getStr_YMD()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.year + "-" + this.month + "-" + this.day);
		return sb.toString();
	}

	public String getStr_HMS()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.hour + ":" + this.minute + ":" + this.second);
		return sb.toString();
	}

	public String getStr_YMDHMSM()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.year + "-" + this.month + "-" + this.day + " " + this.hour + ":" + this.minute + ":" + this.second + "." + this.millisecond);
		return sb.toString();
	}

	public long getTimeMillis()
	{
		return time;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public int getMonth()
	{
		return month;
	}

	public void setMonth(int month)
	{
		this.month = month;
	}

	public int getDay()
	{
		return day;
	}

	public void setDay(int day)
	{
		this.day = day;
	}

	public int getHour()
	{
		return hour;
	}

	public void setHour(int hour)
	{
		this.hour = hour;
	}

	public int getMinute()
	{
		return minute;
	}

	public void setMinute(int minute)
	{
		this.minute = minute;
	}

	public int getSecond()
	{
		return second;
	}

	public void setSecond(int second)
	{
		this.second = second;
	}

	public int getMillisecond()
	{
		return millisecond;
	}

	public void setMillisecond(int millisecond)
	{
		this.millisecond = millisecond;
	}

}
