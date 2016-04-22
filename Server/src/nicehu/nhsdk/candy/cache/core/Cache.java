package nicehu.nhsdk.candy.cache.core;

public class Cache
{
	private String data;
	private long updateTime;

	public Cache(String data, long updateTime)
	{
		super();
		this.data = data;
		this.updateTime = updateTime;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public long getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(long updateTime)
	{
		this.updateTime = updateTime;
	}

}
