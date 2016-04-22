package nicehu.server.manageserver.config.commonconfig;

import java.util.HashMap;

import org.dom4j.Element;

import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.struct.Pair;
import nicehu.nhsdk.candy.time.Time;
import nicehu.nhsdk.candy.xml.XmlU;
import nicehu.server.manageserver.config.core.ConfigPath;

public class CommonConfigMgr 
{
	public static CommonConfigMgr instance = new CommonConfigMgr();
	public CommonConfig cfg = new CommonConfig();

	public void reload()
	{
		Element root = XmlU.getXmlRootFromFilePath(ConfigPath.file_common);
		CommonConfig cfg = new CommonConfig();
		cfg.setReleaseModel(ParseU.pBool(root.elementText("ReleaseModel"), true));
		Element modelDetail = null;
		if (cfg.isReleaseModel())
		{
			modelDetail = root.element("Release");
		}
		else
		{
			modelDetail = root.element("Develop");
		}
		Element common = modelDetail.element("Common");
		cfg.setDbCacheSqlExpiredTime(ParseU.pLong(common.elementText("DBCacheSqlExpiredTime"), 10 * Time.MINUTE));
		cfg.setMemCacheExpiredTime(ParseU.pLong(common.elementText("MemCacheExpiredTime"), 5 * Time.MINUTE));

		Element gameServer = modelDetail.element("GameServer");
		cfg.setAlwaysAuthToken(ParseU.pBool(gameServer.elementText("AlwaysAuthToken"), true));
		cfg.setPlayerShowIdBase(ParseU.pLong(gameServer.elementText("PlayerShowIdBase"), 1324570001L));
		cfg.setOnlineExpiredTime(ParseU.pLong(gameServer.elementText("OnlineExpiredTime"), 30 * Time.MINUTE));
		cfg.setOfflineExpiredTime(ParseU.pLong(gameServer.elementText("OfflineExpiredTime"), 5 * Time.MINUTE));
		cfg.setDataCachePlayerExpiredTime(ParseU.pLong(gameServer.elementText("CacheExpiredTime"), 10 * Time.MINUTE));
		cfg.setOfflinePlayerNum(ParseU.pInt(gameServer.elementText("OfflinePlayerNum"), 10000));
		cfg.setRandomNameSize(ParseU.pInt(gameServer.elementText("RandomNameSize"), 10));
		cfg.setEmailMaxCount(ParseU.pInt(gameServer.elementText("EmailMaxCount"), 20));
		this.cfg = cfg;
	}

	private static Pair<String, String> loadServerTextFromXml(Element element)
	{
		HashMap<String, String> kv = XmlU.getAttribute(element);
		Pair<String, String> serverText = new Pair<String, String>(kv.get("name"), element.getText());
		return serverText;
	}

}
