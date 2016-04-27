package nicehu.nhsdk.core.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.network.NetworkHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHMsgBase.BaseMsg;

public class BaseInboundHandler extends ChannelDuplexHandler
{
	private static final Logger logger = LoggerFactory.getLogger(BaseInboundHandler.class);

	public ConnectNode connectNode;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
		throws Exception
	{
		ByteBuf buffer = (ByteBuf)msg;
		buffer.markReaderIndex();

		buffer.readerIndex(0);
		short messageType = buffer.readShort();
		int length = buffer.readInt();
		if (messageType == NetworkHandler.MSG_TYPE_REGISTER)
		{
			if (length != 4)
			{
				return;
			}
			int remoteServerId = buffer.readInt();
			logger.debug("receive MSG_TYPE_REGISTER ,remote ServerId:　" + Integer.toHexString(remoteServerId));
			SD.transmitter.addServerNode(remoteServerId, ctx);

			if (SD.serverType == ServerType.MANAGE)
			{
				logger.warn("connection Open localAddr={} connected remoteAddr={}， remoteServerID = {}",
					ctx.channel().localAddress(),
					ctx.channel().remoteAddress(),
					Integer.toHexString(remoteServerId));
			}
			else
			{
				logger.warn("Connect  RemoteServer Success , remoteServerId = {} remoteAddr={},", Integer.toHexString(remoteServerId), ctx.channel().remoteAddress());
				logger.debug("!!!One Connect Event OVER!!!");

				if (ServerType.getType(remoteServerId) == ServerType.MANAGE)
				{
					if (SD.mainAfter != null)
					{
						logger.debug("Send ServerLoginReq .....");
						SD.mainAfter.serverLogin(ctx, SD.serverType, SD.serverName);
					}
				}
			}
			return;
		}
		buffer.resetReaderIndex();

		Message message = decodeProtoBuf((ByteBuf)msg);
		if (message == null)
		{
			return;
		}
		HandlerExecutor.start(new HandlerExecutor(ctx, message, connectNode));
	}

	private Message decodeProtoBuf(ByteBuf buf)
		throws Exception
	{
		if (buf.readableBytes() < 4)
		{
			return null;
		}

		BaseMsg.Builder baseMsg = BaseMsg.newBuilder();
		baseMsg.mergeFrom(new ByteBufInputStream(buf)).build();

		int id = baseMsg.getId();
		int playerId = baseMsg.getPlayerId();
		Message message = new Message(id, playerId, baseMsg.build());

		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		// cause.printStackTrace();
		ctx.close();
	}

}
