package nicehu.server.gameserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.nhsdk.util.EnumUtil.ConnectStatus;
import nicehu.pb.NHDefine.EGMI;
import nicehu.server.manageserver.logic.freeze.data.FreezeInfo;
import nicehu.server.manageserver.logic.freeze.data.FreezeMgr;

public class GameBaseHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GameBaseHandler.class);

	public static void handleSocket(ChannelHandlerContext ctx, ConnectNode connectNode, Message msg)
	{
		if (connectNode == null)
		{
			SD.handlerMgr.handle(ctx, msg);
			return;
		}
		int serverType = ServerType.getType(connectNode.getId());

		if (serverType != ServerType.PROXY)
		{
			SD.handlerMgr.handle(connectNode, msg);
		}
		else
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
							SD.handlerMgr.handle(ctx, msg);
							return;
						}
						else
						{
							GameSession session = GSD.sessions.get(playerId);
							if (session != null && session.getStatus() == ConnectStatus.Authed)
							{
								SD.handlerMgr.handle(session, msg);
								return;
							}
							// TODO token验证失败,提示玩家重新登录
						}
					}
				} while (false);
			}
			finally
			{

			}
		}

	}

}
