package nicehu.server.authserver.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgAuth.CreateAccountReq;
import nicehu.pb.NHMsgAuth.LoginReq;
import nicehu.pb.NHMsgServer.QueryTokenReq;
import nicehu.server.authserver.logic.login.handler.CreateAccountHandler;
import nicehu.server.authserver.logic.login.handler.LoginHandler;
import nicehu.server.authserver.logic.login.handler.SyncTokenHandler;
import nicehu.server.common.core.CommonHandlerRegister;

public class AuthHandlerRegister
{
	public static void init()
	{
		CommonHandlerRegister.init();
		
		SD.handlerMgr.addHandler(EGMI.EGMI_CREATE_ACCOUNT_REQ_VALUE, new CreateAccountHandler());
		SD.handlerMgr.addHandler(EGMI.EGMI_LOGIN_REQ_VALUE, new LoginHandler());
		SD.handlerMgr.addHandler(EGMI.EGMI_SERVER_QUERY_TOKEN_REQ_VALUE, new SyncTokenHandler());
	}



}
