package nicehu.nhsdk.candy.test;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

public class ConcurrentLinkedHashMapTest
{
	public static void main(String[] args)
	{
		ConcurrentLinkedHashMap<String, String> map =
			new ConcurrentLinkedHashMap.Builder<String, String>().maximumWeightedCapacity(5).build();

		String a = "1";
		map.put(a, a);
		System.out.println("put " + a);
		System.out.println(map.keySet().toString());

		a = "2";
		map.put(a, a);
		System.out.println("put " + a);
		System.out.println(map.keySet().toString());

		a = "3";
		map.put(a, a);
		System.out.println("put " + a);
		System.out.println(map.keySet().toString());

		a = "4";
		map.put(a, a);
		System.out.println("put " + a);
		System.out.println(map.keySet().toString());

		a = "5";
		map.put(a, a);
		System.out.println("put " + a);
		System.out.println(map.keySet().toString());
		
		a = "2";
		map.get(a);
		System.out.println("get " + a);
		System.out.println(map.keySet().toString());
		
		a = "1";
		map.get(a);
		System.out.println("get " + a);
		System.out.println(map.keySet().toString());
		
		a = "6";
		map.put(a, a);
		System.out.println("put " + a);
		System.out.println(map.keySet().toString());
		
		System.out.println("ascendingKeySet");
		System.out.println(map.ascendingKeySet().toString());
		
		System.out.println("descendingKeySet");
		System.out.println(map.descendingKeySet().toString());
	}

}
