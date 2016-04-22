package nicehu.server.authserver.logic.login.handler.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHMsgAuth.CreateAccountReq;
import nicehu.pb.NHMsgAuth.CreateAccountRes;
import nicehu.pb.NHMsgBase;
import nicehu.server.authserver.logic.login.data.AccountData;
import nicehu.server.authserver.logic.login.db.LoginDB;

public class CA_CreateAccountHandler extends LogicHandler
{

	private static final Logger logger = LoggerFactory.getLogger(CA_CreateAccountHandler.class);

	@Override
	public void handle(ChannelHandlerContext ctx, Message msg)
	{

		CreateAccountReq request = (CreateAccountReq)msg.protoBuf;
		logger.info("recv CA_CreateAccountReq, account: " + request.getAccount() + " pass: " + request.getPass());

		CreateAccountRes.Builder builder = CreateAccountRes.newBuilder();
		msg.setId(NHDefine.EGMI.EGMI_CREATE_ACCOUNT_RES_VALUE);
		int result = NHDefine.EGEC.EGEC_CORE_SUCCESS_VALUE;

		String account = request.getAccount();
		String pass = request.getPass();
		NHMsgBase.DeviceInfo deviceInfo = request.getDeviceInfo();

		try
		{
			do
			{
				if (Empty.is(account))
				{
					logger.warn("ACCOUNT IS EMPTY");
					result = EGEC.EGEC_CORE_DATA_CHECK_FAILD_VALUE;
					break;
				}
				if (Empty.is(pass))
				{
					logger.warn("PASSWORD IS EMPTY");
					result = EGEC.EGEC_CORE_DATA_CHECK_FAILD_VALUE;
					break;
				}
				AccountData accountData = new AccountData(account,pass,deviceInfo);
                //TODO 判断账号是否存在
				//创建账号
				LoginDB.createAccount(accountData);

			} while (false);
		}
		finally
		{

		}

		builder.setResult(result);
		msg.setProtoBuf(builder.build());
		SD.transmitter.sendAndClose(ctx, msg);
	}

}
