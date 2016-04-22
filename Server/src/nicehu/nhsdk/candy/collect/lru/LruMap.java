package nicehu.nhsdk.candy.collect.lru;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import nicehu.nhsdk.candy.log.LogU;

public class LruMap<K, V> implements Serializable
{
	Logger logger = LogU.getLogger(LruMap.class);
	private static final long serialVersionUID = 1L;
	private Object obj = new Object();
	private boolean clearOpen = false;
	private long cacheTime = 0L;
	private int maxChangeCount = 10000;
	private AtomicInteger changeCount = new AtomicInteger(0);
	private ConcurrentLinkedHashMap<K, Long> timeMap = null;
	private ConcurrentLinkedHashMap<K, V> dataMap = null;

	public void clear()
	{
		if (clearOpen && changeCount.get() > maxChangeCount)
		{
			synchronized (obj)
			{
				if (changeCount.get() > maxChangeCount)
				{
					long now = System.currentTimeMillis();
					Map<K, Long> delTimeMap = new HashMap<>();
					Map<K, V> delDataMap = new HashMap<>();
					for (Map.Entry<K, Long> entry : timeMap.ascendingMap().entrySet())
					{
						V dataValue = dataMap.get(entry.getKey());
						if (entry.getValue() + cacheTime < now)
						{
							// logger.error("!!!!!!!!!!" + timeMap.size() + "  " + dataMap.size() + "   " +
							// changeCount.get());
							K key = entry.getKey();
							delTimeMap.put(key, entry.getValue());
							delDataMap.put(key, dataValue);
						}
						else
						{
							break;
						}
					}
					for (Map.Entry<K, Long> entry : delTimeMap.entrySet())
					{
						K key = entry.getKey();
						timeMap.remove(key, entry.getValue());
						dataMap.remove(key, delDataMap.get(key));
					}
					changeCount.set(0);
					// logger.error("end!!!!!!!!!!" + timeMap.size() + "  " + dataMap.size() + "   " +
					// changeCount.get());
				}
			}
		}
	}

	public LruMap(int size, long cacheTime)
	{
		this.cacheTime = cacheTime;
		if (cacheTime > 0)
		{
			clearOpen = true;
			timeMap = new ConcurrentLinkedHashMap.Builder<K, Long>().maximumWeightedCapacity(size).build();
		}
		dataMap = new ConcurrentLinkedHashMap.Builder<K, V>().maximumWeightedCapacity(size).build();

	}

	public void put(K key, V value)
	{
		if (clearOpen)
		{
			changeCount.incrementAndGet();
			timeMap.put(key, System.currentTimeMillis());
		}
		dataMap.put(key, value);
	}

	public V get(K key)
	{
		clear();
		return dataMap.get(key);
	}

	public Map<K, V> ascendingMap()
	{
		return dataMap.ascendingMap();
	}

	public Map<K, V> descendingMap()
	{
		return dataMap.descendingMap();
	}

	public int size()
	{
		return dataMap.size();
	}

	public boolean containsKey(Object key)
	{
		return dataMap.containsKey(key);
	}

	public void remove(Object key)
	{
		dataMap.remove(key);
	}

	public void remove(Object key, Object value)
	{
		dataMap.remove(key, value);
	}

	public boolean isEmpty()
	{
		return dataMap.isEmpty();
	}

	public ConcurrentLinkedHashMap<K, V> getMap()
	{
		return dataMap;
	}

	public void setMap(ConcurrentLinkedHashMap<K, V> map)
	{
		this.dataMap = map;
	}

}
