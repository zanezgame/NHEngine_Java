package nicehu.server.gameserver.logic.costandreward;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.server.gameserver.logic.costandreward.struct.CRS;
import nicehu.server.gameserver.logic.costandreward.struct.Cost;
import nicehu.server.gameserver.logic.costandreward.struct.Reward;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;

public class CRSManager
{
	private static final Logger logger = LoggerFactory.getLogger(CRSManager.class);

	public static boolean costs(PlayerNode playerNode, List<Cost> costs, CRS crs, int eventId)
	{
		return CRSCost.costs(playerNode, costs, crs, eventId);
	}

	public static void addReward(PlayerNode playerNode, Reward reward, int eventId)
	{
		CRSAdd.addReward(playerNode, reward, eventId);
	}

}
