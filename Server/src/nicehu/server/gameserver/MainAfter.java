package nicehu.server.gameserver;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.cache.core.MemCacheImpl;
import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.time.TimeZoneU;
import nicehu.nhsdk.core.data.AreaData;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.db.DBMgr;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgBase.StreamObject;
import nicehu.server.common.CommonMainAfter;
import nicehu.server.common.thread.Statistics;
import nicehu.server.gameserver.core.GSD;
import nicehu.server.gameserver.thread.ThreadUtil;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;
import nicehu.server.manageserver.config.core.ConfigReloadMgr;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.manageserver.logic.freeze.data.FreezeMgr;

public class MainAfter extends CommonMainAfter
{
	private static final Logger logger = LoggerFactory.getLogger(MainAfter.class);

	@Override
	public void mainAfter(ConnectNode sender, int result, int serverId, String serverConfigStr, List<StreamObject> serverStreamObjects, List<StreamObject> clientsObjects,
		int areaId, int timeZone)
	{
		logger.warn("mainAfter localServerId:" + Integer.toHexString(serverId));
		TimeZoneU.setTimezone(timeZone);
		AreaData.setAreaId(areaId);

		if (result == EGEC.EGEC_CORE_SUCCESS_VALUE)
		{
			ServerConfig serverConfig = JsonU.getJavaObj(ServerConfig.class, serverConfigStr);
			ConfigReloadMgr.instance.loadServerConfig(serverStreamObjects);

			SD.initServerConfig(serverConfig.getServerName());

			DBMgr.init(ServerType.GAME);
			SD.memCache = new MemCacheImpl(serverConfig.getMemCacheConfigsList(), 5, CommonConfigMgr.instance.cfg.getMemCacheExpiredTime(), true);
			// SD.memCache.close();

			GSD.init();
			FreezeMgr.instance.init();

			SD.statistics = new Statistics();
			ThreadUtil.startGameThreads();

			try
			{
				int portForServer = ParseU.pInt(serverConfig.getAttr("PortForServer"), 0);
				logger.info("GameServer openPort portForServer={}", portForServer);
				boolean portForServerOpen = GSD.socketServerForS.openPort(new InetSocketAddress(portForServer));

				if (portForServerOpen)
				{
					serverLoginConfirm(sender.getId(), 2);
					logger.warn("Send ServerLoginConfimReq !!!");
					logger.warn("Game Start Success!!!___________________________________________________________________________________Game!!!");
				}
			}
			catch (Exception e)
			{
				logger.error("Bind port failed !!! {}", ExceptionUtils.getStackTrace(e));
				System.exit(0);
			}

		}
	}

	@Override
	public void connectOtherServers(List<ServerInfo> servers)
	{
		for (ServerInfo serverInfo : servers)
		{
			if (ServerInfo.SERVER_STATUS_NORMAL == serverInfo.getStatus() && ServerType.getType(serverInfo.getId()) != ServerType.GAME)
			{
				logger.warn("GameServer connectTo " + serverInfo.getName() + " ServerId: " + Integer.toHexString(serverInfo.getId()) + " ip: "
					+ serverInfo.getIpForServer() + " port : " + serverInfo.getPortForServer());
				GSD.socketServerForS.connectTo(new InetSocketAddress(serverInfo.getIpForServer(), serverInfo.getPortForServer()));

			}
		}
	}

}