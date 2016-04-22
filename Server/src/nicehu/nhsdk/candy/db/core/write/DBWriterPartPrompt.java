package nicehu.nhsdk.candy.db.core.write;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;

import nicehu.nhsdk.candy.db.core.DBConnectionPool;
import nicehu.nhsdk.candy.db.core.DBSql;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.thread.ThreadU;

public class DBWriterPartPrompt extends DBWriterPart
{
	private static final Logger logger = LogU.getLogger(DBWriterPartPrompt.class);
	ConcurrentLinkedDeque<DBSql> sqls = new ConcurrentLinkedDeque<>();

	public void insertCommand(int playerId, String sql)
	{
		long now = System.currentTimeMillis();
		latestWriteTime.put(playerId, now);
		sqls.add(new DBSql(playerId, sql, now));
	}

	@Override
	public void execute()
	{
		while (currentSqls != null && tryDoCount <= MaxRedoCount)
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
			ArrayList<DBSql> exSqlList = new ArrayList<DBSql>();
			for (int i = 0; i < MaxBatchDoCount; i++)
			{
				if (sqls.isEmpty())
				{
					break;
				}
				exSqlList.add(sqls.poll());
			}
			// excute
			if (exSqlList.size() > 0)
			{
				currentSqls = exSqlList;
				tryDoCount = 1;
				this.excuteSqls(currentSqls);
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
		while (!sqls.isEmpty() || currentSqls != null)
		{
			this.execute();
		}
	}

	public DBWriterPartPrompt(DBConnectionPool connectionPool)
	{
		super(connectionPool);
	}

}
