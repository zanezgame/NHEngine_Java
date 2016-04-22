package nicehu.nhsdk.core.data.session;

import io.netty.channel.ChannelHandlerContext;

public class GateSession
{
	private int playerId;
	private ChannelHandlerContext ctx;
	private long updateTime;

	public GateSession(int playerId, ChannelHandlerContext ctx)
	{
		this.playerId = playerId;
		this.ctx = ctx;
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

	public ChannelHandlerContext getCtx()
	{
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx)
	{
		this.ctx = ctx;
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
