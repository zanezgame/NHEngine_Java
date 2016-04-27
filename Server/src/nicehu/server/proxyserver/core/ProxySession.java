package nicehu.server.proxyserver.core;

import io.netty.channel.ChannelHandlerContext;

public class ProxySession
{
	private int playerId;
	private ChannelHandlerContext ctx;

	public ProxySession(int playerId, ChannelHandlerContext ctx)
	{
		this.playerId = playerId;
		this.ctx = ctx;
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

}
