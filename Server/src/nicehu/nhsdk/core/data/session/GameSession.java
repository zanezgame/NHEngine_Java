package nicehu.nhsdk.core.data.session;

import io.netty.channel.Channel;

import nicehu.nhsdk.util.EnumUtil.ConnectStatus;

public class GameSession
{
	private int playerId;
	private String token;
	private long updateTime;
	private ConnectStatus status;

	public void updateTime()
	{
		this.updateTime = System.currentTimeMillis();
	}

	public GameSession(int playerId, String token)
	{
		this.playerId = playerId;
		this.token = token;
		this.updateTime = System.currentTimeMillis();
		this.status = ConnectStatus.Connected;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public long getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(long updateTime)
	{
		this.updateTime = updateTime;
	}

	public ConnectStatus getStatus()
	{
		return status;
	}

	public void setStatus(ConnectStatus status)
	{
		this.status = status;
	}

}
