package nicehu.server.manageserver.config.whiteipinfo;

import javax.sql.rowset.CachedRowSet;

import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.util.CloseU;
import nicehu.server.manageserver.core.data.MSD;

public class WhiteIpInfoMgr
{
	public static WhiteIpInfoMgr instance = new WhiteIpInfoMgr();
	public WhiteIpInfo cfg = new WhiteIpInfo();

	public void reload()
	{
		String sql = "select * from white_ip_info";
		CachedRowSet rs = null;
		try
		{
			rs = MSD.dbCluster.getManagerDbClient().executeQuery(sql);

			WhiteIpInfo cfg = new WhiteIpInfo();
			while (rs != null && rs.next())
			{
				cfg.getWhiteIps().add(rs.getString("ip"));
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
