package nicehu.nhsdk.candy.timer.core;

import nicehu.nhsdk.candy.thread.NHRunnable;
import nicehu.nhsdk.candy.time.Time;
import nicehu.nhsdk.candy.timer.TimerMgr;

public class FixTaskThread extends NHRunnable
{
	public static long sleepTime = 100 * Time.MillSECOND;

	public FixTaskThread()
	{
		super(sleepTime, ThreadLevel.HIGH);
	}

	@Override
	public void execute()
	{
		TimerMgr.instance.fix();
	}
}