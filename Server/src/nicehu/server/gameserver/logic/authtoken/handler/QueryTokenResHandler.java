package nicehu.server.gameserver.logic.authtoken.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.nhsdk.util.EnumUtil.ConnectStatus;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgGame.AuthTokenRes;
import nicehu.pb.NHMsgServer.QueryTokenRes;
import nicehu.server.common.dblog.LogUtil;
import nicehu.server.gameserver.core.GSD;
import nicehu.server.gameserver.core.GameSession;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.mgr.PM;

public class QueryTokenResHandler extends LogicHandler
{

	private static final Logger logger = LoggerFactory.getLogger(QueryTokenResHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{

		QueryTokenRes request = (QueryTokenRes)msg.getPb(QueryTokenRes.getDefaultInstance());
		logger.info("recv QueryTokenResHandler from authServer playerId:" + msg.playerId);
		
		AuthTokenRes.Builder builder = AuthTokenRes.newBuilder();
		msg.setId(EGMI.EGMI_AUTH_TOKEN_RES_VALUE);
		int result =EGEC.EGEC_CORE_SUCCESS_VALUE;
		int playerId = msg.playerId;
		

		GameSession session = GSD.sessions.get(playerId);
		if (session == null)
		{
			logger.debug("QueryToken session is null ,request.getPlayerId() = {}", request.getPlayerId());
			return;
		}

		try
		{
			do
			{
				if (request.getResult() == EGEC.EGEC_CORE_SUCCESS_VALUE)
				{
					if (session.getToken().equals(request.getToken()))
					{
						session.updateTime();
						GSD.tokens.put(playerId, request.getToken());
						PlayerNode playerNode = PM.getPlayerNode(playerId, true);
						session.setStatus(ConnectStatus.Authed);
						LogUtil.authToken(playerNode, "GameSession not  have Channle  anymore");
					}
					else
					{
						logger.warn("Token is wrong");
						// TextU.get("E_AUTH_CA_LOGIN_FAILED_WHITE_IP")
						result = EGEC.EGEC_AUTH_TOKEN_INCORRECT_VALUE;
					}

				}
				else if (request.getResult() == EGEC.EGEC_AUTH_TOKEN_TOKEN_EXPIRED_VALUE)
				{
					logger.warn("E_INTERFACE_TOKEN_EXPIRED");
					result = EGEC.EGEC_AUTH_TOKEN_TOKEN_EXPIRED_VALUE;
				}

				else if (request.getResult() == EGEC.EGEC_CORE_PROC_ERROR_VALUE)
				{
					logger.warn("E_SERVER_PROC_ERROR");
					result = EGEC.EGEC_CORE_PROC_ERROR_VALUE;
				}
				else
				{
					logger.warn("E_SERVER_PROC_ERROR");
					result = EGEC.EGEC_CORE_PROC_ERROR_VALUE;
				}
			} while (false);
		}
		finally
		{
		}
		
		builder.setResult(result);
		msg.genBaseMsg(builder.build());
		SD.transmitter.sendToServer(ServerType.PROXY, SD.areaId, msg);

	}
}
