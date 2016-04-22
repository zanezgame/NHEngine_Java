package nicehu.nhsdk.candy.db.core;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public final class DBConnectionPool
{
	private static final int PARTITION_COUNT = 1;

	private int minConnections = 5;
	private int maxConnections = 20;

	private BoneCP connectionPool;

	public DBConnectionPool(String jdbcUrl, String userName, String password, int maxConnections, int minConnections)
		throws SQLException, ClassNotFoundException
	{
		this.minConnections = minConnections;
		this.maxConnections = maxConnections;
		this.init(jdbcUrl, userName, password);
	}

	public DBConnectionPool(String jdbcUrl, String userName, String password)
		throws SQLException, ClassNotFoundException
	{
		this.init(jdbcUrl, userName, password);
	}

	private void init(String jdbcUrl, String userName, String password)
		throws SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(jdbcUrl + "?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&socketTimeout=120000");
		config.setUsername(userName);
		config.setPassword(password);
		config.setMinConnectionsPerPartition(this.minConnections);
		config.setMaxConnectionsPerPartition(this.maxConnections);
		config.setPartitionCount(PARTITION_COUNT);
		config.setIdleMaxAgeInSeconds(600);
		config.setIdleConnectionTestPeriodInSeconds(60);
		this.connectionPool = new BoneCP(config);
	}

	public Connection getConnection()
		throws SQLException
	{
		return this.connectionPool.getConnection();
	}
}
