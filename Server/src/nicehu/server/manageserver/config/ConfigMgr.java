package nicehu.server.manageserver.config;

import nicehu.server.manageserver.config.areaConfig.AreaInfoMgr;
import nicehu.server.manageserver.config.baseinfo.BaseInfoMgr;
import nicehu.server.manageserver.config.commonconfig.CommonConfigMgr;
import nicehu.server.manageserver.config.dbconfig.DBConfigMgr;
import nicehu.server.manageserver.config.serverconfig.ServerConfigMgr;
import nicehu.server.manageserver.config.whiteipinfo.WhiteIpInfoMgr;

public class ConfigMgr
{
	
	public static void loadFromFile()
	{
		CommonConfigMgr.instance.reload();
		DBConfigMgr.instance.reload();
		ServerConfigMgr.instance.reload();
	}
	public static void loadFromDB()
	{
		BaseInfoMgr.instance.reload();
		AreaInfoMgr.instance.reload();
		WhiteIpInfoMgr.instance.reload();
	}

}
