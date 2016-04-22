package nicehu.server.gameserver.logic.costandreward.struct;

import java.util.ArrayList;
import java.util.List;

public class CRS
{
	private ArrayList<Cost> costs = new ArrayList<Cost>();
	private Reward reward = new Reward();
	private Cost notEnoughCost = new Cost();
	private Reward viewFixReward = new Reward();// 开宝箱显示用的固定奖励
	private Reward viewRandomReward = new Reward();// 开宝箱显示用的随机奖励

	public CRS meger(CRS _temp)
	{
		CRS result = new CRS();
		result.copy(_temp);

		if (_temp.reward != null)
		{
			if (this.reward == null)
			{
				this.reward = new Reward();
			}
			this.reward.megerReward(_temp.reward);
		}

		if (_temp.costs != null)
		{
			if (this.costs == null)
			{
				this.costs = new ArrayList<Cost>();
			}

			Cost.mergeCost(this.costs, _temp.costs);
		}

		if (_temp.viewFixReward != null)
		{
			if (this.viewFixReward == null)
			{
				this.viewFixReward = new Reward();
			}
			this.viewFixReward.megerReward(_temp.viewFixReward);
		}

		if (_temp.viewRandomReward != null)
		{
			if (this.viewRandomReward == null)
			{
				this.viewRandomReward = new Reward();
			}
			this.viewRandomReward.megerReward(_temp.viewRandomReward);
		}

		return this;
	}

	public void mergeCosts(List<Cost> _costs)
	{
		if (this.costs == null)
		{
			this.costs = new ArrayList<Cost>();
		}
		if (_costs != null)
		{
			Cost.mergeCost(this.costs, _costs);
		}
	}

	public void mergeReward(Reward _reward)
	{
		if (this.reward == null)
		{
			this.reward = new Reward();
		}
		if (_reward != null)
		{
			this.reward.megerReward(_reward);
		}

	}

	public CRS copy(CRS costAndRewardAndSync)
	{
		if (costAndRewardAndSync.costs != null)
		{
			this.costs = new ArrayList<Cost>();
			for (Cost cost : costAndRewardAndSync.costs)
			{
				Cost _temp = new Cost();
				_temp.copy(cost);
				this.costs.add(_temp);
			}
		}

		if (costAndRewardAndSync.reward != null)
		{
			this.reward = costAndRewardAndSync.reward.clone();
		}

		if (costAndRewardAndSync.getNotEnoughCost() != null)
		{
			this.notEnoughCost = new Cost();
			this.notEnoughCost.copy(costAndRewardAndSync.getNotEnoughCost());
		}

		if (costAndRewardAndSync.viewFixReward != null)
		{
			this.viewFixReward = costAndRewardAndSync.viewFixReward.clone();
		}

		if (costAndRewardAndSync.viewRandomReward != null)
		{
			this.viewRandomReward = costAndRewardAndSync.viewRandomReward.clone();
		}

		return this;
	}

	public ArrayList<Cost> getCosts()
	{
		return costs;
	}

	public void setCosts(ArrayList<Cost> costs)
	{
		this.costs = costs;
	}

	public Cost getNotEnoughCost()
	{
		return notEnoughCost;
	}

	public void setNotEnoughCost(Cost notEnoughCost)
	{
		this.notEnoughCost = notEnoughCost;
	}

	public void setViewFixReward(Reward viewFixReward)
	{
		this.viewFixReward = viewFixReward;
	}

	public void setViewRandomReward(Reward viewRandomReward)
	{
		this.viewRandomReward = viewRandomReward;
	}

}
