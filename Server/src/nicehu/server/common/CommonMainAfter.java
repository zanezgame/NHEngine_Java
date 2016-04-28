package nicehu.server.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgBase.Pair;
import nicehu.pb.NHMsgServer.ServerLoginConfirm;
import nicehu.pb.NHMsgServer.ServerLoginReq;

public abstract class CommonMainAfter
{
	private static final Logger logger = LoggerFactory.getLogger(CommonMainAfter.class);

	public void serverLogin(ChannelHandlerContext ctx, int serverType, String serverName)
	{
		ServerLoginReq.Builder builder = ServerLoginReq.newBuilder();
		Message message = new Message(EGMI.EGMI_SERVER_LOGIN_REQ_VALUE);
		builder.setServerType(serverType);
		builder.setServerName(serverName);
		message.genBaseMsg(builder.build());
		SD.transmitter.send(ctx, message);
	}

	public void serverLoginConfirm(int manageServerId, int status)
	{
		logger.warn("Send ServerLoginConfimReq !!!");
		ServerLoginConfirm.Builder builder = ServerLoginConfirm.newBuilder();
		Message message = new Message(EGMI.EGMI_SERVER_LOGIN_CONFIRM_VALUE);
		builder.setStatus(status);
		message.genBaseMsg(builder.build());
		SD.transmitter.sendToServer(manageServerId, message);
	}

	public abstract void mainAfter(ConnectNode sender, int result,List<Pair> serverStreamObjects,
		List<Pair> clientsObjects);

	public void connectOtherServers(List<ServerInfo> servers)
	{
	}

}