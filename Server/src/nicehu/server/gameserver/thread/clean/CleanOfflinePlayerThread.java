package nicehu.server.gameserver.thread.clean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nicehu.nhsdk.candy.lock.LockU;
import nicehu.nhsdk.candy.thread.WindRunnable;
import nicehu.nhsdk.core.data.SD;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.mgr.PM;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;

public class CleanOfflinePlayerThread extends WindRunnable
{
	public static long sleepTime = 1 * 60 * 1000L;

	public CleanOfflinePlayerThread()
	{
		super(sleepTime, ThreadLevel.LOW);
	}

	// offline五分钟,即从内存中清理掉
	@Override
	public void execute()
	{
		HashMap<Integer, Long> delMap = new HashMap<>();
		long expiredTime = System.currentTimeMillis() - CommonConfigMgr.instance.cfg.getOfflineExpiredTime();

		Iterator<Map.Entry<Integer, Long>> it = PM.offlineIds.ascendingMap().entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<Integer, Long> entry = it.next();
			if (PM.offlineIds.size() >= CommonConfigMgr.instance.cfg.getOfflinePlayerNum())
			{
				delMap.put(entry.getKey(), entry.getValue());
			}
			else if (entry.getValue() < expiredTime)
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
					if (PM.onlineIds.contains(playerId))
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
					PM.offlineIds.remove(playerId, entry.getValue());
					playerNode.setInfoPlayer(null);
					if (!PM.cacheIds.containsKey(playerId))
					{
						playerNode.setCachePlayer(null);
					}
				}
				finally
				{
					LockU.unLock(playerId);
				}
			}
		}
	}
}