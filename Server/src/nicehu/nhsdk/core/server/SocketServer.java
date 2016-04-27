package nicehu.nhsdk.core.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;

import org.slf4j.Logger;

import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.random.RandomU;
import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.nhsdk.core.channelhandler.ChannelHandler;
import nicehu.nhsdk.core.handler.base.HandlerExecutor;

public class SocketServer
{
	private static final Logger logger = LogU.getLogger(SocketServer.class);

	boolean isForServer;
	ServerBootstrap serverBootstrap;
	Bootstrap bootstrap;

	public SocketServer(boolean isForServer)
	{
		this.isForServer = isForServer;
	}

	public void initialize(int threadNum)
	{
		HandlerExecutor.init(threadNum);
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
				serverBootstrap.childHandler(new ChannelHandler(isForServer));
			}
			finally
			{
			}
		}

		if (isForServer)
		{
			{
				EventLoopGroup group = new NioEventLoopGroup();
				try
				{
					bootstrap = new Bootstrap();
					bootstrap.group(group);
					bootstrap.channel(NioSocketChannel.class);
					bootstrap.option(ChannelOption.TCP_NODELAY, true);
					bootstrap.handler(new ChannelHandler(true));
				}
				finally
				{
				}
			}
		}
	}

	public boolean openPort(SocketAddress localAddress)
		throws Exception
	{
		return serverBootstrap.bind(localAddress).sync().isSuccess();
	}

	public void connectTo(SocketAddress remoteAddress)
	{
		int myPortLocal = RandomU.randomInt(54000, 54999);
		int portDelta = 0;
		int nowPort;
		ChannelFuture cf = null;

		while (true)
		{
			while (true)
			{
				nowPort = (myPortLocal + portDelta) % 65536;
				java.net.SocketAddress loacalAddr = new java.net.InetSocketAddress(nowPort);
				cf = bootstrap.bind(loacalAddr);
				while (cf.isDone() == false)
				{
					ThreadU.sleep(10);
				}
				if (cf.isSuccess())
				{
					break;
				}
				portDelta++;
			}

			logger.debug("try to connect to {}", remoteAddress.toString());
			ChannelFuture cf2 = cf.channel().connect(remoteAddress);
			while (cf2.isDone() == false)
			{
				ThreadU.sleep(10);
			}
			if (cf2.isSuccess())
			{
				logger.debug("connect {} success", remoteAddress.toString());
				break;
			}
			else
			{
				cf2.channel().close();
				ThreadU.sleep(1000);
			}
		}
	}
}
