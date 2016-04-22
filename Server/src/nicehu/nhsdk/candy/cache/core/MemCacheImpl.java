package nicehu.nhsdk.candy.cache.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import nicehu.nhsdk.candy.cache.MemCache;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.thread.ThreadU;

public class MemCacheImpl implements MemCache
{
	private static Logger logger = LogU.getLogger(MemCacheImpl.class);

	public boolean open = false;
	public AtomicInteger failCount = new AtomicInteger();
	private MemCachedClient write = null;
	private MemCachedClient read = null;

	private List<MemCacheThread> mCacheThreads = new ArrayList<>();

	public MemCacheImpl(List<String> address, int threadCount, long expiredTime, boolean flushAll)
	{
		// init memcache connect pool
		if (Empty.is(address))
		{
			logger.error(" Init MemCache Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!");
			return;
		}
		String[] servers = address.toArray(new String[0]);
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers(servers);
		// when more servers,can use load balanced
		// Integer[] weights = {1, 1, 1};
		// pool.setWeights(weights);
		pool.setFailover(true);
		pool.setInitConn(10);
		pool.setMinConn(5);
		pool.setMaxConn(200);
		pool.setMaintSleep(30);
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.initialize();

		// init client
		write = new MemCachedClient();
		read = new MemCachedClient();

		// test write
		if (!write.set("test", "test"))
		{
			logger.error(" Init MemCache Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!Faild !!!!!  address: " + address.toString());
			return;
		}
		this.open = true;

		// start thread
		for (int i = 0; i < threadCount; i++)
		{
			MemCacheThread mCacheThread = new MemCacheThread(expiredTime, this);
			this.mCacheThreads.add(mCacheThread);
			Thread thread = new Thread(mCacheThread, ThreadU.genName("Wind_MCacheThread"));
			thread.start();
		}
		if (flushAll)
		{
			this.flushAll();
		}
		logger.info(" Init MemCache Success!!!");
	}

	public boolean executeSet(String key, String value)
	{
		if (this.open)
		{
			logger.debug("MemCache Set  key: " + key + "  value: " + value);
			int tryDoCount = 1;
			boolean success = write.set(key, value);
			while (!success)
			{
				tryDoCount++;
				if (tryDoCount <= MemCache.MaxTryDoCount)
				{
					logger.error(" MemCache tryDoFail,then tryDoCount: " + tryDoCount);
					success = write.set(key, value);
				}
				else
				{
					this.doFail(key, value);
					break;
				}
			}
			return success;
		}
		return false;
	}

	public void executeAsyncSet(String key, String value)
	{
		if (this.open)
		{
			int hashCode = Math.abs(key.hashCode());
			MemCacheThread thread = mCacheThreads.get(hashCode % mCacheThreads.size());
			thread.set(key, value);
		}
	}

	public String executeGet(String key)
	{
		if (this.open)
		{
			return (String)read.get(key);
		}
		return null;
	}

	public boolean isOpen()
	{
		return this.open;
	}

	public void close()
	{
		this.open = false;
	}

	public int getFailCount()
	{
		return failCount.get();
	}

	public void doFail(String key, String value)
	{
		failCount.incrementAndGet();
		logger.error("Memcache Set Faild!! key: " + key + "value: " + value + " FaildCount: " + failCount);
		if (failCount.get() > MemCache.MaxTryDoCount)
		{
			this.open = false;
			logger.error("Memcache Fail To Much,Auto Close!!!  FaildCount: " + failCount);
			logger.error("Fail!Fail!Fail!Fail!Fail!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("Fail!Fail!Fail!Fail!Fail!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("Fail!Fail!Fail!Fail!Fail!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("Fail!Fail!Fail!Fail!Fail!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.error("Fail!Fail!Fail!Fail!Fail!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			// System.exit(0);

		}
	}

	// set all data expire
	public boolean flushAll()
	{
		if (this.open)
		{
			logger.warn("Memcache flushAll !!!!!!");
			return write.flushAll();
		}
		return true;
	}

	public long getUnWriteCount()
	{
		long count = 0;
		for (MemCacheThread memCacheThread : mCacheThreads)
		{
			count += memCacheThread.dataMap.size();
		}
		return count;
	}

	public String dumpMemCacheStatus()
	{

		StringBuffer sb = new StringBuffer();
		sb.append("MemcacheOpen: " + this.open);
		sb.append(" faildCount: " + this.failCount);
		sb.append(" UnWriteMemacheCount: " + this.getUnWriteCount());
		return sb.toString();
	}
}
