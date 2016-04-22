package nicehu.server.gameserver.logic.initinfo.db.transact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.rowset.CachedRowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.db.transact.DBTransact;
import nicehu.nhsdk.candy.db.transact.Transact;
import nicehu.server.gameserver.logic.initinfo.data.struct.GamePlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;

public class T_LoadAllGamePlayer implements Transact
{
	
	private static final Logger logger = LoggerFactory.getLogger(T_LoadAllGamePlayer.class);
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}

	private ConcurrentHashMap<Integer, PlayerNode> players = new ConcurrentHashMap<>();

	public ConcurrentHashMap<Integer, PlayerNode> getPlayers()
	{
		return players;
	}

	public void setPlayers(ConcurrentHashMap<Integer, PlayerNode> players)
	{
		this.players = players;
	}

	@Override
	public void doProc(Connection con, PreparedStatement[] vps, CachedRowSet[] vrs)
		throws SQLException
	{
		int queryIndex = -1;

		ConcurrentHashMap<Integer, PlayerNode> hasPlayerNodes = this.selectPrivateList(++queryIndex, con, vps, vrs);
		setPlayers(hasPlayerNodes);
	}

	// 查询全部
	public ConcurrentHashMap<Integer, PlayerNode> selectPrivateList(int queryIndex, Connection con,
		PreparedStatement[] vps, CachedRowSet[] vrs)
		throws SQLException
	{

		ConcurrentHashMap<Integer, PlayerNode> hashMapRobPlayer = new ConcurrentHashMap<>();

		long totalNum = 0;
		int startIndex = 0;
		int wantNum = 5000;

		while (true)
		{
			String sql = "select player_id,show_id,money from player LIMIT " + startIndex + "," + wantNum;

			vps[queryIndex] = con.prepareStatement(sql);
			DBTransact.closeRowSet(vrs, queryIndex);
			vrs[queryIndex] = DBTransact.query(vps[queryIndex], sql, null);
			if (vrs[queryIndex] != null)
			{
				CachedRowSet rs = vrs[queryIndex];

				int curLoadNum = 0;
				while (rs.next())
				{
					++curLoadNum;
					++totalNum;

					int playerId = rs.getInt("player_id");

					PlayerNode playerNode = new PlayerNode(playerId);
					GamePlayer gamePlayer = new GamePlayer(playerId);
					playerNode.setGamePlayer(gamePlayer);

					playerNode.setPlayerId(playerId);
					gamePlayer.setMoney(rs.getInt("money"));

					hashMapRobPlayer.put(playerNode.getPlayerId(), playerNode);
				}

				logger.debug("===server GamePlayer step=== loading...total={}", totalNum);

				if (curLoadNum == 0)
				{
					logger.debug("===server GamePlayer step=== total={}", totalNum);
					return hashMapRobPlayer;
				}

				startIndex += wantNum;
			}
			else
			{
				logger.debug("===server GamePlayer step=== total={}", totalNum);
				return hashMapRobPlayer;
			}
		}
	}
}
