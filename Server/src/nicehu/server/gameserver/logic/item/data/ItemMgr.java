package nicehu.server.gameserver.logic.item.data;

import nicehu.server.common.dblog.LogUtil;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.item.db.ItemDB;

public class ItemMgr
{
	public static boolean addItemCount(PlayerNode playerNode, int id, int change, int event)
	{
		int before = playerNode.getInfoPlayer().getItemData().getItemCount(id);
		if (playerNode.getInfoPlayer().getItemData().addItemCount(id, change))
		{
			int after = playerNode.getInfoPlayer().getItemData().getItemCount(id);
			ItemDB.update(playerNode);
			LogUtil.item(playerNode, event, id, after, before, change);
			return true;
		}
		return false;
	}
}
