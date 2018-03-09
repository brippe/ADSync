package com.ripware.apps.activedirectory.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * Ehcache Wrapper used for caching objects
 * @author Brad Rippe
 * @see net.sf.ehcache.CacheManager
 *
 */
public class EhcacheWrapper<K, V> implements CacheWrapper<K, V> {

	private final String cacheName;
    private final CacheManager cacheManager;

    /**
     * Creates a ehcache by name and cache manager
     * @param cacheName	the name of the cache to create
     * @param cacheManager	the cachemanager
     */
    public EhcacheWrapper(final String cacheName, final CacheManager cacheManager) {
        this.cacheName = cacheName;
        this.cacheManager = cacheManager;
    }

    /**
     * @see CacheWrapper#put(K, V)
     */
    public void put(final K key, final V value) {
        getCache().put(new Element(key, value));
    }

    /**
     * @see CacheWrapper#get(K)
     */
    @SuppressWarnings("unchecked")
	public V get(final K key) {
        Element element = getCache().get(key);
        if (element != null) {
            return (V) element.getObjectValue();
        }
        return null;
    }

    /**
     * Retrieves the cache manager
     * @return the cache manager
     */
    public Ehcache getCache() {
        return cacheManager.getEhcache(cacheName);
    }

    /**
     * Returns the number of elements in the cache
     * @return the number of elements in the cache
     */
    public int size() {
    	return cacheManager.getEhcache(cacheName).getSize();
    }
}
