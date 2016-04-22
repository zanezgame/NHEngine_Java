package nicehu.server.manageserver.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.handler.ServerHandler;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgServer.ReloadConfigRes;
import nicehu.pb.NHMsgServer.ServerLoginConfirm;
import nicehu.pb.NHMsgServer.ServerLoginReq;
import nicehu.server.manageserver.core.handler.XM_ReloadConfigConfirmHandler;
import nicehu.server.manageserver.logic.serverlogin.handler.XM_ServerLoginConfirmHandler;
import nicehu.server.manageserver.logic.serverlogin.handler.XM_ServerLoginReqHandler;

public class ManageHandler
{

	public static void init()
	{
		ServerHandler.init();
		ManageHandler.initProtobuf();
		ManageHandler.initSocketHandler();
		ManageHandler.initForwardHnadler();
		ManageHandler.initHttpHandler();
	}

	public static void initProtobuf()
	{
		SD.sController.addProto(EGMI.EGMI_SERVER_LOGIN_REQ_VALUE, ServerLoginReq.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_SERVER_LOGIN_CONFIRM_VALUE, ServerLoginConfirm.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_SERVER_RELOAD_CONFIG_RES_VALUE, ReloadConfigRes.getDefaultInstance());
	}

	public static void initSocketHandler()
	{
		SD.sController.addHandler(EGMI.EGMI_SERVER_LOGIN_REQ_VALUE, new XM_ServerLoginReqHandler());
		SD.sController.addHandler(EGMI.EGMI_SERVER_LOGIN_CONFIRM_VALUE, new XM_ServerLoginConfirmHandler());
		SD.sController.addHandler(EGMI.EGMI_SERVER_RELOAD_CONFIG_RES_VALUE, new XM_ReloadConfigConfirmHandler());
	}

	public static void initForwardHnadler()
	{
	}

	public static void initHttpHandler()
	{
	}

}
