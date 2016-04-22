package nicehu.nhsdk.core.handler.serverinit;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ServerNode;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgBase.StreamObject;
import nicehu.pb.NHMsgServer.ServerLoginConfirm;
import nicehu.pb.NHMsgServer.ServerLoginReq;

public class ServerInitHandler
{
	private static final Logger logger = LoggerFactory.getLogger(ServerInitHandler.class);

	public void processServerLoginRes(ServerNode sender, int result, int serverID, String ServerConfig, List<StreamObject> serverStreamObjects,
		List<StreamObject> clientsObjects, int areaId, int tiemZone)
	{
		logger.warn("receive ServerLoginRes localServerId:" + Integer.toHexString(serverID));
		if (result == 0)
		{
			SD.serverId = serverID;
			serverLoginConfirm(sender.getServerId(), 2);
		}
	}

	public void processServerSync(List<ServerInfo> servers)
	{
	}

	public void serverLogin(ChannelHandlerContext ctx, int serverType, String serverName)
	{
		ServerLoginReq.Builder builder = ServerLoginReq.newBuilder();
		Message message = new Message(EGMI.EGMI_SERVER_LOGIN_REQ_VALUE);
		builder.setServerType(serverType);
		builder.setServerName(serverName);
		message.setProtoBuf(builder.build());
		SD.transmitter.send(ctx, message);
	}

	public void serverLoginConfirm(int manageServerId, int status)
	{
		ServerLoginConfirm.Builder builder = ServerLoginConfirm.newBuilder();
		Message message = new Message(EGMI.EGMI_SERVER_LOGIN_CONFIRM_VALUE);
		builder.setStatus(status);
		message.setProtoBuf(builder.build());
		SD.transmitter.sendToServer(manageServerId, message);
	}

}