package nicehu.nhsdk.core.db;

import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.struct.Pair;
import nicehu.nhsdk.core.data.AreaData;

public class DBEvent
{
	public static final int PLAYER_UPDATE_PLAYERLEVELANDEXP = 100;
	public static final int PLAYER_UPDATE_PLAYER_ATTR = 101;

	// dungeon
	public static final int DUNGEON_UPDATE = 200;
	// item
	public static final int ITEM_UPDATE = 210;
	// friend
	public static final int FRIEND_UPDATE = 220;
	// email
	public static final int EMAIL_UPDATE = 230;

	public static String genKey(int dbEvent, int playerId)
	{
		return AreaData.getAreaId() + "|" + dbEvent + "|" + playerId;
	}

	public static Pair<Integer, Integer> getEventAndId(String key)
	{
		String[] strs = key.split("\\|");
		if (strs.length == 3)
		{
			return new Pair<Integer, Integer>(ParseU.pInt(strs[1]), ParseU.pInt(strs[2]));
		}
		return null;
	}
}
