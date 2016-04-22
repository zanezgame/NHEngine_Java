package nicehu.server.gameserver.logic.mgr;

import nicehu.pb.NHDefine.EIdSpecial;
import nicehu.server.common.dblog.LogUtil;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.initinfo.db.PlayerDB;

public class PAM
{

	public static void updateLoginTime(PlayerNode playerNode)
	{
		long now = System.currentTimeMillis();
		playerNode.getGamePlayer().setLoginTime(now);
		PlayerDB.update(playerNode);
	}

	public static boolean addMoney(PlayerNode playerNode, int change, int event)
	{
		if (playerNode.getGamePlayer().testAddMoney(change))
		{
			long before = playerNode.getGamePlayer().getMoney();
			playerNode.getGamePlayer().addMoney(change);
			long after = playerNode.getGamePlayer().getMoney();
			PlayerDB.update(playerNode);
			LogUtil.special(playerNode, event, EIdSpecial.EIS_Money_VALUE, after, before, change);
			return true;
		}
		return false;
	}

	public static boolean updateDungeonMaxId(PlayerNode playerNode, int dungeonMaxId, int event)
	{
		if (dungeonMaxId > playerNode.getGamePlayer().getDungeonMaxId())
		{
			playerNode.getGamePlayer().setDungeonMaxId(dungeonMaxId);
			PlayerDB.update(playerNode);
			return true;
		}
		return false;

	}

	public static boolean updateDungeonStars(PlayerNode playerNode, int dungeonStars, int event)
	{
		playerNode.getGamePlayer().setDungeonStars(dungeonStars);
		PlayerDB.update(playerNode);
		return true;
	}

}
