package nicehu.nhsdk.candy.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nicehu.nhsdk.candy.object.Empty;

public class CollectionU
{
	public static HashMap<String, String> splitToMap(String str, String split1, String split2)
	{
		HashMap<String, String> map = new HashMap<String, String>();

		if (!Empty.is(str))
		{
			String[] mapStrs = str.split(split1);
			int length = mapStrs.length;
			for (int i = 0; i < length; i++)
			{
				String mapStr = mapStrs[i];
				int index = mapStr.indexOf(split2);
				if (index != -1)
				{
					String key = mapStr.substring(0, index).trim();
					String value = mapStr.substring(index + 1, mapStr.length()).trim();
					map.put(key, value);
				}
			}
		}
		return map;
	}

	static public List<Integer> stringListToIntList(List<String> list)
	{
		List<Integer> intList = new ArrayList<Integer>();
		for (String s : list)
		{
			intList.add(Integer.parseInt(s));
		}
		return intList;
	}

	// list转string
	public static <T> String listToString(List<T> list, String separated)
	{
		String str = "";
		for (Object obj : list)
		{
			str += obj + separated;
		}
		return str;
	}

	// Hashset转string
	public static <T> String setToString(HashSet<T> hashSet, String separated)
	{
		String str = "";
		for (Object obj : hashSet)
		{
			str += obj + separated;
		}
		return str;
	}

	// Map转string
	public static String MapToString(Map<Integer, Integer> map)
	{
		String str = "";
		for (Map.Entry<Integer, Integer> entry : map.entrySet())
		{
			str += entry.getKey() + "&" + entry.getValue() + "|";
		}
		return str;
	}

}
