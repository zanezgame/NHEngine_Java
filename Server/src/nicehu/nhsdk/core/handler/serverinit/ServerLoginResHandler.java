package nicehu.nhsdk.core.handler.serverinit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgServer.ServerLoginRes;

public class ServerLoginResHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(ServerLoginResHandler.class);

	@Override
	public void handle(ServerNode sender, Message msg)
	{
		if (msg.getId() == EGMI.EGMI_SERVER_LOGIN_RES_VALUE)
		{
			ServerLoginRes request = (ServerLoginRes)msg.getProtoBuf();

			if (SD.serverId <= 0)
			{
				SD.serverInitHandler.processServerLoginRes(sender,
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
				SD.serverInitHandler.serverLoginConfirm(sender.getServerId(), 2);
			}

		}

	}

}