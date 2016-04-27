package nicehu.server.manageserver.logic.freeze.db;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.db.core.DBClient;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.server.gameserver.core.GSD;
import nicehu.server.manageserver.logic.freeze.data.FreezeInfo;
import nicehu.server.manageserver.logic.freeze.data.FreezeMgr;

public class FreezeDB
{
	private static final Logger logger = LoggerFactory.getLogger(FreezeDB.class);

	public static void add(FreezeInfo freezeInfo)
	{
		String sql =
			String.format("insert into freeze_info (account_id, show_id,type,begin_time,end_time) VALUES (%d,%d,%d,'%s','%s')",
				freezeInfo.getAccountId(),
				freezeInfo.getShowId(),
				freezeInfo.getType(),
				TimeU.getStr(freezeInfo.getBeginTime()),
				TimeU.getStr(freezeInfo.getEndTime()));
		GSD.dbCluster.getManagerDbClient().executeAsyncUpdate(sql);
	}

	public static void delete(FreezeInfo freezeInfo)
	{
		String sql =
			String.format("update  freeze_info set end_time = '%s' where account_id = %d and begin_time = '%s'",
				TimeU.getStr(freezeInfo.getEndTime()),
				freezeInfo.getAccountId(),
				TimeU.getStr(freezeInfo.getBeginTime()));
		GSD.dbCluster.getManagerDbClient().executeAsyncUpdate(sql);
	}

	public static void query()
	{
		String sql = String.format("select * from freeze_info where end_time > '%s'", TimeU.getStr());
		DBClient dbClient = GSD.dbCluster.getManagerDbClient();
		CachedRowSet rs = null;
		try
		{
			rs = dbClient.executeQuery(sql);
			while (rs != null && rs.next())
			{

				int accountId = rs.getInt("account_id");
				int showId = rs.getInt("show_id");
				int type = rs.getInt("type");
				long beginTime = rs.getTime("begin_time").getTime();
				long endTime = rs.getTime("end_time").getTime();
				FreezeInfo freezeInfo = new FreezeInfo(accountId, showId, type, beginTime, endTime);
				FreezeMgr.instance.addFreezeInfo(freezeInfo);
			}
		}
		catch (SQLException e)
		{
			logger.error("queryStIdAndPlayerId  failed.{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		finally
		{
			if (rs != null)
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
					logger.error("queryStIdAndPlayerId  failed.{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
				}
		}
	}
}
