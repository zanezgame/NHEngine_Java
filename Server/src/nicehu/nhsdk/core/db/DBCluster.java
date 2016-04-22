package nicehu.nhsdk.core.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.db.core.DBClient;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.server.manageserver.config.dbconfig.AreaDBConfig;
import nicehu.server.manageserver.config.dbconfig.DBConfig;
import nicehu.server.manageserver.config.dbconfig.DBConfigMgr;

public class DBCluster
{
	private static Logger logger = LoggerFactory.getLogger(DBCluster.class);

	public DBClient managerDbClient = null;
	public DBClient accountDBClient = null;
	public DBClient purchaseDbClient = null;
	public DBClient gameDBClient = null;
	public List<DBClient> gameSDBClients = new ArrayList<>();
	public DBClient logDBClient = null;

	public DBCluster(int areaId, int serverType)
	{
		try
		{
			initialize(DBConfigMgr.instance, areaId, serverType);
			logger.warn("DBCluster initialize success ！！！    ");
		}
		catch (Exception e)
		{
			LogU.error(e);
			System.exit(0);
		}
	}

	private void initialize(DBConfigMgr dbConfigMgr, int areaId, int serverType)
		throws SQLException, ClassNotFoundException
	{

		DBConfig config = null;
		switch (serverType)
		{
			case ServerType.MANAGE:
			{
				config = dbConfigMgr.manageDBConfig;
				this.managerDbClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 5, 1, 0);
				config = dbConfigMgr.accountDBConfig;
				this.accountDBClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 5, 1, 0);
				break;
			}
			case ServerType.AUTH:
			{
				config = dbConfigMgr.manageDBConfig;
				this.managerDbClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 5, 1, 0);
				config = dbConfigMgr.accountDBConfig;
				this.accountDBClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 10, 20, 5, 0);
				break;
			}
			case ServerType.PROXY:
			{
				config = dbConfigMgr.manageDBConfig;
				this.managerDbClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 5, 1, 0);
				AreaDBConfig areaDBConfig = dbConfigMgr.getAreaDbConfig(areaId);
				if (areaDBConfig != null)
				{
					config = areaDBConfig.getLogDBConfig();
					logDBClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 20, 5, 0);
				}

				break;
			}

			case ServerType.GAME:
			{
				config = dbConfigMgr.manageDBConfig;
				this.managerDbClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 5, 1, 0);
				AreaDBConfig areaDBConfig = dbConfigMgr.getAreaDbConfig(areaId);
				if (areaDBConfig != null)
				{
					config = areaDBConfig.getGameDBConfig();
					gameDBClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 10, 40, 5, 20);
					config = areaDBConfig.getLogDBConfig();
					logDBClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 20, 5, 0);
				}

				break;
			}
			case ServerType.WORLD:
			{
				config = dbConfigMgr.manageDBConfig;
				this.managerDbClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 5, 1, 0);
				AreaDBConfig areaDBConfig = dbConfigMgr.getAreaDbConfig(areaId);
				if (areaDBConfig != null)
				{
					config = areaDBConfig.getGameDBConfig();
					gameDBClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 20, 5, 5);
					config = areaDBConfig.getLogDBConfig();
					logDBClient = new DBClient(config.getJdbcUrl(), config.getUserName(), config.getPassword(), 1, 20, 5, 0);
				}

				break;
			}
		}
	}

	public DBClient getManagerDbClient()
	{
		return managerDbClient;
	}

	public DBClient getGameDBClient()
	{
		return gameDBClient;
	}

	public DBClient getPurchaseDbClient()
	{
		return purchaseDbClient;
	}

	public DBClient getLogDBClient()
	{
		return logDBClient;
	}

	public DBClient getAccountDBClient()
	{
		return this.accountDBClient;
	}

}
