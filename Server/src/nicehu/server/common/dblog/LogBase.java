package nicehu.server.common.dblog;

public class LogBase
{

	private static final String[] table_auth_token = new String[] {"auth_token", "time", "player_id", "area_id", "ip"};

	public static void authToken(String time, int playerId, int areaId, String ip)
	{
		LogDB.addLog(table_auth_token, time, playerId, areaId, ip);
	}

	private static final String[] table_login = new String[] {"login", "time", "player_id", "area_id", "ip"};

	public static void login(String time, int playerId, int areaId, String ip)
	{
		LogDB.addLog(table_login, time, playerId, areaId, ip);
	}

	private static final String[] table_special = new String[] {"special", "time", "player_id", "event_id", "vid", "vafter", "vbefore", "vchange",};

	public static void special(String time, int playerId, int eventId, int id, long after, long from, int change)
	{
		LogDB.addLog(table_special, time, playerId, eventId, id, after, from, change);
	}

	private static final String[] table_item = new String[] {"item", "time", "player_id", "event_id", "vid", "vafter", "vbefore", "vchange",};

	public static void item(String time, int playerId, int eventId, int id, long after, long from, int change)
	{
		LogDB.addLog(table_item, time, playerId, eventId, id, after, from, change);
	}

	private static final String[] table_online_status = new String[] {"online_status", "area_id", "start_time", "end_time", "max", "min", "avg", "online_max",
		"offline_max", "cache_max", "online_num", "offline_num", "cache_num", "session_size", "token_size"};

	public static void onlineStatus(Integer areaId, String startTime, String endTime, Integer max, Integer min, Integer avg, Integer online_Max, Integer offline_Max,
		Integer cache_Max, Integer onlineNum, Integer offlineNum, Integer cacheNum, Integer sessionSize, Integer tokenSize)
	{
		LogDB.addLog(table_online_status,
			areaId,
			startTime,
			endTime,
			max,
			min,
			avg,
			online_Max,
			offline_Max,
			cache_Max,
			onlineNum,
			offlineNum,
			cacheNum,
			sessionSize,
			tokenSize);
	}

	private static final String[] table_server_statistics = new String[] {"server_statistics", "area_id", "start_time", "end_time", "online_player_count",
		"offline_player_count", "global_player_count", "session_count", "token_count", "data_total_player_count", "data_info_player_count", "data_cache_player_count","unwrite_prompt_sql_count",
		"unwrite_cache_sql_count", "memcache_fail_count", "memcache_unwrite_count", "memcache_open"};

	public static void serverStatistics(Integer areaId, String startTime, String endTime, Integer online_player_count, Integer offline_player_count,
		Integer global_player_count, Integer session_count, Integer token_count, Integer data_total_player_count, Integer data_info_player_count,Integer data_cache_player_count,
		Integer unwrite_prompt_sql_count, Integer unwrite_cache_sql_count, Integer memcache_fail_count, Integer memcache_unwrite_count, Integer memcache_open)
	{
		LogDB.addLog(table_server_statistics,
			areaId,
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
