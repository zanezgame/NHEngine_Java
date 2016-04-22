package nicehu.server.authserver.logic.login.data;

import nicehu.pb.NHMsgBase;

public class AccountData
{
	private int accountId;
	private String account;
	private String pass;
	private DeviceInfo deviceInfo;

	public AccountData()
	{

	}

	public AccountData(int accountId, String account, String pass)
	{
		this.accountId = accountId;
		this.account = account;
		this.pass = pass;
	}

	public AccountData(String account, String pass, NHMsgBase.DeviceInfo deviceInfo)
	{
		this.account = account;
		this.pass = pass;
		this.deviceInfo = new DeviceInfo().fromProtobuf(deviceInfo);
	}

	public int getAccountId()
	{
		return accountId;
	}

	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}

	public DeviceInfo getDeviceInfo()
	{
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo)
	{
		this.deviceInfo = deviceInfo;
	}

}
