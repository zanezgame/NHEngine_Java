package nicehu.server.proxyserver.core;

import java.util.concurrent.ConcurrentHashMap;

import nicehu.nhsdk.core.data.SD;

public class PSD extends SD
{
	public static ConcurrentHashMap<Integer, ProxySession> sessions = new ConcurrentHashMap<>();

}
