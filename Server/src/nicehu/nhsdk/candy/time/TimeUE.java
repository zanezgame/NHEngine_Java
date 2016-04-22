package nicehu.nhsdk.candy.time;

public class TimeUE
{
	public static boolean inDayTime(int start, int end)
	{
		return inDayTime(System.currentTimeMillis(), start, end);
	}

	public static boolean inDayTime(long timeL, int start, int end)
	{
		Time time = new Time(timeL);
		if (time.getHour() >= start && time.getHour() <= end)
		{
			return true;
		}
		return false;
	}

	public static boolean exceed(long createTime, long expireTime)
	{
		return (System.currentTimeMillis() - createTime) > expireTime;
	}

	public static long getNextRefreshTime(long time, String refreshTime)
	{
		long todayRefreshTime = TimeU.getLong(TimeU.getStr_YMD() + " " + refreshTime);
		// relative time
		long dtime = todayRefreshTime - time;
		dtime = dtime % Time.DAY;
		if (dtime <= 0)
		{
			dtime += Time.DAY;
		}
		return time + dtime;
	}

	public static boolean isNeedRefresh(String refreshTime, long lastRefreshTime)
	{
		long last_nextRefreshTime = getNextRefreshTime(lastRefreshTime, refreshTime);
		long now_nextRefreshTime = getNextRefreshTime(System.currentTimeMillis(), refreshTime);
		if (last_nextRefreshTime > now_nextRefreshTime)
		{
			return false;
		}
		return last_nextRefreshTime != now_nextRefreshTime;
	}

	public static boolean isNeedRefreshWeek(Long initRefreshTime, long lastRefreshTime)
	{
		long nowWeeks = (System.currentTimeMillis() - initRefreshTime) / Time.WEEK + 1;
		long lastTimeWeeks = (lastRefreshTime - initRefreshTime) / Time.WEEK + 1;
		return nowWeeks != lastTimeWeeks;
	}

	public static boolean isNeedRefreshInvertal(Long initRefreshTime, long lastRefreshTime, long intervalTime)
	{
		long nowWeeks = (System.currentTimeMillis() - initRefreshTime) / intervalTime + 1;
		long lastTimeWeeks = (lastRefreshTime - initRefreshTime) / intervalTime + 1;
		return nowWeeks != lastTimeWeeks;
	}

}
