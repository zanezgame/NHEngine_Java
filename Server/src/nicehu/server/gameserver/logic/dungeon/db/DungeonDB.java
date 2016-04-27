package nicehu.server.gameserver.logic.dungeon.db;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.db.core.DBClient;
import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.nhsdk.candy.util.CloseU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.db.DBEvent;
import nicehu.server.gameserver.core.GSD;
import nicehu.server.gameserver.logic.dungeon.data.DungeonData;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;

public class DungeonDB
{
	private static final Logger logger = LoggerFactory.getLogger(DungeonDB.class);

	public static void update(PlayerNode playerNode)
	{
		DungeonData data = playerNode.getCachePlayer().getDungeonData();
		String sql =
			String.format("replace into dungeon (player_id, dungeons_info,update_time) VALUES (%d,'%s','%s')",
				playerNode.getPlayerId(),
				data.toDBString(),
				TimeU.getStr());
		String key = DBEvent.genKey(DBEvent.DUNGEON_UPDATE, playerNode.getPlayerId());
		SD.memCache.executeAsyncSet(key, JsonU.getJsonStr(data));
		SD.dbCluster.getGameDBClient().executeAsyncUpdate(key, playerNode.getPlayerId(), sql);
	}

	public static DungeonData query(int playerId)
	{

		DungeonData data = JsonU.getJavaObj(DungeonData.class, SD.memCache.executeGet(DBEvent.genKey(DBEvent.DUNGEON_UPDATE, playerId)));
		if (data != null)
		{
			return data;
		}
		data = new DungeonData();
		String sql = String.format("select * from dungeon where player_id= %d", playerId);
		DBClient dbClient = GSD.dbCluster.getGameDBClient();
		CachedRowSet rs = null;
		try
		{
			rs = dbClient.executeQuery(sql);
			if (rs != null && rs.next())
			{
				String dungeons_info = rs.getString("dungeons_info");
				if (!Empty.is(dungeons_info))
				{
					data.fromDBString(dungeons_info);
				}
				return data;
			}
		}
		catch (SQLException e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		finally
		{
			CloseU.close(rs);
		}
		return data;
	}
}
