package nicehu.nhsdk.core.type;

import java.util.ArrayList;
import java.util.List;

import nicehu.nhsdk.candy.object.Empty;

public class ServerType
{
	public static final int MANAGE = 0x00010000;
	public static final int AUTH = 0x00020000;
	public static final int GATE = 0x00030000;
	public static final int GAME = 0x00050000;
	public static final int CENTER = 0x00060000;
	public static final int PURCHASE = 0x00100000;

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
				serverTypes.add(GATE);
				break;
			}
			case GATE:
			{
				serverTypes.add(GAME);
				break;
			}
			case GAME:
			{
				serverTypes.add(AUTH);
				serverTypes.add(GATE);
				serverTypes.add(CENTER);
				break;
			}
			case CENTER:
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
			case GATE:
			{
				return "GATE";
			}
			case GAME:
			{
				return "GAME";
			}
			case CENTER:
			{
				return "CENTER";
			}

			case PURCHASE:
			{
				return "PURCHASE";
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
			case "GATE":
			{
				return GATE;
			}
			case "GAME":
			{
				return GAME;
			}
			case "CENTER":
			{
				return CENTER;
			}

			case "PURCHASE":
			{
				return PURCHASE;
			}
		}
		return 0;
	}
}
