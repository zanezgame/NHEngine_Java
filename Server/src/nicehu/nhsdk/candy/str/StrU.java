package nicehu.nhsdk.candy.str;

public class StrU
{
	/**
	 * 补齐字符串,使之长度达到size
	 * 
	 * @param obj 待补齐的字符串
	 * @param size 想要达到的长度
	 * @param fill 填充元素
	 * @param type <0 左补齐 >0右补齐
	 * @return
	 */
	public static String fixSize(Object value, int size, String fill, int type)
	{
		String result = String.valueOf(value);
		if (type <= 0)
		{
			while (result.length() < size)
			{
				result = fill + result;
			}
		}
		else
		{
			while (result.length() < size)
			{
				result = result + fill;
			}
		}
		return result;

	}
}
