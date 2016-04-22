package nicehu.nhsdk.candy.random;

import java.util.Random;

public class RandomU
{
	/**
	 * 按指定范围生成一个doulbe,[min,max)
	 */
	public static double randomDouble(double min, double max)
	{
		Random random = new Random();
		double result = random.nextDouble();
		result = result * (max - min) + min;
		return result;
	}

	/**
	 * 返回 [min,max] 区间的值
	 */
	public static int randomInt(int min, int max)
	{
		Random random = new Random();
		int randInt = random.nextInt(max - min + 1);
		return min + randInt;

	}

}
