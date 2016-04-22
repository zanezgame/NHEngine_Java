package nicehu.nhsdk.candy.db.core.write;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.db.core.DBConnectionPool;
import nicehu.nhsdk.candy.db.core.DBSql;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.thread.WindRunnable;
import nicehu.nhsdk.candy.util.CloseU;

public abstract class DBWriterPart extends WindRunnable
{
	private static final Logger loggerPlayerSqlLog = LoggerFactory.getLogger("PlayerSqlLog");
	private static final Logger logger = LoggerFactory.getLogger(DBWriterPart.class);

	public static final long sleepTime = 20;
	public static final int MaxBatchDoCount = 20;
	public static final int MaxRedoCount = 5;

	public DBConnectionPool connectionPool;
	// if one playerId is at here ,that mean the player has sql not write complete ,so the player cannot clean now!
	public ConcurrentHashMap<Integer, Long> latestWriteTime = new ConcurrentHashMap<>();
	public ArrayList<DBSql> currentSqls = null;
	public int tryDoCount = 0;

	public DBWriterPart(DBConnectionPool connectionPool)
	{
		super(sleepTime, WindRunnable.ThreadLevel.HIGH);
		this.connectionPool = connectionPool;

	}

	public void resetCurrentSqls()
	{
		if (currentSqls != null)
		{
			for (DBSql sql : currentSqls)
			{
				latestWriteTime.remove(sql.getPlayerId(), sql.getUpdateTime());
			}
		}
		tryDoCount = 0;
		currentSqls = null;
	}

	public void dumpSqlsError()
	{
		if (currentSqls != null)
		{
			loggerPlayerSqlLog.error("lastSqlDidCount={}", tryDoCount);
			for (DBSql ps : currentSqls)
			{
				loggerPlayerSqlLog.error("lastSqlDumpError={}", ps.getSql());
			}
		}
	}

	public boolean hasUnWriteCompleteSql(int playerId)
	{
		return latestWriteTime.containsKey(playerId);
	}

	public void excuteSqls(ArrayList<DBSql> sqls)
	{
		Connection connection = null;
		Statement statement = null;
		try
		{
			connection = this.connectionPool.getConnection();
			try
			{
				connection.setAutoCommit(false);
				try
				{
					statement = connection.createStatement();
					for (DBSql sql : sqls)
					{
						if (Empty.is(sql.getSql()))
						{
							continue;
						}
						logger.debug("DBWriterPart BatchExcute SQL::" + sql.getSql());
						statement.addBatch(sql.getSql());
					}
					int[] result = statement.executeBatch();
					connection.commit();
					this.resetCurrentSqls();
				}
				catch (SQLException e)
				{
					logger.error("Error: {}\n{}", e.getErrorCode(), ExceptionUtils.getStackTrace(e));
					loggerPlayerSqlLog.error("Error: {}\n{}", e.getErrorCode(), ExceptionUtils.getStackTrace(e));
					connection.rollback();
				}
				catch (Exception e)
				{
					logger.error("Error: {}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
					loggerPlayerSqlLog.error("Error: {}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
				}
			}
			catch (SQLException e)
			{
				logger.error("Error: {}\n{}", e.getErrorCode(), ExceptionUtils.getStackTrace(e));
				loggerPlayerSqlLog.error("Error: {}\n{}", e.getErrorCode(), ExceptionUtils.getStackTrace(e));
			}
			finally
			{
				try
				{
					connection.setAutoCommit(true);
				}
				catch (Exception e)
				{
					logger.error("Error: {}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
					loggerPlayerSqlLog.error("Error: {}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
				}
				CloseU.close(statement);
				CloseU.close(connection);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error: {}\n{}", e.getErrorCode(), ExceptionUtils.getStackTrace(e));
			loggerPlayerSqlLog.error("Error: {}\n{}", e.getErrorCode(), ExceptionUtils.getStackTrace(e));
		}
	}

}
