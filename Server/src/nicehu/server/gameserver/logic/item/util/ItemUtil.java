package nicehu.server.gameserver.logic.item.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nicehu.server.gameserver.logic.item.data.Item;

public class ItemUtil
{
	// 合并道具
	public static void mergeItem(ArrayList<Item> items, ArrayList<Item> itemsAdd)
	{
		// 复制
		HashMap<Integer, Item> itemMap = new HashMap<Integer, Item>();
		for (Item item : items)
		{
			itemMap.put(item.getId(), item);
		}
		// 合并
		for (Item item : itemsAdd)
		{
			if (itemMap.containsKey(item.getId()))
			{
				Item tmp = itemMap.get(item.getId());
				tmp.addCount(item.getCount());
			}
			else
			{
				itemMap.put(item.getId(), item);
			}
		}
		// 重新生成
		items.clear();
		for (Map.Entry<Integer, Item> entry : itemMap.entrySet())
		{
			items.add(entry.getValue());
		}
	}

	public static Item find(ArrayList<Item> items, int id)
	{
		for (Item item : items)
		{
			if (item.getId() == id)
			{
				return item;
			}
		}

		return null;
	}

}
