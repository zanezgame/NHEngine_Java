package nicehu.nhsdk.candy.collect.cache.core;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import nicehu.nhsdk.candy.collect.cache.CacheMap;

/**
 * clear when memory is not enough
 * 
 * @author windw
 *
 * @param <K>
 * @param <V>
 */
public class SoftCacheMap<K, V> extends AbstractCacheMap<K, V> implements CacheMap<K, V>
{
	protected synchronized void cleanQueue()
	{
		SoftEntry en = null;
		while ((en = (SoftEntry)queue.poll()) != null)
		{
			K key = en.getKey();
			map.remove(key);
		}
	}

	protected Reference<V> newReference(K key, V value, ReferenceQueue<V> vReferenceQueue)
	{
		return new SoftEntry(key, value, vReferenceQueue);
	}

	private class SoftEntry extends SoftReference<V>
	{
		private K key;

		SoftEntry(K key, V referent, ReferenceQueue<? super V> q)
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
