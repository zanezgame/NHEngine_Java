package nicehu.nhsdk.candy.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadU
{
	private static final Logger logger = LoggerFactory.getLogger(ThreadU.class);

	private static final AtomicInteger IdGen = new AtomicInteger(0);
	private static Executor threadPool = null;

	public static void start(Runnable runnable)
	{
		if (threadPool == null)
		{
			threadPool = Executors.newCachedThreadPool();
		}
		threadPool.execute(runnable);
	}

	public static void sleep(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public static String genName(String name)
	{
		if (name != null)
		{
			return name + "_" + IdGen.incrementAndGet();
		}
		else
		{
			return "Wind_Thread" + "_" + IdGen.incrementAndGet();
		}
	}

}
