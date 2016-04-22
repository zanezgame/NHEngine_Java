package nicehu.nhsdk.candy.byteu;

public class ByteBuffer
{
	private int markReadIndex = 0;
	private int readIndex = 0;
	private int writeIndex = 0;

	private byte[] array;

	public ByteBuffer(int len)
	{
		array = new byte[len];
	}

	public void fromArray(byte[] from, int index, int len)
	{
		ensureWritable(len);

		System.arraycopy(from, index, array, 0, len);
		writeIndex = len;
	}

	public void writeByte(int value)
	{
		array[writeIndex] = (byte)(value & 0xFF);
		writeIndex++;
	}

	public void writeBytes(ByteBuffer in, int len)
	{
		System.arraycopy(in.array, in.readerIndex(), array, writeIndex, len);
		in.readerIndex(in.readerIndex() + len);
		writeIndex += len;
	}

	public int readerIndex()
	{
		return readIndex;
	}

	public void readerIndex(int index)
	{
		this.readIndex = index;
	}

	public int writerIndex()
	{
		return writeIndex;
	}

	public int getInt(int index)
	{
		if (index + 4 > array.length)
		{
			throw new IndexOutOfBoundsException("getInt index is outofBound");
		}
		else if (index < 0)
		{
			throw new IndexOutOfBoundsException("getInt index is < 0");
		}
		return byteArrayToInt(array, index);
	}

	private int byteArrayToInt(byte[] b, int offset)
	{
		int value = 0;
		for (int i = 0; i < 4; i++)
		{
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}

	public byte getByte(int index)
	{
		return array[index];
	}

	public boolean isReadable()
	{
		return writeIndex > readIndex;
	}

	public int readableBytes()
	{
		return writeIndex - readIndex;
	}

	public short readUnsignedByte()
	{
		short result = (short)(array[readIndex] & 0xFF);
		readIndex++;
		return result;
	}

	public byte readByte()
	{
		byte result = array[readIndex];
		readIndex++;
		return result;
	}

	public short readShort()
	{
		short result = (short)(array[readIndex] << 8 | array[readIndex + 1] & 0xFF);
		readIndex += 2;

		return result;
	}

	public int readUnsignedMedium()
	{
		int result = 0;
		for (int i = 0; i < 3; i++)
		{
			int shift = (4 - 1 - i) * 8;
			result += (array[i + readIndex] & 0x000000FF) << shift;// 往高位游
		}

		readIndex += 3;

		return result;
	}

	public int readInt()
	{
		if (readIndex + 4 > array.length)
		{
			throw new IndexOutOfBoundsException("getInt index is outofBound");
		}
		int result = byteArrayToInt(array, readIndex);
		readIndex += 4;
		return result;
	}

	public void readBytes(ByteBuffer out, int len)
	{
		out.writeBytes(this, len);
		readIndex += len;
	}

	public void ensureWritable(int newLen)
	{
		if (newLen > array.length)
		{
			byte[] newArray = new byte[newLen];
			System.arraycopy(array, 0, newArray, 0, array.length);

			array = newArray;
		}
	}

	public void markReaderIndex()
	{
		markReadIndex = readIndex;
	}

	public void resetReaderIndex()
	{
		readIndex = markReadIndex;
	}

	public byte[] array()
	{
		return array;
	}
}
