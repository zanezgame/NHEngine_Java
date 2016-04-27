package nicehu.nhsdk.core.handler;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.server.gameserver.core.GameSession;

public abstract class LogicHandler
{
	public void handle(ChannelHandlerContext ctx, Message msg)
	{
	}

	public void handle(ConnectNode serverNode, Message msg)
	{
	}


	public void handle(GameSession session, Message msg)
	{
	}

}
