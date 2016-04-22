package nicehu.server.proxyserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.AreaData;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.session.GateSession;
import nicehu.nhsdk.core.datatransmitter.data.ClientNode;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGMI;
import nicehu.server.proxyserver.core.data.GTSD;

public class ProxyBaseHandler
{
	private static Logger logger = LoggerFactory.getLogger(ProxyBaseHandler.class);

	public static void handleSocket(ChannelHandlerContext ctx, ServerNode serverNode, ClientNode clientNode, Message msg)
	{
		// GateServer放弃使用ClientNode
		// if (clientNode != null)
		// {
		// // 转发给gameServer
		// SD.transmitter.sendToGameServer(AreaData.getAreaId(), msg.getPlayerId(), msg);
		// logger.info("Forward To GameServer for PlayerId:" + msg.getPlayerId());
		// return;
		// }
		if (serverNode != null)
		{
			//if (ProtoType.getType(msg.id) == ProtoType.GAME)
			//TODO
			if (msg.id>1000)
			{
				// 转发给给客户端的协议
				GateSession session = GTSD.sessions.get(msg.getPlayerId());
				if (session == null)
				{
					logger.error("Receive Forward Req,But the playerId do not have Available Session, PlayerId: " + msg.playerId);
					return;
				}
				SD.transmitter.send(session.getCtx(), msg);

				logger.info("Forward To PlayerId: " + msg.getPlayerId() + " for GameServer, pId: " + msg.id);
				logger.info("This Forward Total Cost Time: " + (System.currentTimeMillis() - session.getUpdateTime()) + " PlayerId:" + msg.playerId + " pId: " + msg.id);

				return;

			}
			else
			{
				// 服务器内部协议
				SD.sController.procProto(serverNode, msg);
				return;
			}
		}
		else
		{
			//TODO GateServer对各种连接状态的处理  GameServer对各种连接状态的处理
			if (msg.id == EGMI.EGMI_AUTH_TOKEN_REQ_VALUE)
			{
				// 保存channel信息
				GateSession session = new GateSession(msg.getPlayerId(), ctx);
				GTSD.sessions.put(msg.getPlayerId(), session);
				// 转发给GameServer
				SD.transmitter.sendToServer(ServerType.GAME,AreaData.getAreaId(), msg);
				logger.info("Forward To GameServer for PlayerId:" + msg.getPlayerId());
				return;
			}
			else
			{
				// 转发给gameServer
				SD.transmitter.sendToServer(ServerType.GAME,AreaData.getAreaId(), msg);
				logger.info("Forward To GameServer for PlayerId:" + msg.getPlayerId());
				return;
			}
		}

	}

	public static void handleHttp(ChannelHandlerContext ctx, Message msg, JSONObject result)
	{
		logger.error("GateServer Do not Support Http");
	}
}
