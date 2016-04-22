package nicehu.server.gameserver.logic.initinfo.data.struct;

import nicehu.server.gameserver.logic.item.data.ItemData;

public class InfoPlayer
{
	private int playerId;
	private ItemData itemData = new ItemData();

	public InfoPlayer()
	{
	}

	public InfoPlayer(int playerId)
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

	public ItemData getItemData()
	{
		return itemData;
	}

	public void setItemData(ItemData itemData)
	{
		this.itemData = itemData;
	}

	

}