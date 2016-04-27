package nicehu.server.manageserver.config.baseinfo;

import javax.sql.rowset.CachedRowSet;

import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.util.CloseU;
import nicehu.server.manageserver.core.MSD;

public class BaseInfoMgr
{
	public static BaseInfoMgr instance = new BaseInfoMgr();
	public BaseInfo cfg = new BaseInfo();

	public void reload()
	{
		String sql = "select * from base_info";
		CachedRowSet rs = null;
		try
		{
			rs = MSD.dbCluster.getManagerDbClient().executeQuery(sql);

			BaseInfo cfg = new BaseInfo();
			while (rs != null && rs.next())
			{
				cfg.setServerType(ParseU.pInt(rs.getString("server_type"), 0));
				cfg.setWhiteIpOpen(rs.getBoolean("white_ip_open"));
			}
			this.cfg = cfg;
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		finally
		{
			CloseU.close(rs);
		}
	}
}
