package nicehu.server.manageserver.logic.freeze.data;

public class FreezeInfo
{

	private int accountId;
	private int showId;
	private int type;
	private long beginTime;
	private long endTime;

	public FreezeInfo(int accountId, int showId, int type, long beginTime, long endTime)
	{
		super();
		this.accountId = accountId;
		this.showId = showId;
		this.type = type;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}

	public int getAccountId()
	{
		return accountId;
	}

	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}

	public int getShowId()
	{
		return showId;
	}

	public void setShowId(int showId)
	{
		this.showId = showId;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public long getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(long beginTime)
	{
		this.beginTime = beginTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

}
