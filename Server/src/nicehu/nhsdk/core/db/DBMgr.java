package nicehu.nhsdk.core.db;

import nicehu.nhsdk.candy.db.core.write.DBWriter;
import nicehu.nhsdk.core.data.SD;

public class DBMgr
{
    public static void init(int serverType)
    {
    	DBWriter.init();
		SD.dbCluster = new DBCluster(SD.areaId, serverType);
    }
}
