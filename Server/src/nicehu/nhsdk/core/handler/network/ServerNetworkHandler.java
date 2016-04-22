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
		if (SD.serverId > 0)
		{
			logger.debug("!!!One Connetion Event BEGIN!!!");

			ByteBuf exchangeInfoMsg = ctx.alloc().buffer(10);
			exchangeInfoMsg.writeShort(NETWORK_MSG_EXCHANGE_INFO);
			exchangeInfoMsg.writeInt(4);
			exchangeInfoMsg.writeInt(SD.serverId);
			ctx.writeAndFlush(exchangeInfoMsg);

			logger.debug("localServerNameId :" + SD.getServerNameId() + " conneted one remoteNode success ,has send back Network_Msg_Exchange_info");
		}
		else
		{
			logger.debug("localServerNameId :" + SD.getServerNameId() + " conneted one remoteNode success .not send and backinfo");
		}
		ctx.fireChannelActive();
	}
}
