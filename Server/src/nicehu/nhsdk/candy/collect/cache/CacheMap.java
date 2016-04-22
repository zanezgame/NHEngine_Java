package nicehu.nhsdk.candy.collect.cache;

public interface CacheMap<K, V>
{

	public void put(K key, V value);

	public V get(K key);

	public boolean contains(K key);

	public void remove(K key);
}
