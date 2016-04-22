package nicehu.server.authserver.core;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;

public class AuthBaseHandler
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

	public static void handleHttp(ChannelHandlerContext ctx, Message msg)
	{
		SD.hController.procProto(ctx, msg);
	}
}
