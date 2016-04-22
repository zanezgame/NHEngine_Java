package nicehu.server.gameserver.logic.initinfo.util;

import nicehu.server.gameserver.logic.initinfo.data.struct.CachePlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.GamePlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.InfoPlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;

public class PlayerNodeGen
{
	public static PlayerNode genNewPlayerFromConfig(int playerId)
	{
		PlayerNode playerNode = new PlayerNode(playerId);
		GamePlayer gamePlayer = new GamePlayer(playerId);
		CachePlayer cachePlayer = new CachePlayer(playerId);
		InfoPlayer infoPlayer = new InfoPlayer(playerId);
		playerNode.setGamePlayer(gamePlayer);
		playerNode.setCachePlayer(cachePlayer);
		playerNode.setInfoPlayer(infoPlayer);

		//gamePlayer.setShowId(ServerUtil.getShowId(playerId));
		gamePlayer.setMoney(1000);
		long now = System.currentTimeMillis();
		gamePlayer.setLoginTime(now);

		
		infoPlayer.getItemData().addItemCount(1,10);
		infoPlayer.getItemData().addItemCount(2,10);
		infoPlayer.getItemData().addItemCount(3,10);
		return playerNode;
	}

}
