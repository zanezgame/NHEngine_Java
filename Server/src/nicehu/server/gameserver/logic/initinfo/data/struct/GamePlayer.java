package nicehu.server.gameserver.logic.initinfo.data.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nicehu.nhsdk.candy.object.Empty;

public class GamePlayer
{
	private int playerId;
	private String name;
	private int money;
	private int diamond;
	private int dungeonMaxId;
	private int dungeonStars;
	private long loginTime = 0; // 本次登录时间

	@JsonIgnore
	public String getFixName()
	{
		if (Empty.is(name))
		{
			return "$" + playerId;
		}
		return name;
	}

	public boolean testAddMoney(int change)
	{
		if (this.money + change < 0)
		{
			return false;
		}
		return true;
	}

	public boolean addMoney(int change)
	{
		if (testAddMoney(change))
		{
			this.money += change;
			return true;
		}
		return false;
	}

	public GamePlayer()
	{
	}

	public GamePlayer(int playerId)
	{
		super();
		this.playerId = playerId;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public int getMoney()
	{
		return money;
	}

	public void setMoney(int money)
	{
		this.money = money;
	}

	public long getLoginTime()
	{
		return loginTime;
	}

	public void setLoginTime(long loginTime)
	{
		this.loginTime = loginTime;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getDungeonMaxId()
	{
		return dungeonMaxId;
	}

	public void setDungeonMaxId(int dungeonMaxId)
	{
		this.dungeonMaxId = dungeonMaxId;
	}

	public int getDungeonStars()
	{
		return dungeonStars;
	}

	public void setDungeonStars(int dungeonStars)
	{
		this.dungeonStars = dungeonStars;
	}

	public int getDiamond()
	{
		return diamond;
	}

	public void setDiamond(int diamond)
	{
		this.diamond = diamond;
	}

}
