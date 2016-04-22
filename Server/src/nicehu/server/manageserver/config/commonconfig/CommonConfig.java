package nicehu.server.manageserver.config.commonconfig;

import java.util.HashMap;

public class CommonConfig
{
	// ServerText
	private HashMap<String, String> serverTexts = new HashMap<>();

	// model
	private boolean releaseModel = true;
	private long dbCacheSqlExpiredTime;
	private long memCacheExpiredTime;

	// DataServer
	private long dataInfoPlayerExpiredTime;
	private long dataCachePlayerExpiredTime;

	// GameServer
	private boolean alwaysAuthToken = true;
	private long playerShowIdBase;
	private long onlineExpiredTime;
	private long offlineExpiredTime;
	private int offlinePlayerNum;
	private int randomNameSize;
	private int emailMaxCount;

	public HashMap<String, String> getServerTexts()
	{
		return serverTexts;
	}

	public void setServerTexts(HashMap<String, String> serverTexts)
	{
		this.serverTexts = serverTexts;
	}

	public boolean isReleaseModel()
	{
		return releaseModel;
	}

	public void setReleaseModel(boolean releaseModel)
	{
		this.releaseModel = releaseModel;
	}

	public long getDbCacheSqlExpiredTime()
	{
		return dbCacheSqlExpiredTime;
	}

	public void setDbCacheSqlExpiredTime(long dbCacheSqlExpiredTime)
	{
		this.dbCacheSqlExpiredTime = dbCacheSqlExpiredTime;
	}

	public boolean isAlwaysAuthToken()
	{
		return alwaysAuthToken;
	}

	public void setAlwaysAuthToken(boolean alwaysAuthToken)
	{
		this.alwaysAuthToken = alwaysAuthToken;
	}

	public long getPlayerShowIdBase()
	{
		return playerShowIdBase;
	}

	public void setPlayerShowIdBase(long playerShowIdBase)
	{
		this.playerShowIdBase = playerShowIdBase;
	}

	public long getOnlineExpiredTime()
	{
		return onlineExpiredTime;
	}

	public void setOnlineExpiredTime(long onlineExpiredTime)
	{
		this.onlineExpiredTime = onlineExpiredTime;
	}

	public long getOfflineExpiredTime()
	{
		return offlineExpiredTime;
	}

	public void setOfflineExpiredTime(long offlineExpiredTime)
	{
		this.offlineExpiredTime = offlineExpiredTime;
	}

	public int getOfflinePlayerNum()
	{
		return offlinePlayerNum;
	}

	public void setOfflinePlayerNum(int offlinePlayerNum)
	{
		this.offlinePlayerNum = offlinePlayerNum;
	}

	public int getRandomNameSize()
	{
		return randomNameSize;
	}

	public void setRandomNameSize(int randomNameSize)
	{
		this.randomNameSize = randomNameSize;
	}

	public int getEmailMaxCount()
	{
		return emailMaxCount;
	}

	public void setEmailMaxCount(int emailMaxCount)
	{
		this.emailMaxCount = emailMaxCount;
	}

	public long getMemCacheExpiredTime()
	{
		return memCacheExpiredTime;
	}

	public void setMemCacheExpiredTime(long memCacheExpiredTime)
	{
		this.memCacheExpiredTime = memCacheExpiredTime;
	}

	public long getDataInfoPlayerExpiredTime()
	{
		return dataInfoPlayerExpiredTime;
	}

	public void setDataInfoPlayerExpiredTime(long dataInfoPlayerExpiredTime)
	{
		this.dataInfoPlayerExpiredTime = dataInfoPlayerExpiredTime;
	}

	public long getDataCachePlayerExpiredTime()
	{
		return dataCachePlayerExpiredTime;
	}

	public void setDataCachePlayerExpiredTime(long dataCachePlayerExpiredTime)
	{
		this.dataCachePlayerExpiredTime = dataCachePlayerExpiredTime;
	}

}
