package nicehu.server.gameserver.logic.initinfo.db.transact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import nicehu.nhsdk.candy.db.transact.DBTransact;
import nicehu.nhsdk.candy.db.transact.Transact;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.server.gameserver.logic.initinfo.data.struct.CachePlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.GamePlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.InfoPlayer;
import nicehu.server.gameserver.logic.initinfo.data.struct.PlayerNode;
import nicehu.server.gameserver.logic.initinfo.util.PlayerNodeGen;
import nicehu.server.gameserver.logic.item.db.RowItem;

public class T_InsertAndLoadPlayerNode implements Transact
{
	private int playerId;
	private PlayerNode playerNode = null;
	private boolean isNew = false;

	public T_InsertAndLoadPlayerNode(int playerId)
	{
		this.playerId = playerId;
	}

	public PlayerNode getPlayerNode()
	{
		return playerNode;
	}

	public boolean isNew()
	{
		return isNew;
	}

	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}

	public void doProc(Connection con, PreparedStatement[] vps, CachedRowSet[] vrs)
		throws SQLException
	{
		int sqlIndex = -1;

		int count = this.selectPrivateCount(++sqlIndex, con, vps, vrs, playerId);
		if (count >= 1)
		{
			playerNode = this.selectPrivate(++sqlIndex, con, vps, vrs, playerId);
		}
		else if (count == 0)
		{
			this.isNew = true;
			PlayerNode playerNode = PlayerNodeGen.genNewPlayerFromConfig(playerId);
			boolean ret = true;
			if (ret)
			{
				ret = this.insertPrivate(++sqlIndex, con, vps, vrs, playerNode);
			}
			if (ret)
			{
				ret = RowItem.insertPrivateList(++sqlIndex, con, vps, vrs, playerId, playerNode);
			}
			this.playerNode = playerNode;
		}

	}

	public PlayerNode selectPrivate(int queryIndex, Connection con, PreparedStatement[] vps, CachedRowSet[] vrs, int playerId)
		throws SQLException
	{
		String sql = "select * from player where player_id=? LIMIT 1";
		vps[queryIndex] = con.prepareStatement(sql);
		DBTransact.closeRowSet(vrs, queryIndex);
		vrs[queryIndex] = DBTransact.query(vps[queryIndex], sql, new Object[] {playerId});
		if (vrs[queryIndex] != null)
		{
			CachedRowSet rs = vrs[queryIndex];
			if (rs.next())
			{
				PlayerNode playerNode = new PlayerNode(playerId);
				GamePlayer gamePlayer = new GamePlayer(playerId);
				CachePlayer cachePlayer = new CachePlayer(playerId);
				InfoPlayer infoPlayer = new InfoPlayer(playerId);
				playerNode.setGamePlayer(gamePlayer);
				playerNode.setCachePlayer(cachePlayer);
				playerNode.setInfoPlayer(infoPlayer);

				playerNode.setPlayerId(rs.getInt("player_id"));
				gamePlayer.setMoney(rs.getInt("money"));

				long now = System.currentTimeMillis();
				gamePlayer.setLoginTime(now);

				return playerNode;
			}
		}

		return null;
	}

	public boolean insertPrivate(int queryIndex, Connection con, PreparedStatement[] vps, CachedRowSet[] vrs, PlayerNode playerNode)
		throws SQLException
	{
		String createTimeStr = TimeU.getStr(System.currentTimeMillis());
		GamePlayer gamePlayer = playerNode.getGamePlayer();
		String sql = String.format("insert into player (player_id,money,create_time,login_time)" + "VALUES (?,?,'%s','%s')", createTimeStr, createTimeStr);
		vps[queryIndex] = con.prepareStatement(sql);

		boolean ret = DBTransact.update(vps[queryIndex], sql, new Object[] {playerNode.getPlayerId(),  gamePlayer.getMoney()});

		return ret;
	}

	// 查询个数
	public int selectPrivateCount(int queryIndex, Connection con, PreparedStatement[] vps, CachedRowSet[] vrs, int playerId)
		throws SQLException
	{
		String sql = "select count(*) from player where player_id=? LIMIT 1";
		vps[queryIndex] = con.prepareStatement(sql);
		DBTransact.closeRowSet(vrs, queryIndex);
		vrs[queryIndex] = DBTransact.query(vps[queryIndex], sql, new Object[] {playerId});
		if (vrs[queryIndex] != null)
		{
			CachedRowSet rs = vrs[queryIndex];
			while (rs.next())
			{
				int count = rs.getInt(1);
				return count;
			}
		}

		return -1;
	}

}
