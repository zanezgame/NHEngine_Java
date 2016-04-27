package nicehu.server.gameserver;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.log.LogBackMgr;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.common.handler.ShutdownReqHandler;
import nicehu.server.gameserver.core.GameHandlerRegister;
import nicehu.server.manageserver.config.serverconfig.ServerConfigMgr;
import sun.misc.Signal;

public class Main
{
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static int serverType = ServerType.GAME;
	private static String serverName = "GameServer_001";

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			serverName = args[0];
		}
		SD.init(serverType, serverName);
		LogBackMgr.init();
		logger.warn("Server Name: {}", serverName);

		ServerConfigMgr.instance.reload();

		SD.mainAfter = new MainAfter();
		SD.socketServerForS.initialize(16);
		GameHandlerRegister.init();

		String manageIp = ServerConfigMgr.instance.getManageIp();
		int managePort = ServerConfigMgr.instance.getManagePort();
		logger.warn("GameServer connecting ManageServer  ip={} port={}", manageIp, managePort);
		SD.socketServerForS.connectTo(new InetSocketAddress(manageIp, managePort));

		Signal.handle(new Signal("TERM"), new ShutdownReqHandler());
	}

}