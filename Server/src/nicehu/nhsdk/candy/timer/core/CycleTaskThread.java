package nicehu.nhsdk.candy.timer.core;

import nicehu.nhsdk.candy.thread.NHRunnable;
import nicehu.nhsdk.candy.time.Time;
import nicehu.nhsdk.candy.timer.TimerMgr;

public class CycleTaskThread extends NHRunnable
{
	public static long sleepTime = 100 * Time.MillSECOND;

	public CycleTaskThread()
	{
		super(sleepTime, ThreadLevel.HIGH);
	}

	@Override
	public void execute()
	{
		TimerMgr.instance.cycle();
	}
}