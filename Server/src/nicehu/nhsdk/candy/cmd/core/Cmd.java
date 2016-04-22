package nicehu.nhsdk.candy.cmd.core;

import java.util.HashMap;
import java.util.Map;

public class Cmd
{
	public String name;
	public Map<String, String> attrs = new HashMap<>();

	public void addAttr(Object key, Object value)
	{
		this.attrs.put(key.toString(), value.toString());
	}

	public String getAttr(String key)
	{
		return this.attrs.get(key);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("cmdName=").append(name);
		for (Map.Entry<String, String> entry : attrs.entrySet())
		{
			sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		return sb.toString();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Map<String, String> getAttrs()
	{
		return attrs;
	}

	public void setAttrs(Map<String, String> attrs)
	{
		this.attrs = attrs;
	}
}
