package nicehu.server.manageserver.config.areaConfig;

import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import nicehu.nhsdk.candy.db.core.DBClient;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.nhsdk.candy.util.CloseU;
import nicehu.nhsdk.core.data.AreaData;
import nicehu.pb.NHDefine;
import nicehu.server.manageserver.core.data.MSD;

public class AreaInfoMgr
{
	public static AreaInfoMgr instance = new AreaInfoMgr();
	public List<AreaInfo> areas = new ArrayList<AreaInfo>();

	public void reload()
	{
		String areaSql = "select * from area_info order by id asc";
		CachedRowSet rs = null;
		try
		{
			DBClient managerDb = MSD.dbCluster.getManagerDbClient();
			rs = managerDb.executeQuery(areaSql);

			List<AreaInfo> areas = new ArrayList<AreaInfo>();
			while (rs != null && rs.next())
			{
				AreaInfo area = new AreaInfo();
				area.setAreaId(ParseU.pInt(rs.getString("id"), 0));
				area.setAreaName(ParseU.pStr(rs.getString("name"), ""));
				area.setAreaStartTime(TimeU.getLong(TimeU.getStr(rs.getDate("areaStartTime").getTime()), System.currentTimeMillis()));
				area.setStatus(ParseU.pInt(rs.getString("status"), NHDefine.EAreaStatus.EAS_Unknow_VALUE));
				if (area.getAreaId() == AreaData.getAreaId())
				{
					AreaData.setAreaStartTimeMS(area.getAreaStartTime());
				}
				areas.add(area);

			}
			if (areas.size() == 0)
			{
				AreaInfo area = new AreaInfo();
				area.setAreaId(ParseU.pInt("1", 0));
				area.setAreaName(ParseU.pStr("默认一区", ""));
				area.setAreaStartTime(TimeU.getLong(TimeU.getStr(), System.currentTimeMillis()));
				area.setStatus(ParseU.pInt("1", NHDefine.EAreaStatus.EAS_Unknow_VALUE));
				if (area.getAreaId() == AreaData.getAreaId())
				{
					AreaData.setAreaStartTimeMS(area.getAreaStartTime());
				}
				areas.add(area);

			}
			this.areas = areas;
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
