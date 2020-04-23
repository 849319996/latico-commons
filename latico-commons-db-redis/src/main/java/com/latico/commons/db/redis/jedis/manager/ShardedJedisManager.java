package com.latico.commons.db.redis.jedis.manager;

import com.latico.commons.db.redis.jedis.JedisConfig;
import com.latico.commons.db.redis.jedis.JedisUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;
import java.util.Set;

/**
 * <PRE>
 * 切片模式，分布式
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-24 20:51
 * @version: 1.0
 */
public class ShardedJedisManager {
    /**
     * 切片连接池
     */
    private ShardedJedisPool shardedJedisPool;
    /**
     * 私有单例对象，需要使用volatile，让其他线程直接可见
     */
    private static volatile ShardedJedisManager instance;

    /**
     * 提供给外界获取单例的方法
     * @return 单例对象
     */
    public static ShardedJedisManager getInstance() {
        //第一层检测，在锁的外面判断是否为空，效率高
        if (instance == null) {
            //开始进入加锁创建对象
            synchronized (ShardedJedisManager.class) {
                //第二层检测，因为第一层检测期间可能其他线程正则创建，但是未创建完成，所以需要在这里进行二次判断
                if (instance == null) {
                    //创建实例
                    instance = new ShardedJedisManager();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造方法，让外界不能创建对象，在这里做初始化逻辑处理
     */
    private ShardedJedisManager() {
        Set<String> socketSet = JedisConfig.getInstance().getHostAndPorts();
        String[] sockets = JedisConfig.getInstance().getHostAndPorts().toArray(new String[socketSet.size()]);
        List<JedisShardInfo> jedisShardInfos = JedisUtils.createJedisShardInfosBySocket(JedisConfig.getInstance().getPassword(), sockets);
        this.shardedJedisPool = JedisUtils.createShardedJedisPool(getJedisPoolConfig(), jedisShardInfos);
    }
    private JedisPoolConfig getJedisPoolConfig() {
        int maxTotal = JedisConfig.getInstance().getMaxTotal();
        int maxIdle = JedisConfig.getInstance().getMaxIdle();
        int minIdle = JedisConfig.getInstance().getMinIdle();
        int maxWaitMillis = JedisConfig.getInstance().getMaxWaitMillis();

        return JedisUtils.createJedisPoolConfig(maxTotal, maxIdle, minIdle, maxWaitMillis, JedisConfig.getInstance().isTestOnBorrow(), JedisConfig.getInstance().isTestOnReturn(), JedisConfig.getInstance().isBlockWhenExhausted());
    }
    /**
     * 切片客户端连接
     */
    public ShardedJedis getShardedJedis() {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        return shardedJedis;
    }

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }
}
