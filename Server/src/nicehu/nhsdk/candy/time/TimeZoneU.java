package nicehu.nhsdk.candy.time;

import java.util.TimeZone;

import org.slf4j.Logger;


import nicehu.nhsdk.candy.log.LogU;

public class TimeZoneU
{
	private static final Logger logger = LogU.getLogger(TimeZoneU.class);

	private static int timezone = 8;

	public static void setTimezone(int timezone)
	{
		logger.warn("Set Timezone = {}", timezone);
		TimeZoneU.timezone = timezone;
	}

	public static int getTimeZoneInt()
	{
		return timezone;
	}

	public static TimeZone getTimeZone()
	{
		return TimeZone.getTimeZone(TimeZoneU.getTimeZoneStr());
	}

	public static TimeZone getTimeZone(int timeZone)
	{
		return TimeZone.getTimeZone(TimeZoneU.getTimeZoneStr(timeZone));
	}

	public static String getTimeZoneStr()
	{
		return "GMT+" + timezone;
	}

	public static String getTimeZoneStr(int timeZone)
	{
		return "GMT+" + timeZone;
	}

}
