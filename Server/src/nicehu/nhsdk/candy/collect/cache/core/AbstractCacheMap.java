package nicehu.nhsdk.candy.collect.cache.core;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

import nicehu.nhsdk.candy.collect.cache.CacheMap;

public abstract class AbstractCacheMap<K, V> implements CacheMap<K, V>
{
	protected final Map<K, Reference<V>> map = new HashMap<K, Reference<V>>();
	protected final ReferenceQueue<V> queue = new ReferenceQueue<V>();

	public void put(K key, V value)
	{
		cleanQueue();
		if (map.containsKey(key))
		{
			throw new IllegalArgumentException("Key: " + key + " already exists in map");
		}
		Reference<V> entry = newReference(key, value, queue);
		map.put(key, entry);

	}

	public V get(K key)
	{
		cleanQueue();
		Reference<V> reference = map.get(key);
		if (reference != null)
		{
			return reference.get();
		}
		return null;

	}

	public boolean contains(K key)
	{
		cleanQueue();
		return map.containsKey(key);
	}

	public void remove(K key)
	{
		map.remove(key);
	}

	protected abstract void cleanQueue();

	protected abstract Reference<V> newReference(K key, V value, ReferenceQueue<V> queue);
}
