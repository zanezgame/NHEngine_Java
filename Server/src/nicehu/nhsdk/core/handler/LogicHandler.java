package nicehu.nhsdk.core.handler;

import io.netty.channel.ChannelHandlerContext;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.session.GameSession;
import nicehu.nhsdk.core.datatransmitter.data.ClientNode;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;

public abstract class LogicHandler
{
	public void handle(ChannelHandlerContext ctx, Message msg)
	{
	}

	public void handle(ServerNode serverNode, Message msg)
	{
	}

	public void handle(ClientNode clientNode, Message msg)
	{
	}

	public void handle(GameSession session, Message msg)
	{
	}

}
