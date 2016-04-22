package nicehu.nhsdk.candy.str;

import nicehu.nhsdk.candy.object.Empty;

public class ParseU
{
	public static int pInt(String str, int defaultValue)
	{
		if (!Empty.is(str))
		{
			return Integer.parseInt(str);
		}
		return defaultValue;
	}

	public static int pInt(String str)
	{
		if (!Empty.is(str))
		{
			return Integer.parseInt(str);
		}
		return 0;
	}

	public static int pHexInt(String str, int defaultValue)
	{
		if (!Empty.is(str))
		{
			Long value = Long.valueOf(str, 16);
			if (value != null)
			{
				return value.intValue();
			}
		}
		return defaultValue;
	}

	public static int pHexInt(String str)
	{
		return pHexInt(str, 0);
	}

	public static long pLong(String str, long defaultValue)
	{
		if (!Empty.is(str))
		{
			return Long.parseLong(str);
		}
		return defaultValue;
	}

	public static long pLong(String str)
	{
		if (!Empty.is(str))
		{
			return Long.parseLong(str);
		}
		return 0;
	}

	public static String pStr(Object str)
	{
		if (!Empty.is(str))
		{
			return String.valueOf(str);
		}
		return "";
	}

	public static String pStr(String str, String defaultValue)
	{
		if (!Empty.is(str))
		{
			return str;
		}
		return defaultValue;
	}

	public static boolean pBool(String str)
	{
		if (!Empty.is(str))
		{
			if (str.equals("1"))
			{
				return true;
			}
			return Boolean.parseBoolean(str);
		}
		return false;
	}

	public static boolean pBool(String str, boolean defaultValue)
	{
		if (!Empty.is(str))
		{
			return Boolean.parseBoolean(str);
		}
		return defaultValue;
	}
}
