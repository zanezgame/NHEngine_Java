package nicehu.server.gameserver.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.handler.ServerHandler;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgGame.AuthTokenReq;
import nicehu.pb.NHMsgGame.QueryPlayerReq;
import nicehu.pb.NHMsgServer.QueryTokenRes;
import nicehu.server.gameserver.logic.authtoken.handler.AG_QueryTokenResHandler;
import nicehu.server.gameserver.logic.authtoken.handler.CG_AuthTokenReqHandler;
import nicehu.server.gameserver.logic.initinfo.handler.CG_QueryPlayerReqHandler;

public class GameHandler
{
	public static void init()
	{
		ServerHandler.init();
		GameHandler.initProtobuf();
		GameHandler.initSocketHandler();
		GameHandler.initForwardHnadler();
		GameHandler.initHttpHandler();
	}

	public static void initProtobuf()
	{
		SD.sController.addProto(EGMI.EGMI_AUTH_TOKEN_REQ_VALUE, AuthTokenReq.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_SERVER_QUERY_TOKEN_RES_VALUE, QueryTokenRes.getDefaultInstance());
		SD.sController.addProto(EGMI.EGMI_QUERY_PLAYER_REQ_VALUE, QueryPlayerReq.getDefaultInstance());

	}

	public static void initSocketHandler()
	{
		SD.sController.addHandler(EGMI.EGMI_AUTH_TOKEN_REQ_VALUE, new CG_AuthTokenReqHandler());
		SD.sController.addHandler(EGMI.EGMI_SERVER_QUERY_TOKEN_RES_VALUE, new AG_QueryTokenResHandler());
		SD.sController.addHandler(EGMI.EGMI_QUERY_PLAYER_REQ_VALUE, new CG_QueryPlayerReqHandler());

	}

	public static void initForwardHnadler()
	{
	}

	public static void initHttpHandler()
	{
	}

}
