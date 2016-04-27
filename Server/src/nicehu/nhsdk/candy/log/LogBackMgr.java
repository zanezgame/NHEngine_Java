package nicehu.nhsdk.candy.log;

import java.io.File;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import nicehu.server.manageserver.config.core.ConfigPath;

public class LogBackMgr
{
	private static final Logger logger = LoggerFactory.getLogger(LogBackMgr.class);
	
	public static void init()
	{
		LogBackMgr.init(ConfigPath.file_logback);
	}

	public static void init(String logBackFilePath)
	{
		LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
		File externalConfigFile = new File(logBackFilePath);
		if (!externalConfigFile.exists())
		{
			logger.error("Logback External Config File Parameter does not reference a file that exists");
		}
		else
		{
			if (!externalConfigFile.isFile())
			{
				logger.error("Logback External Config File Parameter exists, but does not reference a file");
			}
			else
			{
				if (!externalConfigFile.canRead())
				{
					logger.error("Logback External Config File exists and is a file, but cannot be read.");
				}
				else
				{
					try
					{
						JoranConfigurator configurator = new JoranConfigurator();
						configurator.setContext(lc);
						lc.reset();
						configurator.doConfigure(logBackFilePath);
					}
					catch (JoranException e)
					{
						logger.error(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}

	}

}