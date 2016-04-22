package nicehu.server.authserver.logic.login.db;

import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.nhsdk.candy.util.CloseU;
import nicehu.nhsdk.core.data.SD;
import nicehu.server.authserver.core.data.ASD;
import nicehu.server.authserver.logic.login.data.AccountData;

public class LoginDB
{
	public static void updateLoginTime(AccountData accountData)
	{
		String sql = String.format("update account set  login_time = '%s' where account_idx = %d ", TimeU.getStr(System.currentTimeMillis()), accountData.getAccountId());
		SD.dbCluster.accountDBClient.executeAsyncUpdate(sql);
	}

	public static void updateLastAreaId(int accountId, int areaId)
	{
		String sql = String.format("update account set lastarea_id= %d where account_idx=%d", areaId, accountId);
		SD.dbCluster.getAccountDBClient().executeAsyncUpdate(accountId, sql);
	}

	public static AccountData selectAccount(String account, String pass)
	{
		if (Empty.is(account))
		{
			return null;
		}
		String sql = "select * from account where account = '%s' ";
		if (Empty.is(pass))
		{
			sql = String.format(sql, account);
		}
		else
		{
			sql += " and pass = '%s' ";
			sql = String.format(sql, account, pass);
		}

		CachedRowSet rs = null;
		AccountData accountData = null;
		try
		{
			rs = SD.dbCluster.accountDBClient.executeQuery(sql);
			if (rs != null)
			{
				if (rs.next())
				{
					int accountId = rs.getInt("account_idx");
					account = rs.getString("account");
					pass = rs.getString("pass");
					accountData = new AccountData(accountId, account, pass);
				}
			}
		}
		catch (SQLException e)
		{
			LogU.error(e);
		}
		finally
		{
			CloseU.close(rs);
		}
		return accountData;
	}

	public static void updateAccountArea(int accountId, int areaId)
	{
		String sql = String.format("replace into account_area (account_idx,area_id) values (%d, %d)", accountId, areaId);
		SD.dbCluster.getAccountDBClient().executeAsyncUpdate(accountId, sql);
	}

	public static void createAccount(AccountData accountData)
	{
		String sql =
			String.format("insert into account(account_idx,account, pass, os_type, os_version, device_name,udid ,create_time) values(%d,'%s', '%s', '%s', '%s','%s', '%s', '%s')",
				accountData.getAccountId(),
				accountData.getAccount(),
				accountData.getPass(),
				accountData.getDeviceInfo().getoSType(),
				accountData.getDeviceInfo().getoSVersion(),
				accountData.getDeviceInfo().getDeviceName(),
				accountData.getDeviceInfo().getUdid(),
				TimeU.getStr(System.currentTimeMillis()));
		// SD.dbCluster.getAccountDBClient().executeUpdate(sql);
		try
		{
			int accountId = (int)SD.dbCluster.getAccountDBClient().executeInsertAndReturnIncrementId(sql);
			accountData.setAccountId(accountId);
		}
		catch (SQLException e)
		{
			LogU.error(e);
		}
	}

}
