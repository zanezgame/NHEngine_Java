package nicehu.nhsdk.candy.test;


public class DBInsert
{
	public static void insetTestSql()
	{

		for (int i = 0; i < 100000L; i++)
		{
//			String sql = String.format("replace into test (player_id) VALUES (%d)", i);
//			GSD.dbCluster.getGameDBClient().executeAsyncUpdate(i, sql);
		}

	}

}
