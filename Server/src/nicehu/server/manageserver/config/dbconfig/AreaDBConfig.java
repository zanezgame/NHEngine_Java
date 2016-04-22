package nicehu.server.manageserver.config.dbconfig;

import java.util.HashMap;
import java.util.Map;

public class AreaDBConfig
{

	private int areaId;
	private DBConfig gameDBConfig = null;
	private DBConfig logDBConfig = null;

	public AreaDBConfig()
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

	public DBConfig getGameDBConfig()
	{
		return gameDBConfig;
	}

	public void setGameDBConfig(DBConfig gameDBConfig)
	{
		this.gameDBConfig = gameDBConfig;
	}

	public DBConfig getLogDBConfig()
	{
		return logDBConfig;
	}

	public void setLogDBConfig(DBConfig logDBConfig)
	{
		this.logDBConfig = logDBConfig;
	}



}
