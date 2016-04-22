package nicehu.nhsdk.core.sync;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncMgr
{
	private static ConcurrentHashMap<Integer, SyncMessage> messages = new ConcurrentHashMap<>();
	public static AtomicInteger seqIdSeed = new AtomicInteger();

	public static void receiveMessage(int seqId, Object message)
	{
		SyncMessage syncMessage = messages.get(seqId);
		if (syncMessage != null)
		{
			syncMessage.receivedResponse(message);
		}
	}

	public static SyncMessage addMessage(int seqId)
	{
		SyncMessage message = new SyncMessage(seqId);
		messages.put(seqId, message);
		return message;
	}

	public static SyncMessage removeMessage(int seqId)
	{
		return messages.remove(seqId);
	}

}
