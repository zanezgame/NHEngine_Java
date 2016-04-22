package nicehu.nhsdk.candy.compress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.xerial.snappy.Snappy;

import nicehu.nhsdk.candy.log.LogU;

public class SnappyCompress
{
	public static final int MAX_COMPRESS_MESSAGE_SIZE = 1048576;  //1024*1024
	public static final int MAX_UNCOMPRESS_MESSAGE_SIZE = 3145728;// 1024*1024*3

	public static ByteBuf compress(ByteBuf body)
	{
		try
		{
			byte[] array = new byte[body.readableBytes()];
			if (body.hasArray())
			{
				System.arraycopy(body.array(), body.arrayOffset(), array, 0, body.readableBytes());
			}
			else
			{
				body.getBytes(0, array);
			}
			byte[] compressed = Snappy.compress(array);
			// System.out.println(" compresse Length: "+compressed.length);
			return Unpooled.wrappedBuffer(compressed, 0, compressed.length);
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		return null;
	}

	public static ByteBuf unCompress(ByteBuf body)
	{
		try
		{
			// System.out.println(" unCompresse Length: "+body.readableBytes());
			byte[] array = new byte[body.readableBytes()];
			if (body.hasArray())
			{
				System.arraycopy(body.array(), body.arrayOffset() + body.readerIndex(), array, 0, body.readableBytes());
			}
			else
			{
				body.getBytes(body.readerIndex(), array);
			}
			byte[] uncompressed = Snappy.uncompress(array);
			return Unpooled.wrappedBuffer(uncompressed, 0, uncompressed.length);
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		return null;
	}
}
