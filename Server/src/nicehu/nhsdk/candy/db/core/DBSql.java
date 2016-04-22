package nicehu.nhsdk.candy.db.core;

public class DBSql
{
	private int playerId;
	private String sql;
	private long updateTime;

	public DBSql(int playerId, String sql, long updateTime)
	{
		this.playerId = playerId;
		this.sql = sql;
		this.updateTime = updateTime;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public String getSql()
	{
		return sql;
	}

	public long getUpdateTime()
	{
		return updateTime;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public void setUpdateTime(long updateTime)
	{
		this.updateTime = updateTime;
	}

}
