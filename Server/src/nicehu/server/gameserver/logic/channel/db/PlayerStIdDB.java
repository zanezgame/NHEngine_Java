package nicehu.server.gameserver.logic.channel.db;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nicehu.nhsdk.candy.db.core.DBClient;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.struct.Pair;
import nicehu.nhsdk.candy.util.CloseU;
import nicehu.server.gameserver.core.data.GSD;

public class PlayerStIdDB
{

	private static final Logger logger = LoggerFactory.getLogger(PlayerStIdDB.class);

	public static void update(int playerId, String stId)
	{
		String sql = String.format("replace into player_stid (player_id, st_id) VALUES (%d,'%s')", playerId, stId);
		GSD.dbCluster.getGameDBClient().executeAsyncUpdate(playerId, sql);
	}

	public static Pair<String, Integer> query(int playerId, String stId)
	{
		String sql = null;
		if (playerId != 0)
		{
			sql = String.format("select player_id,st_id from player_stid where player_id = %d", playerId);
		}
		else if (!Empty.is(stId))
		{
			sql = String.format("select player_id,st_id from player_stid where st_id = '%s'", stId);
		}
		else
		{
			return null;
		}
		DBClient dbClient = GSD.dbCluster.getGameDBClient();
		CachedRowSet rs = null;
		try
		{
			rs = dbClient.executeQuery(sql);
			if (rs != null && rs.next())
			{
				int player_id = rs.getInt("player_id");
				String st_id = rs.getString("st_id");
				return new Pair<String, Integer>(st_id, player_id);
			}
		}
		catch (SQLException e)
		{
			logger.error("queryStIdAndPlayerId  failed.{}\n{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		finally
		{
			CloseU.close(rs);
		}
		return null;
	}

}
