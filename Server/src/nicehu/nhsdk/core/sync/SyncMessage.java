package nicehu.nhsdk.core.sync;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.thread.ThreadU;

public class SyncMessage
{
	private static final Logger logger = LoggerFactory.getLogger(SyncMessage.class);
	private static final int timeout = 5;

	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private Object message;
	private int seqId;
	private boolean ready = false;

	public Object waitForResponse()
	{
		return this.waitForResponse(timeout);
	}

	public Object waitForResponse(int second)
	{
		Object result = null;
		long remainTime = 0;
		lock.lock();
		try
		{
			ready = true;
			remainTime = condition.awaitNanos(second * 1000000000L);// 毫微秒
			if (remainTime > 0)
			{
				result = message;
			}
			else
			{
				logger.error("SyncMessage  Timeout seqId={}\n{}", seqId, ExceptionUtils.getStackTrace(new Throwable()));
			}
		}
		catch (Exception ex)
		{
			logger.error(ExceptionUtils.getStackTrace(ex));
		}
		finally
		{
			lock.unlock();
			SyncMgr.removeMessage(seqId);
		}

		return result;
	}

	public void receivedResponse(Object message)
	{
		lock.lock();
		while (false == ready)
		{
			lock.unlock();
			ThreadU.sleep(1);
			lock.lock();
		}
		try
		{
			this.message = message;
			condition.signal();
		}
		catch (Exception ex)
		{
			logger.error(ExceptionUtils.getStackTrace(ex));
		}
		finally
		{
			lock.unlock();
		}
	}

	public SyncMessage()
	{

	}

	public SyncMessage(int seqId)
	{
		this.seqId = seqId;
	}
}
