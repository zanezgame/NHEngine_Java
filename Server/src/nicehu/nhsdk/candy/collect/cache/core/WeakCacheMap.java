package nicehu.nhsdk.candy.collect.cache.core;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import nicehu.nhsdk.candy.collect.cache.CacheMap;

/**
 * clear when gabage find this ref
 * 
 * @author windw
 *
 * @param <K>
 * @param <V>
 */
public class WeakCacheMap<K, V> extends AbstractCacheMap<K, V> implements CacheMap<K, V>
{

	@Override
	protected synchronized void cleanQueue()
	{
		Entry en = null;
		while ((en = (Entry)queue.poll()) != null)
		{
			K key = en.getKey();
			map.remove(key);
		}
	}

	@Override
	protected Reference<V> newReference(K key, V value, ReferenceQueue<V> vReferenceQueue)
	{
		return new Entry(key, value, vReferenceQueue);
	}

	private class Entry extends WeakReference<V>
	{
		private K key;

		Entry(K key, V referent, ReferenceQueue<? super V> q)
		{
			super(referent, q);
			this.key = key;
		}

		K getKey()
		{
			return key;
		}
	}
}
