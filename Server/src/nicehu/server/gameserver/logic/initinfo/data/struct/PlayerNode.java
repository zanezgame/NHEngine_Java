package nicehu.server.gameserver.logic.initinfo.data.struct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.pb.NHMsgBase;
import nicehu.server.gameserver.logic.item.data.ItemData;

public class PlayerNode
{
	private static final Logger logger = LoggerFactory.getLogger(PlayerNode.class);

	private int playerId;
	private GamePlayer gamePlayer;
	private CachePlayer cachePlayer;
	private InfoPlayer infoPlayer;

	public boolean hasEmpty()
	{
		return (this.getGamePlayer() == null || this.getCachePlayer() == null || this.getInfoPlayer() == null);
	}
	
	public NHMsgBase.Player toProtobuf()
	{
		NHMsgBase.Player.Builder builder = NHMsgBase.Player.newBuilder();
		builder.setPlayerId(playerId);
		builder.setName(gamePlayer.getFixName());
		builder.setMoney(gamePlayer.getMoney());
		builder.setDiamond(gamePlayer.getDiamond());
		
		ItemData data = infoPlayer.getItemData();
		data.getItems().values().forEach(x->{
			NHMsgBase.Item.Builder itemBuilder = NHMsgBase.Item.newBuilder();
			itemBuilder.setId(x.getId());
			itemBuilder.setCount(x.getCount());
			builder.addItems(itemBuilder.build());
		});
		
		return builder.build();
	}

	public PlayerNode()
	{

	}

	public PlayerNode(int playerId)
	{
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

	public InfoPlayer getInfoPlayer()
	{
		return infoPlayer;
	}

	public void setInfoPlayer(InfoPlayer infoPlayer)
	{
		this.infoPlayer = infoPlayer;
	}

	public GamePlayer getGamePlayer()
	{
		return gamePlayer;
	}

	public void setGamePlayer(GamePlayer gamePlayer)
	{
		this.gamePlayer = gamePlayer;
	}

	public void setCachePlayer(CachePlayer cachePlayer)
	{
		this.cachePlayer = cachePlayer;
	}

	public CachePlayer getCachePlayer()
	{
		return cachePlayer;
	}

}
