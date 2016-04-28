package nicehu.nhsdk.core;

/**
 * 
 * @author cherish
 * @Description:
 *
 */
public abstract class NHBehaviour
{
	public boolean init()
	{
		return true;
	}

	public boolean afterInit()
	{
		return true;
	}

	public boolean execute()
	{
		return true;
	}

	public boolean beforeShut()
	{
		return true;
	}

	public boolean shut()
	{
		return true;
	}

}
