package nicehu.nhsdk.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketAddress;

import nicehu.nhsdk.core.channelhandler.HttpChannelHandler;
import nicehu.nhsdk.core.handler.base.HttpHandlerExecutor;

public class HttpServer
{
	ServerBootstrap serverBootstrap;

	public void initialize(int threadNum)
	{
		HttpHandlerExecutor.init(threadNum);
		{
			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try
			{
				serverBootstrap = new ServerBootstrap();
				serverBootstrap.group(bossGroup, workerGroup);
				serverBootstrap.channel(NioServerSocketChannel.class);
				serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
				serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
				serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
				serverBootstrap.option(ChannelOption.SO_TIMEOUT, 10 * 1000);
				serverBootstrap.childHandler(new HttpChannelHandler());
			}
			finally
			{
			}
		}

	}

	public boolean openPort(SocketAddress localAddress)
		throws Exception
	{
		return serverBootstrap.bind(localAddress).sync().isSuccess();
	}
}