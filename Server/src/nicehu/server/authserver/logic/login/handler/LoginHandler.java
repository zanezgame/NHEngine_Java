package nicehu.server.authserver.logic.login.handler;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import nicehu.nhsdk.candy.data.Message;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.data.ServerInfo;
import nicehu.nhsdk.core.handler.LogicHandler;
import nicehu.pb.NHDefine;
import nicehu.pb.NHDefine.EGEC;
import nicehu.pb.NHDefine.EGMI;
import nicehu.pb.NHMsgAuth.LoginReq;
import nicehu.pb.NHMsgAuth.LoginRes;
import nicehu.pb.NHMsgBase;
import nicehu.server.authserver.core.ASD;
import nicehu.server.authserver.core.AuthSession;
import nicehu.server.authserver.logic.login.data.AccountData;
import nicehu.server.authserver.logic.login.data.DeviceInfo;
import nicehu.server.authserver.logic.login.db.LoginDB;
import nicehu.server.manageserver.config.areaConfig.AreaInfo;
import nicehu.server.manageserver.config.areaConfig.AreaInfoMgr;

public class LoginHandler extends LogicHandler
{

	private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

	@Override
	public void handle(ChannelHandlerContext ctx, Message msg)
	{

		LoginReq request = (LoginReq)msg.getPb(LoginReq.getDefaultInstance());
		logger.debug("recv LoginHandler, Account = {}", request.getAccount());

		LoginRes.Builder builder = LoginRes.newBuilder();
		msg.setId(EGMI.EGMI_LOGIN_RES_VALUE);
		int result = EGEC.EGEC_CORE_SUCCESS_VALUE;

		String clientUuid = request.getRandomSeed();
		String account = request.getAccount();
		String pass = request.getPass();
		DeviceInfo deviceInfo = new DeviceInfo(request.getDeviceInfo());
		do
		{
			if (Empty.is(clientUuid) || clientUuid.length() > 36)
			{
				logger.warn("RANDOM_SEED_LENGTH_INVALID :{}", clientUuid);
				result = EGEC.EGEC_CORE_DATA_CHECK_FAILD_VALUE;
				break;
			}
			if (Empty.is(account))
			{
				logger.warn("EMAIL_LENGTH_NOT_VALID :{}", account);
				result = EGEC.EGEC_CORE_DATA_CHECK_FAILD_VALUE;
				break;
			}
			if (Empty.is(pass))
			{
				logger.warn("PASSWORD_IS_NULL");
				result = EGEC.EGEC_CORE_DATA_CHECK_FAILD_VALUE;
				break;
			}
			if (deviceInfo == null || Empty.is(deviceInfo.getUdid()))
			{
				result = EGEC.EGEC_CORE_DATA_CHECK_FAILD_VALUE;
				break;
			}
			AccountData accountData = LoginDB.selectAccount(account, pass);
			if (accountData == null)
			{
				logger.warn("INCORRECT_NAME_OR_PASSWORD");
				result = EGEC.EGEC_CORE_DATA_CHECK_FAILD_VALUE;
				break;
			}
			LoginDB.updateLoginTime(accountData);

			// return area list
			List<AreaInfo> areas = AreaInfoMgr.instance.areas;
			for (int i = 0; i < areas.size(); i++)
			{
				AreaInfo area = areas.get(i);
				if (area.getStatus() == NHDefine.EAreaStatus.EAS_Unknow_VALUE)
				{
					continue;
				}
				int areaId = area.getAreaId();
				NHMsgBase.Area.Builder areaBuilder = NHMsgBase.Area.newBuilder();
				areaBuilder.setAreaId(areaId);
				areaBuilder.setName(area.getAreaName());
				areaBuilder.setStatus(area.getStatus());
				// 获取该区的Proxy是否开启
				ServerInfo proxyServerInfo = SD.getProxyServer(areaId);
				if (proxyServerInfo != null)
				{
					areaBuilder.setGateIp(proxyServerInfo.getIpForClient());
					areaBuilder.setGatePort(proxyServerInfo.getPortForSocketClient());
				}
				builder.addAreas(areaBuilder);
			}

			// gen token
			String serverUuid = UUID.randomUUID().toString();
			String token = clientUuid + serverUuid;
			AuthSession session = new AuthSession(token, deviceInfo, ctx.channel().remoteAddress().toString());
			ASD.sessions.put(accountData.getAccountId(), session);

			// return data
			builder.setToken(serverUuid);
			builder.setAccountId(accountData.getAccountId());

		} while (false);

		builder.setResult(result);
		msg.genBaseMsg(builder.build());
		SD.transmitter.sendAndClose(ctx, msg);
	}

}
