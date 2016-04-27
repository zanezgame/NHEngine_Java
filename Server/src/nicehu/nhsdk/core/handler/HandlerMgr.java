package nicehu.nhsdk.core.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.google.protobuf.MessageLite;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.server.gameserver.core.GameSession;

public class HandlerMgr
{
	private static Logger logger = LogU.getLogger(HandlerMgr.class);

	private Map<Integer, LogicHandler> handlers = new ConcurrentHashMap<>();

	public void addHandler(int pId, LogicHandler logicHandler)
	{
		if (handlers.containsKey(pId))
		{
			logger.error("already register this id and handler!!!  id" + pId);
		}
		handlers.put(pId, logicHandler);
	}

	public void handle(ChannelHandlerContext ctx, Message msg)
	{
		LogicHandler handler = this.handlers.get(msg.getId());
		if (handler == null)
		{
			return;
		}
		handler.handle(ctx, msg);
	}

	public void handle(ConnectNode serverNode, Message msg)
	{
		LogicHandler handler = this.handlers.get(msg.getId());
		if (handler == null)
		{
			return;
		}
		handler.handle(serverNode, msg);
	}

	public void handle(GameSession session, Message msg)
	{
		LogicHandler handler = handlers.get(msg.getId());
		if (handler != null && session != null)
		{
			session.setUpdateTime(System.currentTimeMillis());
			handler.handle(session, msg);
		}
		else if (handler == null)
		{
			logger.error("Do With Protocol Faild!!! No Handler Match!!!  msgId: " + msg.getId());
		}
		else if (session == null)
		{
			logger.error("Do With Protocol Faild!!! Session is Null !!!  msgId: " + msg.getId());
		}
	}

}
