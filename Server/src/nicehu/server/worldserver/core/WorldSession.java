package nicehu.server.worldserver.core;

import io.netty.channel.Channel;

public class WorldSession
{
	private int playerId;
	private long updateTime;

	public WorldSession(int playerId, Channel channel)
	{
		this.playerId = playerId;
		this.updateTime = System.currentTimeMillis();
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public long getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(long updateTime)
	{
		this.updateTime = updateTime;
	}

}
