package nicehu.nhsdk.candy.log;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogU
{
	private static final Logger logger = LoggerFactory.getLogger(LogU.class);

	public static <T> Logger getLogger(Class<T> classInfo)
	{
		return LoggerFactory.getLogger(classInfo);
	}

	public static void printTime()
	{
		logger.debug("Now: " + System.currentTimeMillis());
	}

	public static void error(Exception e)
	{
		logger.error(ExceptionUtils.getStackTrace(e));
	}

	public static void info(Exception e)
	{
		logger.info(ExceptionUtils.getStackTrace(e));
	}

	public static void debug(Object obj)
	{
		logger.debug(obj.toString());
	}
	public static void info(Object obj)
	{
		logger.info(obj.toString());
	}
	public static void error(Object obj)
	{
		logger.error(obj.toString());
	}

}
