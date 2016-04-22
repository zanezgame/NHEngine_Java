package nicehu.nhsdk.core.handler.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.compress.SnappyCompress;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ClientNode;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.manageserver.core.data.MSD;

public class NetworkHandler extends ChannelDuplexHandler
{

	private static final Logger logger = LoggerFactory.getLogger(NetworkHandler.class);

	public static final short NETWORK_MSG_DATA = 1;
	public static final short NETWORK_MSG_EXCHANGE_INFO = 2;
	public static final short NETWORK_MSG_DATA_LZO = 3;

	public ServerNode serverNode;
	public ClientNode clientNode;
	public static long appSendSize = 0;
	public static long appReceiveSize = 0;

	@Override
	public void channelActive(ChannelHandlerContext ctx)
		throws Exception
	{
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx)
		throws Exception
	{
		if (serverNode != null && serverNode.getCtx().channel() == ctx.channel())
		{
			int serverId = serverNode.getServerId();
			SD.transmitter.removeServerNode(serverNode.getServerId());

			if (SD.serverType == ServerType.MANAGE)
			{
				logger.warn("connection Close RemoteServerID = {}, remoteAddr={} ", Integer.toHexString(serverId), ctx.channel().remoteAddress());
				ServerInfo serverInfo = MSD.serverInfoMgr.getServer(serverId);
				if (serverInfo != null)
				{
					serverInfo.setStatus(ServerInfo.SERVER_STATUS_CLOSED);
				}
				else
				{
					logger.error("connection Close serverInfo is null");
				}
				MSD.serverInfoMgr.sendThisServerToCareServers(serverInfo);
			}
			else
			{
				logger.warn("localAddr={} disconnected remoteAddr={}, remoteServerID = {}",
					ctx.channel().localAddress(),
					ctx.channel().remoteAddress(),
					Integer.toHexString(serverId));
			}
		}
		else if (clientNode != null)
		{
			// clientNode.getChannel().close().addListener(ChannelFutureListener.CLOSE);// 感觉这个close毫无疑义...
			logger.warn("localAddr={} disconnected remoteAddr={}, remoteServerID = {}",
				ctx.channel().localAddress(),
				ctx.channel().remoteAddress(),
				Integer.toHexString(0));
		}
		else
		{
			ctx.fireChannelInactive();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
		throws Exception
	{
		ByteBuf buffer = (ByteBuf)msg;
		NetworkHandler.appReceiveSize += buffer.readableBytes();

		short messageType = buffer.readShort();
		int length = buffer.readInt();
		if (messageType == NETWORK_MSG_DATA)
		{
			ctx.fireChannelRead(buffer);
		}
		else if (messageType == NETWORK_MSG_DATA_LZO)
		{
			ByteBuf uncompressBuffer = SnappyCompress.unCompress(buffer);
			if (uncompressBuffer == Unpooled.EMPTY_BUFFER)
			{
				if (serverNode != null)
				{
					throw new UnsupportedOperationException("Not supported empty uncompress buffer. remote serverID:" + serverNode.getServerId());
				}
				else
				{
					throw new UnsupportedOperationException("Not supported empty uncompress buffer.");
				}
			}
			ctx.fireChannelRead(uncompressBuffer);
		}
		else if (messageType == NETWORK_MSG_EXCHANGE_INFO)
		{
			ctx.fireChannelRead(buffer);

		}
		else
		{
			throw new UnsupportedOperationException("Not supported message type yet.");
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
		throws Exception
	{
		ByteBuf body = (ByteBuf)msg;
		short msgType = NETWORK_MSG_DATA;

		// logger.debug("before compress body size {}", body.readableBytes());
		if (clientNode != null && body.readableBytes() > 10)
		{
			ByteBuf compressBody = SnappyCompress.compress(body);
			if (compressBody != Unpooled.EMPTY_BUFFER)
			{
				body = compressBody;
				msgType = NETWORK_MSG_DATA_LZO;
			}
		}
		else if (serverNode != null && body.readableBytes() > 2048)
		{
			ByteBuf compressBody = SnappyCompress.compress(body);
			if (compressBody != Unpooled.EMPTY_BUFFER)
			{
				body = compressBody;
				msgType = NETWORK_MSG_DATA_LZO;
			}
		}
		ByteBuf header = ctx.alloc().buffer(6);
		header.writeShort(msgType);// type
		header.writeInt(body.readableBytes());// length
		ctx.writeAndFlush(Unpooled.wrappedBuffer(header, body));

		NetworkHandler.appSendSize += header.readableBytes() + body.readableBytes();
		logger.debug("after compress body size {}", body.readableBytes());
	}

}
