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
import nicehu.nhsdk.core.datatransmitter.data.ClientNode;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.authserver.core.AuthBaseHandler;
import nicehu.server.gameserver.core.GameBaseHandler;
import nicehu.server.manageserver.core.ManageBaseHandler;
import nicehu.server.proxyserver.core.ProxyBaseHandler;
import nicehu.server.worldserver.core.WorldBaseHandler;

public class SocketHandlerExecutor implements Runnable
{
	private static Logger logger = LogU.getLogger(SocketHandlerExecutor.class);

	private static Executor threadPool = null;

	public ServerNode serverNode;
	public ClientNode clientNode;
	public ChannelHandlerContext ctx;
	public Message message;

	public SocketHandlerExecutor(ChannelHandlerContext ctx, Message message, ServerNode serverNode, ClientNode clientNode)
	{
		super();
		this.serverNode = serverNode;
		this.clientNode = clientNode;
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
					AuthBaseHandler.handleSocket(ctx, this.serverNode, message);
					break;
				}
				case ServerType.PROXY:
				{
					ProxyBaseHandler.handleSocket(ctx, this.serverNode, this.clientNode, message);
					break;
				}
				case ServerType.GAME:
				{
					GameBaseHandler.handleSocket(ctx, this.serverNode, message);
					break;
				}
				case ServerType.WORLD:
				{
					WorldBaseHandler.handleSocket(ctx, this.serverNode, message);
					break;
				}
				case ServerType.MANAGE:
				{
					ManageBaseHandler.handleSocket(ctx, this.serverNode, message);
					break;
				}
			}
			stopWatch.stop();

		}
		catch (Exception ex)
		{
			logger.error("{}", ExceptionUtils.getStackTrace(ex));
			// TODO 返回服务器处理失败协议

			// logger.error("{}", ExceptionUtils.getStackTrace(ex));
			// // 未响应，返回
			// GameProtocolsForClient.ReturnExceptionSeq.Builder builder =
			// GameProtocolsForClient.ReturnExceptionSeq.newBuilder();
			// builder.setExceptionSeq(handler.getExceptionCallbackForClient());
			// builder.setResult(ClientProtocols.E_SERVER_PROC_ERROR);
			// Protocol protocol = new Protocol(ClientProtocols.P_GC_FC_RETURN_EXCEPTION_CALLBACK);
			// protocol.setProtoBufMessage(builder.build());
			//
			// handler.getTransmitter().sendToClient(sender, protocol);
		}
	}

}
