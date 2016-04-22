package nicehu.nhsdk.core.handler.base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.data.MessageU;
import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.util.TextConfigU;

public class HttpBaseHandler extends ChannelInboundHandlerAdapter
{

	private static final Logger logger = LoggerFactory.getLogger(HttpBaseHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
		throws Exception
	{
		if (!(msg instanceof HttpRequest))
		{
			ctx.fireChannelRead(msg);
			return;
		}

		JSONObject result = null;
		Message message = null;
		try
		{
			message = MessageU.genHttpMessage(ctx, msg);
			if (message == null)
			{
				result = JsonU.gen(0);
				JsonU.updateErr(result, TextConfigU.get("E_DATA_STRUCT"));
				SD.transmitter.sendAndClose(ctx, result.toString());
				return;
			}
		}
		catch (Exception e)
		{
			LogU.error(e);
			JsonU.updateErr(result, TextConfigU.get("E_SERVER_PROC_ERROR"));
			SD.transmitter.sendAndClose(ctx, result.toString());
			return;
		}

		HttpHandlerExecutor.start(new HttpHandlerExecutor(ctx, message, result));

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
		throws Exception
	{
		ctx.close();
	}

}
