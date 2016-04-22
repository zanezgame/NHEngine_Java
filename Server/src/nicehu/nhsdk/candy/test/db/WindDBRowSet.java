package nicehu.nhsdk.candy.test.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.rowset.CachedRowSetImpl;

public class WindDBRowSet extends WindRowSet
{
	private static final Logger logger = LoggerFactory.getLogger(WindDBRowSet.class);
	CachedRowSet cachedRowSet = null;

	public WindDBRowSet(ResultSet resultSet)
	{
		try
		{
			cachedRowSet = new CachedRowSetImpl();
			cachedRowSet.populate(resultSet);
		}
		catch (SQLException e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

}
