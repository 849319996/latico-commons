package com.latico.commons.cache.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-02-06 23:16
 * @Version: 1.0
 */
public class EhcacheUtils {

    /**
     * 创建一个缓存管理器
     * @return
     */
    public static CacheManager createCacheManager() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        return cacheManager;
    }

    /**
     * 通过缓存管理器创建一个缓存
     * @param cacheManager
     * @param cacheName
     * @param heapSize
     * @param keyClass
     * @param valueClass
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Cache<K, V> createCache(CacheManager cacheManager, String cacheName, long heapSize, Class<K> keyClass, Class<V> valueClass) {
        Cache<K, V> cache = cacheManager.createCache(cacheName,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClass, valueClass,
                        ResourcePoolsBuilder.heap(heapSize)).build());
        return cache;
    }

    /**
     * 关闭缓存管理器
     * @param cacheManager
     */
    public static void close(CacheManager cacheManager) {
        if (cacheManager != null) {
            cacheManager.close();
        }
    }
}
