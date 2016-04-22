package nicehu.server.worldserver.core;

import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.handler.ServerHandler;
import nicehu.server.worldserver.logic.player.handler.GCT_QueryPlayerNodeHandler;

public class WorldHandler
{

	public static void init()
	{
		ServerHandler.init();
		WorldHandler.initProtobuf();
		WorldHandler.initSocketHandler();
		WorldHandler.initForwardHnadler();
		WorldHandler.initHttpHandler();
	}

	public static void initProtobuf()
	{
	}

	public static void initSocketHandler()
	{

	}

	public static void initForwardHnadler()
	{
		//SD.sController.addHandler(ServerProtos.P_CENTER_GCT_QUERY_PLAYERNODE, new GCT_QueryPlayerNodeHandler());

	}

	public static void initHttpHandler()
	{
	}

}
