package nicehu.server.proxyserver;

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
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.db.DBMgr;
import nicehu.nhsdk.core.handler.serverinit.ServerInitHandler;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgBase.StreamObject;
import nicehu.server.authserver.core.data.ASD;
import nicehu.server.common.thread.Statistics;
import nicehu.server.common.thread.StatisticsRunnable;
import nicehu.server.manageserver.config.core.ConfigReloadMgr;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.proxyserver.core.data.GTSD;

public class ProxyInitHandler extends ServerInitHandler
{
	private static final Logger logger = LoggerFactory.getLogger(ProxyInitHandler.class);

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

			DBMgr.init(ServerType.PROXY);

			SD.statistics = new Statistics();
			Thread statisticsThread = new Thread(new StatisticsRunnable(), ThreadU.genName("Wind_StatisticsRunnable"));
			statisticsThread.start();

			try
			{
				int portForServer = ParseU.pInt(serverConfig.getAttr("PortForServer"), 0);
				logger.info("GateServer openPort portForServer={}", portForServer);
				boolean portForServerOpen = GTSD.socketServerForS.openPort(new InetSocketAddress(portForServer));

				int portForSocketClient = ParseU.pInt(serverConfig.getAttr("PortForSocketClient"), 0);
				logger.info("SocketGateServer openPort ={}", portForSocketClient);
				boolean portForSocketClientOpen = ASD.socketServerForC.openPort(new InetSocketAddress(portForSocketClient));

				int portForHttpClient = ParseU.pInt(serverConfig.getAttr("PortForHttpClient"), 0);
				logger.warn("HttpGateServer openPort ={}", portForHttpClient);
				boolean portForHttpClientOpen = GTSD.httpCServer.openPort(new InetSocketAddress(portForHttpClient));

				if (portForServerOpen && portForSocketClientOpen && portForHttpClientOpen)
				{
					serverLoginConfirm(sender.getServerId(), 2);
					logger.warn("Send ServerLoginConfimReq !!!");
					logger.warn("Gate Start Success!!!___________________________________________________________________________________Gate!!!");
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