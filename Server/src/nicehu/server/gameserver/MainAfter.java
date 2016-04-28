package nicehu.server.gameserver;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.cache.core.MemCacheImpl;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.db.DBMgr;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHMsgBase.Pair;
import nicehu.server.common.CommonMainAfter;
import nicehu.server.gameserver.core.GSD;
import nicehu.server.gameserver.thread.GameThread;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;
import nicehu.server.manageserver.config.core.ConfigReloadMgr;
import nicehu.server.manageserver.logic.freeze.data.FreezeMgr;

public class MainAfter extends CommonMainAfter
{
	private static final Logger logger = LoggerFactory.getLogger(MainAfter.class);

	@Override
	public void mainAfter(ConnectNode sender, int result, List<Pair> serverConfigs, List<Pair> clientConfigs)
	{
		//load config
		ConfigReloadMgr.instance.loadServerConfig(serverConfigs);

		//db init
		DBMgr.init(ServerType.GAME);
		SD.memCache = new MemCacheImpl(SD.serverConfig.getMemCacheConfigsList(), 5, CommonConfigMgr.instance.cfg.getMemCacheExpiredTime(), true);

		//logic init
		GSD.init();
		FreezeMgr.instance.init();
		GameThread.start();

		//netty
		SD.isOpen = true;
		try
		{
			int portForServer = ParseU.pInt(SD.serverConfig.getAttr("PortForServer"), 0);
			logger.info("GameServer openPort portForServer: ", portForServer);
			boolean portForServerOpen = SD.serverForS.openPort(new InetSocketAddress(portForServer));

			if (portForServerOpen)
			{
				serverLoginConfirm(sender.getId(), 2);
				logger.warn("Game Start Success!!!_____________________________________________________________");
			}
		}
		catch (Exception e)
		{
			logger.error("Bind port failed !!! {}", ExceptionUtils.getStackTrace(e));
			System.exit(0);
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
				GSD.serverForS.connectTo(new InetSocketAddress(serverInfo.getIpForServer(), serverInfo.getPortForServer()));

			}
		}
	}

}