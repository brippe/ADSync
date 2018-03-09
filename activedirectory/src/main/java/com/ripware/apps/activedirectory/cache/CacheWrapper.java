package com.ripware.apps.activedirectory.cache;

/**
 * Cache wrapper for ehcache use.
 * @author Brad Rippe 
 *
 */
public interface CacheWrapper<K, V> {

	/**
	 * Places an object in the cache by key/value
	 * @param key	the key of the value
	 * @param value the value to cache
	 */
	void put(K key, V value);

	/**
	 * Retrieves a value by its key
	 * @param key the value's key
	 * @return the value
	 */
	V get(K key);
}

