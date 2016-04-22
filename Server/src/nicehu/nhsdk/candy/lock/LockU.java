package nicehu.nhsdk.candy.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.nhsdk.candy.time.Time;
import nicehu.nhsdk.candy.time.TimeUE;

public class LockU
{
	private static final Logger logger = LogU.getLogger(LockU.class);

	public static Object obj = new Object();
	public static ConcurrentHashMap<Integer, Lock> locks = new ConcurrentHashMap<>();

	private static AtomicInteger lockCount = new AtomicInteger();
	private static int maxCount = 500000;
	private static boolean maintain = false;
	private static long lastClearTime = System.currentTimeMillis();

	public static void lock(int key)
	{
		while (maintain)
		{
			ThreadU.sleep(100);
			logger.warn("LockU is in maintain ,wait for open1");
		}
		if (lockCount.incrementAndGet() <= 0)
		{
			logger.error("lock: lockCount " + lockCount.get());
			lockCount.set(1);;
		}
		Lock lock = null;// 竞争点1
		synchronized (obj)
		{
			lock = locks.get(key);
			if (lock == null)
			{
				lock = new ReentrantLock();
				locks.put(key, lock);
			}
		}
		lock.lock();// 竞争点2
	}

	public static void unLock(int key)
	{
		while (maintain)
		{
			ThreadU.sleep(100);
			logger.warn("LockU is in maintain ,wait for open2");
		}
		if (lockCount.decrementAndGet() < 0)
		{
			logger.error("unlock: lockCount " + lockCount.get());
			lockCount.set(0);
		}
		Lock lock = locks.get(key);
		if (lock != null)
		{
			lock.unlock();
		}
		else
		{
			logger.error("UnLock Faild !,lock is not exist. key: " + key);
		}
		clear();
	}

	private synchronized static void clear()
	{
		if (TimeUE.inDayTime(2, 6) ||locks.size()> maxCount)
		{
			if (lockCount.get() == 0 && TimeUE.exceed(lastClearTime, Time.DAY))
			{
				maintain = true;
				try
				{
					ThreadU.sleep(500);
					if (lockCount.get() == 0)
					{
						locks = new ConcurrentHashMap<>();
						lastClearTime = System.currentTimeMillis();
						logger.error("Success: LockUtil Maintain !!!");
					}
					else
					{
						logger.error("Warn: LockUtil Maintain lockCount: " + lockCount.get());
					}
				}
				finally
				{
					maintain = false;
				}
			}
		}
	}
}
