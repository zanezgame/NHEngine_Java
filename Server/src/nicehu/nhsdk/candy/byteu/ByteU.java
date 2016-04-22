package nicehu.nhsdk.candy.byteu;

public class ByteU
{
	public static byte[] newArray(byte[] array, int startIndex, int length)
	{
		byte[] newArray = new byte[length];
		for (int i = 0; i < length; i++)
		{
			newArray[i] = array[i + startIndex];
		}
		return newArray;
	}

	public static byte[] merge(byte[] arrayA, byte[] arrayB)
	{
		int length = arrayA.length + arrayB.length;
		byte[] newArray = new byte[length];
		int i = 0;
		for (int j = 0; j < arrayA.length; j++)
		{
			newArray[i] = arrayA[j];
			++i;
		}
		for (int j = 0; j < arrayB.length; j++)
		{
			newArray[i] = arrayB[j];
			++i;
		}
		return newArray;
	}
}
