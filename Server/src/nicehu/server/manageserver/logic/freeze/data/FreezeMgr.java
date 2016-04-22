package nicehu.server.manageserver.logic.freeze.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import nicehu.server.manageserver.logic.freeze.db.FreezeDB;

public class FreezeMgr
{
	public static FreezeMgr instance = new FreezeMgr();
	public ConcurrentHashMap<Integer, ArrayList<FreezeInfo>> freezeInfos = new ConcurrentHashMap<>();

	public FreezeInfo getFreezeInfo(int accountId)
	{
		List<FreezeInfo> freezeInfos = this.freezeInfos.get(accountId);
		long now = System.currentTimeMillis();
		FreezeInfo result = null;
		if (freezeInfos != null)
		{

			for (FreezeInfo freezeInfo : freezeInfos)
			{
				if (freezeInfo.getEndTime() > now)
				{
					if (result == null)
					{
						result = freezeInfo;
					}
					else
					{
						if (freezeInfo.getEndTime() > result.getEndTime())
						{
							result = freezeInfo;
						}
					}
				}
			}
		}
		return result;
	}

	public void addAndSaveFreezeInfo(FreezeInfo freezeInfo)
	{
		FreezeDB.add(freezeInfo);
		this.addFreezeInfo(freezeInfo);
	}

	public void addFreezeInfo(FreezeInfo freezeInfo)
	{
		ArrayList<FreezeInfo> freezeInfos = this.freezeInfos.get(freezeInfo.getAccountId());
		if (freezeInfos == null)
		{
			freezeInfos = new ArrayList<>();
		}
		freezeInfos.add(freezeInfo);
		this.freezeInfos.put(freezeInfo.getAccountId(), freezeInfos);
	}

	public void deleteFreezeInfo(int accountId, boolean saveToDB)
	{
		long now = System.currentTimeMillis();
		ArrayList<FreezeInfo> freezeInfos = this.freezeInfos.get(accountId);
		if (freezeInfos != null)
		{
			freezeInfos.stream().filter(x -> x.getEndTime() > now).forEach(x -> {
				if (x.getEndTime() > now)
				{
					x.setEndTime(now);
					if (saveToDB)
					{
						FreezeDB.delete(x);
					}
				}
			});
		}
	}

	public void init()
	{
		this.freezeInfos.clear();
		FreezeDB.query();
	}
}
