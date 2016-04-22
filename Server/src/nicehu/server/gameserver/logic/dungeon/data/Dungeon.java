package nicehu.server.gameserver.logic.dungeon.data;

import java.io.Serializable;

public class Dungeon implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int score;
	private int stars;

	public Dungeon()
	{

	}

	public Dungeon(String id, String score, String stars)
	{
		super();
		this.id = Integer.parseInt(id);
		this.score = Integer.parseInt(score);
		this.stars = Integer.parseInt(stars);
	}

	public Dungeon(int id, int score, int stars)
	{
		super();
		this.id = id;
		this.score = score;
		this.stars = stars;
	}

	public Dungeon(int id)
	{
		super();
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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

}
