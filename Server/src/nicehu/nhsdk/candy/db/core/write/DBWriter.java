package nicehu.nhsdk.candy.db.core.write;

import java.util.ArrayList;

import nicehu.nhsdk.candy.db.core.DBConnectionPool;
import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;

public class DBWriter
{
	private static ArrayList<DBWriter> dbwrites = new ArrayList<DBWriter>();

	private DBConnectionPool connectionPool;
	private ArrayList<DBWriterPartPrompt> promptParts = new ArrayList<DBWriterPartPrompt>();
	private ArrayList<DBWriterPartCache> cacheParts = new ArrayList<DBWriterPartCache>();

	public static void init()
	{
		DBWriterPartCache.expiredTime = CommonConfigMgr.instance.cfg.getDbCacheSqlExpiredTime();
	}
	
	public static void init(long cacheSqlExpiredTime)
	{
		DBWriterPartCache.expiredTime = cacheSqlExpiredTime;
	}

	public DBWriter(DBConnectionPool connectionPool, int promptPartNum, int cachePartNum)
	{
		this.connectionPool = connectionPool;
		dbwrites.add(this);

		for (int i = 0; i < promptPartNum; ++i)
		{
			DBWriterPartPrompt part = new DBWriterPartPrompt(this.connectionPool);
			promptParts.add(part);
		}

		for (int i = 0; i < cachePartNum; ++i)
		{
			DBWriterPartCache part = new DBWriterPartCache(this.connectionPool);
			cacheParts.add(part);
		}
	}

	public void startThread()
	{
		for (int i = 0; i < promptParts.size(); ++i)
		{
			DBWriterPartPrompt part = promptParts.get(i);
			Thread thread = new Thread(part, ThreadU.genName("Wind_DBWriterPartPromptThread"));
			thread.start();
		}

		for (int i = 0; i < cacheParts.size(); ++i)
		{
			DBWriterPartCache part = cacheParts.get(i);
			Thread thread = new Thread(part, ThreadU.genName("Wind_DBWriterPartCacheThread"));
			thread.start();
		}
	}

	public void insertCommand(String key, int playerId, String sql)
	{
		DBWriterPartCache part = cacheParts.get(playerId % cacheParts.size());
		if (part != null)
		{
			part.insertCommand(key, playerId, sql);
		}
	}

	public void insertCommand(int playerId, String sql)
	{
		DBWriterPartPrompt part = promptParts.get(playerId % promptParts.size());
		if (part != null)
		{
			part.insertCommand(playerId, sql);
		}
	}

	public boolean hasUnWriteCompleteSql(int playerId)
	{
		DBWriterPartPrompt promptPart = promptParts.get(playerId % promptParts.size());
		if (promptPart != null && promptPart.hasUnWriteCompleteSql(playerId))
		{
			return true;
		}

		DBWriterPartCache cachePart = cacheParts.get(playerId % cacheParts.size());
		if (cachePart != null && cachePart.hasUnWriteCompleteSql(playerId))
		{
			return true;
		}

		return false;
	}

	public static long getUnWritePromptSqlCount()
	{
		long count = 0;
		for (DBWriter dbWriter : dbwrites)
		{
			for (int i = 0; i < dbWriter.promptParts.size(); ++i)
			{
				count += dbWriter.promptParts.get(i).sqls.size();
			}
		}
		return count;
	}

	public static long getUnWriteCacheSqlCount()
	{
		long count = 0;
		for (DBWriter dbWriter : dbwrites)
		{
			for (int i = 0; i < dbWriter.cacheParts.size(); ++i)
			{
				count += dbWriter.cacheParts.get(i).sqls.size();
			}
		}
		return count;
	}

	public static String dumpSqlNum()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("UnWritePromptSqls: " + DBWriter.getUnWritePromptSqlCount());
		sb.append(" UnWriteCacheSqls: " + DBWriter.getUnWriteCacheSqlCount());
		return sb.toString();
	}
}
