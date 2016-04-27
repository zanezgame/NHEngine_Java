package nicehu.nhsdk.core.handler.base;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.nhsdk.util.TextConfigU;

public class HttpHandlerExecutor implements Runnable
{
	private static Logger logger = LogU.getLogger(HttpHandlerExecutor.class);

	private static Executor threadPool = null;

	public ChannelHandlerContext ctx;
	public Message msg;
	public JSONObject result;

	public HttpHandlerExecutor( ChannelHandlerContext ctx, Message msg,JSONObject result)
	{
		super();
		this.ctx = ctx;
		this.msg = msg;
		this.result = result;
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
			StopWatch stopWatch = new Slf4JStopWatch("Http Message " + msg.getId());
			result = JsonU.gen(msg.id);
			switch (SD.serverType)
			{
				case ServerType.PROXY:
				{
					//ProxyBaseHandler.handleHttp(ctx, msg, result);
					break;
				}
				case ServerType.AUTH:
				{
					//AuthBaseHandler.handleHttp(ctx, msg);
					break;
				}
				default:
				{
					logger.error(SD.serverName + " Do not Support Http Message!");
				}
			}
			stopWatch.stop();
		}
		catch (Exception e)
		{
			JsonU.updateErr(result, TextConfigU.get("E_SERVER_PROC_ERROR"));
			SD.transmitter.sendAndClose(ctx, result.toString());
			logger.error("{}", ExceptionUtils.getStackTrace(e));
		}
	}

}
