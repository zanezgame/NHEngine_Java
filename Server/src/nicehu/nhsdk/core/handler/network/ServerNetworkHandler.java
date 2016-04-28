package nicehu.nhsdk.core.handler.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.core.data.SD;

public class ServerNetworkHandler extends NetworkHandler
{
	private static Logger logger = LoggerFactory.getLogger(ServerNetworkHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx)
		throws Exception
	{
		if (SD.isOpen)
		{
			logger.debug("!!!One Connetion Event BEGIN!!!");

			ByteBuf registerServerInfo = ctx.alloc().buffer(10);
			registerServerInfo.writeShort(MSG_TYPE_REGISTER);
			registerServerInfo.writeInt(4);
			registerServerInfo.writeInt(SD.serverId);
			ctx.writeAndFlush(registerServerInfo);

			logger.debug("localServerNameId :" + SD.getServerNameId() + " conneted one remoteNode success ,has send back MSG_TYPE_REGISTER");
		}
		else
		{
			logger.debug("localServerNameId :" + SD.getServerNameId() + " conneted one remoteNode success .not send and backinfo");
		}
		ctx.fireChannelActive();
	}
}
