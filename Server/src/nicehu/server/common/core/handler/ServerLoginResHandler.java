package nicehu.server.common.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine.EGEC;
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

			if (request.getResult() == EGEC.EGEC_CORE_SUCCESS_VALUE)
			{
				if (!SD.isOpen)
				{
					SD.mainAfter.mainAfter(sender, request.getResult(), request.getServerConfigsList(), request.getClientConfigsList());
				}
				else
				{
					logger.warn("server relogin localAddress={}", sender.getCtx().channel().localAddress());
					SD.mainAfter.serverLoginConfirm(sender.getId(), 2);
				}
			}
			else
			{
				logger.error("serverLogin Error!!!");
			}

		}

	}

}