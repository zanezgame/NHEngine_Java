package nicehu.nhsdk.candy.db.core.write;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.collect.lru.LruMap;
import nicehu.nhsdk.candy.collect.lru.MaxLruMap;
import nicehu.nhsdk.candy.db.core.DBConnectionPool;
import nicehu.nhsdk.candy.db.core.DBSql;
import nicehu.nhsdk.candy.thread.ThreadU;

public class DBWriterPartCache extends DBWriterPart
{
	private static final Logger logger = LoggerFactory.getLogger(DBWriterPartCache.class);

	public static long expiredTime = 10 * 1000;
	public static int maxSqlNum = 50000;

	MaxLruMap<String, DBSql> sqls = new MaxLruMap<>();

	public void insertCommand(String key, int playerId, String sql)
	{
		long now = System.currentTimeMillis();
		latestWriteTime.put(playerId, now);
		sqls.put(key, new DBSql(playerId, sql, now));
	}

	@Override
	public void execute()
	{
		while (currentSqls != null && tryDoCount < MaxRedoCount)
		{
			tryDoCount++;
			logger.error("DB BatchUpdate Fail,  then  tryDocount: " + tryDoCount);
			super.excuteSqls(currentSqls);
			if (currentSqls == null)
			{
				logger.error("DB ReBatchUpdate Success");
			}
			else
			{
				logger.error("DB ReBatchUpdate Fail");
			}
			ThreadU.sleep(sleepTime);
		}
		super.dumpSqlsError();
		super.resetCurrentSqls();

		while (!sqls.isEmpty())
		{
			// collect
			ArrayList<DBSql> exSqlList = new ArrayList<>();
			HashMap<String, DBSql> exSqlMap = new HashMap<>();

			Iterator<Map.Entry<String, DBSql>> it = this.sqls.ascendingMap().entrySet().iterator();
			int i = 0;
			while (it.hasNext())
			{
				i++;
				Map.Entry<String, DBSql> entry = it.next();
				if (i > MaxBatchDoCount)
				{
					break;
				}
				if (this.sqls.size() < maxSqlNum && System.currentTimeMillis() < expiredTime + entry.getValue().getUpdateTime())
				{
					break;
				}

				exSqlList.add(entry.getValue());
				exSqlMap.put(entry.getKey(), entry.getValue());
			}
			if (exSqlList == null || exSqlList.size() == 0)
			{
				break;
			}
			else
			{
				for (Map.Entry<String, DBSql> entry : exSqlMap.entrySet())
				{
					sqls.remove(entry.getKey(), entry.getValue());
				}
			}
			// excute
			if (exSqlList != null && exSqlList.size() > 0)
			{
				currentSqls = exSqlList;
				tryDoCount = 1;
				super.excuteSqls(currentSqls);
				if (currentSqls != null)
				{
					break;
				}
			}
		}
	}

	@Override
	public void closeThread()
	{
		expiredTime = 0;
		maxSqlNum = 0;
		while (!sqls.isEmpty() || currentSqls != null)
		{
			execute();
		}
	}

	public DBWriterPartCache(DBConnectionPool connectionPool)
	{
		super(connectionPool);
	}
}
