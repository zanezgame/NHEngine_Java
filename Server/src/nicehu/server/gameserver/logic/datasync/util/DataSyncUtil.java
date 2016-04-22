package nicehu.server.gameserver.logic.datasync.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import nicehu.nhsdk.candy.json.JsonU;
import nicehu.server.gameserver.logic.datasync.data.SData;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.nhsdk.util.TextConfigU;

public class DataSyncUtil
{

	public static void checkCurrency(JSONObject result, JSONObject data, PlayerNode playerNode, SData currency)
	{
		if (JsonU.check(data, "currency"))
		{
			JSONObject currentObj = data.getJSONObject("currency");
			if (JsonU.check(currentObj, "base", "change"))
			{
				int base = currentObj.getInt("base");
				if (playerNode.getGamePlayer().getMoney() != base)
				{
					JsonU.updateErr(result, TextConfigU.get("E_GAME_DATA_SYNC_MONEY_NUMBER"));
					return;
				}
				currency.setBase(base);
				JSONArray changeArray = currentObj.getJSONArray("change");
				for (int i = 0; i < changeArray.size(); i++)
				{
					JSONObject changeObj = changeArray.getJSONObject(i);
					if (JsonU.check(changeObj, "num", "type"))
					{
						int num = changeObj.getInt("num");
						int type = changeObj.getInt("type");
						currency.addChange(num, type);
					}
					else
					{
						JsonU.updateErr(result, TextConfigU.get("E_DATA_STRUCT"));
						return;
					}

				}
			}
			else
			{
				JsonU.updateErr(result, TextConfigU.get("E_DATA_STRUCT"));
				return;
			}
		}
		return;
	}

	public static void checkItems(JSONObject result, JSONObject data, PlayerNode playerNode, List<SData> items)
	{
		if (JsonU.check(data, "items"))
		{
			JSONArray itemsArray = data.getJSONArray("items");
			for (int i = 0; i < itemsArray.size(); i++)
			{
				JSONObject itemObject = itemsArray.getJSONObject(i);
				if (JsonU.check(itemObject, "id", "base", "change"))
				{
					int id = itemObject.getInt("id");
					int base = itemObject.getInt("base");
					if (playerNode.getInfoPlayer().getItemData().getItemCount(id) != base)
					{
						JsonU.updateErr(result, TextConfigU.get("E_GAME_DATA_SYNC_ITEM_NUM"));
						return;
					}
					SData item = new SData(id, base);
					JSONArray changeArray = itemObject.getJSONArray("change");
					for (int j = 0; j < changeArray.size(); j++)
					{
						JSONObject changeObj = changeArray.getJSONObject(j);
						if (JsonU.check(changeObj, "num", "type"))
						{
							int num = changeObj.getInt("num");
							int type = changeObj.getInt("type");
							item.addChange(num, type);
						}
						else
						{
							JsonU.updateErr(result, TextConfigU.get("E_DATA_STRUCT"));
							return;
						}
					}
					items.add(item);
				}
				else
				{
					JsonU.updateErr(result, TextConfigU.get("E_DATA_STRUCT"));
					return;
				}

			}
		}
		return;
	}

	public static void checkLevels(JSONObject result, JSONObject data, PlayerNode playerNode, List<SData> levels, List<Integer> dungeonIds)
	{
		if (JsonU.check(data, "levels"))
		{
			JSONArray levelArray = data.getJSONArray("levels");
			for (int i = 0; i < levelArray.size(); i++)
			{
				JSONObject levelObject = levelArray.getJSONObject(i);
				if (JsonU.check(levelObject, "id", "score", "stars"))
				{
					int id = levelObject.getInt("id");
					int score = levelObject.getInt("score");
					int stars = levelObject.getInt("stars");
					dungeonIds.add(id);
					levels.add(new SData(id, score, stars));
				}
				else
				{
					JsonU.updateErr(result, TextConfigU.get("E_DATA_STRUCT"));
					return;
				}

			}
		}
		{
			Collections.sort(dungeonIds, new IntComparator());
			int maxDungeonId = playerNode.getGamePlayer().getDungeonMaxId();
			for (int dungeonId : dungeonIds)
			{
				if (dungeonId > maxDungeonId + 1)
				{
					JsonU.updateErr(result, TextConfigU.get("E_GAME_DATA_SYNC_DUNGEON_DATA"));
					return;
				}
				if (dungeonId == maxDungeonId + 1)
				{
					maxDungeonId++;
				}
			}
		}
		return;
	}
}

class IntComparator implements Comparator<Integer>
{
	public int compare(Integer o1, Integer o2)
	{
		return o1 - o2;
	}

}
