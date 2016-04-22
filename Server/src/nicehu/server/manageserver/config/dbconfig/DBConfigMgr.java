package nicehu.server.manageserver.config.dbconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.xml.XmlU;
import nicehu.server.manageserver.config.core.ConfigPath;

public class DBConfigMgr
{
	public static DBConfigMgr instance = new DBConfigMgr();
	public DBConfig accountDBConfig = null;
	public DBConfig managerDBConfig = null;
	public DBConfig purchaseDBConfig = null;
	public List<AreaDBConfig> areaDBConfigs = new ArrayList<AreaDBConfig>();

	private String dbUserName;
	private String dbPassword;

	public void reload()
	{
		Element root = XmlU.getXmlRootFromFilePath(ConfigPath.file_DBConfig);

		Element commonElement = root.element("Common");
		HashMap<String, String> attributes = XmlU.getAttribute(commonElement);
		dbUserName = ParseU.pStr(attributes.get("UserName"));
		dbPassword = ParseU.pStr(attributes.get("Password"));

		Element accountDBElement = root.element("AccountDB");
		this.accountDBConfig = parseDB(accountDBElement);

		Element managerDBElement = root.element("ManagerDB");
		this.managerDBConfig = parseDB(managerDBElement);

		Element purchaseDBElement = root.element("PurchaseDB");
		this.purchaseDBConfig = parseDB(purchaseDBElement);

		List<Element> areaElements = root.elements("AreaDBCluster");
		this.areaDBConfigs.clear();
		for (Element areaCfg : areaElements)
		{
			AreaDBConfig areaDBConfig = new AreaDBConfig();
			areaDBConfig.setAreaId(Integer.parseInt(areaCfg.attributeValue("AreaId")));
			areaDBConfig.setGameDBConfig(parseDB(areaCfg.element("GameDB")));
			areaDBConfig.setLogDBConfig(parseDB(areaCfg.element("LogDB")));

			areaDBConfigs.add(areaDBConfig);
		}
	}

	private DBConfig parseDB(Element dbElement)
	{
		HashMap<String, String> attributes = XmlU.getAttribute(dbElement);
		String Jdbc = ParseU.pStr(attributes.get("JdbcUrl"));
		String userName = ParseU.pStr(attributes.get("UserName"));
		if (Empty.is(userName))
		{
			userName = dbUserName;
		}
		String password = ParseU.pStr(attributes.get("Password"));
		if (Empty.is(password))
		{
			password = dbPassword;
		}

		DBConfig dbConfig = new DBConfig(Jdbc, userName, password);
		return dbConfig;
	}

	public AreaDBConfig getAreaDbConfig(int areaId)
	{
		for (AreaDBConfig areaDBConfig : areaDBConfigs)
		{
			if (areaDBConfig.getAreaId() == areaId)
			{
				return areaDBConfig;
			}
		}
		return null;
	}

}
