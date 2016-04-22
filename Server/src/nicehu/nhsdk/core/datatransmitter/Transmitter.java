package nicehu.nhsdk.core.datatransmitter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.Charset;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.core.datatransmitter.data.ClientNode;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.handler.base.SocketBaseInboundHandler;
import nicehu.nhsdk.core.handler.network.NetworkHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.manageserver.config.serverconfig.ServerConfigMgr;

public class Transmitter
{
	private static final Logger logger = LoggerFactory.getLogger(Transmitter.class);

	// all
	private ConcurrentHashMap<Integer, ServerNode> id_ServerNode = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, Vector<ServerNode>> type_ServerNodes = new ConcurrentHashMap<>();
	// except gameServer
	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ServerNode>> area_type_ServerNodes = new ConcurrentHashMap<>();
	
	public ChannelFuture send(ChannelHandlerContext ctx, Message msg)
	{
		return ctx.writeAndFlush(msg);
	}

	public void sendAndClose(ChannelHandlerContext ctx, String str)
	{
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK);
		ByteBuf channelBuffer = Unpooled.copiedBuffer(str, Charset.forName("utf-8"));
		response.content().writeBytes(channelBuffer);

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

	}

	public void sendAndClose(ChannelHandlerContext ctx, Message msg)
	{
		ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
	}

	public void sendToServer(int serverId, Message msg)
	{
		ServerNode serverNode = id_ServerNode.get(serverId);
		if (serverNode != null)
		{
			send(serverNode.getCtx(), msg);
		}
	}

	public boolean sendToServer(int serverType, int areaId, Message msg)
	{
		ConcurrentHashMap<Integer, ServerNode> type_ServerNode = this.area_type_ServerNodes.get(areaId);
		if (type_ServerNode != null)
		{
			ServerNode serverNode = type_ServerNode.get(serverType);
			if (serverNode != null)
			{
				this.send(serverNode.getCtx(), msg);
				return true;
			}
		}
		return false;
	}

	public void sendToServers(int serverType, Message msg)
	{
		Vector<ServerNode> serverNodes = this.type_ServerNodes.get(serverType);
		if (serverNodes != null)
		{
			for (ServerNode serverNode : serverNodes)
			{
				this.send(serverNode.getCtx(), msg);

			}
		}
	}

	public boolean closeConnection(int playerId)
	{
		// GameSession clientSession = GSD.sessions.get(playerId);
		// if (clientSession == null)
		// {
		// return false;
		// }
		// clientSession.getChannel().close();
		return true;
	}

	public ServerNode addServerNode(int serverId, ChannelHandlerContext ctx)
	{
		int serverType = ServerType.getType(serverId);
		ServerConfig serverConfig = ServerConfigMgr.instance.getServerConfig(serverId);
		int areaId = serverConfig.getAreaId();
		int index = ParseU.pInt(serverConfig.getAttr("index"));
		ServerNode serverNode = new ServerNode(ctx, serverId);

		{
			id_ServerNode.put(serverId, serverNode);
			Vector<ServerNode> serverNodes = this.type_ServerNodes.get(serverType);
			if (serverNodes == null)
			{
				serverNodes = new Vector<ServerNode>();
				this.type_ServerNodes.put(serverType, serverNodes);
			}
			serverNodes.add(serverNode);
		}
		if (serverType == ServerType.GATE || serverType == ServerType.GAME || serverType == ServerType.CENTER)
		{
			ConcurrentHashMap<Integer, ServerNode> type_ServerNode = this.area_type_ServerNodes.get(areaId);
			if (type_ServerNode == null)
			{
				type_ServerNode = new ConcurrentHashMap<Integer, ServerNode>();
				this.area_type_ServerNodes.put(areaId, type_ServerNode);
			}
			type_ServerNode.put(serverType, serverNode);
		}

		ChannelHandler handler = ctx.channel().pipeline().get(NetworkHandler.class);
		if (handler != null)
		{
			NetworkHandler networkHandler = (NetworkHandler)handler;
			networkHandler.serverNode = serverNode;
		}

		handler = ctx.channel().pipeline().get(SocketBaseInboundHandler.class);
		if (handler != null)
		{
			SocketBaseInboundHandler messageBaseHandler = (SocketBaseInboundHandler)handler;
			messageBaseHandler.serverNode = serverNode;
		}

		return serverNode;
	}

	public void removeServerNode(int serverId)
	{

		int serverType = ServerType.getType(serverId);
		ServerConfig serverConfig = ServerConfigMgr.instance.getServerConfig(serverId);
		int areaId = serverConfig.getAreaId();
		int index = ParseU.pInt(serverConfig.getAttr("index"));

		id_ServerNode.remove(serverId);
		Vector<ServerNode> serverNodes = this.type_ServerNodes.get(serverType);
		if (serverNodes != null)
		{
			ServerNode serverNode = null;
			for (ServerNode tmp : serverNodes)
			{
				if (tmp.getServerId() == serverId)
				{
					serverNode = tmp;
					break;
				}
			}
			if (serverNode != null)
			{
				serverNodes.remove(serverNode);
			}
		}

		if (serverType == ServerType.GATE || serverType == ServerType.GAME || serverType == ServerType.CENTER)
		{
			ConcurrentHashMap<Integer, ServerNode> type_ServerNode = this.area_type_ServerNodes.get(areaId);
			if (type_ServerNode != null)
			{
				type_ServerNode.remove(serverType);
			}
		}

	}

	// just for socket client  //TODO this is not use,so never use compress data for client
	public void addClientNode(int playerId, ChannelHandlerContext ctx)
	{
		ClientNode clientNode = new ClientNode(playerId);
		ChannelHandler handler = ctx.channel().pipeline().get(NetworkHandler.class);
		if (handler != null)
		{
			NetworkHandler networkHandler = (NetworkHandler)handler;
			networkHandler.clientNode = clientNode;
		}

		handler = ctx.channel().pipeline().get(SocketBaseInboundHandler.class);
		if (handler != null)
		{
			SocketBaseInboundHandler messageBaseHandler = (SocketBaseInboundHandler)handler;
			messageBaseHandler.clientNode = clientNode;
		}

	}

	public ServerNode getServerNode(int serverType, int areaId)
	{
		ConcurrentHashMap<Integer, ServerNode> type_ServerNode = this.area_type_ServerNodes.get(areaId);
		if (type_ServerNode != null)
		{
			ServerNode serverNode = type_ServerNode.get(serverType);
			if (serverNode != null)
			{
				return serverNode;
			}
		}
		return null;
	}

}
