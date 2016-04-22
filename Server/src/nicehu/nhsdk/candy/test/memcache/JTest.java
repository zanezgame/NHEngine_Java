package nicehu.nhsdk.candy.test.memcache;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class JTest
{

	public static void main(String[] args)
	{
		String[] servers = {"192.168.1.250:7777"};
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers(servers);
		pool.setFailover(true);
		pool.setInitConn(10);
		pool.setMinConn(5);
		pool.setMaxConn(200);
		pool.setMaintSleep(30);
		pool.setNagle(false);
		pool.setSocketTO(3000);
		// pool.setHashingAlg(2);
		pool.initialize();

		// 多线程
		// 10个client负责写入
		// 20个cient负责读取

		MemCachedClient cache = new MemCachedClient();
		long now = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++)
		{
			boolean success = cache.set("a" + i, "Hello!");
			String result = (String)cache.get("a" + i);
			System.out.println(String.format("set( %d ): %s", i, success));
			System.out.println(String.format("get( %d ): %s", i, result));
		}
		System.out.println("!!!!!!!!!!! JJJ Cost Time : " + (System.currentTimeMillis() - now));
	}
}