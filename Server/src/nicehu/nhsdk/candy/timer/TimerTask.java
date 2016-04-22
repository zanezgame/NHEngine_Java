package nicehu.nhsdk.candy.timer;

public abstract class TimerTask
{
	private long sleepTime;
	private String refreshTime;
	private long lastTime;

	public abstract void handle();

	public TimerTask(long sleepTime)
	{
		this.sleepTime = sleepTime;
	}

	public long getSleepTime()
	{
		return sleepTime;
	}

	public void setSleepTime(long sleepTime)
	{
		this.sleepTime = sleepTime;
	}

	public String getRefreshTime()
	{
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime)
	{
		this.refreshTime = refreshTime;
	}

	public long getLastTime()
	{
		return lastTime;
	}

	public void setLastTime(long lastTime)
	{
		this.lastTime = lastTime;
	}

}
