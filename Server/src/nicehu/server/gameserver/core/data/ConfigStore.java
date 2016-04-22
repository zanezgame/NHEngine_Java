package nicehu.server.gameserver.core.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nicehu.nhsdk.candy.crypt.md5.MD5U;

public class ConfigStore
{
	// private static final Logger logger = LoggerFactory.getLogger(ConfigStore.class);
	public static ConfigStore instance = new ConfigStore();

	private Map<String, String> xmlStoreDatas = new ConcurrentHashMap<String, String>(); // 配置文件列表

	public boolean isConfigChange(String name, String newContent)
	{
		boolean change = false;
		String oldContent = xmlStoreDatas.get(name);
		if (null == oldContent)
		{
			change = true;
		}
		else
		{
			String newMD5 = MD5U.gen(newContent);
			String oldMD5 = MD5U.gen(oldContent);
			if (!newMD5.equals(oldMD5))
			{
				change = true;
			}
		}
		xmlStoreDatas.put(name, newContent);
		// 测试热更新需要,这里暂时改为恒ture
		change = true;
		return change;
	}

}
