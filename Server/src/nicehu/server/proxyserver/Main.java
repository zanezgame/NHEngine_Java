package nicehu.server.proxyserver;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.log.LogBackMgr;
import nicehu.nhsdk.candy.time.TimeZoneU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.common.handler.ShutdownReqHandler;
import nicehu.server.manageserver.config.serverconfig.ServerConfigMgr;
import nicehu.server.proxyserver.core.ProxyHandlerRegister;
import sun.misc.Signal;

public class Main
{
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static int serverType = ServerType.PROXY;
	private static String serverName = "ProxyServer_001";

	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			serverName = args[0];
		}
		// load config
		LogBackMgr.init();
		ServerConfigMgr.instance.reload();
		logger.warn("Server Name: {}", serverName);

		// SD init
		SD.init(serverType, serverName);
		SD.initServerConfig(serverName);
		SD.mainAfter = new MainAfter();
		SD.serverForS.initialize(16);
		SD.serverForC.initialize(16);

		TimeZoneU.setTimezone(ServerConfigMgr.instance.timeZone);
		ProxyHandlerRegister.init();

		String ip = ServerConfigMgr.instance.getManageIp();
		int port = ServerConfigMgr.instance.getManagePort();
		logger.warn("ProxyServer connecting ManageServer  ip={} port={}", ip, port);
		SD.serverForS.connectTo(new InetSocketAddress(ip, port));

		Signal.handle(new Signal("TERM"), new ShutdownReqHandler());
	}
}