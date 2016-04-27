package nicehu.server.gameserver.logic.initinfo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.db.core.DBClient;
import nicehu.nhsdk.candy.db.transact.DBTransact;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.nhsdk.candy.util.CloseU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.db.DBEvent;
import nicehu.server.gameserver.core.GSD;
import nicehu.server.gameserver.logic.dungeon.db.DungeonDB;
import nicehu.server.gameserver.logic.initinfo.data.struct.CachePlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.GamePlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.InfoPlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.initinfo.db.transact.T_InsertAndLoadPlayerNode;
import nicehu.server.gameserver.logic.initinfo.db.transact.T_LoadAllGamePlayer;
import nicehu.server.gameserver.logic.item.db.ItemDB;

public class PlayerDB
{
	private static final Logger logger = LoggerFactory.getLogger(PlayerDB.class);

	public static ConcurrentHashMap<Integer, PlayerNode> loadAllGamePlayer()
	{
		DBClient dbClient = GSD.dbCluster.getGameDBClient();
		Connection con = null;
		try
		{
			con = dbClient.getConnection();
		}
		catch (SQLException e)
		{
			logger.warn("{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}

		try
		{
			if (con != null)
			{
				T_LoadAllGamePlayer tt = new T_LoadAllGamePlayer();
				DBTransact.doTransact(con, tt);
				return tt.getPlayers();
			}
			return null;
		}
		finally
		{
			CloseU.close(con);
		}
	}

	public static PlayerNode InsertAndLoadPlayer(int playerId)
	{
		logger.debug("InsertAndLoadPlayerNode playerId={} ... ", playerId);

		DBClient dbClient = GSD.dbCluster.getGameDBClient();
		Connection con = null;
		try
		{
			con = dbClient.getConnection();
		}
		catch (SQLException e)
		{
			logger.warn("{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		try
		{
			if (con != null)
			{
				T_InsertAndLoadPlayerNode tt = new T_InsertAndLoadPlayerNode(playerId);
				DBTransact.doTransact(con, tt);
				return tt.getPlayerNode();

			}
			return null;
		}
		finally
		{
			CloseU.close(con);
			logger.debug("InsertAndLoadPlayerNode playerId={} done", playerId);
		}
	}

	public static PlayerNode LoadInfoPlayer(PlayerNode playerNode)
	{
		logger.info("load InfoPlayer playerId={}", playerNode.getPlayerId());
		int playerId = playerNode.getPlayerId();
		InfoPlayer infoPlayer = new InfoPlayer(playerId);
		infoPlayer.setItemData(ItemDB.query(playerId));
		playerNode.setInfoPlayer(infoPlayer);
		return playerNode;
	}

	public static PlayerNode LoadCachePlayer(PlayerNode playerNode)
	{
		logger.info("load CachePlayer playerId={}", playerNode.getPlayerId());
		int playerId = playerNode.getPlayerId();
		CachePlayer cachePlayer = new CachePlayer(playerId);
		cachePlayer.setDungeonData(DungeonDB.query(playerId));
		playerNode.setCachePlayer(cachePlayer);
		return playerNode;
	}

	public static void update(PlayerNode playerNode)
	{
		GamePlayer gamePlayer = playerNode.getGamePlayer();
		String sql =
			String.format("update player set  money = %d,dungeon_max_id = %d,dungeon_stars = %d ,login_time = '%s' where player_id=%d",
				gamePlayer.getMoney(),
				gamePlayer.getDungeonMaxId(),
				gamePlayer.getDungeonStars(),
				TimeU.getStr(gamePlayer.getLoginTime()),
				playerNode.getPlayerId());
		String key = DBEvent.genKey(DBEvent.PLAYER_UPDATE_PLAYER_ATTR, playerNode.getPlayerId());
		// SD.memCache.executeAsyncSet(key, JsonU.getJsonStr(data));
		SD.dbCluster.getGameDBClient().executeAsyncUpdate(key, playerNode.getPlayerId(), sql);
	}

}
