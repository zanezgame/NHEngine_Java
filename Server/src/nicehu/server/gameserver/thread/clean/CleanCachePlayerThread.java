package nicehu.server.gameserver.thread.clean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nicehu.nhsdk.candy.lock.LockU;
import nicehu.nhsdk.candy.thread.NHRunnable;
import nicehu.nhsdk.core.data.SD;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.mgr.PM;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;

public class CleanCachePlayerThread extends NHRunnable
{
	public static long sleepTime = 1 * 60 * 1000L;

	public CleanCachePlayerThread()
	{
		super(sleepTime, ThreadLevel.LOW);
	}

	// 10分钟不用,即释放
	@Override
	public void execute()
	{
		HashMap<Integer, Long> delMap = new HashMap<>();
		long expiredTime = System.currentTimeMillis() - CommonConfigMgr.instance.cfg.getDataCachePlayerExpiredTime();

		Iterator<Map.Entry<Integer, Long>> it = PM.cacheIds.ascendingMap().entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<Integer, Long> entry = it.next();
			if (entry.getValue() < expiredTime)
			{
				delMap.put(entry.getKey(), entry.getValue());
			}
			else
			{
				break;
			}
		}

		for (Map.Entry<Integer, Long> entry : delMap.entrySet())
		{
			int playerId = entry.getKey();
			PlayerNode playerNode = PM.playerNodes.get(playerId);
			if (playerNode != null)
			{
				LockU.lock(playerId);
				try
				{
					if (PM.offlineIds.containsKey(playerId))
					{
						continue;
					}
					if (entry.getValue() >= expiredTime)
					{
						continue;
					}
					if (SD.dbCluster.getGameDBClient().hasUnWriteCompleteSql(playerId))
					{
						continue;
					}
					PM.cacheIds.remove(playerId, entry.getValue());
					playerNode.setCachePlayer(null);

				}
				finally
				{
					LockU.unLock(playerId);
				}
			}
		}
	}
}