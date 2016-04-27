package nicehu.server.authserver.logic.login.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import nicehu.nhsdk.candy.collect.CollectionU;
import nicehu.nhsdk.candy.log.LogU;
import nicehu.nhsdk.candy.object.Empty;
import nicehu.nhsdk.candy.str.ParseU;
import nicehu.nhsdk.candy.time.TimeU;
import nicehu.nhsdk.candy.util.CloseU;
import nicehu.nhsdk.core.data.SD;
import nicehu.server.authserver.core.ASD;
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
			if (rs != null && rs.next())
			{
				int accountId = rs.getInt("account_idx");
				account = rs.getString("account");
				pass = rs.getString("pass");

				List<Integer> areaIds = new ArrayList<Integer>();

				// TODO 封装成工具 CollectionU
				String area_ids_Str = rs.getString("area_ids");
				if (!Empty.is(area_ids_Str))
				{
					String[] aaa = area_ids_Str.split(",");

					for (String a : aaa)
					{
						areaIds.add(Integer.parseInt(a));
					}
				}
				accountData = new AccountData(accountId, account, pass, areaIds);

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

	public static void createAccount(AccountData accountData)
	{
		String sql = String.format(
			"insert into account(account_idx,account, pass, os_type, os_version, device_name,udid ,area_ids,create_time) values(%d,'%s', '%s', '%s', '%s','%s', '%s', '%s', '%s')",
			accountData.getAccountId(),
			accountData.getAccount(),
			accountData.getPass(),
			accountData.getDeviceInfo().getoSType(),
			accountData.getDeviceInfo().getoSVersion(),
			accountData.getDeviceInfo().getDeviceName(),
			accountData.getDeviceInfo().getUdid(),
			CollectionU.listToString(accountData.getAreaIds(), ","),
			TimeU.getStr(System.currentTimeMillis()));
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
