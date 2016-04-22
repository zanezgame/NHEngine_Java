package nicehu.server.gameserver.logic.initinfo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.lock.LockU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.session.GameSession;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgGame.QueryPlayerReq;
import nicehu.pb.NHMsgGame.QueryPlayerRes;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.mgr.PM;

public class CG_QueryPlayerReqHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(CG_QueryPlayerReqHandler.class);

	@Override
	public void handle(GameSession session, Message msg)
	{
		QueryPlayerReq request = (QueryPlayerReq)msg.protoBuf;
		logger.info("recv CG_QueryPlayerqHandler, playerId = {}", msg.playerId);

		QueryPlayerRes.Builder builder = QueryPlayerRes.newBuilder();
		msg.setId(EGMI.EGMI_QUERY_PLAYER_RES_VALUE);
		int result = EGEC.EGEC_CORE_SUCCESS_VALUE;
		int playerId = msg.playerId;

		// String clientToken = request.getToken();

		PlayerNode playerNode = null;
		LockU.lock(playerId);
		try
		{
			do
			{
				playerNode = PM.getPlayerNode(playerId);
				if (playerNode == null || playerNode.getInfoPlayer() == null)
				{
					result = EGEC.EGEC_CORE_DATA_CHECK_FAILD_VALUE;
					break;
				}

				{
					builder.setPlayer(playerNode.toProtobuf());
				}
			} while (false);
		}
		finally
		{
			LockU.unLock(playerId);
		}

		builder.setResult(result);
		msg.setProtoBuf(builder.build());
		SD.transmitter.sendToServers(ServerType.PROXY, msg);
	}
}
