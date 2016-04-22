package nicehu.nhsdk.candy.time;

import org.slf4j.Logger;

import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.str.StrU;

/**
 * 注意!!使用本工具类之前一定要在TimezoneUtil中指定时区,否则就使用默认+8时区. simplaeDateformat使用系统时区,不靠谱,建议关键时间全部使用calendar
 * 
 * @author windw
 *
 */
public class TimeU
{
	private static final Logger logger = LogU.getLogger(TimeU.class);

	public static String getStr()
	{
		return new Time().getStr();
	}

	public static String getStr_YMD()
	{
		return new Time().getStr_YMD();
	}

	public static String getStr_HMS()
	{
		return new Time().getStr_HMS();
	}

	public static String getStr_YMDHMSM()
	{
		return new Time().getStr_YMDHMSM();
	}

	public static String getStr(long time)
	{
		return new Time(time).getStr();
	}

	public static String getStr_YMD(long time)
	{
		return new Time(time).getStr_YMD();
	}

	public static String getStr_HMS(long time)
	{
		return new Time(time).getStr_HMS();
	}

	public static String getStr_YMDHMSM(long time)
	{
		return new Time(time).getStr_YMDHMSM();
	}

	public static String fixSize(String str)
	{
		StringBuilder sb = new StringBuilder();
		String[] cells = str.split("\\:|\\-|\\ |\\.");
		if (cells.length >= 3)
		{
			sb.append(StrU.fixSize(cells[0], 2, "0", -1) + "-" + StrU.fixSize(cells[1], 2, "0", -1) + "-" + StrU.fixSize(cells[2], 2, "0", -1));
		}
		if (cells.length >= 6)
		{
			sb.append(" ");
			sb.append(StrU.fixSize(cells[3], 2, "0", -1) + ":" + StrU.fixSize(cells[4], 2, "0", -1) + ":" + StrU.fixSize(cells[5], 2, "0", -1));
		}
		if (cells.length >= 7)
		{
			sb.append(".");
			sb.append(cells[6]);
		}
		return sb.toString();
	}

	public static long getLong(String str, long defaultValue)
	{
		if (Empty.is(str))
		{
			return defaultValue;
		}
		return TimeU.getLong(str);
	}

	public static long getLong(String str)
	{
		return new Time(str).getTimeMillis();
	}

	public static boolean isSameDay(long time1, long time2)
	{
		Time timeA = new Time(time1);
		Time timeB = new Time(time2);
		return (timeA.getYear() == timeB.getYear() && timeA.getMonth() == timeB.getMonth() && timeA.getDay() == timeB.getDay());
	}

	public static String getDayBeginStr(long value)
	{
		Time time = new Time(value);
		time.setHour(0);
		time.setMinute(0);
		time.setSecond(0);
		return time.getStr();
	}

	public static String getDayEndStr(long value)
	{
		Time time = new Time(value);
		time.setHour(23);
		time.setMinute(59);
		time.setSecond(59);
		return time.getStr();
	}

}
