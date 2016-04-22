package nicehu.nhsdk.core.data.session;

import io.netty.channel.Channel;

public class CenterSession
{
	private int playerId;
	private long updateTime;

	public CenterSession(int playerId, Channel channel)
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
