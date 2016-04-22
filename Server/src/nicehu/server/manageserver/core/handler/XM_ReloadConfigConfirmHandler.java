package nicehu.server.manageserver.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgServer.ReloadConfigRes;
import nicehu.server.manageserver.core.data.MSD;


public class XM_ReloadConfigConfirmHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(XM_ReloadConfigConfirmHandler.class);

	@Override
	public void handle(ServerNode sender, Message msg)
	{
		logger.info("receive reloadConfigConfirmHandler, serverId = {}", sender.getServerId());
		ReloadConfigRes request = (ReloadConfigRes)msg.getProtoBuf();
		ServerInfo thisServer = MSD.serverInfoMgr.getServer(sender.getServerId());
		if (request.getCode() == EGEC.EGEC_CORE_SUCCESS_VALUE)
		{
			thisServer.setReloadState(ServerInfo.SERVER_RELOAD_SUCCESS);
		}
		else if (request.getCode() == EGEC.EGEC_CORE_ERROR_VALUE)
		{
			thisServer.setReloadState(ServerInfo.SERVER_RELOAD_FAILED);
		}

	}
}
