package nicehu.server.authserver;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.log.LogBackMgr;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.authserver.core.AuthHandlerRegister;
import nicehu.server.common.handler.ShutdownReqHandler;
import nicehu.server.manageserver.config.serverconfig.ServerConfigMgr;
import sun.misc.Signal;

public class Main
{
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static int serverType = ServerType.AUTH;
	private static String serverName = "AuthServer";

	public static void main(String[] args)
	{
		// load config
		LogBackMgr.init();
		ServerConfigMgr.instance.reload();
		logger.warn("Server Name: {}", serverName);

		// SD init
		SD.init(serverType, serverName);
		SD.initServerConfig(serverName);
		SD.mainAfter = new MainAfter();

		AuthHandlerRegister.init();

		SD.serverForS.initialize(16);
		SD.serverForC.initialize(16);

		String ip = ServerConfigMgr.instance.getManageIp();
		int port = ServerConfigMgr.instance.getManagePort();
		logger.warn("AuthServer connecting ManageServer  ip={} port={}", ip, port);
		SD.serverForS.connectTo(new InetSocketAddress(ip, port));

		Signal.handle(new Signal("TERM"), new ShutdownReqHandler());
	}

}
