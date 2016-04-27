package nicehu.server.authserver.logic.login.handler;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgServer.QueryTokenReq;
import nicehu.pb.NHMsgServer.QueryTokenRes;
import nicehu.server.authserver.core.ASD;
import nicehu.server.authserver.core.AuthSession;
import nicehu.server.authserver.logic.login.db.LoginDB;

public class SyncTokenHandler extends LogicHandler
{

	private static final Logger logger = LoggerFactory.getLogger(SyncTokenHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{

		QueryTokenReq request = (QueryTokenReq)msg.getPb(QueryTokenReq.getDefaultInstance());
		logger.info("recv QueryTokenReq, playerId = {}", request.getPlayerId());

		msg.setId(EGMI.EGMI_SERVER_QUERY_TOKEN_RES_VALUE);
		QueryTokenRes.Builder builder = QueryTokenRes.newBuilder();
		int code = EGEC.EGEC_CORE_SUCCESS_VALUE;
		int playerId = request.getPlayerId();

		int accountId = request.getPlayerId();
		String gameToken = request.getToken();
		String authToken = null;

		try
		{
			AuthSession session = ASD.sessions.get(accountId);
			if (session == null)
			{
				logger.error("Token expired. PlayerId = {}", accountId);
				code = EGEC.EGEC_AUTH_TOKEN_TOKEN_EXPIRED_VALUE;
			}
			else
			{
				authToken = session.getToken();
				if (authToken == null || authToken.equals(""))
				{
					logger.error("Token expired. PlayerId = {}", accountId);
					code = EGEC.EGEC_AUTH_TOKEN_TOKEN_EXPIRED_VALUE;
				}
				else
				{
					LoginDB.updateLastAreaId(playerId, request.getAreaId());
					builder.setToken(session.getToken());
					builder.setLoginTime(session.getLoginTime());
					// TODO 记录areaIds
				}
			}
		}
		catch (Exception ex)
		{
			logger.error(ExceptionUtils.getStackTrace(ex));
			builder.setResult(EGEC.EGEC_CORE_PROC_ERROR_VALUE);
		}

		builder.setPlayerId(playerId);
		builder.setResult(code);
		msg.genBaseMsg(builder.build());
		SD.transmitter.send(sender.ctx, msg);
	}

}
