package nicehu.server.authserver.logic.login.data;

import nicehu.pb.NHMsgBase;

public class DeviceInfo
{
	private String oSType;
	private String oSVersion;
	private String deviceName;
	private String udid;

	public DeviceInfo()
	{
	}

	public DeviceInfo(String oSType, String oSVersion, String deviceName, String udid)
	{
		super();
		this.oSType = oSType;
		this.oSVersion = oSVersion;
		this.deviceName = deviceName;
		this.udid = udid;
	}
	public DeviceInfo(NHMsgBase.DeviceInfo deviceInfo)
	{
		this.oSType = deviceInfo.getOSType();
		this.oSVersion = deviceInfo.getOSVersion();
		this.deviceName = deviceInfo.getDeviceName();
		this.udid = deviceInfo.getUdid();
	}

	public DeviceInfo fromProtobuf(NHMsgBase.DeviceInfo deviceInfo)
	{
		this.oSType = deviceInfo.getOSType();
		this.oSVersion = deviceInfo.getOSVersion();
		this.deviceName = deviceInfo.getDeviceName();
		this.udid = deviceInfo.getUdid();
		return this;
	}

	public NHMsgBase.DeviceInfo toProtobuf()
	{
		NHMsgBase.DeviceInfo.Builder builder = NHMsgBase.DeviceInfo.newBuilder();
		builder.setOSType(this.getoSType());
		builder.setOSVersion(this.getoSVersion());
		builder.setDeviceName(this.getDeviceName());
		builder.setUdid(this.getUdid());
		return builder.build();
	}

	public String getoSType()
	{
		return oSType;
	}

	public void setoSType(String oSType)
	{
		this.oSType = oSType;
	}

	public String getoSVersion()
	{
		return oSVersion;
	}

	public void setoSVersion(String oSVersion)
	{
		this.oSVersion = oSVersion;
	}

	public String getDeviceName()
	{
		return deviceName;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}

	public String getUdid()
	{
		return udid;
	}

	public void setUdid(String udid)
	{
		this.udid = udid;
	}

}
