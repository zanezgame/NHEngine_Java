package nicehu.nhsdk.candy.cache.core;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danga.MemCached.MemCachedClient;
import nicehu.nhsdk.candy.cache.MemCache;
import nicehu.nhsdk.candy.collect.lru.LruMap;
import nicehu.nhsdk.candy.collect.lru.MaxLruMap;
import nicehu.nhsdk.candy.thread.WindRunnable;

public class MemCacheThread extends WindRunnable
{
	private static Logger logger = LoggerFactory.getLogger(MemCacheThread.class);
	private static final long sleepTime = 20;
	private static int maxNum = 50000;

	private long expiredTime = 0;
	private MemCache memCache = null;
	public MaxLruMap<String, Cache> dataMap = new MaxLruMap<>();
	public MemCachedClient client = new MemCachedClient();

	public void set(String key, String value)
	{
		this.dataMap.put(key, new Cache(value, System.currentTimeMillis()));
	}

	@Override
	public void execute()
	{
		if (this.memCache.isOpen())
		{
			Map<String, Cache> exDataMap = new HashMap<>();
			for (Map.Entry<String, Cache> entry : this.dataMap.ascendingMap().entrySet())
			{
				if (this.dataMap.size() < maxNum && System.currentTimeMillis() < expiredTime + entry.getValue().getUpdateTime())
				{
					break;
				}
				exDataMap.put(entry.getKey(), entry.getValue());
			}
			if (exDataMap.size() != 0)
			{
				for (Map.Entry<String, Cache> entry : exDataMap.entrySet())
				{
					logger.debug("MemCache Set  key: " + entry.getKey() + "  value: " + entry.getValue().getData());
					int tryDoCount = 1;
					boolean success = client.set(entry.getKey(), entry.getValue().getData());
					while (!success)
					{
						tryDoCount++;
						if (tryDoCount <= MemCache.MaxTryDoCount)
						{
							logger.error(" MemCache tryDoFail,then tryDoCount: " + tryDoCount);
							success = client.set(entry.getKey(), entry.getValue().getData());
						}
						else
						{
							this.memCache.doFail(entry.getKey(), entry.getValue().getData());
							break;
						}
					}
					logger.debug("MemCacheThread asyncSet  key: " + entry.getKey() + "  value: " + entry.getValue().getData());
					dataMap.remove(entry.getKey(), entry.getValue());
				}
			}

		}
	}

	@Override
	public void closeThread()
	{
		expiredTime = 0;
		maxNum = 0;
		while (!dataMap.isEmpty())
		{
			execute();
		}
		client = null;
	}

	public MemCacheThread(long expiredTime, MemCache memCache)
	{
		super(sleepTime, ThreadLevel.HIGH);
		this.expiredTime = expiredTime;
		this.memCache = memCache;
	}

}
