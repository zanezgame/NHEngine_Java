package nicehu.server.common.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.data.Data;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.core.handler.network.NetworkHandler;

public class Statistics
{
	private static final Logger logger = LoggerFactory.getLogger(Statistics.class);
	private Sigar sigar = new Sigar();;
	private List<NetInterfaceConfig> netInterfaceConfigs = new ArrayList<>();
	private HashMap<String, NetInterfaceStat> lastNetInterfaceStatMap = new HashMap<>();
	private HashMap<String, NetInterfaceStat> currentNetInterfaceStatMap = new HashMap<>();
	private long lastSampleTime = System.currentTimeMillis();;
	private long lastAppReceiveSize = 0;
	private long lastAppSendSize = 0;
	private long currentAppReceiveSize = 0;
	private long currentAppSendSize = 0;

	public Statistics()
	{
		try
		{
			this.netInterfaceConfigs = this.getNetInterfaceConfigs();
			this.recordNetInterfaceState();
			this.recordAppReceiveState();
			this.recordAppSendState();
		}
		catch (Exception e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private List<NetInterfaceConfig> getNetInterfaceConfigs()
		throws SigarException
	{
		List<NetInterfaceConfig> netInterfaceConfigs = new ArrayList<NetInterfaceConfig>();
		String[] netInterfaceNameList = this.sigar.getNetInterfaceList();
		for (String netInterfaceName : netInterfaceNameList)
		{
			NetInterfaceConfig netInterfaceConfig = sigar.getNetInterfaceConfig(netInterfaceName);
			netInterfaceConfigs.add(netInterfaceConfig);
		}
		return netInterfaceConfigs;
	}

	private void recordAppReceiveState()
	{
		this.lastAppReceiveSize = this.currentAppReceiveSize;
		this.currentAppReceiveSize = NetworkHandler.appReceiveSize;
	}

	private void recordAppSendState()
	{
		this.lastAppSendSize = this.currentAppSendSize;
		this.currentAppSendSize = NetworkHandler.appSendSize;
	}

	private void recordNetInterfaceState()
		throws SigarException
	{
		this.lastNetInterfaceStatMap = this.currentNetInterfaceStatMap;
		this.currentNetInterfaceStatMap = new HashMap<String, NetInterfaceStat>();
		for (NetInterfaceConfig netInterfaceConfig : this.netInterfaceConfigs)
		{
			NetInterfaceStat stat = this.sigar.getNetInterfaceStat(netInterfaceConfig.getName());
			this.currentNetInterfaceStatMap.put(netInterfaceConfig.getAddress(), stat);
		}
	}

	public int getAppReceiveSpeed(long spaceTime)
	{
		this.recordAppReceiveState();
		return (int)((this.currentAppReceiveSize - this.lastAppReceiveSize) / spaceTime);

	}

	public int getAppSendSpeed(long spaceTime)
	{
		this.recordAppSendState();
		return (int)((this.currentAppSendSize - this.lastAppSendSize) / spaceTime);

	}

	public List<Data> getDatas()
	{
		List<Data> datas = new ArrayList<Data>();
		Data data = new Data();
		try
		{
			long spaceTime = (System.currentTimeMillis() - this.lastSampleTime) / 1000;
			if (spaceTime == 0)
			{
				return datas;
			}
			this.lastSampleTime = System.currentTimeMillis();
			ProcMem procMem = sigar.getProcMem(sigar.getPid());
			data.addAttr("virtualMemory", procMem.getSize());
			data.addAttr("residentMemory", procMem.getResident());
			data.addAttr("sharedMemory", procMem.getShare());
			data.addAttr("cpuPercent", (int)(10 * sigar.getProcCpu(this.sigar.getPid()).getPercent()));
			data.addAttr("appReceiveSpeed", this.getAppReceiveSpeed(spaceTime));
			data.addAttr("appSendSpeed", this.getAppSendSpeed(spaceTime));
			datas.add(data);
			datas.addAll(this.getNetworkStatus(spaceTime));
		}
		catch (Exception e)
		{
			LogU.error(e);
		}
		return datas;
	}

	private List<Data> getNetworkStatus(long spaceTime)
		throws SigarException
	{
		this.recordNetInterfaceState();
		List<Data> datas = new ArrayList<Data>();
		for (Entry<String, NetInterfaceStat> entry : this.currentNetInterfaceStatMap.entrySet())
		{
			NetInterfaceStat lastStat = this.lastNetInterfaceStatMap.get(entry.getKey());
			if (null != lastStat)
			{
				int receiveSpeed = (int)((entry.getValue().getRxBytes() - lastStat.getRxBytes()) / spaceTime);
				int sendSpeed = (int)((entry.getValue().getTxBytes() - lastStat.getTxBytes()) / spaceTime);
				Data data = new Data();
				data.addAttr("ip", entry.getKey());
				data.addAttr("receiveSpeed", receiveSpeed);
				data.addAttr("sendSpeed", sendSpeed);
				datas.add(data);
			}
		}
		return datas;
	}

}
