package nicehu.nhsdk.candy.thread;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * when use this U,you onle need is to overwrite the execute()
 * @author windw
 *
 */
public abstract class WindRunnable implements Runnable
{
	private static Logger logger = LoggerFactory.getLogger(WindRunnable.class);

	public enum ThreadLevel
	{
		LOW, MEDIUM, HIGH
	}

	public static volatile boolean shutdown = false;
	public static AtomicInteger lowThreadNum = new AtomicInteger(0);
	public static AtomicInteger mediumThreadNum = new AtomicInteger(0);
	public static AtomicInteger highThreadNum = new AtomicInteger(0);

	private long sleepTime = 1000L;
	private long minSleeptime = 100L;
	private ThreadLevel shutdownLevel = ThreadLevel.LOW;

	public WindRunnable(long sleepTime, ThreadLevel shutdownLevel)
	{
		this.sleepTime = sleepTime;
		this.minSleeptime = this.minSleeptime < sleepTime ? this.minSleeptime : sleepTime;
		this.shutdownLevel = shutdownLevel;
	}

	public abstract void execute();

	public void closeThread()
	{
	}

	@Override
	public final void run()
	{
		switch (shutdownLevel)
		{
			case LOW:
				lowThreadNum.incrementAndGet();
				break;
			case MEDIUM:
				mediumThreadNum.incrementAndGet();
				break;
			case HIGH:
				highThreadNum.incrementAndGet();
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
					ThreadU.sleep(100);
				}
			}
			catch (Exception e)
			{
				logger.error(ExceptionUtils.getStackTrace(e));
				ThreadU.sleep(sleepTime);
			}
		}
		switch (shutdownLevel)
		{
			case LOW:
				closeThread();
				lowThreadNum.decrementAndGet();
				break;
			case MEDIUM:
				while (lowThreadNum.get() != 0)
				{
					ThreadU.sleep(500);
				}
				closeThread();
				mediumThreadNum.decrementAndGet();
				break;
			case HIGH:
				while (mediumThreadNum.get() != 0 || lowThreadNum.get() != 0)
				{
					ThreadU.sleep(500);
				}
				closeThread();
				highThreadNum.decrementAndGet();
				break;
			default:
				break;
		}
		logger.error(Thread.currentThread().getName() + " stop!!! shutdown. level:" + this.shutdownLevel.toString());
	}

	public static void shutdwonAll()
	{
		logger.error("Begin To Stop Threads!!!  ::" + ThreadLevel.LOW.toString() + ":" + lowThreadNum.get() + "\t" + ThreadLevel.MEDIUM.toString() + ":"
			+ mediumThreadNum.get() + "\t" + ThreadLevel.HIGH.toString() + ":" + highThreadNum.get());
		shutdown = true;
		while (highThreadNum.get() != 0 || mediumThreadNum.get() != 0 || lowThreadNum.get() != 0)
		{
			ThreadU.sleep(500);
		}
	}

}
