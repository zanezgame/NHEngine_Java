package nicehu.nhsdk.core.datatransmitter.data;

import io.netty.channel.ChannelHandlerContext;

public class ServerNode
{
	public int serverId;
	public ChannelHandlerContext ctx;


	public ServerNode(ChannelHandlerContext ctx, int remoteServerID)
	{
		this.ctx = ctx;
		this.serverId = remoteServerID;
	}

	public int getServerId()
	{
		return serverId;
	}

	public void setServerId(int serverId)
	{
		this.serverId = serverId;
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
