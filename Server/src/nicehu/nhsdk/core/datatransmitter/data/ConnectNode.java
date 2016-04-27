package nicehu.nhsdk.core.datatransmitter.data;

import io.netty.channel.ChannelHandlerContext;

public class ConnectNode
{
	public int id;
	public ChannelHandlerContext ctx;
	public boolean isServer;

	public ConnectNode(int id, ChannelHandlerContext ctx, boolean isServer)
	{
		this.id = id;
		this.ctx = ctx;
		this.isServer = isServer;

	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public ChannelHandlerContext getCtx()
	{
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx)
	{
		this.ctx = ctx;
	}

	public boolean isServer()
	{
		return isServer;
	}

	public void setServer(boolean isServer)
	{
		this.isServer = isServer;
	}

}
