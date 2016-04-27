package nicehu.nhsdk.core.data;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.cache.core.MemCacheImpl;
import nicehu.nhsdk.candy.str.NumU;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.core.datatransmitter.Transmitter;
import nicehu.nhsdk.core.db.DBCluster;
import nicehu.nhsdk.core.handler.HandlerMgr;
import nicehu.nhsdk.core.server.HttpServer;
import nicehu.nhsdk.core.server.SocketServer;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.common.CommonMainAfter;
import nicehu.server.common.thread.Statistics;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.manageserver.config.serverconfig.ServerConfigMgr;

public class SD
{
	private static Logger logger = LoggerFactory.getLogger(SD.class);

	// ServerInfo
	public static boolean isOpen = true;
	public static int serverId = 0;
	public static int serverType;
	public static String serverName = "";
	public static ServerConfig serverConfig = null;
	public static int index = 0;
	// NettyServer
	public static SocketServer socketServerForS = new SocketServer(true);
	public static SocketServer socketServerForC = new SocketServer(false);
	public static HttpServer httpCServer = new HttpServer();

	public static HandlerMgr handlerMgr = new HandlerMgr();
	public static Transmitter transmitter = new Transmitter();
	// db
	public static DBCluster dbCluster = null;
	public static MemCacheImpl memCache = null;

	public static CommonMainAfter mainAfter = null;
	public static Statistics statistics = null;

	public static ConcurrentHashMap<Integer, Vector<ServerInfo>> serveType_SeverInfos = new ConcurrentHashMap<>();

	public static void init(int serverType, String serverName)
	{
		SD.serverType = serverType;
		SD.serverName = serverName;
	}

	public static void initServerConfig(String serverName)
	{
		ServerConfig serverConfig = ServerConfigMgr.instance.getServerConfig(serverName);
		SD.serverId = serverConfig.getServerId();
		SD.index = ParseU.pInt(serverConfig.getAttr("index"));
		SD.serverConfig = serverConfig;
	}

	public static String getServerNameId()
	{
		return SD.serverName + ":" + SD.getHexServerId();
	}

	public static String getHexServerId()
	{
		return NumU.getHex(serverId);
	}

	public static ServerInfo getProxyServer(int areaId)
	{
		Vector<ServerInfo> serverInfos = SD.serveType_SeverInfos.get(ServerType.PROXY);
		if (serverInfos != null && serverInfos.size() > 0)
		{
			for (ServerInfo serverInfo : serverInfos)
			{
				if (serverInfo.getAreaId() == areaId)
				{
					return serverInfo;
				}
			}
		}
		return null;
	}

	public static ServerInfo getServerInfo(int type, int serverId)
	{
		Vector<ServerInfo> serverInfos = SD.serveType_SeverInfos.get(type);
		if (serverInfos == null || serverInfos.size() == 0)
		{
			return null;
		}
		for (ServerInfo serverInfo : serverInfos)
		{
			if (serverInfo.getId() == serverId)
			{
				return serverInfo;
			}
		}
		return null;
	}

	public static void addServerInfo(ServerInfo serverInfo)
	{
		int serverType = ServerType.getType(serverInfo.getId());
		Vector<ServerInfo> serverInfos = SD.serveType_SeverInfos.get(serverType);
		if (serverInfos == null)
		{
			serverInfos = new Vector<ServerInfo>();
			SD.serveType_SeverInfos.put(serverType, serverInfos);
		}
		serverInfos.add(serverInfo);
	}

	public static void removeServerInfo(int serverId)
	{
		int serverType = ServerType.getType(serverId);
		Vector<ServerInfo> serverInfos = SD.serveType_SeverInfos.get(serverType);
		if (serverInfos == null)
		{
			return;
		}
		for (ServerInfo serverInfo : serverInfos)
		{
			if (serverInfo.getId() == serverId)
			{
				serverInfos.remove(serverInfo);
				return;
			}
		}
	}
}