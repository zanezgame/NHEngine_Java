package nicehu.nhsdk.candy.data;

import java.util.HashMap;

public class Data
{
	public int seq;
	public int id;
	public int playerId;

	public HashMap<String, String> attrs = new HashMap<>();

	public String getAttr(String key)
	{
		return this.attrs.get(key);
	}

	public <T> void addAttr(T key, T value)
	{
		this.attrs.put(key.toString(), value.toString());
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public HashMap<String, String> getAttrs()
	{
		return attrs;
	}

	public void setAttrs(HashMap<String, String> attrs)
	{
		this.attrs = attrs;
	}

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}
}
