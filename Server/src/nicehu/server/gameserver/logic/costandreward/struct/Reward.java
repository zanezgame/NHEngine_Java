package nicehu.server.gameserver.logic.costandreward.struct;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.server.gameserver.logic.item.data.Item;
import nicehu.server.gameserver.logic.item.util.ItemUtil;

public class Reward
{
	private static final Logger logger = LoggerFactory.getLogger(Reward.class);
	private ArrayList<Item> specials = new ArrayList<Item>();
	private ArrayList<Item> items = new ArrayList<Item>();

	public static Reward genReward(int id, int count)
	{
		Reward reward = new Reward();
		reward.getItems().add(new Item(id, count));
		return reward;
	}

	public Reward megerReward(Reward reward)
	{
		ItemUtil.mergeItem(specials, reward.clone().getSpecials());
		ItemUtil.mergeItem(items, reward.clone().getItems());
		return this;
	}

	public Reward addSpecial(int id, int count, boolean compress)
	{
		if (compress)
		{
			Item exsitSpecial = ItemUtil.find(this.getSpecials(), id);
			if (exsitSpecial != null)
			{
				exsitSpecial.setCount(count + exsitSpecial.getCount());
				return this;
			}

		}
		this.getSpecials().add(new Item(id, count));
		return this;
	}

	public Reward addItem(int id, int count, boolean compress)
	{
		if (compress)
		{
			Item exsitItem = ItemUtil.find(this.getItems(), id);
			if (exsitItem != null)
			{
				exsitItem.setCount(count + exsitItem.getCount());
				return this;
			}

		}
		this.getItems().add(new Item(id, count));
		return this;
	}

	public void compress()
	{
		{
			ArrayList<Item> newSpecials = new ArrayList<Item>();
			for (Item special : specials)
			{
				Item exsitItem = ItemUtil.find(newSpecials, special.getId());
				if (exsitItem != null)
				{
					exsitItem.setCount(special.getCount() + exsitItem.getCount());
				}
				else
				{
					newSpecials.add(special.clone());
				}
			}
			this.setSpecials(newSpecials);
		}
		{
			ArrayList<Item> newItems = new ArrayList<Item>();
			for (Item item : items)
			{
				Item exsitItem = ItemUtil.find(newItems, item.getId());
				if (exsitItem != null)
				{
					exsitItem.setCount(item.getCount() + exsitItem.getCount());
				}
				else
				{
					newItems.add(item.clone());
				}
			}
			this.setItems(newItems);
		}

	}

	public Reward clone()
	{
		Reward clone = new Reward();
		for (Item special : this.getSpecials())
		{
			clone.getSpecials().add(special.clone());
		}
		for (Item item : this.getItems())
		{
			clone.getItems().add(item.clone());
		}
		return clone;
	}

	public Reward()
	{

	}

	public ArrayList<Item> getItems()
	{
		return items;
	}

	public void setItems(ArrayList<Item> items)
	{
		this.items = items;
	}

	public ArrayList<Item> getSpecials()
	{
		return specials;
	}

	public void setSpecials(ArrayList<Item> specials)
	{
		this.specials = specials;
	}

}
