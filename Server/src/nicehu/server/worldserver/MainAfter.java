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
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.db.DBMgr;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgBase.StreamObject;
import nicehu.server.common.CommonMainAfter;
import nicehu.server.common.thread.Statistics;
import nicehu.server.common.thread.StatisticsRunnable;
import nicehu.server.manageserver.config.core.ConfigReloadMgr;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.proxyserver.core.PSD;
import nicehu.server.worldserver.core.WSD;

public class MainAfter extends CommonMainAfter
{
	private static final Logger logger = LoggerFactory.getLogger(MainAfter.class);

	@Override
	public void mainAfter(ConnectNode sender, int result, int serverID, String ServerCfg, List<StreamObject> serverStreamObjects,
		List<StreamObject> clientsObjects, int areaId, int timeZone)
	{
		logger.warn("receive ServerLoginRes localServerId:" + Integer.toHexString(serverID));
		TimeZoneU.setTimezone(timeZone);
		AreaData.setAreaId(areaId);

		if (result == EGEC.EGEC_CORE_SUCCESS_VALUE)
		{

			ServerConfig serverConfig = JsonU.getJavaObj(ServerConfig.class, ServerCfg);
			ConfigReloadMgr.instance.loadServerConfig(serverStreamObjects);

			SD.initServerConfig(serverConfig.getServerName());

			DBMgr.init(ServerType.WORLD);

			SD.statistics = new Statistics();
			Thread thread = new Thread(new StatisticsRunnable(), ThreadU.genName("Wind_StatisticsRunnable"));
			thread.start();

			try
			{
				int portForServer = ParseU.pInt(serverConfig.getAttr("PortForServer"), 0);
				logger.info("CenterServer openPort portForServer={}", portForServer);
				boolean portForServerOpen = PSD.socketServerForS.openPort(new InetSocketAddress(portForServer));

				if (portForServerOpen)
				{
					serverLoginConfirm(sender.getId(), 2);
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

}