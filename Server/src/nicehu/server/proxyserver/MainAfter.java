package nicehu.server.proxyserver;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.db.DBMgr;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgBase.Pair;
import nicehu.server.authserver.core.ASD;
import nicehu.server.common.CommonMainAfter;
import nicehu.server.common.thread.Statistics;
import nicehu.server.common.thread.StatisticsRunnable;
import nicehu.server.manageserver.config.core.ConfigReloadMgr;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.proxyserver.core.PSD;

public class MainAfter extends CommonMainAfter
{
	private static final Logger logger = LoggerFactory.getLogger(MainAfter.class);

	@Override
	public void mainAfter(ConnectNode sender, int result, List<Pair> serverConfigs, List<Pair> clientConfigs)
	{
		// load config
		ConfigReloadMgr.instance.loadServerConfig(serverConfigs);

		// db init
		DBMgr.init(ServerType.PROXY);

		Thread statisticsThread = new Thread(new StatisticsRunnable(), ThreadU.genName("Wind_StatisticsRunnable"));
		statisticsThread.start();

		SD.isOpen = true;
		try
		{
			int portForServer = ParseU.pInt(SD.serverConfig.getAttr("PortForServer"), 0);
			logger.info("ProxyServer openPort portForServer={}", portForServer);
			boolean portForServerOpen = PSD.serverForS.openPort(new InetSocketAddress(portForServer));

			int portForSocketClient = ParseU.pInt(SD.serverConfig.getAttr("PortForClient"), 0);
			logger.info("ProxyServer openPort PortForClient={}", portForSocketClient);
			boolean portForSocketClientOpen = ASD.serverForC.openPort(new InetSocketAddress(portForSocketClient));

			if (portForServerOpen && portForSocketClientOpen)
			{
				serverLoginConfirm(sender.getId(), 2);
				logger.warn("Proxy Start Success!!!___________________________________________________________________________________Gate!!!");
			}
		}
		catch (Exception e)
		{
			logger.error("Bind port failed !!! {}", ExceptionUtils.getStackTrace(e));
			System.exit(0);
		}

	}
}