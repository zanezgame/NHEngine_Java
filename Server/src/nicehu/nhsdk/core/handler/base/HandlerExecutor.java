package nicehu.nhsdk.core.handler.base;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.authserver.core.AuthBaseHandler;
import nicehu.server.gameserver.core.GameBaseHandler;
import nicehu.server.manageserver.core.ManageBaseHandler;
import nicehu.server.proxyserver.core.ProxyBaseHandler;
import nicehu.server.worldserver.core.WorldBaseHandler;

public class HandlerExecutor implements Runnable
{
	private static Logger logger = LogU.getLogger(HandlerExecutor.class);

	private static Executor threadPool = null;

	public ConnectNode connectNode;
	public ChannelHandlerContext ctx;
	public Message message;

	public HandlerExecutor(ChannelHandlerContext ctx, Message message, ConnectNode serverNode)
	{
		super();
		this.connectNode = serverNode;
		this.ctx = ctx;
		this.message = message;
	}

	public static void init(int threadNum)
	{
		threadPool = Executors.newFixedThreadPool(threadNum);
	}

	public static void start(Runnable runnable)
	{
		threadPool.execute(runnable);
	}

	@Override
	public final void run()
	{
		try
		{
			StopWatch stopWatch = null;

			stopWatch = new Slf4JStopWatch("Socket Message " + Integer.toHexString(message.getId()));

			switch (SD.serverType)
			{
				case ServerType.AUTH:
				{
					AuthBaseHandler.handleSocket(ctx, this.connectNode, message);
					break;
				}
				case ServerType.PROXY:
				{
					ProxyBaseHandler.handleSocket(ctx, this.connectNode, message);
					break;
				}
				case ServerType.GAME:
				{
					GameBaseHandler.handleSocket(ctx, this.connectNode, message);
					break;
				}
				case ServerType.WORLD:
				{
					WorldBaseHandler.handleSocket(ctx, this.connectNode, message);
					break;
				}
				case ServerType.MANAGE:
				{
					ManageBaseHandler.handleSocket(ctx, this.connectNode, message);
					break;
				}
			}
			stopWatch.stop();

		}
		catch (Exception ex)
		{
			logger.error("{}", ExceptionUtils.getStackTrace(ex));
			// TODO 返回服务器处理失败协议
		}
	}

}
