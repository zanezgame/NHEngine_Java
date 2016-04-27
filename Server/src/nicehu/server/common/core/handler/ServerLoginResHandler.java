package nicehu.server.common.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgServer.ServerLoginRes;

public class ServerLoginResHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(ServerLoginResHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{
		if (msg.getId() == EGMI.EGMI_SERVER_LOGIN_RES_VALUE)
		{
			ServerLoginRes request = (ServerLoginRes)msg.getPb(ServerLoginRes.getDefaultInstance());

			if (SD.serverId <= 0)
			{
				SD.mainAfter.mainAfter(sender,
					request.getResult(),
					request.getServerId(),
					request.getServerConfig(),
					request.getStreamObjectsList(),
					request.getClientObjectsList(),
					request.getAreaId(),
					request.getTimeZone());
			}
			else
			{
				logger.warn("server relogin localAddress={}", sender.getCtx().channel().localAddress());
				SD.mainAfter.serverLoginConfirm(sender.getId(), 2);
			}

		}

	}

}