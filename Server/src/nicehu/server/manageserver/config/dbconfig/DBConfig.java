package nicehu.server.manageserver.config.dbconfig;

public class DBConfig
{
	private String jdbcUrl = null;
	private String userName = null;
	private String password = null;

	public DBConfig()
	{
	}

	public DBConfig(String jdbcUrl, String userName, String password)
	{
		super();
		this.jdbcUrl = jdbcUrl;
		this.userName = userName;
		this.password = password;
	}

	public String getJdbcUrl()
	{
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl)
	{
		this.jdbcUrl = jdbcUrl;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
