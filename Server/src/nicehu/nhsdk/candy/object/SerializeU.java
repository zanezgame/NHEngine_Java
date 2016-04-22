package nicehu.nhsdk.candy.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.str.HexStr;

public class SerializeU
{
	private static final Logger logger = LoggerFactory.getLogger(SerializeU.class);

	public static String serializeToStr(Object object)
	{
		return HexStr.toHexString(SerializeU.serialize(object));
	}

	public static byte[] serialize(Object object)
	{

		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.flush();
			return baos.toByteArray();
		}
		catch (IOException e)
		{
			logger.error("serialize  failed.{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	public static Object unSerialize(String value)
	{
		return SerializeU.unSerialize(value.getBytes());
	}

	public static Object unSerialize(byte[] buf)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(buf);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (IOException e)
		{
			logger.error("unSerialize  failed.{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		catch (ClassNotFoundException e)
		{
			logger.error("unSerialize  failed.{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
}
