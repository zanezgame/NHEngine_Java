package nicehu.nhsdk.core.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import nicehu.nhsdk.candy.data.Message;
import nicehu.pb.NHMsgBase.BaseMsg;

public class BaseOutboundHandler extends ChannelDuplexHandler
{
	private static final Logger logger = LoggerFactory.getLogger(BaseOutboundHandler.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
		throws Exception
	{
		if (msg instanceof Message)
		{
			ByteBuf buffer = encodeProtobuf(ctx, (Message)msg);
			ctx.writeAndFlush(buffer, promise);

			return;
		}
		else
		{
			ctx.writeAndFlush(msg, promise);
		}
	}

	protected ByteBuf encodeProtobuf(ChannelHandlerContext ctx, Message msg)
	{
		BaseMsg baseMsg = msg.getBaseMsg();
		byte[] baseMsgByte = baseMsg.toByteArray();

		ByteBuf buffer = ctx.alloc().buffer(baseMsgByte.length);
		buffer.writeBytes(baseMsgByte);

		return buffer;
	}

}
