package nicehu.server.common.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgBase;
import nicehu.pb.NHMsgServer.ReloadConfigReq;
import nicehu.pb.NHMsgServer.ServerLoginRes;
import nicehu.server.common.core.handler.ServerLoginResHandler;
import nicehu.server.common.core.handler.SyncServerInfoHandler;
import nicehu.server.common.handler.ReloadConfigReqHandler;

public class CommonHandlerRegister
{

	public static void init()
	{
		SD.handlerMgr.addHandler(EGMI.EGMI_SERVER_LOGIN_RES_VALUE, new ServerLoginResHandler());
		SD.handlerMgr.addHandler(EGMI.EGMI_SERVER_SERVERINFO_SYNC_VALUE, new SyncServerInfoHandler());
		SD.handlerMgr.addHandler(EGMI.EGMI_SERVER_RELOAD_CONFIG_REQ_VALUE, new ReloadConfigReqHandler());
	}
}
