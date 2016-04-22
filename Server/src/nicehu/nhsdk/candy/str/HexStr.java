package nicehu.nhsdk.candy.str;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class HexStr
{
	public static String toHexString(byte[] bin)
	{
		if (bin.length > 0)
		{
			return "0x" + HexBin.encode(bin);
		}

		return null;
	}

	public static byte[] fromHexString(String hexString)
	{
		if (hexString != null && hexString.startsWith("0x"))
		{
			return HexBin.decode(hexString.substring(2, hexString.length()));
		}

		return null;
	}
}
