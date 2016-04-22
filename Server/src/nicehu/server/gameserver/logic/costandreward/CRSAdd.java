package nicehu.server.gameserver.logic.costandreward;

import nicehu.pb.NHDefine.EIdSpecial;
import nicehu.server.gameserver.logic.costandreward.struct.Reward;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.item.data.Item;
import nicehu.server.gameserver.logic.item.data.ItemMgr;
import nicehu.server.gameserver.logic.mgr.PAM;

public class CRSAdd
{

	public static void addReward(PlayerNode playerNode, Reward reward, int eventId)
	{
		if (reward == null)
			return;

		for (Item special : reward.getSpecials())
		{
			CRSAdd.add_Special(playerNode, special, eventId);
		}
		for (Item item : reward.getItems())
		{
			ItemMgr.addItemCount(playerNode, item.getId(), item.getCount(), eventId);
		}
	}

	private static void add_Special(PlayerNode playerNode, Item item, int eventId)
	{
		switch (item.getId())
		{
			case EIdSpecial.EIS_Money_VALUE:
			{
				PAM.addMoney(playerNode, item.getCount(), eventId);
				break;
			}
			default:
			{
				break;
			}
		}
	}

}
