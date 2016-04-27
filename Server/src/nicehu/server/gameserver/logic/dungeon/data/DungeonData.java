package nicehu.server.gameserver.logic.dungeon.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonData implements Serializable
{
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, Dungeon> dungeons = new HashMap<Integer, Dungeon>();

	public boolean updateDungeon(int id, int stars, int score)
	{
		boolean change = false;
		Dungeon dungeon = this.dungeons.get(id);
		if (dungeon == null)
		{
			dungeons.put(id, new Dungeon(id, score, stars));
			change = true;
		}
		else
		{
			if (dungeon.getScore() < score)
			{
				dungeon.setScore(score);
				change = true;
			}
			if (dungeon.getStars() < stars)
			{
				dungeon.setStars(stars);
				change = true;
			}
		}
		return change;
	}

	public void setDungeon(int id, int stars, int score)
	{
		this.dungeons.put(id, new Dungeon(id, score, stars));

	}

	public int genDungeonMaxId()
	{
		int maxId = 0;
		for (int id : dungeons.keySet())
		{
			if (id > maxId)
			{
				maxId = id;
			}
		}
		return maxId;
	}

	public int genDungeonStars()
	{
		int stars = 0;
		for (Map.Entry<Integer, Dungeon> entry : dungeons.entrySet())
		{
			stars += entry.getValue().getStars();
		}
		return stars;
	}

	public Dungeon getDungeon(int dungeonId)
	{
		return dungeons.get(dungeonId);
	}

	public String toDBString()
	{
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, Dungeon> entry : dungeons.entrySet())
		{
			Dungeon dungeon = entry.getValue();
			if (sb.length() != 0)
			{
				sb.append("|");
			}
			sb.append(dungeon.getId()).append("&").append(dungeon.getScore()).append("&").append(dungeon.getStars());
		}
		return sb.toString();
	}

	public void fromDBString(String str)
	{
		List<String> dungeonStrs = Arrays.asList(str.split("\\|"));
		for (String dungeognStr : dungeonStrs)
		{
			String[] datas = dungeognStr.split("&");
			if (datas.length == 3)
			{
				Dungeon dungeon = new Dungeon(datas[0], datas[1], datas[2]);
				this.dungeons.put(dungeon.getId(), dungeon);
			}
		}
	}

	public HashMap<Integer, Dungeon> getDungeons()
	{
		return dungeons;
	}

	public void setDungeons(HashMap<Integer, Dungeon> dungeons)
	{
		this.dungeons = dungeons;
	}

}
