package nicehu.server.common.core.handler;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHMsgBase;
import nicehu.pb.NHMsgBase.SyncServers;


public class SyncServerInfoHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(SyncServerInfoHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{
		logger.warn(SD.serverName + " Receive ServerSyncInfo! Update New remote ServerInfo!");
		SyncServers request = (SyncServers)msg.getPb(SyncServers.getDefaultInstance());
		List<ServerInfo> servers = new LinkedList<ServerInfo>();
		for (NHMsgBase.ServerInfo serverInfoPro : request.getServersList())
		{
			ServerInfo serverInfo = new ServerInfo(serverInfoPro);
			servers.add(serverInfo);

			if (serverInfo.getStatus() == ServerInfo.SERVER_STATUS_NORMAL)
			{
				SD.addServerInfo(serverInfo);
			}
			else if (serverInfo.getStatus() == ServerInfo.SERVER_STATUS_CLOSED)
			{
				SD.removeServerInfo(serverInfo.getId());
			}
			logger.warn("remote serverId: " + serverInfo.getId() + " Status: " + serverInfo.getStatus());
		}

		SD.mainAfter.connectOtherServers(servers);
	}

}