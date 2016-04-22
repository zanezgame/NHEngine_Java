package nicehu.server.gameserver.logic.datasync.data;

public class Change
{
	private int num;
	private int type;

	public Change(int num, int type)
	{
		super();
		this.num = num;
		this.type = type;
	}

	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

}
