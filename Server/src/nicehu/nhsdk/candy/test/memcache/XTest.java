package nicehu.nhsdk.candy.test.memcache;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XTest
{
	private static final Logger logger = LoggerFactory.getLogger(XTest.class);

	public static void main(String[] args)
	{
		MemcachedClient cache = null;
		try
		{
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("192.168.1.250:7777"));
			// builder.setFailureMode(true);
			// builder.setCommandFactory(new BinaryCommandFactory());
			builder.setConnectionPoolSize(10);
			builder.getConfiguration().setStatisticsServer(false);
			builder.setEnableHealSession(false);
			cache = builder.build();
			cache.setEnableHeartBeat(false);
			cache.setEnableHealSession(false);

			long now = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++)
			{
				boolean success = cache.set("a" + i, 0, "Hello!");
				String result = (String)cache.get("a" + i);
				System.out.println(String.format("set( %d ): %s", i, success));
				System.out.println(String.format("get( %d ): %s", i, result));
			}
			System.out.println("!!!!!!!!!!! XXX Cost Time : " + (System.currentTimeMillis() - now));

		}
		catch (Exception e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		try
		{
			if (cache != null)
			{
				cache.shutdown();
			}

		}
		catch (IOException e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
}