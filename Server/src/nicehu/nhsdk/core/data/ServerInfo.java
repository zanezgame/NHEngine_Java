package nicehu.nhsdk.core.data;

import java.util.ArrayList;
import java.util.List;

import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.core.type.ServerType;
import nicehu.pb.NHMsgBase;
import nicehu.pb.NHMsgServer.ServerLoginReq;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;

public class ServerInfo
{
	public static final int SERVER_STATUS_NORMAL = 1;
	public static final int SERVER_STATUS_STARTING = 2;
	public static final int SERVER_STATUS_CLOSED = 3;
	public static final int SERVER_RELOAD_NORMAL = 1;
	public static final int SERVER_RELOAD_DOING = 2;
	public static final int SERVER_RELOAD_SUCCESS = 3;
	public static final int SERVER_RELOAD_FAILED = 4;

	private int id;
	private String name;
	private int status;
	private int reloadState = SERVER_RELOAD_NORMAL;
	private String ipForServer = null;
	private String ipForClient = null;
	private int portForServer = 0;
	private int portForSocketClient = 0;
	private int portForHttpClient = 0;
	private int areaId = 0;
	private List<Integer> serverTypes = new ArrayList<Integer>();// 关心的serve类型

	public ServerInfo(ServerConfig serverConfig)
	{
		this.id = serverConfig.getServerId();
		this.name = serverConfig.getServerName();
		this.status = ServerInfo.SERVER_STATUS_NORMAL;
		this.areaId = serverConfig.getAreaId();
		this.serverTypes = ServerType.getCareServerTypes(ServerType.getType(serverConfig.getServerId()));
		this.initIpPort(serverConfig);
	}

	public ServerInfo(ServerConfig serverConfig, ServerLoginReq req)
	{
		this.id = serverConfig.getServerId();
		this.name = req.getServerName();
		this.status = ServerInfo.SERVER_STATUS_STARTING;
		this.areaId = serverConfig.getAreaId();
		this.serverTypes = ServerType.getCareServerTypes(req.getServerType());
		this.initIpPort(serverConfig);
	}

	public ServerInfo(NHMsgBase.ServerInfo serverInfo)
	{
		this.id = serverInfo.getId();
		this.name = serverInfo.getServerName();
		this.status = serverInfo.getStatus();
		this.areaId = serverInfo.getAreaId();
		this.ipForServer = serverInfo.getIpForServer();
		this.portForServer = serverInfo.getPortForServer();
		this.ipForClient = serverInfo.getIpForClient();
		this.portForSocketClient = serverInfo.getPortForSocketClient();
		this.portForHttpClient = serverInfo.getPortForHttpClient();
	}

	public NHMsgBase.ServerInfo toProto()
	{
		NHMsgBase.ServerInfo.Builder builder = NHMsgBase.ServerInfo.newBuilder();
		builder.setId(id);
		builder.setServerName(name);
		builder.setStatus(status);
		builder.setAreaId(this.areaId);// .addAllAreaIds(this.areaIds);

		if (null != this.ipForServer)
		{
			builder.setIpForServer(this.ipForServer);
		}
		builder.setPortForServer(this.portForServer);
		if (null != this.ipForClient)
		{
			builder.setIpForClient(this.ipForClient);
		}
		builder.setPortForSocketClient(this.portForSocketClient);
		builder.setPortForHttpClient(this.portForHttpClient);

		return builder.build();
	}

	public void initIpPort(ServerConfig serverConfig)
	{
		String str = serverConfig.getAttr("IpForServer");
		if (!Empty.is(str))
		{
			this.setIpForServer(ParseU.pStr(str, ""));
		}
		str = serverConfig.getAttr("PortForServer");
		if (!Empty.is(str))
		{
			this.setPortForServer(ParseU.pInt(str, 0));
		}
		str = serverConfig.getAttr("IpForClient");
		if (!Empty.is(str))
		{
			this.setIpForClient(ParseU.pStr(str, ""));
		}
		str = serverConfig.getAttr("PortForSocketClient");
		if (!Empty.is(str))
		{
			this.setPortForSocketClient(ParseU.pInt(str, 0));
		}
		str = serverConfig.getAttr("PortForHttpClient");
		if (!Empty.is(str))
		{
			this.setPortForHttpClient(ParseU.pInt(str, 0));
		}
	}

	public String getStateName()
	{
		switch (status)
		{
			case SERVER_STATUS_NORMAL:
			{
				return "正常运行中";
			}
			case SERVER_STATUS_STARTING:
			{
				return "正在启动中";
			}
			case SERVER_STATUS_CLOSED:
			{
				return "服务器已关闭!";
			}
		}
		return "";
	}

	public String getReloadStateName()
	{
		switch (reloadState)
		{
			case SERVER_RELOAD_NORMAL:
			{
				return "正在运行";
			}
			case SERVER_RELOAD_DOING:
			{
				return "正在更新";
			}
			case SERVER_RELOAD_SUCCESS:
			{
				return "更新成功";
			}
			case SERVER_RELOAD_FAILED:
			{
				return "更新失败";
			}
		}
		return "";
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getAreaId()
	{
		return areaId;
	}

	public void setAreaId(int areaId)
	{
		this.areaId = areaId;
	}

	public List<Integer> getServerTypes()
	{
		return serverTypes;
	}

	public String getIpForServer()
	{
		return ipForServer;
	}

	public void setIpForServer(String ipForServer)
	{
		this.ipForServer = ipForServer;
	}

	public String getIpForClient()
	{
		return ipForClient;
	}

	public void setIpForClient(String ipForClient)
	{
		this.ipForClient = ipForClient;
	}

	public int getPortForServer()
	{
		return portForServer;
	}

	public void setPortForServer(int portForServer)
	{
		this.portForServer = portForServer;
	}

	

	public int getPortForSocketClient()
	{
		return portForSocketClient;
	}

	public void setPortForSocketClient(int portForSocketClient)
	{
		this.portForSocketClient = portForSocketClient;
	}

	public int getPortForHttpClient()
	{
		return portForHttpClient;
	}

	public void setPortForHttpClient(int portForHttpClient)
	{
		this.portForHttpClient = portForHttpClient;
	}

	public int getReloadState()
	{
		return reloadState;
	}

	public void setReloadState(int reloadState)
	{
		this.reloadState = reloadState;
	}

}
