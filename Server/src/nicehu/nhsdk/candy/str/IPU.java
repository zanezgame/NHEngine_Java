package nicehu.nhsdk.candy.str;

public class IPU
{

	public static String getIp(String remoteAddr)
	{
		int index1 = remoteAddr.indexOf("/");
		int index2 = remoteAddr.indexOf(":");
		String ip = remoteAddr.substring(index1 + 1, index2);
		return ip;
	}

	public static String getPort(String remoteAddr)
	{
		int index2 = remoteAddr.indexOf(":");
		String port = remoteAddr.substring(index2);
		return port;
	}

	// 将127.0.0.1形式的IP地址转换成十进制整数
	public static long transferIPToLong(String stringIP)
	{
		// 192.168.1.1
		// 192.168
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = stringIP.indexOf(".");
		int position2 = stringIP.indexOf(".", position1 + 1);
		int position3 = stringIP.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(stringIP.substring(0, position1));
		ip[1] = Long.parseLong(stringIP.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(stringIP.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(stringIP.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	// 将十进制整数形式转换成127.0.0.1形式的ip地址
	public static String transferLongToIP(long longIP)
	{
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIP >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIP & 0x000000FF)));
		return sb.toString();
	}

}
