package nicehu.server.worldserver.core;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;

public class WorldBaseHandler
{
	public static void handleSocket(ChannelHandlerContext ctx, ConnectNode serverNode, Message msg)
	{

		if (serverNode == null)
		{
			SD.handlerMgr.handle(ctx, msg);
			return;
		}
		SD.handlerMgr.handle(serverNode, msg);

	}
}
