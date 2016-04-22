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

public class SocketBaseOutboundHandler extends ChannelDuplexHandler
{
	private static final Logger logger = LoggerFactory.getLogger(SocketBaseOutboundHandler.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
		throws Exception
	{
		if (msg instanceof Message)
		{
			ByteBuf buffer = encodeProtobuf1(ctx, (Message)msg);
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
		byte[] body = msg.getProtoBuf().toByteArray();
		
		ByteBuf buffer = ctx.alloc().buffer(8 + body.length);
		buffer.writeInt(msg.getId());
		buffer.writeInt(msg.getPlayerId());
		buffer.writeBytes(body);
		
		return buffer;
	}
	
	protected ByteBuf encodeProtobuf1(ChannelHandlerContext ctx, Message msg)
	{
		byte[] msgByte = msg.getProtoBuf().toByteArray();
		
		BaseMsg.Builder builder = BaseMsg.newBuilder();
		builder.setId(msg.id);
		builder.setPlayerId(msg.getPlayerId());
		builder.setMsgData(ByteString.copyFrom(msgByte));
		byte[] baseMsgByte = builder.build().toByteArray();
		
		ByteBuf buffer = ctx.alloc().buffer(baseMsgByte.length);
		buffer.writeBytes(baseMsgByte);
		
		return buffer;
	}

}
