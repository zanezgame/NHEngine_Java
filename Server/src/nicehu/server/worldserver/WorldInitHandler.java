package nicehu.server.worldserver;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.nhsdk.candy.time.TimeZoneU;
import nicehu.nhsdk.core.data.AreaData;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.db.DBMgr;
import nicehu.nhsdk.core.handler.serverinit.ServerInitHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgBase.StreamObject;
import nicehu.server.common.thread.Statistics;
import nicehu.server.common.thread.StatisticsRunnable;
import nicehu.server.manageserver.config.core.ConfigReloadMgr;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.proxyserver.core.data.GTSD;
import nicehu.server.worldserver.core.data.CSD;

public class WorldInitHandler extends ServerInitHandler
{
	private static final Logger logger = LoggerFactory.getLogger(WorldInitHandler.class);

	@Override
	public void processServerLoginRes(ServerNode sender, int result, int serverID, String ServerCfg, List<StreamObject> serverStreamObjects,
		List<StreamObject> clientsObjects, int areaId, int timeZone)
	{
		logger.warn("receive ServerLoginRes localServerId:" + Integer.toHexString(serverID));
		TimeZoneU.setTimezone(timeZone);
		AreaData.setAreaId(areaId);

		if (result == EGEC.EGEC_CORE_SUCCESS_VALUE)
		{

			ServerConfig serverConfig = JsonU.getJavaObj(ServerConfig.class, ServerCfg);
			ConfigReloadMgr.instance.loadServerConfig(serverStreamObjects);

			SD.initServerConfig(serverConfig);

			DBMgr.init(ServerType.CENTER);

			SD.statistics = new Statistics();
			Thread thread = new Thread(new StatisticsRunnable(), ThreadU.genName("Wind_StatisticsRunnable"));
			thread.start();

			try
			{
				int portForServer = ParseU.pInt(serverConfig.getAttr("PortForServer"), 0);
				logger.info("CenterServer openPort portForServer={}", portForServer);
				boolean portForServerOpen = GTSD.socketServerForS.openPort(new InetSocketAddress(portForServer));

				if (portForServerOpen)
				{
					serverLoginConfirm(sender.getServerId(), 2);
					logger.warn("Send ServerLoginConfimReq !!!");
					logger.warn("Center Start Success!!!___________________________________________________________________________________Center!!!");
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
	public void processServerSync(List<ServerInfo> servers)
	{
		for (ServerInfo serverInfo : servers)
		{
//			if (ServerInfo.SERVER_STATUS_NORMAL == serverInfo.getStatus() && ServerType.getType(serverInfo.getId()) == ServerType.DATA)
//			{
//				processServerStartup(serverInfo);
//			}
		}
	}

	private void processServerStartup(ServerInfo serverInfo)
	{
		logger.warn("CenterServer connectTo " + serverInfo.getName() + " ServerId: " + Integer.toHexString(serverInfo.getId()) + " ip: " + serverInfo.getIpForServer()
			+ " port : " + serverInfo.getPortForServer());
		CSD.socketServerForS.connectTo(new InetSocketAddress(serverInfo.getIpForServer(), serverInfo.getPortForServer()));

	}
}