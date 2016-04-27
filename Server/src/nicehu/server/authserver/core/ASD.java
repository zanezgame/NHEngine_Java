package nicehu.server.authserver.core;

import nicehu.nhsdk.candy.collect.lru.LruMap;
import nicehu.nhsdk.candy.time.Time;
import nicehu.nhsdk.core.data.SD;

public class ASD extends SD
{
	public static LruMap<Integer, AuthSession> sessions = new LruMap<>(200000, Time.DAY * 3);

}
