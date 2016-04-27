package nicehu.server.gameserver.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgGame.AuthTokenReq;
import nicehu.pb.NHMsgGame.QueryPlayerReq;
import nicehu.pb.NHMsgServer.QueryTokenRes;
import nicehu.server.common.core.CommonHandlerRegister;
import nicehu.server.gameserver.logic.authtoken.handler.QueryTokenResHandler;
import nicehu.server.gameserver.logic.authtoken.handler.AuthTokenReqHandler;
import nicehu.server.gameserver.logic.initinfo.handler.QueryPlayerReqHandler;

public class GameHandlerRegister
{
	public static void init()
	{
		CommonHandlerRegister.init();

		SD.handlerMgr.addHandler(EGMI.EGMI_AUTH_TOKEN_REQ_VALUE, new AuthTokenReqHandler());
		SD.handlerMgr.addHandler(EGMI.EGMI_SERVER_QUERY_TOKEN_RES_VALUE, new QueryTokenResHandler());
		SD.handlerMgr.addHandler(EGMI.EGMI_QUERY_PLAYER_REQ_VALUE, new QueryPlayerReqHandler());
	}

}
