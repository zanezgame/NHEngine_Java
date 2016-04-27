package nicehu.nhsdk.core.datatransmitter;

import java.nio.charset.Charset;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.base.BaseInboundHandler;
import nicehu.nhsdk.core.handler.network.NetworkHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.manageserver.config.serverconfig.ServerConfigMgr;
import nicehu.server.proxyserver.core.PSD;
import nicehu.server.proxyserver.core.ProxySession;

public class Transmitter
{
	private static final Logger logger = LoggerFactory.getLogger(Transmitter.class);

	// all
	private ConcurrentHashMap<Integer, ConnectNode> id_ServerNode = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, Vector<ConnectNode>> type_ServerNodes = new ConcurrentHashMap<>();
	// except gameServer
	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConnectNode>> area_type_ServerNodes = new ConcurrentHashMap<>();

	public ChannelFuture send(ChannelHandlerContext ctx, Message msg)
	{
		return ctx.writeAndFlush(msg);
	}
	
	public ChannelFuture sendToPlayer(int playerId, Message msg)
	{
		ProxySession session = PSD.sessions.get(playerId);
		if(session != null)
		{
			return session.getCtx().writeAndFlush(msg);
		}
		return null;
		
	}

	public void sendAndClose(ChannelHandlerContext ctx, Message msg)
	{
		ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
	}

	public void sendToServer(int serverId, Message msg)
	{
		ConnectNode serverNode = id_ServerNode.get(serverId);
		if (serverNode != null)
		{
			send(serverNode.getCtx(), msg);
		}
	}

	public boolean sendToServer(int serverType, int areaId, Message msg)
	{
		ConcurrentHashMap<Integer, ConnectNode> type_ServerNode = this.area_type_ServerNodes.get(areaId);
		if (type_ServerNode != null)
		{
			ConnectNode serverNode = type_ServerNode.get(serverType);
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
		Vector<ConnectNode> serverNodes = this.type_ServerNodes.get(serverType);
		if (serverNodes != null)
		{
			for (ConnectNode serverNode : serverNodes)
			{
				this.send(serverNode.getCtx(), msg);

			}
		}
	}

	// http
	public void sendAndClose(ChannelHandlerContext ctx, String str)
	{
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK);
		ByteBuf channelBuffer = Unpooled.copiedBuffer(str, Charset.forName("utf-8"));
		response.content().writeBytes(channelBuffer);

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

	}

	public ConnectNode addServerNode(int serverId, ChannelHandlerContext ctx)
	{
		int serverType = ServerType.getType(serverId);
		ServerConfig serverConfig = ServerConfigMgr.instance.getServerConfig(serverId);
		int areaId = serverConfig.getAreaId();
		ConnectNode serverNode = new ConnectNode(serverId, ctx, true);

		{
			id_ServerNode.put(serverId, serverNode);
			Vector<ConnectNode> serverNodes = this.type_ServerNodes.get(serverType);
			if (serverNodes == null)
			{
				serverNodes = new Vector<ConnectNode>();
				this.type_ServerNodes.put(serverType, serverNodes);
			}
			serverNodes.add(serverNode);
		}
		if (serverType == ServerType.PROXY || serverType == ServerType.GAME || serverType == ServerType.WORLD)
		{
			ConcurrentHashMap<Integer, ConnectNode> type_ServerNode = this.area_type_ServerNodes.get(areaId);
			if (type_ServerNode == null)
			{
				type_ServerNode = new ConcurrentHashMap<Integer, ConnectNode>();
				this.area_type_ServerNodes.put(areaId, type_ServerNode);
			}
			type_ServerNode.put(serverType, serverNode);
		}

		ChannelHandler handler = ctx.channel().pipeline().get(NetworkHandler.class);
		if (handler != null)
		{
			NetworkHandler networkHandler = (NetworkHandler)handler;
			networkHandler.connectNode = serverNode;
		}

		handler = ctx.channel().pipeline().get(BaseInboundHandler.class);
		if (handler != null)
		{
			BaseInboundHandler messageBaseHandler = (BaseInboundHandler)handler;
			messageBaseHandler.connectNode = serverNode;
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
		Vector<ConnectNode> serverNodes = this.type_ServerNodes.get(serverType);
		if (serverNodes != null)
		{
			ConnectNode serverNode = null;
			for (ConnectNode tmp : serverNodes)
			{
				if (tmp.getId() == serverId)
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

		if (serverType == ServerType.PROXY || serverType == ServerType.GAME || serverType == ServerType.WORLD)
		{
			ConcurrentHashMap<Integer, ConnectNode> type_ServerNode = this.area_type_ServerNodes.get(areaId);
			if (type_ServerNode != null)
			{
				type_ServerNode.remove(serverType);
			}
		}

	}

	public void addClientNode(int playerId, ChannelHandlerContext ctx)
	{
		ConnectNode connectNode = new ConnectNode(playerId, ctx, false);
		ChannelHandler handler = ctx.channel().pipeline().get(NetworkHandler.class);
		if (handler != null)
		{
			NetworkHandler networkHandler = (NetworkHandler)handler;
			networkHandler.connectNode = connectNode;
		}

		handler = ctx.channel().pipeline().get(BaseInboundHandler.class);
		if (handler != null)
		{
			BaseInboundHandler messageBaseHandler = (BaseInboundHandler)handler;
			messageBaseHandler.connectNode = connectNode;
		}

	}

	public ConnectNode getServerNode(int serverType, int areaId)
	{
		ConcurrentHashMap<Integer, ConnectNode> type_ServerNode = this.area_type_ServerNodes.get(areaId);
		if (type_ServerNode != null)
		{
			ConnectNode serverNode = type_ServerNode.get(serverType);
			if (serverNode != null)
			{
				return serverNode;
			}
		}
		return null;
	}

}
