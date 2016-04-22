package nicehu.nhsdk.core.type;

import java.util.ArrayList;
import java.util.List;

import nicehu.nhsdk.candy.object.Empty;

public class ServerType
{
	public static final int MANAGE = 0x00010000;
	public static final int AUTH = 0x00020000;
	public static final int PROXY = 0x00030000;
	public static final int GAME = 0x00050000;
	public static final int WORLD = 0x00060000;

	public static int getType(Integer id)
	{
		return 0xffff0000 & id;
	}

	public static int getId(Integer id)
	{
		return 0x0000ffff & id;
	}

	public static List<Integer> getCareServerTypes(int serverType)
	{
		List<Integer> serverTypes = new ArrayList<Integer>();
		switch (serverType)
		{
			case MANAGE:
			{
				serverTypes.add(MANAGE);
				break;
			}
			case AUTH:
			{
				serverTypes.add(GAME);
				serverTypes.add(PROXY);
				break;
			}
			case PROXY:
			{
				serverTypes.add(GAME);
				break;
			}
			case GAME:
			{
				serverTypes.add(AUTH);
				serverTypes.add(PROXY);
				serverTypes.add(WORLD);
				break;
			}
			case WORLD:
			{
				serverTypes.add(GAME);
				break;
			}

		}
		return serverTypes;
	}

	public static String getName(Integer id)
	{
		switch (0xffff0000 & id)
		{
			case MANAGE:
			{
				return "MANAGE";
			}
			case AUTH:
			{
				return "AUTH";
			}
			case PROXY:
			{
				return "PROXY";
			}
			case GAME:
			{
				return "GAME";
			}
			case WORLD:
			{
				return "WORLD";
			}

		}
		return "";
	}

	public static int getType(String name)
	{
		if (Empty.is(name))
		{
			return 0;
		}
		switch (name)
		{
			case "MANAGE":
			{
				return MANAGE;
			}
			case "AUTH":
			{
				return AUTH;
			}
			case "PROXY":
			{
				return PROXY;
			}
			case "GAME":
			{
				return GAME;
			}
			case "WORLD":
			{
				return WORLD;
			}

		}
		return 0;
	}
}
