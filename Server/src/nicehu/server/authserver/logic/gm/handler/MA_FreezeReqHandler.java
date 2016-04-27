package nicehu.server.authserver.logic.gm.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.core.datatransmitter.data.ConnectNode;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHMsgServer.FreezeReq;
import nicehu.server.manageserver.logic.freeze.data.FreezeInfo;
import nicehu.server.manageserver.logic.freeze.data.FreezeMgr;

public class MA_FreezeReqHandler extends LogicHandler
{

	private static final Logger logger = LoggerFactory.getLogger(MA_FreezeReqHandler.class);

	@Override
	public void handle(ConnectNode sender, Message msg)
	{
		logger.info("recv MA_FreezeReqHandler");
		FreezeReq request = (FreezeReq)msg.getPb(FreezeReq.getDefaultInstance());

		// Protocol protocol = new Protocol(ServerProtos.P_MANAGE_FREEZE_RES);
		// FreezeRes.Builder builder = FreezeRes.newBuilder();
		// int code = ServerCodes.E_MANAGE_FREEZE_SUCCESS;

		int accountId = request.getAccountId();
		int showId = request.getShowId();
		int type = request.getShowId();
		long beginTime = request.getBeginTime();
		long endTime = request.getEndTime();

		try
		{
			do
			{
				FreezeMgr.instance.addFreezeInfo(new FreezeInfo(accountId, showId, type, beginTime, endTime));

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
