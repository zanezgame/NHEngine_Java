package nicehu.nhsdk.candy.data;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.protobuf.MessageLite;

import net.sf.json.JSONObject;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.core.data.SD;
import nicehu.nhsdk.core.type.ServerType;

public class Message
{
	public int id;
	public int playerId;

	public MessageLite protoBuf;
	public JSONObject jsonObj;



	public Message()
	{

	}

	public Message(int pid)
	{
		this.setId(pid);
	}

	public Message(int id, int playerId)
	{
		super();
		this.id = id;
		this.playerId = playerId;
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

	@JsonIgnore
	public MessageLite getProtoBuf()
	{
		return protoBuf;
	}

	public void setProtoBuf(MessageLite protoBuf)
	{
		this.protoBuf = protoBuf;
	}



	@JsonIgnore
	public JSONObject getJsonObj()
	{
		return jsonObj;
	}

	public void setJsonObj(JSONObject jsonObj)
	{
		this.jsonObj = jsonObj;
	}

}
