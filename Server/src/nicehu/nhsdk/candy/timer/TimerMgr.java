package nicehu.nhsdk.candy.timer;

import java.util.ArrayList;
import java.util.List;

import nicehu.nhsdk.candy.thread.ThreadU;
import nicehu.nhsdk.candy.time.TimeUE;
import nicehu.nhsdk.candy.timer.core.CycleTaskThread;
import nicehu.nhsdk.candy.timer.core.FixTaskThread;

public class TimerMgr
{
	public static TimerMgr instance = new TimerMgr();

	private List<TimerTask> fixTasks = new ArrayList<>();
	private List<TimerTask> cycleTasks = new ArrayList<>();

	public void init()
	{
		Thread thread = new Thread(new FixTaskThread(), ThreadU.genName("Wind_FixTaskThread"));
		thread.start();
		thread = new Thread(new CycleTaskThread(), ThreadU.genName("Wind_CycleTaskThread"));
		thread.start();
	}

	public void fix()
	{
		for (TimerTask task : fixTasks)
		{
			if (TimeUE.isNeedRefresh(task.getRefreshTime(),task.getLastTime()))
			{
				task.setLastTime(System.currentTimeMillis());
				task.handle();
			}
		}
	}

	public void cycle()
	{
		for (TimerTask task : cycleTasks)
		{
			if (TimeUE.exceed(task.getLastTime(), task.getSleepTime()))
			{
				task.setLastTime(System.currentTimeMillis());
				task.handle();
			}
		}
	}

	public void addFixTask(TimerTask task)
	{
		this.fixTasks.add(task);
	}

	public void addCycleTask(TimerTask task)
	{
		this.cycleTasks.add(task);
	}

}
