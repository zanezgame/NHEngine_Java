package nicehu.nhsdk.candy.test.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WindRowSet
{
	private static final Logger logger = LoggerFactory.getLogger(WindRowSet.class);

	public static String genMCacheValue(CachedRowSet rs)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			if (rs.next())
			{
				ResultSetMetaData metaData = rs.getMetaData();
				int size = metaData.getColumnCount();
				for (int i = 1; i <= size; i++)
				{
					String key = metaData.getColumnLabel(i);
					String value = rs.getString(key);
					sb.append(key).append("<&>").append(value).append("<|>");
				}
			}
			rs.beforeFirst();
		}
		catch (SQLException e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		System.out.println("!!!!!!  GEN MCache Str: " + sb.toString());
		return sb.toString();
	}
}
