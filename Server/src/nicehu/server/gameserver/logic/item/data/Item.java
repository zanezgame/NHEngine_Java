package nicehu.server.gameserver.logic.item.data;

public class Item
{
	private int id;
	private int count;

	public Item clone()
	{
		return new Item(this.id, this.count);
	}

	public void addCount(int delta)
	{
		this.count = this.count + delta;
	}

	public Item()
	{

	}

	public Item(String id, String count)
	{
		this.id = Integer.parseInt(id);
		this.count = Integer.parseInt(count);
	}

	public Item(int id, int count)
	{
		this.id = id;
		this.count = count;
	}

	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

}
