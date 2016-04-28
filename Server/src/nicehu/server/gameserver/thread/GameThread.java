package nicehu.server.gameserver.thread;

import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.server.common.thread.StatisticsRunnable;
import nicehu.server.gameserver.thread.clean.CleanCachePlayerThread;
import nicehu.server.gameserver.thread.clean.CleanOfflinePlayerThread;
import nicehu.server.gameserver.thread.clean.CleanOnlineSessionThread;

public class GameThread
{

	public static void start()
	{
		Thread thread = new Thread(new CleanCachePlayerThread(), ThreadU.genName("Wind_CleanCachePlayerThread"));
		thread.start();
		thread = new Thread(new CleanOnlineSessionThread(), ThreadU.genName("Wind_CleanSessionThread"));
		thread.start();
		thread = new Thread(new CleanOfflinePlayerThread(), ThreadU.genName("Wind_CleanOfflinePlayerThread"));
		thread.start();
		thread = new Thread(new StatisticsRunnable(), ThreadU.genName("Wind_StatisticsRunnable"));
		thread.start();

		// Thread onlineStatusLogThread = new Thread(new OnlineLogThread(), ThreadU.genName("Wind_OnlineLogThread"));
		// onlineStatusLogThread.start();
	}

}
