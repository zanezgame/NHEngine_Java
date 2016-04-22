package nicehu.server.common.dblog;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.db.core.DBClient;
import nicehu.nhsdk.core.data.SD;

public class LogDB
{
	private static final Logger logger = LoggerFactory.getLogger(LogDB.class);

	public static void addLog(String[] names, Object... args)
	{
		DBClient dbClient = SD.dbCluster.getLogDBClient();
		if (args.length + 1 == names.length && names.length >= 2)
		{

			StringBuffer sb = new StringBuffer();

			sb.append("INSERT INTO `").append(names[0]).append("`");

			sb.append(" (");
			for (int i = 1; i < names.length; ++i)
			{
				if (i != 1)
				{
					sb.append(",");
				}
				sb.append(names[i]);
			}
			sb.append(")");

			sb.append(" VALUES ");

			sb.append(" (");
			for (int i = 1; i < names.length; ++i)
			{
				if (i != 1)
				{
					sb.append(",");
				}

				String x = fix(args[i - 1] != null ? args[i - 1].toString() : null);

				// if (names[i].charAt(0) == 'i')
				// {
				// sb.append(x);
				// }
				// else
				// {
				if (x != null)
				{
					sb.append("'");
					sb.append(x);
					sb.append("'");
				}
				else
				{
					sb.append(x);
				}
				// }
			}
			sb.append(")");
			addSql(dbClient, sb, 0);
		}
		else
		{
			logger.error("param num error \n{}", ExceptionUtils.getStackTrace(new Throwable()));
		}
	}

	private static void addSql(DBClient dbClient, StringBuffer sqlSb, int playerId)
	{
		if (dbClient != null && sqlSb != null)
		{
			dbClient.executeAsyncUpdate(playerId, sqlSb.toString());
		}
		else
		{
			logger.error("bad dbClient or sql \n{}", ExceptionUtils.getStackTrace(new Throwable()));
		}
	}

	private static String fix(String value)
	{
		if (value != null)
		{
			value = value.replace("|", "");
			value = value.replace("\"", "");
			value = value.replace("'", "");
			value = value.replace("\n", "");
			value = value.replace("\t", "");
		}
		return value;
	}

}
