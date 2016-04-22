package nicehu.nhsdk.core.channelhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import nicehu.nhsdk.core.handler.base.HttpBaseHandler;

public class HttpChannelHandler extends ChannelInitializer<SocketChannel>
{

	@Override
	public void initChannel(SocketChannel ch)
	{
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
		pipeline.addLast("handler", new HttpBaseHandler());
	}
}