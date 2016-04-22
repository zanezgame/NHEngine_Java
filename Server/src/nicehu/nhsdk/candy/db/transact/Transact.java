package nicehu.nhsdk.candy.db.transact;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

public interface Transact {
	public void doProc(Connection con, PreparedStatement[] vps, CachedRowSet[] vrs) throws SQLException;
	public String getName();
}
