package com.latico.commons.cache.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.UserManagedCache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;
import org.junit.Test;

public class EhcacheUtilsTest {
    @Test
    public void testCacheManager() {
        //构建一个缓存管理器，创建一个默认的缓存 "preConfigured"
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",         //缓存别名
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.heap(100))         //设置缓存堆容纳元素个数
                                .build())
                .build(true);               //创建之后立即初始化

//从缓存管理器中获取预定的缓存
        Cache<Long, String> preConfigured
                = cacheManager.getCache("preConfigured", Long.class, String.class);

//直接从缓存管理器创建一个新的缓存
        Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                        ResourcePoolsBuilder.heap(100)).build());

//向缓存里添加缓存键值
        myCache.put(1L, "Hello World!");
//从指定缓存里获取键值
        String value = myCache.get(1L);

        cacheManager.close();
    }

    @Test
    public void testUserManagedCache() {
        UserManagedCache<Integer, String> userManagedCache =
                UserManagedCacheBuilder.newUserManagedCacheBuilder(Integer.class, String.class)
                        .build(false);
        userManagedCache.init();

        for (int i = 0; i <= 20; i++) {
            //写
            userManagedCache.put(i, "#" + i);
            //读
            String value = userManagedCache.get(i);
            System.out.println("get at " + i + ":" + value);
        }

        userManagedCache.close();
    }

    @Test
    public void name(){
        CacheManager cacheManager = EhcacheUtils.createCacheManager();
        Cache<String, String> cache = EhcacheUtils.createCache(cacheManager, "cacheName", 100, String.class, String.class);

        cache.put("abc", "123");
        cache.put("abc2", "1234");
        cache.put("abc3", "1235");

        System.out.println(cache.get("abc"));
        System.out.println(cache.get("abc2"));
        System.out.println(cache.get("abc3"));

        EhcacheUtils.close(cacheManager);

//        关闭后，就失败了
        System.out.println(cache.get("abc"));
        System.out.println(cache.get("abc2"));
        System.out.println(cache.get("abc3"));
    }

}