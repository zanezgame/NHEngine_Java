package nicehu.server.common.dblog;

import nicehu.nhsdk.candy.time.TimeU;
import nicehu.nhsdk.core.data.AreaData;
import nicehu.nhsdk.core.data.session.GameSession;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;

public class LogUtil
{

	public static void authToken(PlayerNode playerNode, String ip)
	{
		LogBase.authToken(TimeU.getStr(), playerNode.getPlayerId(), AreaData.getAreaId(), ip);
	}

	public static void login(PlayerNode playerNode, GameSession session)
	{
		LogBase.login(TimeU.getStr(), playerNode.getPlayerId(), AreaData.getAreaId(), "GameSession not  have Channle  anymore");
	}

	public static void special(PlayerNode playerNode, int event, int id, long after, long from, int change)
	{
		LogBase.special(TimeU.getStr(), playerNode.getPlayerId(), event, id, after, from, change);
	}

	public static void item(PlayerNode playerNode, int event, int id, long after, long from, int change)
	{
		LogBase.item(TimeU.getStr(), playerNode.getPlayerId(), event, id, after, from, change);
	}

	public static void onlineStatus(Integer areaId, String startTime, String endTime, Integer max, Integer min, Integer avg, Integer online_Max, Integer offline_Max,
		Integer cache_Max, Integer onlineNum, Integer offlineNum, Integer cacheNum, Integer sessionSize, Integer tokenSize)
	{
		LogBase.onlineStatus(areaId, startTime, endTime, max, min, avg, online_Max, offline_Max, cache_Max, onlineNum, offlineNum, cacheNum, sessionSize, tokenSize);
	}

	public static void serverStatistics(Integer areaId, String startTime, String endTime, Integer online_player_count, Integer offline_player_count,
		Integer global_player_count, Integer session_count, Integer token_count, Integer data_total_player_count, Integer data_info_player_count,
		Integer data_cache_player_count, Integer unwrite_prompt_sql_count, Integer unwrite_cache_sql_count, Integer memcache_fail_count, Integer memcache_unwrite_count,
		Integer memcache_open)
	{
		LogBase.serverStatistics(areaId,
			startTime,
			endTime,
			online_player_count,
			offline_player_count,
			global_player_count,
			session_count,
			token_count,
			data_total_player_count,
			data_info_player_count,
			data_cache_player_count,
			unwrite_prompt_sql_count,
			unwrite_cache_sql_count,
			memcache_fail_count,
			memcache_unwrite_count,
			memcache_open);
	}

}
