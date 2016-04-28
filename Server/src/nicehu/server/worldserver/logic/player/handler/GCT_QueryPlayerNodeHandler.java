package nicehu.server.worldserver.logic.player.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;

public class GCT_QueryPlayerNodeHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GCT_QueryPlayerNodeHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{
		logger.info("GCT_QueryPlayerNodeHandler");

		int playerId = msg.playerId;
//		int otherPlayerId = ParseU.pInt(msg.getAttr("playerId"));
//		int serverIndex = ParseU.pInt(msg.getAttr("index"));

		try
		{
			do
			{
//				if (msg.id == ServerProtos.P_CENTER_GCT_QUERY_PLAYERNODE)
//				{
//					msg.setId(ServerProtos.P_GAME_CTG_QUERY_PLAYERNODE);
//					Message message = MessageU.genForwardMessage(msg);
//					SD.transmitter.sendToServer(ServerType.GAME, SD.areaId, message);
//				}
//				else if (msg.id == ServerProtos.P_GAME_CTG_QUERY_PLAYERNODE)
//				{
//					msg.setId(ServerProtos.P_CENTER_GCT_QUERY_PLAYERNODE);
//					msg.seq = ParseU.pInt(msg.getAttr("initSeq"));
//					msg.setSync(true);
//					Message message = MessageU.genForwardMessage(msg);
//					SD.transmitter.sendToServer(ServerType.GAME, SD.areaId, message);
//				}
			} while (false);
		}
		finally
		{
		}

	}
}
