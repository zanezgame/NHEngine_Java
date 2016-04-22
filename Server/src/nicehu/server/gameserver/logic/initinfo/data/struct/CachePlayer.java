package nicehu.server.gameserver.logic.initinfo.data.struct;

import nicehu.server.gameserver.logic.dungeon.data.DungeonData;

public class CachePlayer
{
	private int playerId;
	private DungeonData dungeonData = new DungeonData();

	public CachePlayer()
	{
	}

	public CachePlayer(int playerId)
	{
		super();
		this.playerId = playerId;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public void setDungeonData(DungeonData dungeonData)
	{
		this.dungeonData = dungeonData;
	}

	public DungeonData getDungeonData()
	{
		return dungeonData;
	}

}
