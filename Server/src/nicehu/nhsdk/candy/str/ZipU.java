package nicehu.nhsdk.candy.str;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.util.CloseU;

public class ZipU
{
	public static String compress(String str)
	{
		if (Empty.is(str))
		{
			return str;
		}
		ByteArrayOutputStream out = null;
		GZIPOutputStream gzip = null;
		try
		{
			out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
			gzip.finish();
			gzip.flush();
			out.flush();
			return out.toString("ISO-8859-1");
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		finally
		{
			CloseU.close(gzip);
			CloseU.close(out);
		}
		return null;
	}

	public static String uncompress(String str)
	{
		if (Empty.is(str))
		{
			return str;
		}
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		GZIPInputStream gzip = null;
		try
		{
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			gzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gzip.read(buffer)) >= 0)
			{
				out.write(buffer, 0, n);
			}
			out.flush();

			// toString()使用平台默认编码，也可以显式的指定如toString("GBK")
			return out.toString();
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		finally
		{
			CloseU.close(out);
			CloseU.close(in);
			CloseU.close(gzip);
		}
		return null;
	}
}