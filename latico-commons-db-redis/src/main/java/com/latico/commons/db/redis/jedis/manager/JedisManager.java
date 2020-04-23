package com.latico.commons.db.redis.jedis.manager;

import com.latico.commons.db.redis.jedis.JedisConfig;
import com.latico.commons.db.redis.jedis.JedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <PRE>
 * 单机模式
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-24 20:50
 * @version: 1.0
 */
public class JedisManager {

    /**
     * 非切片连接池
     */
    private JedisPool jedisPool;

    /**
     * 私有单例对象，需要使用volatile，让其他线程直接可见
     */
    private static volatile JedisManager instance;

    /**
     * 提供给外界获取单例的方法
     * @return 单例对象
     */
    public static JedisManager getInstance() {
        //第一层检测，在锁的外面判断是否为空，效率高
        if (instance == null) {
            //开始进入加锁创建对象
            synchronized (JedisManager.class) {
                //第二层检测，因为第一层检测期间可能其他线程正则创建，但是未创建完成，所以需要在这里进行二次判断
                if (instance == null) {
                    //创建实例
                    instance = new JedisManager();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造方法，让外界不能创建对象，在这里做初始化逻辑处理
     */
    private JedisManager() {
        String socket = JedisConfig.getInstance().getHostAndPorts().iterator().next();
        this.jedisPool = JedisUtils.createJedisPool(getJedisPoolConfig(), socket, JedisConfig.getInstance().getPassword(), JedisConfig.getInstance().getConnectionTimeout(), JedisConfig.getInstance().getDatabase(), JedisConfig.getInstance().getClientName());
    }
    private JedisPoolConfig getJedisPoolConfig() {
        int maxTotal = JedisConfig.getInstance().getMaxTotal();
        int maxIdle = JedisConfig.getInstance().getMaxIdle();
        int minIdle = JedisConfig.getInstance().getMinIdle();
        int maxWaitMillis = JedisConfig.getInstance().getMaxWaitMillis();

        return JedisUtils.createJedisPoolConfig(maxTotal, maxIdle, minIdle, maxWaitMillis, JedisConfig.getInstance().isTestOnBorrow(), JedisConfig.getInstance().isTestOnReturn(), JedisConfig.getInstance().isBlockWhenExhausted());
    }
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
