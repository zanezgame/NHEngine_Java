package nicehu.nhsdk.core.controller;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.google.protobuf.MessageLite;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.core.data.session.GameSession;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.handler.LogicHandler;

public class Controller
{
	private static Logger logger = LogU.getLogger(Controller.class);

	private Map<Integer, MessageLite> protobufs = new ConcurrentHashMap<>();
	 Map<Integer, LogicHandler> handlers = new ConcurrentHashMap<>();

	public void addHandler(int pId, LogicHandler logicHandler)
	{
		if (handlers.containsKey(pId))
		{
			logger.error("already register this id and handler!!!  id" + pId);
		}
		handlers.put(pId, logicHandler);
	}

	public void addProto(int protocolID, MessageLite protoBufMessageLite)
	{
		protobufs.put(protocolID, protoBufMessageLite);
	}

	public MessageLite getProtobuf(int pId)
	{
		return protobufs.get(pId);
	}

	public void procProto(ChannelHandlerContext ctx, Message msg)
	{
		LogicHandler handler = this.handlers.get(msg.getId());
		if (handler == null)
		{
			return;
		}
		handler.handle(ctx, msg);
	}

	public void procProto(ServerNode serverNode, Message msg)
	{
		LogicHandler handler = this.handlers.get(msg.getId());
		if (handler == null)
		{
			return;
		}
		handler.handle(serverNode, msg);
	}

	public void procProto(GameSession session, Message msg)
	{
		LogicHandler handler = this.handlers.get(msg.getId());
		if (handler == null)
		{
			return;
		}
		handler.handle(session, msg);
	}

}
