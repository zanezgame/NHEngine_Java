package nicehu.nhsdk.util;

import nicehu.nhsdk.core.data.AreaData;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;

public class ServerUtil
{

	static public int getServerType(int serverID)
	{
		return serverID & 0xffff0000;
	}

	public static long getShowId(int playerId)
	{
		return AreaData.getAreaId() + (CommonConfigMgr.instance.cfg.getPlayerShowIdBase() + playerId);
	}

	public static int getPlayerId(long showId)
	{
		return (int)(showId - AreaData.getAreaId() - CommonConfigMgr.instance.cfg.getPlayerShowIdBase());
	}
}
