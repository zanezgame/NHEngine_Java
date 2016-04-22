package nicehu.nhsdk.candy.test.db;

import java.util.HashMap;
import java.util.Map;

public class WindMemRowSet extends WindRowSet
{
	Map<String, String> map = new HashMap<>();

	public WindMemRowSet(String value)
	{
		String[] strs = value.split("<|>");
		for (String str : strs)
		{
			String[] strMap = str.split("<&>");
			map.put(strMap[0], strMap[1]);
		}
	}

}
