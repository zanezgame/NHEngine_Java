package nicehu.server.gameserver.logic.item.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ItemData
{
	private HashMap<Integer, Item> items = new HashMap<Integer, Item>();

	public String toDBString()
	{
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, Item> entry : items.entrySet())
		{
			Item item = entry.getValue();
			if (sb.length() != 0)
			{
				sb.append("|");
			}
			sb.append(item.getId()).append("&").append(item.getCount());
		}
		return sb.toString();
	}

	public void fromDBString(String str)
	{
		List<String> itemStrs = Arrays.asList(str.split("\\|"));
		for (String itemStr : itemStrs)
		{
			String[] datas = itemStr.split("&");
			if (datas.length == 2)
			{
				Item item = new Item(datas[0], datas[1]);
				this.items.put(item.getId(), item);
			}
		}
	}

	public Item getItem(int id)
	{
		return items.get(id);
	}

	public int getItemCount(int id)
	{
		Item item = this.getItem(id);
		if (item != null)
		{
			return item.getCount();
		}
		return 0;
	}

	public boolean testAddItemCount(int id, int change)
	{
		if (change == 0)
		{
			return true;
		}
		if (getItemCount(id) + change >= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean addItemCount(int id, int change)
	{
		if (this.testAddItemCount(id, change))
		{
			int newCount = this.getItemCount(id) + change;
			if (newCount > 0)
			{
				Item item = new Item(id, newCount);
				this.getItems().put(id, item);
			}
			else
			{
				this.getItems().remove(id);
			}
			return true;
		}
		return false;
	}

	@JsonIgnore
	public ArrayList<Item> getItemsList()
	{
		return new ArrayList<Item>(items.values());
	}

	public HashMap<Integer, Item> getItems()
	{
		return items;
	}

	public void setItems(HashMap<Integer, Item> items)
	{
		this.items = items;
	}

}
