package nicehu.nhsdk.core.db;

import nicehu.nhsdk.candy.db.core.write.DBWriter;
import nicehu.nhsdk.core.data.AreaData;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.type.ServerType;

public class DBMgr
{
    public static void init(int serverType)
    {
    	DBWriter.init();
		SD.dbCluster = new DBCluster(AreaData.getAreaId(), serverType);
    }
}
