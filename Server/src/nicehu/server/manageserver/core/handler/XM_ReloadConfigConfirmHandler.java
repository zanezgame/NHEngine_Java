package nicehu.server.manageserver.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgServer.ReloadConfigRes;
import nicehu.server.manageserver.core.MSD;


public class XM_ReloadConfigConfirmHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(XM_ReloadConfigConfirmHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{
		logger.info("receive reloadConfigConfirmHandler, serverId = {}", sender.getId());
		ReloadConfigRes request = (ReloadConfigRes)msg.getPb(ReloadConfigRes.getDefaultInstance());
		ServerInfo thisServer = MSD.serverInfoMgr.getServer(sender.getId());
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
