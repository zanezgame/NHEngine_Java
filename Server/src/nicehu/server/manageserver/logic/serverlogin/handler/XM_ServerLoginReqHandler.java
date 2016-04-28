package nicehu.server.manageserver.logic.serverlogin.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.json.JsonU;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgBase.Pair;
import nicehu.pb.NHMsgServer.ServerLoginReq;
import nicehu.pb.NHMsgServer.ServerLoginRes;
import nicehu.server.manageserver.config.areaConfig.AreaInfoMgr;
import nicehu.server.manageserver.config.baseinfo.BaseInfoMgr;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;
import nicehu.server.manageserver.config.core.ConfigPath;
import nicehu.server.manageserver.config.dbconfig.DBConfigMgr;
import nicehu.server.manageserver.config.serverconfig.ServerConfig;
import nicehu.server.manageserver.config.serverconfig.ServerConfigMgr;
import nicehu.server.manageserver.config.whiteipinfo.WhiteIpInfoMgr;
import nicehu.server.manageserver.core.MSD;

public class XM_ServerLoginReqHandler extends LogicHandler
{
	private static final Logger logger = LoggerFactory.getLogger(XM_ServerLoginReqHandler.class);

	@Override
	public void handle(ChannelHandlerContext ctx, Message msg)
	{
		ServerLoginReq request = (ServerLoginReq)msg.getPb(ServerLoginReq.getDefaultInstance());
		ServerLoginRes.Builder builder = ServerLoginRes.newBuilder();
		Message message = new Message(EGMI.EGMI_SERVER_LOGIN_RES_VALUE);
		logger.warn("receive ServerLoginHandler, ServerName: " + request.getServerName() + " remoteAddress :" + ctx.channel().remoteAddress().toString());

		ServerConfig serverConfig = ServerConfigMgr.instance.getServerConfig(request.getServerName());
		ServerInfo serverInfo = null;
		if (serverConfig != null)
		{
			serverInfo = new ServerInfo(serverConfig, request);
		}
		do
		{
			if (serverInfo == null)
			{
				builder.setResult(EGEC.EGEC_CORE_ERROR_VALUE);
				break;
			}

			ServerConfigMgr.instance.reload();
			DBConfigMgr.instance.reload();

			MSD.transmitter.addServerNode(serverInfo.getId(), ctx);
			MSD.serverInfoMgr.addServer(serverInfo);

			builder.setResult(EGEC.EGEC_CORE_SUCCESS_VALUE);

			builder.addServerConfigs(this.genStreamObject(ConfigPath.file_common, JsonU.getJsonStr(CommonConfigMgr.instance)));
			builder.addServerConfigs(this.genStreamObject(ConfigPath.file_DBConfig, JsonU.getJsonStr(DBConfigMgr.instance)));

			builder.addServerConfigs(this.genStreamObject(ConfigPath.db_BaseInfo, JsonU.getJsonStr(BaseInfoMgr.instance)));
			builder.addServerConfigs(this.genStreamObject(ConfigPath.db_AreaInfo, JsonU.getJsonStr(AreaInfoMgr.instance)));
			builder.addServerConfigs(this.genStreamObject(ConfigPath.db_WhiteIpInfo, JsonU.getJsonStr(WhiteIpInfoMgr.instance)));

			// if (request.getServerType() == ServerType.AUTH || request.getServerType() == ServerType.GAME ||
			// request.getServerType() == ServerType.CENTER)
			// {
			// for (Map.Entry<String, String> entry : ClientConfigMgr.instance.getClientCompressConfigs().entrySet())
			// {
			// StreamObject.Builder tmp = StreamObject.newBuilder();
			// tmp.setName(entry.getKey());
			// tmp.setStreamBuffer(entry.getValue());
			// builder.addClientObjects(tmp.build());
			// }
			// }
		} while (false);

		message.genBaseMsg(builder.build());
		MSD.transmitter.sendToServer(serverInfo.getId(), message);
	}

	public Pair genStreamObject(String name, String value)
	{
		Pair.Builder tmp = Pair.newBuilder();
		tmp.setKey(name);
		tmp.setValue(value);
		return tmp.build();
	}

}