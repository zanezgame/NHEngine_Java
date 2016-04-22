package nicehu.server.gameserver.logic.costandreward;

import java.util.List;

import nicehu.nhsdk.util.IdTypeU;
import nicehu.pb.NHDefine;
import nicehu.server.gameserver.logic.costandreward.struct.CRS;
import nicehu.server.gameserver.logic.costandreward.struct.Cost;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.item.data.ItemMgr;
import nicehu.server.gameserver.logic.mgr.PAM;

public class CRSCost
{
	public static boolean costs(PlayerNode playerNode, List<Cost> costs, CRS crs, int eventId)
	{
		Cost notEnough = new Cost();
		if (!CRSCheck.checkCost(playerNode, costs, eventId, notEnough))
		{
			crs.setNotEnoughCost(notEnough);
			return false;
		}
		CRSCost.cost(playerNode, costs, eventId);
		crs.mergeCosts(costs);
		return true;

	}

	private static void cost(PlayerNode playerNode, List<Cost> costs, int eventId)
	{
		for (Cost cost : costs)
		{
			cost(playerNode, cost, eventId);
		}
	}

	private static void cost(PlayerNode playerNode, Cost cost, int eventId)
	{
		NHDefine.EIdType idType = IdTypeU.getIdType(cost.getId());
		switch (idType)
		{
			case EIT_Special:
			{
				cost_Special(playerNode, cost, eventId);
				break;
			}
			case EIT_Item:
			{
				ItemMgr.addItemCount(playerNode, cost.getId(), -cost.getCount(), eventId);
				break;
			}
			default:
			{
				break;
			}
		}
	}

	private static void cost_Special(PlayerNode playerNode, Cost cost, int eventId)
	{
		switch (cost.getId())
		{
			case NHDefine.EIdSpecial.EIS_Money_VALUE:// 钱
			{
				PAM.addMoney(playerNode, -cost.getCount(), eventId);
				break;
			}
			default:
			{
				break;
			}
		}
	}

}
