package nicehu.server.gameserver.logic.item.db;

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
import nicehu.server.gameserver.core.data.GSD;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.item.data.ItemData;

public class ItemDB
{

	private static final Logger logger = LoggerFactory.getLogger(ItemDB.class);

	public static void update(PlayerNode playerNode)
	{
		ItemData data = playerNode.getInfoPlayer().getItemData();
		String sql =
			String.format("replace into item(player_id, items_info, update_time) values(%d, '%s', '%s')", playerNode.getPlayerId(), data.toDBString(), TimeU.getStr());
		String key = DBEvent.genKey(DBEvent.ITEM_UPDATE, playerNode.getPlayerId());
		SD.memCache.executeAsyncSet(key, JsonU.getJsonStr(data));
		SD.dbCluster.getGameDBClient().executeAsyncUpdate(key, playerNode.getPlayerId(), sql);
	}

	public static ItemData query(int playerId)
	{

		ItemData data = JsonU.getJavaObj(ItemData.class, SD.memCache.executeGet(DBEvent.genKey(DBEvent.ITEM_UPDATE, playerId)));
		if (data != null)
		{
			return data;
		}
		data = new ItemData();
		String sql = String.format("select * from item where player_id= %d", playerId);
		DBClient dbClient = GSD.dbCluster.getGameDBClient();
		CachedRowSet rs = null;
		try
		{
			rs = dbClient.executeQuery(sql);
			if (rs != null && rs.next())
			{
				String items_info = rs.getString("items_info");
				if (!Empty.is(items_info))
				{
					data.fromDBString(items_info);
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("ItemDB  failed.{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		finally
		{
			CloseU.close(rs);
		}
		return data;
	}
}
