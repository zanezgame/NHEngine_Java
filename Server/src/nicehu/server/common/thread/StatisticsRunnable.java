package nicehu.server.common.thread;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Data;
import nicehu.nhsdk.candy.db.core.write.DBWriter;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.thread.NHRunnable;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.nhsdk.core.data.SD;

public class StatisticsRunnable extends NHRunnable
{

	private static final Logger logger = LoggerFactory.getLogger("StatisticsRunnable");

	public StatisticsRunnable()
	{
		super(30 * 1000, ThreadLevel.LOW);
	}

	@Override
	public void execute()
	{
		try
		{
			List<Data> datas = SD.statistics.getDatas();
			if (datas.size() == 0)
			{
				return;
			}
			logger.info(SD.serverName + "    " + TimeU.getStr_YMDHMSM());
			this.printBaseInfo(datas.get(0));
			logger.info("                                                 NETWORK:");
			datas.remove(0);
			this.printNetwork(datas);
			logger.info("                                                 DB");
			logger.info(DBWriter.dumpSqlNum());
			if (SD.memCache != null)
			{
				logger.info(SD.memCache.dumpMemCacheStatus());
			}
			logger.info("");
		}
		catch (Exception ex)
		{
			logger.error("{}", ExceptionUtils.getStackTrace(ex));
		}
	}

	private void printBaseInfo(Data data)
	{
		if (data == null)
		{
			return;
		}

		String title =
			String.format("%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s",
				"Cpu Percent",
				"Resident Memory",
				"Virtual Memory",
				"Shared Memory",
				"App Recveive Speed",
				"App Send Speed");
		logger.info("{}", title);
		String str =
			String.format("%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s",
				(data.getAttr("cpuPercent")) + "%",
				this.format(ParseU.pLong(data.getAttr("residentMemory")), ""),
				this.format(ParseU.pLong(data.getAttr("virtualMemory")), ""),
				this.format(ParseU.pLong(data.getAttr("sharedMemory")), ""),
				this.format(ParseU.pInt(data.getAttr("appReceiveSpeed")), "/s"),
				this.format(ParseU.pInt(data.getAttr("appSendSpeed")), "/s"));
		logger.info("{}", str);
	}

	private void printNetwork(List<Data> datas)
	{
		if (datas == null || datas.size() == 0)
		{
			return;
		}
		String title = String.format("%1$-20s%2$-20s%3$-20s", "IP", "Receive Speed", "Send Speed");
		logger.info(title);
		for (Data data : datas)
		{
			String str =
				String.format("%1$-20s%2$-20s%3$-20s",
					data.getAttr("ip"),
					this.format(ParseU.pInt(data.getAttr("receiveSpeed")), "/s"),
					this.format(ParseU.pInt(data.getAttr("sendSpeed")), "/s"));
			logger.info("{}", str);
		}
	}

	private String format(long memorySize, String str)
	{
		int sizeMB = (int)(memorySize / (1024 * 1024));
		if (1 <= sizeMB)
		{
			return sizeMB + " MB" + str;
		}

		int sizeKB = (int)(memorySize / 1024);
		if (1 <= sizeKB)
		{
			return sizeKB + " KB" + str;
		}

		return memorySize + " B" + str;
	}

}
