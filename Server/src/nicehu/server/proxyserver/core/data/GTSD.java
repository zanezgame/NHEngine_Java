package nicehu.server.proxyserver.core.data;

import nicehu.nhsdk.candy.collect.lru.LruMap;
import nicehu.nhsdk.candy.time.Time;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.session.GateSession;

public class GTSD extends SD
{
	// gateserver max have 20wSession ,means gateServer max can handler 20w OnlinePlayer
	public static LruMap<Integer, GateSession> sessions = new LruMap<>(200000,Time.HOUR);

}
