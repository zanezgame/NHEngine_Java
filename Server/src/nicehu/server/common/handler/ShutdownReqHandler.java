package nicehu.server.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.db.core.write.DBWriterPartCache;
import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.nhsdk.candy.thread.NHRunnable;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;

public class ShutdownReqHandler implements SignalHandler
{
	private static final Logger logger = LoggerFactory.getLogger(ShutdownReqHandler.class);

	@Override
	public void handle(Signal arg0)
	{
		logger.warn("Receive ShutDown Signal!!!  ServerNameId:" + SD.getServerNameId());
		SD.isOpen = false;
		// write data to db
		DBWriterPartCache.expiredTime = 0;
		DBWriterPartCache.maxSqlNum = 0;
		ThreadU.sleep(2000);
		// close thread
		NHRunnable.shutdwonAll();
		logger.warn(SD.getServerNameId() + " STOP SUCCESS__________________________All Threads has been stopped!");
		System.exit(0);
	}
}
