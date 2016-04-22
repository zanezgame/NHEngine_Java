package nicehu.nhsdk.candy.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.util.zip.GZIPOutputStream;

import javax.sql.rowset.CachedRowSet;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import nicehu.nhsdk.candy.log.LogU;

public class CloseU
{
	public static void close(CachedRowSet obj)
	{
		if (obj != null)
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
	}

	public static void close(Connection connection)
	{
		if (connection != null)
		{
			try
			{
				connection.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
		}
	}

	public static void close(Statement obj)
	{
		if (obj != null)
		{
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
		}
	}

	public static void close(InputStream obj)
	{
		if (obj != null)
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
	}

	public static void close(PrintWriter obj)
	{
		if (obj != null)
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
	}

	public static void close(BufferedReader obj)
	{
		if (obj != null)
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
	}

	public static void close(HttpURLConnection obj)
	{
		if (obj != null)
		{
			try
			{
				obj.disconnect();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
		}
	}

	public static void close(ByteArrayOutputStream obj)
	{
		if (obj != null)
		{
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
		}
	}

	public static void close(ByteArrayInputStream obj)
	{
		if (obj != null)
		{
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
		}
	}

	public static void close(GZIPOutputStream obj)
	{
		if (obj != null)
		{
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
		}
	}

	public static void close(CloseableHttpClient obj)
	{
		if (obj != null)
		{
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
		}
	}

	public static void close(CloseableHttpResponse obj)
	{
		if (obj != null)
		{
			try
			{
				obj.close();
			}
			catch (Exception e)
			{
				LogU.error(e);
			}
		}
	}

}
