package nicehu.nhsdk.candy.test.memcache;

import java.util.ArrayList;
import java.util.List;

import nicehu.nhsdk.candy.cache.core.MemCacheImpl;

public class Test
{
	public static void main(String[] args)
	{

		List<String> address = new ArrayList<>();
		address.add("192.168.1.250:7777");
		MemCacheImpl cache = new MemCacheImpl(address, 1, 0, true);

		long now = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++)
		{
			String pre = "b";
			cache.executeSet(pre + i, "Hello!");
			String result = (String)cache.executeGet(pre + i);
			System.out.println(String.format("set( %d ):", i));
			System.out.println(String.format("get( %d ): %s", i, result));
		}
		System.out.println("!!!!!!!!!!! JJJ Cost Time : " + (System.currentTimeMillis() - now));
	}
}