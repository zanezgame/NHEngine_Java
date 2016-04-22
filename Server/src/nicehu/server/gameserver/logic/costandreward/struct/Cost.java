package nicehu.server.gameserver.logic.costandreward.struct;

import java.util.List;

public class Cost
{
	private int id;
	private int count;
	private String guid;

//	public static ArrayList<Cost> getCostsFromConfig(system.Collections.Generic.List_1 costs)
//	{
//		ArrayList<Cost> results = new ArrayList<Cost>();
//		for (int i = 0; i < costs.get_Count(); i++)
//		{
//			ClientServerCommon.Cost cost = (ClientServerCommon.Cost)costs.get_Item(i);
//			results.add(new Cost(cost.get_Id(), cost.get_Count()));
//		}
//		return results;
//	}

	public static List<Cost> mergeCost(List<Cost> costs1, List<Cost> costs2)
	{
		for (Cost cost : costs2)
		{
			if (cost.getGuid() != null)
			{
				costs1.add(cost);
				continue;
			}

			boolean exist = false;
			for (int index = 0; index < costs1.size(); index++)
			{
				if (costs1.get(index).getId() == cost.getId())
				{
					exist = true;
					int count = costs1.get(index).getCount() + cost.getCount();
					costs1.get(index).setCount(count);
				}
			}

			if (exist == false)
			{
				costs1.add(cost);
			}
		}
		return costs1;
	}

	public void copy(Cost cost)
	{
		id = cost.getId();
		if (cost.getGuid() != null)
		{
			guid = cost.getGuid();
		}
		count = cost.getCount();
	}

	public String toString()
	{
		String sGuid = "";
		if (guid != null)
		{
			sGuid = guid.toString();
		}
		return "Cost [id=" + id + ", count=" + count + ", guid=" + sGuid + "]";
	}

	public Cost()
	{
	}

	public Cost(int id, int count)
	{
		this.id = id;
		this.count = count;
	}

	public Cost(int id, String guid)
	{
		this.id = id;
		this.guid = guid;
	}

	public Cost(int id, int count, String guid)
	{
		this.id = id;
		this.count = count;
		this.guid = guid;
	}

	public int getId()
	{
		return id;
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

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

}
