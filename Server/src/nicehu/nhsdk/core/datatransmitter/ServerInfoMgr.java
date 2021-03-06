package nicehu.nhsdk.core.datatransmitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgServer.SyncServerInfos;
import nicehu.server.manageserver.core.MSD;

public class ServerInfoMgr
{
	private static final Logger logger = LoggerFactory.getLogger(ServerInfoMgr.class);
	private HashMap<Integer, ServerInfo> servers = new HashMap<>();

	public synchronized void addServer(ServerInfo serverInfo)
	{
		this.servers.put(serverInfo.getId(), serverInfo);
	}

	public synchronized ServerInfo getServer(int serverID)
	{
		return this.servers.get(serverID);
	}

	public synchronized void removeServer(int serverID)
	{
		this.servers.remove(serverID);
	}

	public synchronized Collection<ServerInfo> getServers()
	{
		return servers.values();
	}

	public synchronized ServerInfo getManageServer()
	{
		for (ServerInfo serverInfo : this.servers.values())
		{
			if (ServerType.getType(serverInfo.getId()) == ServerType.MANAGE)
			{
				return serverInfo;
			}
		}
		return null;
	}

	public synchronized void sendCareServersToThisServer(ConnectNode serverNode, ServerInfo thisServer)
	{
		SyncServerInfos.Builder builder = SyncServerInfos.newBuilder();
		Message msg = new Message(EGMI.EGMI_SERVER_SERVERINFO_SYNC_VALUE);

		int thisServerType = ServerType.getType(thisServer.getId());
		List<String> careServerTyps = new ArrayList<String>();
		for (int type : thisServer.getServerTypes())
		{
			careServerTyps.add(Integer.toHexString(type));
		}
		List<String> careServerIds = new ArrayList<String>();
		for (ServerInfo server : servers.values())
		{
			int serverType = ServerType.getType(server.getId());
			if (server.getStatus() == ServerInfo.SERVER_STATUS_NORMAL)
			{
				if ((thisServerType == ServerType.PROXY && serverType == ServerType.GAME) || (thisServerType == ServerType.GAME && serverType == ServerType.PROXY))
				{
					if (server.getAreaId() != thisServer.getAreaId())
					{
						continue;
					}
				}
				if (thisServer.getServerTypes().contains(ServerType.getType(server.getId())))
				{
					builder.addServerInfos(JsonU.getJsonStr(server));
					careServerIds.add(Integer.toHexString(server.getId()));
				}
			}
		}
		if (builder.getServerInfosCount() > 0)
		{
			msg.genBaseMsg(builder.build());
			logger.info("sendToThisServer, thisServerId : " + Integer.toHexString(serverNode.getId()) + " careServerTypes: " + careServerTyps + " careServerIds: "
				+ careServerIds);
			MSD.transmitter.send(serverNode.getCtx(), msg);
		}
	}

	public synchronized void sendThisServerToCareServers(ServerInfo thisServer)
	{
		Message syncServersProtocol = new Message(EGMI.EGMI_SERVER_SERVERINFO_SYNC_VALUE);

		int thisServerType = ServerType.getType(thisServer.getId());
		for (ServerInfo server : this.servers.values())
		{
			int serverType = ServerType.getType(server.getId());
			if (server.getId() == thisServer.getId())
			{
				continue;
			}
			if (server.getStatus() == ServerInfo.SERVER_STATUS_NORMAL)
			{
				if ((thisServerType == ServerType.PROXY && serverType == ServerType.GAME) || (thisServerType == ServerType.GAME && serverType == ServerType.PROXY))
				{
					if (server.getAreaId() != thisServer.getAreaId())
					{
						continue;
					}
				}
				if (server.getServerTypes().contains(thisServerType))
				{
					SyncServerInfos.Builder builder = SyncServerInfos.newBuilder();
					builder.addServerInfos(JsonU.getJsonStr(thisServer));
					syncServersProtocol.genBaseMsg(builder.build());
					logger.info("sendToCareServers, careServerId : " + Integer.toHexString(server.getId()) + " thisServerId: " + Integer.toHexString(thisServer.getId()));
					MSD.transmitter.sendToServer(server.getId(), syncServersProtocol);
				}
			}
		}
	}
}
