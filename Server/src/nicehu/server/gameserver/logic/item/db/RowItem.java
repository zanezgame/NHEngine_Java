package nicehu.server.gameserver.logic.item.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import nicehu.nhsdk.candy.db.transact.DBTransact;
import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.db.DBEvent;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.item.data.ItemData;

public class RowItem
{

	// 插入
	public static boolean insertPrivateList(int queryIndex, Connection con, PreparedStatement[] vps, CachedRowSet[] vrs, int playerId, PlayerNode playerNode)
		throws SQLException
	{

		String sql = "insert ignore into item ( player_id,items_info,update_time)  VALUES (?,?,?)";
		vps[queryIndex] = con.prepareStatement(sql);

		Object[] objList = new Object[] {playerId, playerNode.getInfoPlayer().getItemData().toDBString(), TimeU.getStr()};
		boolean ret = DBTransact.update(vps[queryIndex], sql, objList);

		String key = DBEvent.genKey(DBEvent.ITEM_UPDATE, playerNode.getPlayerId());
		SD.memCache.executeAsyncSet(key, JsonU.getJsonStr(playerNode.getInfoPlayer().getItemData()));

		return ret;
	}

	// 查询
	public static void selectPrivate(int queryIndex, Connection con, PreparedStatement[] vps, CachedRowSet[] vrs, PlayerNode playerNode)
		throws SQLException
	{
		ItemData data = JsonU.getJavaObj(ItemData.class, SD.memCache.executeGet(DBEvent.genKey(DBEvent.ITEM_UPDATE, playerNode.getPlayerId())));
		if (data != null)
		{
			playerNode.getInfoPlayer().setItemData(data);
			return;
		}

		String sql = "select * from item where player_id=?";
		vps[queryIndex] = con.prepareStatement(sql);
		DBTransact.closeRowSet(vrs, queryIndex);
		vrs[queryIndex] = DBTransact.query(vps[queryIndex], sql, new Object[] {playerNode.getPlayerId()});

		if (vrs[queryIndex] != null)
		{

			CachedRowSet rs = vrs[queryIndex];
			while (rs.next())
			{
				data = playerNode.getInfoPlayer().getItemData();
				String items_info = rs.getString("items_info");
				if (!Empty.is(items_info))
				{
					data.fromDBString(items_info);
				}
			}
		}
	}
}
