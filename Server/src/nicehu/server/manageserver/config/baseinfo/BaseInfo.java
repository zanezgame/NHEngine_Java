package nicehu.server.manageserver.config.baseinfo;

public class BaseInfo
{

	private int serverType;
	private boolean whiteIpOpen;

	public boolean isWhiteIpOpen()
	{
		return whiteIpOpen;
	}

	public void setWhiteIpOpen(boolean whiteIpOpen)
	{
		this.whiteIpOpen = whiteIpOpen;
	}

	public int getServerType()
	{
		return serverType;
	}

	public void setServerType(int serverType)
	{
		this.serverType = serverType;
	}

}
