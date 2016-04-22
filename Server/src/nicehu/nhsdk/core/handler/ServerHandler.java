package nicehu.nhsdk.core.handler;

import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.handler.serverinit.ServerLoginResHandler;
import nicehu.nhsdk.core.handler.serverinit.SyncServerInfoHandler;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgBase;
import nicehu.pb.NHMsgServer.ReloadConfigReq;
import nicehu.pb.NHMsgServer.ServerLoginRes;
import nicehu.server.common.handler.ReloadConfigReqHandler;

public class ServerHandler
{

	public static void init()
	{
		ServerHandler.initCommon();
	}

	public static void initCommon()
	{
		SD.sController.addProto(EGMI.EGMI_SERVER_LOGIN_RES_VALUE, ServerLoginRes.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_SERVER_SERVERINFO_SYNC_VALUE, NHMsgBase.SyncServers.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_SERVER_RELOAD_CONFIG_REQ_VALUE, ReloadConfigReq.getDefaultInstance());

		SD.sController.addHandler(EGMI.EGMI_SERVER_LOGIN_RES_VALUE, new ServerLoginResHandler());
		SD.sController.addHandler(EGMI.EGMI_SERVER_SERVERINFO_SYNC_VALUE, new SyncServerInfoHandler());
		SD.sController.addHandler(EGMI.EGMI_SERVER_RELOAD_CONFIG_REQ_VALUE, new ReloadConfigReqHandler());
	}
}
