package nicehu.server.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.db.core.write.DBWriterPartCache;
import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.nhsdk.candy.thread.WindRunnable;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.handler.LogicHandler;

public class ShutdownReqHandler extends LogicHandler implements SignalHandler
{
	private static final Logger logger = LoggerFactory.getLogger(ShutdownReqHandler.class);

	private void shutdown()
	{
		logger.error("Receive ShutDown Signal!!!  ServerNameId:" + SD.getServerNameId());
		SD.isOpen = false;
		// write data to db
		DBWriterPartCache.expiredTime = 0;
		DBWriterPartCache.maxSqlNum = 0;
		ThreadU.sleep(2000);
		// close thread
		WindRunnable.shutdwonAll();
		logger.error(SD.getServerNameId() + " STOP SUCCESS__________________________All Threads has been stopped!");
		System.exit(0);
	}

	@Override
	public void handle(ServerNode sender, Message msg)
	{
		shutdown();
	}

	@Override
	public void handle(Signal arg0)
	{
		shutdown();
	}
}
