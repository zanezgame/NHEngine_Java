package nicehu.nhsdk.candy.db.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.rowset.CachedRowSetImpl;
import nicehu.nhsdk.candy.db.core.write.DBWriter;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.util.CloseU;

public class DBClient
{
	/**
	 * DBClient--ConnectionPool--DBWriter--DBWriterPartPrompt--DBWriterPartCache
	 * ---1-----------1-------------1------------n--------------------n---------
	 */
	private DBConnectionPool connectionPool;
	private DBWriter dbWriter;

	private static final Logger logger = LoggerFactory.getLogger(DBClient.class);

	public DBClient(String jdbcUrl, String userName, String password)
		throws SQLException, ClassNotFoundException
	{
		this.connectionPool = new DBConnectionPool(jdbcUrl, userName, password);
	}

	public DBClient(String jdbcUrl, String userName, String password, int maxConnections, int minConnections, int promptPartNum, int cachePartNum)
		throws SQLException, ClassNotFoundException
	{
		this.connectionPool = new DBConnectionPool(jdbcUrl, userName, password, minConnections, maxConnections);
		if (!Empty.is(promptPartNum) || !Empty.is(cachePartNum))
		{
			this.dbWriter = new DBWriter(this.connectionPool, promptPartNum, cachePartNum);
			dbWriter.startThread();
		}
	}

	public void executeAsyncUpdate(String sql)
	{
		this.dbWriter.insertCommand(0, sql);
	}

	public void executeAsyncUpdate(int playerId, String sql)
	{
		this.dbWriter.insertCommand(playerId, sql);
	}

	public void executeAsyncUpdate(String key, int playerId, String sql)
	{
		this.dbWriter.insertCommand(key, playerId, sql);
	}

	public boolean hasUnWriteCompleteSql(int playerId)
	{
		return this.dbWriter.hasUnWriteCompleteSql(playerId);
	}

	public CachedRowSet executeQuery(String sql)
		throws SQLException
	{
		logger.debug("{}", sql);
		Connection connection = null;
		Statement statement = null;
		CachedRowSet cachedRowSet = null;
		try
		{
			connection = this.connectionPool.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			cachedRowSet = new CachedRowSetImpl();
			cachedRowSet.populate(resultSet);
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		finally
		{
			CloseU.close(statement);
			CloseU.close(connection);
		}
		return cachedRowSet;
	}

	public int executeUpdate(String command)
	{
		Connection connection = null;
		Statement statement = null;
		int affectedLine = 0;
		try
		{
			connection = this.connectionPool.getConnection();
			statement = connection.createStatement();
			logger.debug("{}", command);
			affectedLine = statement.executeUpdate(command);
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		finally
		{
			CloseU.close(statement);
			CloseU.close(connection);
		}
		return affectedLine;
	}

	public long executeInsertAndReturnIncrementId(String sql)
		throws SQLException
	{
		Connection connection = null;
		Statement statement = null;
		long autoIncrementIdx = 0;
		try
		{
			connection = this.connectionPool.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next())
			{
				autoIncrementIdx = resultSet.getInt(1);
			}
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		finally
		{
			CloseU.close(statement);
			CloseU.close(connection);
		}
		return autoIncrementIdx;
	}

	public Connection getConnection()
		throws SQLException
	{
		return this.connectionPool.getConnection();
	}

}
