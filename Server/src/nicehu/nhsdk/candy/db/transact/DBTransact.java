package nicehu.nhsdk.candy.db.transact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.rowset.CachedRowSetImpl;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.util.CloseU;

public class DBTransact
{
	private static final Logger logger = LoggerFactory.getLogger(DBTransact.class);

	public static final int MaxQueryNum = 150;

	public static boolean update(PreparedStatement ps, String sql, Object[] objList)
		throws SQLException
	{
		logger.debug(sql);
		if (objList != null)
		{
			for (int i = 0; i < objList.length; ++i)
			{
				ps.setObject(i + 1, objList[i]);
			}
		}
		int ret = ps.executeUpdate();
		return ret >= 0;
	}

	public static int updateAndReturn(PreparedStatement ps, String sql, Object[] objList)
		throws SQLException
	{
		logger.debug(sql);
		if (objList != null)
		{
			for (int i = 0; i < objList.length; ++i)
			{
				ps.setObject(i + 1, objList[i]);
			}
		}
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		int result = -1;
		while (rs.next())
		{
			result = rs.getInt(1);
		}
		return result;
	}

	public static CachedRowSet query(PreparedStatement ps, String sql, Object[] objList)
		throws SQLException
	{
		if (objList != null)
		{
			for (int i = 0; i < objList.length; ++i)
			{
				ps.setObject(i + 1, objList[i]);
			}
		}
		logger.debug(sql);
		ResultSet resultSet = ps.executeQuery();
		CachedRowSet cachedRowSet = new CachedRowSetImpl();
		cachedRowSet.populate(resultSet);
		return cachedRowSet;
	}

	// 事务处理
	public static boolean doTransact(Connection con, Transact tt)
	{
		boolean ret = false;
		PreparedStatement[] vps = new PreparedStatement[MaxQueryNum];
		CachedRowSet[] vrs = new CachedRowSet[MaxQueryNum];
		try
		{
			con.setAutoCommit(false);
			try
			{
				logger.debug("Transact proc {} ...", tt.getName());
				StopWatch watch_Transact = new Slf4JStopWatch(tt.getName());
				tt.doProc(con, vps, vrs);
				watch_Transact.stop();
				logger.debug("Transact proc {} done", tt.getName());
				con.commit();
				ret = true;

			}
			catch (SQLException e)
			{
				con.rollback();
				LogU.error(e);
			}
			finally
			{
				try
				{
					con.setAutoCommit(true);
				}
				catch (SQLException e)
				{
					LogU.error(e);
				}
			}
		}
		catch (SQLException e)
		{
			LogU.error(e);
		}
		finally
		{
			for (int i = 0; i < vps.length; ++i)
			{
				CloseU.close(vps[i]);

			}
			for (int i = 0; i < vrs.length; ++i)
			{
				closeRowSet(vrs, i);
			}
		}

		return ret;
	}

	public static void closeRowSet(CachedRowSet[] vrs, int index)
	{
		if (index >= 0 && index < vrs.length)
		{
			try
			{
				if (vrs[index] != null)
				{
					vrs[index].close();
				}
			}
			catch (SQLException e)
			{
				logger.error("{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
			}
			vrs[index] = null;
		}
	}

}
