package nicehu.nhsdk.candy.thread;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * when use this U,you only need is to overwrite the execute()
 * 
 * @author cherish
 *
 */
public abstract class NHRunnable implements Runnable
{
	private static Logger logger = LoggerFactory.getLogger(NHRunnable.class);

	public enum ThreadLevel
	{
		LOW, MEDIUM, HIGH
	}

	public static volatile boolean shutdown = false;
	public static AtomicInteger lNum = new AtomicInteger(0);
	public static AtomicInteger mNum = new AtomicInteger(0);
	public static AtomicInteger hNum = new AtomicInteger(0);

	private long sleepTime = 1000L;
	private long minSleeptime = 100L;
	private ThreadLevel level = ThreadLevel.LOW;

	public NHRunnable(long sleepTime, ThreadLevel level)
	{
		this.sleepTime = sleepTime;
		this.level = level;
		// 低于1秒的,不使用睡眠切分.
		if (sleepTime < 1000)
		{
			minSleeptime = sleepTime;
		}
	}

	public abstract void execute();

	public void closeThread()
	{
	}

	@Override
	public final void run()
	{
		switch (level)
		{
			case LOW:
				lNum.incrementAndGet();
				break;
			case MEDIUM:
				mNum.incrementAndGet();
				break;
			case HIGH:
				hNum.incrementAndGet();
				break;
			default:
				break;
		}
		while (!shutdown)
		{
			try
			{
				execute();

				long alreadySleepTime = 0;
				while (!shutdown && alreadySleepTime < sleepTime)
				{
					alreadySleepTime += this.minSleeptime;
					ThreadU.sleep(minSleeptime);
				}
			}
			catch (Exception e)
			{
				logger.error(ExceptionUtils.getStackTrace(e));
				ThreadU.sleep(sleepTime);
			}
		}
		switch (level)
		{
			case LOW:
				closeThread();
				lNum.decrementAndGet();
				break;
			case MEDIUM:
				while (lNum.get() != 0)
				{
					ThreadU.sleep(minSleeptime);
				}
				closeThread();
				mNum.decrementAndGet();
				break;
			case HIGH:
				while (mNum.get() != 0 || lNum.get() != 0)
				{
					ThreadU.sleep(minSleeptime);
				}
				closeThread();
				hNum.decrementAndGet();
				break;
			default:
				break;
		}
		logger.warn(Thread.currentThread().getName() + " stop!!! shutdown. level:" + this.level.toString());
	}

	public static void shutdwonAll()
	{
		logger.warn("Begin To Stop Threads!!!  ::" + ThreadLevel.LOW.toString() + ":" + lNum.get() + "\t" + ThreadLevel.MEDIUM.toString() + ":" + mNum.get() + "\t"
			+ ThreadLevel.HIGH.toString() + ":" + hNum.get());
		shutdown = true;
		while (hNum.get() != 0 || mNum.get() != 0 || lNum.get() != 0)
		{
			ThreadU.sleep(100);
		}
	}

}
