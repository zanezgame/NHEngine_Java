package nicehu.server.manageserver.config.areaConfig;


public class AreaInfo
{
	private int areaId;
	private String areaName;
	private int status;
	private long areaStartTime;

	public AreaInfo()
	{

	}

	public int getAreaId()
	{
		return areaId;
	}

	public void setAreaId(int areaId)
	{
		this.areaId = areaId;
	}

	public String getAreaName()
	{
		return areaName;
	}

	public void setAreaName(String areaName)
	{
		this.areaName = areaName;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public long getAreaStartTime()
	{
		return areaStartTime;
	}

	public void setAreaStartTime(long areaStartTime)
	{
		this.areaStartTime = areaStartTime;
	}

}
