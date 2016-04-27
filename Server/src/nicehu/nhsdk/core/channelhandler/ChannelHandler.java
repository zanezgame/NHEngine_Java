package nicehu.nhsdk.core.channelhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import nicehu.nhsdk.candy.compress.SnappyCompress;
import nicehu.nhsdk.core.handler.base.BaseInboundHandler;
import nicehu.nhsdk.core.handler.base.BaseOutboundHandler;
import nicehu.nhsdk.core.handler.network.NetworkHandler;
import nicehu.nhsdk.core.handler.network.ServerNetworkHandler;

public class ChannelHandler extends ChannelInitializer<SocketChannel>
{
	private boolean isForServer;

	public ChannelHandler(boolean isForServer)
	{
		this.isForServer = isForServer;
	}

	@Override
	public void initChannel(SocketChannel ch)
	{
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(SnappyCompress.MAX_COMPRESS_MESSAGE_SIZE, 2, 4));
		if (isForServer)
		{
			pipeline.addLast("networkHandler", new ServerNetworkHandler());
		}
		else
		{
			pipeline.addLast("networkHandler", new NetworkHandler());
		}
		pipeline.addLast("messageBaseOutboundHandler", new BaseOutboundHandler());
		pipeline.addLast("messageBaseInboundHandler", new BaseInboundHandler());

	}
}