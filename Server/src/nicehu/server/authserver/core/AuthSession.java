package nicehu.server.authserver.core;

import nicehu.server.authserver.logic.login.data.DeviceInfo;

public class AuthSession
{
	private String token;
	private DeviceInfo deviceInfo;
	private String ip;
	private long loginTime;

	public AuthSession(String token, DeviceInfo deviceInfo, String ip)
	{
		this.token = token;
		this.ip = ip;
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

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
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
