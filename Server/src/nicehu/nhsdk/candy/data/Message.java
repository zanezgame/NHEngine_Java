package nicehu.nhsdk.candy.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import net.sf.json.JSONObject;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.pb.NHMsgBase.BaseMsg;

public class Message
{
	public int id;
	public int playerId;

	public BaseMsg baseMsg;
	public JSONObject jsonObj;

	public void genBaseMsg(MessageLite messageLite)
	{
		byte[] msgByte = messageLite.toByteArray();
		BaseMsg.Builder builder = BaseMsg.newBuilder();
		builder.setId(id);
		builder.setPlayerId(playerId);
		builder.setMsgData(ByteString.copyFrom(msgByte));
		baseMsg = builder.build();
	}

	@JsonIgnore
	public MessageLite getPb(MessageLite messageLite)
	{
		try
		{
			return messageLite.newBuilderForType().mergeFrom(baseMsg.getMsgData()).build();
		}
		catch (InvalidProtocolBufferException e)
		{
			LogU.error(e);
		}
		return null;
	}

	public Message()
	{

	}

	public Message(int pid)
	{
		this.setId(pid);
	}

	public Message(int id, int playerId, BaseMsg baseMsg)
	{
		super();
		this.id = id;
		this.playerId = playerId;
		this.baseMsg = baseMsg;
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
	public JSONObject getJsonObj()
	{
		return jsonObj;
	}

	public void setJsonObj(JSONObject jsonObj)
	{
		this.jsonObj = jsonObj;
	}

	@JsonIgnore
	public BaseMsg getBaseMsg()
	{
		return baseMsg;
	}

	public void setBaseMsg(BaseMsg baseMsg)
	{
		this.baseMsg = baseMsg;
	}

}
