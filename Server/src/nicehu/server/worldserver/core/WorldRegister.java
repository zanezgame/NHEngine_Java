package nicehu.server.worldserver.core;

import nicehu.server.common.core.CommonHandlerRegister;

public class WorldRegister
{

	public static void init()
	{
		CommonHandlerRegister.init();
		WorldRegister.registerPB();
		WorldRegister.registerHandler();
	}

	public static void registerPB()
	{
	}

	public static void registerHandler()
	{

	}

}
