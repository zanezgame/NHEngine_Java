package nicehu.server.gameserver.logic.mgr;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;

import nicehu.nhsdk.candy.collect.lru.MaxLruMap;
import nicehu.nhsdk.candy.lock.LockU;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.server.common.dblog.LogUtil;
import nicehu.server.gameserver.core.GSD;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.initinfo.db.PlayerDB;

public class PM
{
	private static final Logger logger = LogU.getLogger(PM.class);

	public static ConcurrentHashMap<Integer, PlayerNode> playerNodes = new ConcurrentHashMap<>();

	public static ConcurrentSkipListSet<Integer> onlineIds = new ConcurrentSkipListSet<>();
	public static MaxLruMap<Integer, Long> offlineIds = new MaxLruMap<Integer, Long>();
	public static MaxLruMap<Integer, Long> cacheIds = new MaxLruMap<Integer, Long>();

	public static PlayerNode getPlayerNode(int playerId)
	{
		return getPlayerNode(playerId, false);
	}

	public static PlayerNode getPlayerNode(int playerId, boolean isSelfLogin)
	{
		PlayerNode playerNode = null;
		LockU.lock(playerId);
		try
		{
			playerNode = getMemoryPlayerNode(playerId);
			if (playerNode == null)
			{
				playerNode = new PlayerNode(playerId);
				PM.playerNodes.put(playerId, playerNode);
			}
			boolean isOnline = true;
			if (playerNode.hasEmpty())
			{
				isOnline = false;
				PlayerNode playerNodeTemp = PlayerDB.InsertAndLoadPlayer(playerId);
				if (playerNodeTemp != null)
				{
					if (playerNode.getGamePlayer() == null)
					{
						playerNode.setGamePlayer(playerNodeTemp.getGamePlayer());
					}
					if (playerNode.getCachePlayer() == null)
					{
						PlayerDB.LoadCachePlayer(playerNode);
					}
					if (playerNode.getInfoPlayer() == null)
					{
						PlayerDB.LoadInfoPlayer(playerNode);
					}
				}
			}
			if (isSelfLogin)
			{
				PM.onlineIds.add(playerId);
				PM.offlineIds.remove(playerId);
				PAM.updateLoginTime(playerNode);
				LogUtil.login(playerNode, GSD.sessions.get(playerId));
			}
			else
			{
				if (!isOnline)
				{
					PM.offlineIds.put(playerId, System.currentTimeMillis());
				}
			}
		}
		finally
		{
			LockU.unLock(playerId);
		}
		if (playerNode.getGamePlayer() != null)
		{
			return playerNode;
		}
		return null;

	}

	public static PlayerNode getCachePlayer(int playerId)
	{
		PlayerNode playerNode = null;
		LockU.lock(playerId);
		try
		{
			playerNode = getMemoryPlayerNode(playerId);
			if (playerNode == null)
			{
				return null;
			}
			PM.cacheIds.put(playerId, System.currentTimeMillis());
			boolean isOnline = true;
			if (playerNode.getGamePlayer() == null || playerNode.getCachePlayer() == null)
			{
				isOnline = false;
				PlayerNode playerNodeTemp = PlayerDB.LoadCachePlayer(playerNode);
				if (playerNodeTemp != null)
				{
					if (playerNode.getCachePlayer() == null)
					{
						playerNode.setCachePlayer(playerNodeTemp.getCachePlayer());
					}
				}
			}
			if (!isOnline)
			{
				PM.offlineIds.put(playerId, System.currentTimeMillis());
			}
		}
		finally
		{
			LockU.unLock(playerId);
		}
		if (playerNode.getGamePlayer() != null)
		{
			return playerNode;
		}
		return null;
	}

	public static PlayerNode getMemoryPlayerNode(int playerId)
	{
		return PM.playerNodes.get(playerId);
	}

	public static boolean isOnline(int playerId)
	{
		return PM.onlineIds.contains(playerId);
	}

	public static boolean exist(int playerId)
	{
		return PM.playerNodes.get(playerId) != null;
	}

}
