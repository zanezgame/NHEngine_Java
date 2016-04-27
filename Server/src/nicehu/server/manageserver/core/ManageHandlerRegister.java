package nicehu.server.manageserver.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.pb.NHDefine.EGMI;
import nicehu.server.common.core.CommonHandlerRegister;
import nicehu.server.manageserver.core.handler.XM_ReloadConfigConfirmHandler;
import nicehu.server.manageserver.logic.serverlogin.handler.XM_ServerLoginConfirmHandler;
import nicehu.server.manageserver.logic.serverlogin.handler.XM_ServerLoginReqHandler;

public class ManageHandlerRegister
{

	public static void init()
	{
		CommonHandlerRegister.init();
		
		SD.handlerMgr.addHandler(EGMI.EGMI_SERVER_LOGIN_REQ_VALUE, new XM_ServerLoginReqHandler());
		SD.handlerMgr.addHandler(EGMI.EGMI_SERVER_LOGIN_CONFIRM_VALUE, new XM_ServerLoginConfirmHandler());
		SD.handlerMgr.addHandler(EGMI.EGMI_SERVER_RELOAD_CONFIG_RES_VALUE, new XM_ReloadConfigConfirmHandler());
	}



}
