package nicehu.server.authserver.logic.gm.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHMsgServer.FreezeReq;
import nicehu.server.manageserver.logic.freeze.data.FreezeMgr;


public class MA_UnFreezeReqHandler extends LogicHandler
{

	private static final Logger logger = LoggerFactory.getLogger(MA_UnFreezeReqHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{
		logger.info("recv MA_UnFreezeReqHandler");
		FreezeReq request = (FreezeReq)msg.getPb(FreezeReq.getDefaultInstance());

		// Protocol protocol = new Protocol(ServerProtos.P_MANAGE_FREEZE_RES);
		// FreezeRes.Builder builder = FreezeRes.newBuilder();
		// int code = ServerCodes.E_MANAGE_FREEZE_SUCCESS;

		int accountId = request.getAccountId();

		try
		{
			do
			{
				FreezeMgr.instance.deleteFreezeInfo(accountId, false);

			} while (false);
		}
		finally
		{
		}

		// builder.setResult(result);
		// protocol.setProtoBuf(builder.build());
		// SD.transmitter.send(sender.getChannel(), protocol);
	}
}
