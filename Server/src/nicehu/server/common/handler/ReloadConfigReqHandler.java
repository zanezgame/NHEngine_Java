package nicehu.server.common.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgBase.StreamObject;
import nicehu.pb.NHMsgServer.ReloadConfigReq;
import nicehu.pb.NHMsgServer.ReloadConfigRes;
import nicehu.server.manageserver.config.core.ConfigReloadMgr;

public class ReloadConfigReqHandler extends LogicHandler
{

	private static final Logger logger = LoggerFactory.getLogger(ReloadConfigReqHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{
		logger.info("recv MG_ReloadConfigReqHandler");
		ReloadConfigReq request = (ReloadConfigReq)msg.getPb(ReloadConfigReq.getDefaultInstance());

		Message message = new Message(EGMI.EGMI_SERVER_RELOAD_CONFIG_RES_VALUE);
		ReloadConfigRes.Builder builder = ReloadConfigRes.newBuilder();
		int code = EGEC.EGEC_CORE_SUCCESS_VALUE;

		List<StreamObject> serverConfigs = request.getServerConfigsList();
		List<StreamObject> clientConfigs = request.getClientConfigsList();
		boolean needAddVersion = request.getIsNeedAddVersion();
		boolean isServerConfig = request.getIsServerConfig();

		try
		{
			do
			{

				ConfigReloadMgr.instance.loadServerConfig(serverConfigs);

			} while (false);
		}
		finally
		{
		}

		builder.setCode(code);
		message.genBaseMsg(builder.build());
		SD.transmitter.send(sender.ctx, message);
	}
}
