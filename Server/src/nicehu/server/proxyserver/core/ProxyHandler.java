package nicehu.server.proxyserver.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.handler.ServerHandler;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgGame.AuthTokenReq;
import nicehu.pb.NHMsgGame.AuthTokenRes;
import nicehu.pb.NHMsgGame.QueryPlayerReq;
import nicehu.pb.NHMsgGame.QueryPlayerRes;

public class ProxyHandler
{
	public static void init()
	{
		ServerHandler.init();
		ProxyHandler.initProtobuf();
		ProxyHandler.initSocketHandler();
		ProxyHandler.initForwardHnadler();
		ProxyHandler.initHttpHandler();
		// GateServer转发专用
		ProxyHandler.initForwardProtobuf();
	}

	public static void initProtobuf()
	{

	}

	public static void initSocketHandler()
	{

	}

	public static void initForwardHnadler()
	{
	}

	public static void initHttpHandler()
	{
	}

	public static void initForwardProtobuf()
	{
		SD.sController.addProto(EGMI.EGMI_AUTH_TOKEN_REQ_VALUE, AuthTokenReq.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_AUTH_TOKEN_RES_VALUE, AuthTokenRes.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_QUERY_PLAYER_REQ_VALUE, QueryPlayerReq.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_QUERY_PLAYER_RES_VALUE, QueryPlayerRes.getDefaultInstance());
	}

}
