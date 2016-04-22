package nicehu.server.manageserver.config.serverconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.xml.XmlU;

public class ServerConfig
{
	private int serverId = 0;
	private String serverName = null;
	private int areaId = 0;
	private HashMap<String, String> attritubes = new HashMap<String, String>();
	private List<MemCacheConfig> memCacheConfigs = new ArrayList<>();

	public String getAttr(String name)
	{
		return this.attritubes.get(name);
	}

	@JsonIgnore
	public List<String> getMemCacheConfigsList()
	{
		List<String> list = new ArrayList<>();
		this.memCacheConfigs.stream().forEach(x -> {
			list.add(x.toString());
		});
		return list;
	}

	public ServerConfig()
	{

	}

	public ServerConfig(Element root, int areaId, MemCacheConfig memCacheConfig)
	{
		this.attritubes = XmlU.getAttribute(root);
		this.serverId = ParseU.pHexInt(this.attritubes.get("ServerId"), 0);
		this.serverName = ParseU.pStr(this.attritubes.get("ServerName"), "");
		this.areaId = areaId;
		if(memCacheConfig != null)
		{
			this.memCacheConfigs.add(memCacheConfig);
		}
	}

	public int getServerId()
	{
		return serverId;
	}

	public void setServerId(int serverId)
	{
		this.serverId = serverId;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public int getAreaId()
	{
		return areaId;
	}

	public void setAreaId(int areaId)
	{
		this.areaId = areaId;
	}

	public HashMap<String, String> getAttritubes()
	{
		return attritubes;
	}

	public void setAttritubes(HashMap<String, String> attritubes)
	{
		this.attritubes = attritubes;
	}

	public List<MemCacheConfig> getMemCacheConfigs()
	{
		return memCacheConfigs;
	}

	public void setMemCacheConfigs(List<MemCacheConfig> memCacheConfigs)
	{
		this.memCacheConfigs = memCacheConfigs;
	}

}
