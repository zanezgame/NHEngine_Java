package nicehu.nhsdk.candy.util;

import nicehu.nhsdk.candy.time.TimeU;

public class CandyU
{

	public static void print(Object obj)
	{
		System.out.println(obj.toString());
	}

	public static void printNow()
	{
		System.out.println(TimeU.getStr());
	}

}
