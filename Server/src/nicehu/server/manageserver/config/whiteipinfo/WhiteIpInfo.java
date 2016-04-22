package nicehu.server.manageserver.config.whiteipinfo;

import java.util.ArrayList;
import java.util.List;

public class WhiteIpInfo
{
	public List<String> whiteIps = new ArrayList<>();

	public boolean isIn(String ip)
	{
		for (String whiteIp : whiteIps)
		{
			if (ip.startsWith(whiteIp))
			{
				return true;
			}
		}
		return false;
	}

	public List<String> getWhiteIps()
	{
		return whiteIps;
	}

	public void setWhiteIps(List<String> whiteIps)
	{
		this.whiteIps = whiteIps;
	}

}
