package nicehu.nhsdk.candy.cache;

public interface MemCache
{
	int MaxFailCount = 20;
	int MaxTryDoCount = 5;

	/**
	 * execute sync update
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	boolean executeSet(String key, String value);

	/**
	 * execute async update
	 * 
	 * @param key
	 * @param value
	 */
	void executeAsyncSet(String key, String value);

	/**
	 * execute query
	 * 
	 * @param key
	 * @return
	 */
	String executeGet(String key);

	boolean isOpen();
	void close();

	int getFailCount();

	void doFail(String key, String value);

	boolean flushAll();

	long getUnWriteCount();

	String dumpMemCacheStatus();

}
