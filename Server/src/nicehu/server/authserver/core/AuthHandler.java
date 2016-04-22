package nicehu.server.authserver.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.handler.ServerHandler;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgAuth.CreateAccountReq;
import nicehu.pb.NHMsgAuth.LoginReq;
import nicehu.pb.NHMsgServer.QueryTokenReq;
import nicehu.server.authserver.logic.login.handler.SyncTokenHandler;
import nicehu.server.authserver.logic.login.handler.socket.CA_CreateAccountHandler;
import nicehu.server.authserver.logic.login.handler.socket.CA_LoginHandler;

public class AuthHandler
{
	public static void init()
	{
		ServerHandler.init();
		AuthHandler.initProtobuf();
		AuthHandler.initSocketHandler();
		AuthHandler.initForwardHnadler();
		AuthHandler.initHttpHandler();
	}

	public static void initProtobuf()
	{
		SD.sController.addProto(EGMI.EGMI_CREATE_ACCOUNT_REQ_VALUE,CreateAccountReq.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_LOGIN_REQ_VALUE,LoginReq.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_SERVER_QUERY_TOKEN_REQ_VALUE, QueryTokenReq.getDefaultInstance());


	}

	public static void initSocketHandler()
	{
		SD.sController.addHandler(EGMI.EGMI_CREATE_ACCOUNT_REQ_VALUE, new CA_CreateAccountHandler());
		SD.sController.addHandler(EGMI.EGMI_LOGIN_REQ_VALUE, new CA_LoginHandler());
		SD.sController.addHandler(EGMI.EGMI_SERVER_QUERY_TOKEN_REQ_VALUE, new SyncTokenHandler());


	}

	public static void initForwardHnadler()
	{
	}

	public static void initHttpHandler()
	{
	}

}
