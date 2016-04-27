package nicehu.server.proxyserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.AreaData;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgGame.AuthTokenRes;

public class ProxyBaseHandler
{
	private static Logger logger = LoggerFactory.getLogger(ProxyBaseHandler.class);

	public static void handleSocket(ChannelHandlerContext ctx, ConnectNode connectNode, Message msg)
	{
		if (connectNode != null && connectNode.isServer())
		{
			// forward to client
			if (msg.id > 1000)
			{
				// 转发给给客户端的协议
				ProxySession session = PSD.sessions.get(msg.getPlayerId());
				if (session == null)
				{
					logger.error("Receive Forward Req,But the playerId do not have Available Session, PlayerId: " + msg.playerId);
					return;
				}
				if (msg.id == EGMI.EGMI_AUTH_TOKEN_RES_VALUE)
				{
					AuthTokenRes res = (AuthTokenRes)msg.getPb(AuthTokenRes.getDefaultInstance());
					if (res.getResult() == EGEC.EGEC_CORE_SUCCESS_VALUE)
					{
						SD.transmitter.addClientNode(msg.playerId, session.getCtx());
					}
				}

				SD.transmitter.send(session.getCtx(), msg);
				return;

			}
			// server inner protocal
			else
			{
				// 服务器内部协议
				SD.handlerMgr.handle(connectNode, msg);
				return;
			}
		}
		//client,already authtoken
		else if (connectNode != null && !connectNode.isServer())
		{
			// 转发给gameServer
			SD.transmitter.sendToServer(ServerType.GAME, AreaData.getAreaId(), msg);
			logger.info("Forward To GameServer for PlayerId:" + msg.getPlayerId());
			return;
		}
		//client ,not authtoken
		else
		{
			if (msg.id == EGMI.EGMI_AUTH_TOKEN_REQ_VALUE)
			{
				ProxySession session = new ProxySession(msg.getPlayerId(), ctx);
				PSD.sessions.put(msg.getPlayerId(), session);

				// forwad to game
				SD.transmitter.sendToServer(ServerType.GAME, AreaData.getAreaId(), msg);
				logger.info("Forward To GameServer for PlayerId:" + msg.getPlayerId());
				return;
			}
			logger.error("as have not authtoken,cannot handler msgId: " + msg.id);
		}

	}

}
