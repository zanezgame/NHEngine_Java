package nicehu.server.gameserver.thread.clean;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;

import nicehu.nhsdk.candy.lock.LockU;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.thread.WindRunnable;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.session.GameSession;
import nicehu.server.gameserver.core.data.GSD;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.mgr.PM;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;

public class CleanOnlineSessionThread extends WindRunnable
{
	private static final Logger logger = LogU.getLogger(CleanOnlineSessionThread.class);

	private static long sleepTime = 10 * 1000L;

	public CleanOnlineSessionThread()
	{
		super(sleepTime, ThreadLevel.LOW);
	}

	// online 不操作30分钟,转为offline
	@Override
	public void execute()
	{
		long expiredTime = System.currentTimeMillis() - CommonConfigMgr.instance.cfg.getOnlineExpiredTime();
		ArrayList<Integer> delPlayerIds = new ArrayList<Integer>();
		for (GameSession session : GSD.sessions.values())
		{
			if (session.getUpdateTime() < expiredTime)
			{
				delPlayerIds.add(session.getPlayerId());
			}
		}
		for (Integer playerId : delPlayerIds)
		{
			GameSession session = GSD.sessions.get(playerId);

			this.offLine(session.getPlayerId());

		}
	}

	private void offLine(int playerId)
	{
		long expiredTime = System.currentTimeMillis() - CommonConfigMgr.instance.cfg.getOnlineExpiredTime();
		LockU.lock(playerId);
		try
		{
			if (GSD.sessions.get(playerId).getUpdateTime() >= expiredTime)
			{
				return;
			}
			if (SD.dbCluster.getGameDBClient().hasUnWriteCompleteSql(playerId))
			{
				return;
			}
			GSD.sessions.remove(playerId);
			PlayerNode playerNode = PM.getMemoryPlayerNode(playerId);
			if (playerNode != null)
			{
				PM.onlineIds.remove(playerId);
				PM.offlineIds.put(playerId, System.currentTimeMillis());

				// LoginMessageMgr.updatePlayerLogoutTime(playerNode, System.currentTimeMillis());
			}
		}
		catch (Exception e)
		{
			logger.error("PlayerId = {} /n {}", playerId, ExceptionUtils.getStackTrace(e));
		}
		finally
		{
			LockU.unLock(playerId);
		}
	}

	@Override
	public void closeThread()
	{
		// 踢掉在线玩家
		for (Entry<Integer, GameSession> entry : GSD.sessions.entrySet())
		{
			int playerId = entry.getKey();
			if (PM.isOnline(playerId))
			{
				offLine(playerId);
			}
		}
		logger.debug("all player has been kicked. player number : {}", GSD.sessions.size());
	}

}
