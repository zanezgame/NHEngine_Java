package nicehu.server.gameserver.logic.datasync.data;

import java.util.ArrayList;

public class SData
{
	private int id;
	private int base;
	private int score;
	private int stars;
	private ArrayList<Change> changes = new ArrayList<Change>();

	public SData(int id, int score, int stars)
	{
		super();
		this.id = id;
		this.score = score;
		this.stars = stars;
	}

	public SData(int id, int base)
	{
		super();
		this.id = id;
		this.base = base;
	}

	public SData()
	{
		super();
	}

	public void addChange(int num, int type)
	{
		this.changes.add(new Change(num, type));
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getBase()
	{
		return base;
	}

	public void setBase(int base)
	{
		this.base = base;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public int getStars()
	{
		return stars;
	}

	public void setStars(int stars)
	{
		this.stars = stars;
	}

	public ArrayList<Change> getChanges()
	{
		return changes;
	}

	public void setChanges(ArrayList<Change> changes)
	{
		this.changes = changes;
	}

}
