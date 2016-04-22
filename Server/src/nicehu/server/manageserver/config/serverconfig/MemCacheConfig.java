package nicehu.server.manageserver.config.serverconfig;

public class MemCacheConfig
{
	private String id;
	private String ip;
	private String port;

	public MemCacheConfig()
	{

	}

	public MemCacheConfig(String ip, String port)
	{
		super();
		this.ip = ip;
		this.port = port;
	}

	public String toString()
	{
		return this.ip + ":" + this.port;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

}
