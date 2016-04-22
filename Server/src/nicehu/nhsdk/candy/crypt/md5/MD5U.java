package nicehu.nhsdk.candy.crypt.md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.util.CloseU;

public class MD5U
{
	private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static MessageDigest messagedigest = null;
	static
	{
		try
		{
			messagedigest = MessageDigest.getInstance("MD5");
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
	}
	
	public static void main(String[] args)
	{
		LogU.debug(MD5U.gen("wind"));
		LogU.debug(MD5U.gen("wind"));
	}

	public static String gen(String s)
	{
		return gen(s.getBytes());
	}

	public static String gen(byte[] bytes)
	{
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	public static String gen(File file)
	{
		InputStream in = null;
		try
		{
			in = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = in.read(buffer)) > 0)
			{
				messagedigest.update(buffer, 0, numRead);
			}
		}
		catch(Exception e)
		{
			LogU.error(e);
		}
		finally
		{
			CloseU.close(in);
		}
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[])
	{
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n)
	{
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++)
		{
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer)
	{
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}