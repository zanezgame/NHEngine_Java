package nicehu.server.gameserver.logic.dungeon.data;

import nicehu.server.common.dblog.LogEvent;
import nicehu.server.gameserver.logic.dungeon.db.DungeonDB;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.mgr.PAM;

public class DungeonMgr
{
	public static void updateDB(PlayerNode playerNode)
	{
		DungeonData dungeonData = playerNode.getCachePlayer().getDungeonData();
		int dungeonMaxId = dungeonData.genDungeonMaxId();
		int stars = dungeonData.genDungeonStars();
		PAM.updateDungeonMaxId(playerNode, dungeonMaxId, LogEvent.DataSync);
		PAM.updateDungeonStars(playerNode, stars, LogEvent.DataSync);
		DungeonDB.update(playerNode);
	}
}
