package nicehu.nhsdk.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.session.GameSession;
import nicehu.nhsdk.core.handler.LogicHandler;

public class GameController extends Controller
{
	private static Logger logger = LoggerFactory.getLogger(GameController.class);

	@Override
	public void procProto(GameSession session, Message md)
	{
		LogicHandler handler = handlers.get(md.getId());
		if (handler != null && session != null)
		{
			session.setUpdateTime(System.currentTimeMillis());
			handler.handle(session, md);
		}
		else if (handler == null)
		{
			logger.error("Do With Protocol Faild!!! No Handler Match!!!  msgId: " + md.getId());
		}
		else if (session == null)
		{
			logger.error("Do With Protocol Faild!!! Session is Null !!!  msgId: " + md.getId());
		}

	}
}
