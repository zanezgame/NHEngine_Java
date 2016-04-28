package nicehu.server.gameserver.logic.authtoken.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.nhsdk.util.EnumUtil.ConnectStatus;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgGame.AuthTokenReq;
import nicehu.pb.NHMsgGame.AuthTokenRes;
import nicehu.pb.NHMsgServer.QueryTokenReq;
import nicehu.server.common.dblog.LogUtil;
import nicehu.server.gameserver.core.GSD;
import nicehu.server.gameserver.core.GameSession;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.mgr.PM;

public class AuthTokenReqHandler extends LogicHandler
{

	private static Logger logger = LoggerFactory.getLogger(AuthTokenReqHandler.class);

	@Override
	public void handle(ChannelHandlerContext ctx, Message msg)
	{
		AuthTokenReq request = (AuthTokenReq)msg.getPb(AuthTokenReq.getDefaultInstance());
		logger.info("recv AuthTokenReqHandler, playerId = {}", msg.getPlayerId());

		AuthTokenRes.Builder builder = AuthTokenRes.newBuilder();
		msg.setId(EGMI.EGMI_AUTH_TOKEN_RES_VALUE);
		int result = EGEC.EGEC_CORE_SUCCESS_VALUE;
		int playerId = msg.playerId;

		String clientToken = request.getToken();// .getString("token");
		String serverToken = null;
		GameSession session = GSD.sessions.get(playerId);

		PlayerNode playerNode = null;
		try
		{
			do
			{
				if (session != null && session.getStatus() == ConnectStatus.Authed)
				{
					serverToken = session.getToken();
				}
				if (serverToken != null && serverToken.equals(clientToken))
				{
					logger.info("Token aleady exist in GameServer");
					session.setUpdateTime(System.currentTimeMillis());
					playerNode = PM.getPlayerNode(playerId, true);
					LogUtil.authToken(playerNode, ctx.channel().remoteAddress().toString());
					session.setStatus(ConnectStatus.Authed);

					// 转发结果给GATE
					builder.setResult(result);
					msg.genBaseMsg(builder.build());
					SD.transmitter.send(ctx, msg);
					return;
				}
				else
				{

					logger.info("QueryToken to authServer");
					GameSession newSession = new GameSession(playerId, clientToken);
					GSD.sessions.put(newSession.getPlayerId(), newSession);
					QueryTokenReq.Builder queryTokenbuilder = QueryTokenReq.newBuilder();
					queryTokenbuilder.setPlayerId(playerId);
					queryTokenbuilder.setAreaId(SD.areaId);
					queryTokenbuilder.setToken(clientToken);
					// queryTokenbuilder.setSeqId(newSession.getSessionSeqId());

					msg.setId(EGMI.EGMI_SERVER_QUERY_TOKEN_REQ_VALUE);
					msg.genBaseMsg(queryTokenbuilder.build());
					// 将token发给authServe进行验证
					SD.transmitter.sendToServers(ServerType.AUTH, msg);

				}

			} while (false);
		}
		finally
		{
		}
	}
}