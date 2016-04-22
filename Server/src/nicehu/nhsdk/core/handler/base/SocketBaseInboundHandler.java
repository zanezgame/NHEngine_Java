package nicehu.nhsdk.core.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ClientNode;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.handler.network.NetworkHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHMsgBase.BaseMsg;

public class SocketBaseInboundHandler extends ChannelDuplexHandler
{
	private static final Logger logger = LoggerFactory.getLogger(SocketBaseInboundHandler.class);

	public ServerNode serverNode;
	public ClientNode clientNode;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
		throws Exception
	{
		ByteBuf buffer = (ByteBuf)msg;
		buffer.markReaderIndex();

		buffer.readerIndex(0);
		short messageType = buffer.readShort();
		int length = buffer.readInt();
		if (messageType == NetworkHandler.NETWORK_MSG_EXCHANGE_INFO)
		{
			if (length != 4)
			{
				return;
			}
			int remoteServerId = buffer.readInt();
			logger.debug("receive NetWork_Msg_Exchange_Info ,remote ServerId:　" + Integer.toHexString(remoteServerId));
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
					if (SD.serverInitHandler != null)
					{
						logger.debug("Send ServerLoginReq .....");
						SD.serverInitHandler.serverLogin(ctx, SD.serverType, SD.serverName);
					}
				}
			}
			return;
		}
		buffer.resetReaderIndex();

		Message message = decodeProtoBuf1((ByteBuf)msg);
		if (message == null)
		{
			return;
		}
		SocketHandlerExecutor.start(new SocketHandlerExecutor(ctx, message, serverNode, clientNode));
	}

	private Message decodeProtoBuf(ByteBuf buf)
		throws Exception
	{
		if (buf.readableBytes() < 4)
		{
			return null;
		}
		int id = buf.readInt();
		int playerId = buf.readInt();
		Message message = new Message(id, playerId);

		MessageLite messageLite = SD.sController.getProtobuf(id);
		if (messageLite == null)
		{
			messageLite = SD.hController.getProtobuf(id);
		}
		if (messageLite == null)
		{
			logger.error("!!!!!! un support id,Can Not DecodeProtoBuf,Id: " + id);
			return message;
		}

		if (buf.hasArray())
		{
			final int offset = buf.readerIndex();
			message.setProtoBuf(messageLite.newBuilderForType().mergeFrom(buf.array(), buf.arrayOffset() + offset, buf.readableBytes()).build());

		}
		else
		{
			message.setProtoBuf(messageLite.newBuilderForType().mergeFrom(new ByteBufInputStream(buf)).build());

		}

		return message;
	}

	private Message decodeProtoBuf1(ByteBuf buf)
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
		Message message = new Message(id, playerId);

		MessageLite messageLite = SD.sController.getProtobuf(id);
		if (messageLite == null)
		{
			messageLite = SD.hController.getProtobuf(id);
		}
		if (messageLite == null)
		{
			logger.error("!!!!!! un support id,Can Not DecodeProtoBuf,Id: " + id);
			return message;
		}

		message.setProtoBuf(messageLite.newBuilderForType().mergeFrom(baseMsg.getMsgData()).build());

		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		//cause.printStackTrace();
		ctx.close();
	}

}
