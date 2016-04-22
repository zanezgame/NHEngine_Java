package nicehu.server.manageserver.config.serverconfig;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.xml.XmlU;
import nicehu.server.manageserver.config.core.ConfigPath;

public class ServerConfigMgr
{
	public static ServerConfigMgr instance = new ServerConfigMgr();

	public HashMap<String, ServerConfig> name_Configs = new HashMap<>();
	public HashMap<Integer, ServerConfig> id_Configs = new HashMap<>();

	public void reload()
	{
		Element root = XmlU.getXmlRootFromFilePath(ConfigPath.file_ServerConfig);

		HashMap<String, ServerConfig> name_Configs = new HashMap<>();
		HashMap<Integer, ServerConfig> id_Configs = new HashMap<>();

		Element uniqueServerElement = root.element("UniqueServer");
		{
			MemCacheConfig memCacheConfig = null;
			{
				Element memcacheElement = uniqueServerElement.element("MemCache");
				if (memcacheElement != null)
				{
					String ip = ParseU.pStr(memcacheElement.attributeValue("Ip"), "");
					String port = ParseU.pStr(memcacheElement.attributeValue("Port"), "");
					memCacheConfig = new MemCacheConfig(ip, port);
				}
			}
			{
				ServerConfig serverConfig = new ServerConfig(uniqueServerElement.element("ManageServer"), 0, memCacheConfig);
				name_Configs.put(serverConfig.getServerName(), serverConfig);
				id_Configs.put(serverConfig.getServerId(), serverConfig);
			}
			{
				ServerConfig serverConfig = new ServerConfig(uniqueServerElement.element("AuthServer"), 0, memCacheConfig);
				name_Configs.put(serverConfig.getServerName(), serverConfig);
				id_Configs.put(serverConfig.getServerId(), serverConfig);
			}
		}

		List<Element> areaServerElements = root.elements("AreaServer");
		{
			for (Element areaServerElement : areaServerElements)
			{
				int areaId = ParseU.pInt(areaServerElement.attribute("AreaId").getValue());
				MemCacheConfig memCacheConfig = null;
				{
					Element memcacheElement = areaServerElement.element("MemCache");
					if (memcacheElement != null)
					{
						String ip = ParseU.pStr(memcacheElement.attributeValue("Ip"), "");
						String port = ParseU.pStr(memcacheElement.attributeValue("Port"), "");
						memCacheConfig = new MemCacheConfig(ip, port);
					}
				}
				{
					ServerConfig serverConfig = new ServerConfig(areaServerElement.element("GateServer"), areaId, memCacheConfig);
					name_Configs.put(serverConfig.getServerName(), serverConfig);
					id_Configs.put(serverConfig.getServerId(), serverConfig);
				}
				{
					ServerConfig serverConfig = new ServerConfig(areaServerElement.element("CenterServer"), areaId, memCacheConfig);
					name_Configs.put(serverConfig.getServerName(), serverConfig);
					id_Configs.put(serverConfig.getServerId(), serverConfig);
				}
				List<Element> gameServerElements = areaServerElement.elements("GameServer");
				for (Element gameServerElement : gameServerElements)
				{
					ServerConfig serverConfig = new ServerConfig(gameServerElement, areaId, memCacheConfig);
					name_Configs.put(serverConfig.getServerName(), serverConfig);
					id_Configs.put(serverConfig.getServerId(), serverConfig);
				}

			}
		}

		this.name_Configs = name_Configs;
		this.id_Configs = id_Configs;
	}

	public synchronized ServerConfig getServerConfig(int serverId)
	{
		return this.id_Configs.get(serverId);
	}

	public synchronized ServerConfig getServerConfig(String serverName)
	{
		return this.name_Configs.get(serverName);
	}

	@JsonIgnore
	public synchronized int getTimeZone()
	{
		return ParseU.pInt(getServerConfig("ManageServer").getAttr("TimeZone"), 8);

	}

	@JsonIgnore
	public String getManageIp()
	{
		return ParseU.pStr(getServerConfig("ManageServer").getAttr("IpForServer"), "");
	}

	@JsonIgnore
	public int getManagePort()
	{
		return ParseU.pInt(getServerConfig("ManageServer").getAttr("PortForServer"), 0);
	}

}