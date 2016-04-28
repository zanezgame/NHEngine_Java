package nicehu.server.manageserver.config.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.json.JsonU;
import nicehu.pb.NHMsgBase.Pair;
import nicehu.server.manageserver.config.areaConfig.AreaInfoMgr;
import nicehu.server.manageserver.config.baseinfo.BaseInfoMgr;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;
import nicehu.server.manageserver.config.dbconfig.DBConfigMgr;
import nicehu.server.manageserver.config.whiteipinfo.WhiteIpInfoMgr;

public class ConfigReloadMgr
{
	private static final Logger logger = LoggerFactory.getLogger(ConfigReloadMgr.class);
	public static ConfigReloadMgr instance = new ConfigReloadMgr();

	public synchronized void loadServerConfig(List<Pair> serverConfigs)
	{
		for (Pair serverConfig : serverConfigs)
		{
			switch (serverConfig.getKey())
			{
				case ConfigPath.file_common:
				{
					CommonConfigMgr.instance = JsonU.getJavaObj(CommonConfigMgr.class, serverConfig.getValue());
					break;
				}
				case ConfigPath.file_DBConfig:
				{
					DBConfigMgr.instance = JsonU.getJavaObj(DBConfigMgr.class, serverConfig.getValue());
					break;
				}

				case ConfigPath.db_BaseInfo:
				{
					BaseInfoMgr.instance = JsonU.getJavaObj(BaseInfoMgr.class, serverConfig.getValue());
					break;
				}
				case ConfigPath.db_AreaInfo:
				{
					AreaInfoMgr.instance = JsonU.getJavaObj(AreaInfoMgr.class, serverConfig.getValue());
					break;
				}
				case ConfigPath.db_WhiteIpInfo:
				{
					WhiteIpInfoMgr.instance = JsonU.getJavaObj(WhiteIpInfoMgr.class, serverConfig.getValue());
					break;
				}
			}
		}

	}
}
