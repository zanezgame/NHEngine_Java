package nicehu.server.manageserver.logic.serverlogin.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.server.manageserver.core.data.MSD;


public class XM_ServerLoginConfirmHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(XM_ServerLoginConfirmHandler.class);

	@Override
	public void handle(ServerNode sender, Message msg)
	{

		logger.info("ServerLoginConfirmHandler, serverId = {}", Integer.toHexString(sender.getServerId()));

		// ManageProtocolsForServer.ServerLoginConfirm request = (ManageProtocolsForServer.ServerLoginConfirm)
		// message.getProtoBuf();
		ServerInfo thisServer = MSD.serverInfoMgr.getServer(sender.getServerId());
		thisServer.setStatus(ServerInfo.SERVER_STATUS_NORMAL);

		MSD.serverInfoMgr.sendCareServersToThisServer(sender, thisServer);
		MSD.serverInfoMgr.sendThisServerToCareServers(thisServer);
		logger.warn("!!!One Manage Connect Event OVER!!! ");

	}
}
