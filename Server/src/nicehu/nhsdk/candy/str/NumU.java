package nicehu.nhsdk.candy.str;

public class NumU
{
	public static String getHex(int value)
	{
		return Integer.toHexString(value);
	}

	public static int getDec(int value)
	{
		return Integer.parseInt(String.valueOf(value), 16);
	}

	public static int getDecForHex(String value)
	{
		return Integer.parseInt(value, 16);
	}

	public static int getInt(boolean flag)
	{
		return flag ? 1 : 0;
	}

}
