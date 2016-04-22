package nicehu.nhsdk.core.datatransmitter.data;


public class ClientNode
{
	private int playerId;

	public ClientNode(int playerId)
	{
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

}
