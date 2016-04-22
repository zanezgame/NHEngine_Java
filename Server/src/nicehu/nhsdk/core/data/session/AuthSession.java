package nicehu.nhsdk.core.data.session;

import nicehu.server.authserver.logic.login.data.DeviceInfo;

public class AuthSession
{
	private String token;
	private DeviceInfo deviceInfo;
	private String ipMessage;
	private long loginTime;

	public AuthSession(String token, DeviceInfo deviceInfo, String ipMessage)
	{
		this.token = token;
		this.ipMessage = ipMessage;
		this.deviceInfo = deviceInfo;
		this.loginTime = System.currentTimeMillis();
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public DeviceInfo getDeviceInfo()
	{
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo)
	{
		this.deviceInfo = deviceInfo;
	}

	public String getIpMessage()
	{
		return ipMessage;
	}

	public void setIpMessage(String ipMessage)
	{
		this.ipMessage = ipMessage;
	}

	public long getLoginTime()
	{
		return loginTime;
	}

	public void setLoginTime(long loginTime)
	{
		this.loginTime = loginTime;
	}

}
