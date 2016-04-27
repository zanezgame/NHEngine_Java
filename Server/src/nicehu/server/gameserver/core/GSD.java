package nicehu.server.gameserver.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

import nicehu.nhsdk.candy.collect.lru.LruMap;
import nicehu.nhsdk.candy.time.Time;
import nicehu.nhsdk.core.data.SD;
import nicehu.server.gameserver.logic.initinfo.db.PlayerDB;
import nicehu.server.gameserver.logic.mgr.PM;

public class GSD extends SD
{
	public static LruMap<Integer, String> tokens = new LruMap<Integer, String>(200000, Time.DAY * 3);
	public static Map<Integer, GameSession> sessions = new ConcurrentHashMap<>();

	public static enum SessionStatus
	{
		unverified, // 未验证的
		verified_Connected, // 已验证的
		verified_disconnected // channelClose
	}

	public static boolean init()
	{
		StopWatch stopWatch0 = new Slf4JStopWatch("loadAllGamePlayer");
		if (!loadAllGamePlayer())
		{
			return false;
		}
		stopWatch0.stop();

		return true;
	}

	private static boolean loadAllGamePlayer()
	{
		PM.playerNodes = PlayerDB.loadAllGamePlayer();
		if (PM.playerNodes != null)
		{
			return true;
		}
		return false;
	}

}
