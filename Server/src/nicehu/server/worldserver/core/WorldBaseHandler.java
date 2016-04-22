package nicehu.server.worldserver.core;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;

public class WorldBaseHandler
{
	public static void handleSocket(ChannelHandlerContext ctx, ServerNode serverNode, Message msg)
	{

		if (serverNode == null)
		{
			SD.sController.procProto(ctx, msg);
			return;
		}

		SD.sController.procProto(serverNode, msg);

	}
}
