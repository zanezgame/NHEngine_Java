package nicehu.server.gameserver.logic.initinfo.util;

import java.util.Map;

import nicehu.server.gameserver.logic.dungeon.data.Dungeon;
import nicehu.server.gameserver.logic.dungeon.data.DungeonData;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.item.data.Item;
import nicehu.server.gameserver.logic.item.data.ItemData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class InitInfoUtil
{

	public static void genInitInfoResutl(JSONObject result, PlayerNode playerNode)
	{
		JSONObject resultData = new JSONObject();
		//resultData.put("usid", playerNode.getGamePlayer().getShowId());
		resultData.put("currency", playerNode.getGamePlayer().getMoney());
		JSONArray itemsArray = new JSONArray();
		ItemData itemData = playerNode.getInfoPlayer().getItemData();
		for (Map.Entry<Integer, Item> entry : itemData.getItems().entrySet())
		{
			JSONObject itemObj = new JSONObject();
			itemObj.put("id", entry.getValue().getId());
			itemObj.put("count", entry.getValue().getCount());
			itemsArray.add(itemObj);
		}
		resultData.put("items", itemsArray);
		JSONArray dungeonsArray = new JSONArray();
		DungeonData dungeonData = playerNode.getCachePlayer().getDungeonData();
		for (Map.Entry<Integer, Dungeon> entry : dungeonData.getDungeons().entrySet())
		{
			JSONObject itemObj = new JSONObject();
			itemObj.put("id", entry.getValue().getId());
			itemObj.put("score", entry.getValue().getScore());
			itemObj.put("stars", entry.getValue().getStars());
			itemsArray.add(itemObj);
		}
		resultData.put("level", dungeonsArray);
		result.put("data", resultData);
	}

}
