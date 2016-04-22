package nicehu.nhsdk.core.datatransmitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgBase.SyncServers;
import nicehu.server.manageserver.core.data.MSD;

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

	public synchronized void sendCareServersToThisServer(ServerNode serverNode, ServerInfo thisServer)
	{
		SyncServers.Builder builder = SyncServers.newBuilder();
		Message syncServersProtocol = new Message(EGMI.EGMI_SERVER_SERVERINFO_SYNC_VALUE);

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
				if ((thisServerType == ServerType.GATE && serverType == ServerType.GAME) || (thisServerType == ServerType.GAME && serverType == ServerType.GATE))
				{
					if (server.getAreaId() != thisServer.getAreaId())
					{
						continue;
					}
				}
				if (thisServer.getServerTypes().contains(ServerType.getType(server.getId())))
				{
					builder.addServers(server.toProto());
					careServerIds.add(Integer.toHexString(server.getId()));
				}
			}
		}
		if (builder.getServersCount() > 0)
		{
			syncServersProtocol.setProtoBuf(builder.build());
			logger.info("sendToThisServer, thisServerId : " + Integer.toHexString(serverNode.getServerId()) + " careServerTypes: " + careServerTyps + " careServerIds: "
				+ careServerIds);
			MSD.transmitter.send(serverNode.getCtx(), syncServersProtocol);
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
				if ((thisServerType == ServerType.GATE && serverType == ServerType.GAME) || (thisServerType == ServerType.GAME && serverType == ServerType.GATE))
				{
					if (server.getAreaId() != thisServer.getAreaId())
					{
						continue;
					}
				}
				if (server.getServerTypes().contains(thisServerType))
				{
					SyncServers.Builder builder = SyncServers.newBuilder();
					builder.addServers(thisServer.toProto());
					syncServersProtocol.setProtoBuf(builder.build());
					logger.info("sendToCareServers, careServerId : " + Integer.toHexString(server.getId()) + " thisServerId: " + Integer.toHexString(thisServer.getId()));
					MSD.transmitter.sendToServer(server.getId(), syncServersProtocol);
				}
			}
		}
	}
}
