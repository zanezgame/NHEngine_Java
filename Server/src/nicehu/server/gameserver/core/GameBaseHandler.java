package nicehu.server.gameserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.session.GameSession;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.nhsdk.util.EnumUtil.ConnectStatus;
import nicehu.pb.NHDefine.EGMI;
import nicehu.server.gameserver.core.data.GSD;
import nicehu.server.manageserver.logic.freeze.data.FreezeInfo;
import nicehu.server.manageserver.logic.freeze.data.FreezeMgr;

public class GameBaseHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GameBaseHandler.class);

	public static void handleSocket(ChannelHandlerContext ctx, ServerNode serverNode, Message msg)
	{
		if (serverNode == null)
		{
			SD.sController.procProto(ctx, msg);
			return;
		}
		int serverType = ServerType.getType(serverNode.getServerId());

		if (serverType == ServerType.GATE)
		{
			GameBaseHandler.clientGateForward(ctx, serverNode, msg);
		}
		else
		{
			SD.sController.procProto(serverNode, msg);
		}

	}

	public static void clientGateForward(ChannelHandlerContext ctx, ServerNode serverNode, Message msg)
	{
		int playerId = msg.playerId;// .getPlayerId();
		try
		{
			do
			{
				FreezeInfo freezeInfo = FreezeMgr.instance.getFreezeInfo(playerId);
				if (freezeInfo != null)
				{
					// JsonU.updateErr(result, 1, "此账号处于禁止登陆状态中!解禁时间: " + TimeU.getStr(freezeInfo.getEndTime()));
					break;
				}
				else
				{
					if (msg.getId() == EGMI.EGMI_AUTH_TOKEN_REQ_VALUE)
					{
						SD.sController.procProto(ctx, msg);
						return;
					}
					else
					{
						GameSession session = GSD.sessions.get(playerId);
						if (session != null && session.getStatus() == ConnectStatus.Authed)
						{
							// TODO 验证玩家是否已经autotoken成功过
							SD.sController.procProto(session, msg);
							return;
						}
						// TODO 提示玩家重新登录
					}
				}
			} while (false);
		}
		finally
		{

		}
	}
}
