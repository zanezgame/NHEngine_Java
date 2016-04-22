package nicehu.server.gameserver.logic.costandreward;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.util.IdTypeU;
import nicehu.pb.NHDefine;
import nicehu.pb.NHDefine.EIdType;
import nicehu.server.gameserver.logic.costandreward.struct.Cost;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;

public class CRSCheck
{
	private static final Logger logger = LoggerFactory.getLogger(CRSCheck.class);

	public static boolean checkCost(PlayerNode playerNode, List<Cost> costs, int eventId, Cost notEnoughCost)
	{
		Cost notEnoughCostTemp = new Cost();
		for (Cost cost : costs)
		{
			if (CRSCheck.checkCost(playerNode, cost, eventId, notEnoughCostTemp) == false)
			{
				notEnoughCost.copy(notEnoughCostTemp);
				return false;
			}
		}
		return true;
	}

	public static boolean checkCost(PlayerNode playerNode, Cost cost, int eventId, Cost notEnoughCost)
	{
		// 检查cost的count
		if (cost.getCount() < 0)
		{
			logger.error("checkCost playerId={} cost.getCount()={} eventId={}", new Object[] {playerNode.getPlayerId(), cost.getCount(), eventId});
			return false;
		}

		NHDefine.EIdType idType = IdTypeU.getIdType(cost.getId());
		switch (idType)
		{
			case EIT_Special:
			{
				boolean result = check_Special(playerNode, cost, eventId);
				if (result == false)
				{
					notEnoughCost.setId(cost.getId());
					notEnoughCost.setCount(cost.getCount());
					return false;
				}
				break;
			}
			case EIT_Item:
			{
				return check_Item(playerNode, cost, eventId, notEnoughCost);
			}
			default:
			{
				return true;
			}
		}
		return true;
	}

	public static boolean check_Special(PlayerNode playerNode, Cost cost, int eventId)
	{
		switch (cost.getId())
		{
			case NHDefine.EIdSpecial.EIS_Money_VALUE:// 钱
			{
				if (playerNode.getGamePlayer().getMoney() - cost.getCount() < 0)
				{
					return false;
				}
				break;
			}

			default:
			{
				break;
			}
		}
		return true;
	}

	public static boolean check_Item(PlayerNode playerNode, Cost cost, int eventId, Cost notEnoughCost)
	{
		if (playerNode.getInfoPlayer().getItemData().getItemCount(cost.getId()) - cost.getCount() < 0)
		{
			notEnoughCost.setId(cost.getId());
			notEnoughCost.setCount(cost.getCount());
			return false;
		}
		return true;
	}

}
