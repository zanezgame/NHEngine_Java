package nicehu.nhsdk.candy.collect.lru;

public class MaxLruMap<K, V> extends LruMap<K, V>
{
	private static final long serialVersionUID = 1L;

	public MaxLruMap()
	{
		super(Integer.MAX_VALUE, 0);
	}

}
