package nicehu.server.authserver;

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
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.db.DBMgr;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgBase.StreamObject;
import nicehu.server.authserver.core.ASD;
import nicehu.server.common.CommonMainAfter;
import nicehu.server.common.thread.Statistics;
import nicehu.server.common.thread.StatisticsRunnable;
import nicehu.server.manageserver.config.core.ConfigReloadMgr;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.manageserver.logic.freeze.data.FreezeMgr;

public class MainAfter extends CommonMainAfter
{
	private static final Logger logger = LoggerFactory.getLogger(MainAfter.class);

	@Override
	public void mainAfter(ConnectNode sender, int result, int serverId, String ServerCfg, List<StreamObject> serverStreamObjects,
		List<StreamObject> clientsObjects, int areaId, int timeZone)
	{
		logger.warn("mainAfter localServerId:" + Integer.toHexString(serverId));
		TimeZoneU.setTimezone(timeZone);
		AreaData.setAreaId(-2);

		if (result ==EGEC.EGEC_CORE_SUCCESS_VALUE)
		{
			ServerConfig serverConfig = JsonU.getJavaObj(ServerConfig.class, ServerCfg);
			ConfigReloadMgr.instance.loadServerConfig(serverStreamObjects);

			SD.initServerConfig(serverConfig.getServerName());

			DBMgr.init(ServerType.AUTH);

			FreezeMgr.instance.init();

			SD.statistics = new Statistics();
			Thread statisticsThread = new Thread(new StatisticsRunnable(), ThreadU.genName("Wind_StatisticsRunnable"));
			statisticsThread.start();

			try
			{
				int portForServer = ParseU.pInt(serverConfig.getAttr("PortForServer"), 0);
				logger.info("authServer openPort portForServer={}", portForServer);
				boolean portForServerOpen = ASD.socketServerForS.openPort(new InetSocketAddress(portForServer));

				int portForSocketClient = ParseU.pInt(serverConfig.getAttr("PortForSocketClient"), 0);
				logger.info("httpAuthServer openPort portForClient={}", portForSocketClient);
				boolean portForSocketClientOpen = ASD.socketServerForC.openPort(new InetSocketAddress(portForSocketClient));

				int portForHttpClient = ParseU.pInt(serverConfig.getAttr("PortForHttpClient"), 0);
				logger.info("httpAuthServer openPort portForClient={}", portForHttpClient);
				boolean portForHttpClientOpen = ASD.httpCServer.openPort(new InetSocketAddress(portForHttpClient));

				if (portForServerOpen && portForSocketClientOpen && portForHttpClientOpen)
				{
					serverLoginConfirm(sender.getId(), 2);
					logger.warn("Send ServerLoginConfimReq !!!");
					logger.warn("Auth Start Success!!!___________________________________________________________________________________Auth!!!");
				}
			}
			catch (Exception e)
			{
				logger.error("Bind port failed !!! {}", ExceptionUtils.getStackTrace(e));
				System.exit(0);
			}
		}
	}
}
