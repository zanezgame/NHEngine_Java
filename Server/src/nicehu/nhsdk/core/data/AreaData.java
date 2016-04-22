package nicehu.nhsdk.core.data;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AreaData 
{
	private static final Logger logger = LoggerFactory.getLogger(AreaData.class);
	
	private static int areaId = 0;
	private static long areaStartTimeMS=-1;

	public static int getAreaId() 
	{
		if (areaId ==0)
		{
			logger.error("bad areaId\n{}",ExceptionUtils.getStackTrace(new Throwable()));
		}
		return areaId;
	}
	
	public synchronized static void setAreaId(int areaId)
	{
		logger.warn("Set AreaId = {}", areaId);
		AreaData.areaId = areaId;
	}
	
	public synchronized static void setAreaStartTimeMS(long areaStartTimeMS)
	{
		AreaData.areaStartTimeMS=areaStartTimeMS;
	}
	
	public synchronized static long getAreaStartTimeMS()
	{
		if(AreaData.areaStartTimeMS==-1)
		{
			logger.error("bad areaStartTimeMS\n{}",ExceptionUtils.getStackTrace(new Throwable()));
		}
		return AreaData.areaStartTimeMS;
	}
}
