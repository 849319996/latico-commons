package com.latico.commons.db.redis.jedis.manager;

import com.latico.commons.db.redis.jedis.JedisConfig;
import com.latico.commons.db.redis.jedis.JedisUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * <PRE>
 * 集群模式，单例管理
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-24 20:52
 * @Version: 1.0
 */
public class JedisClusterManager {
    /**
     * 创建后，每次使用后不需要关闭，真正不用的时候才要关闭
     */
    private JedisCluster jedisCluster;

    /**
     * 私有单例对象，需要使用volatile，让其他线程直接可见
     */
    private static volatile JedisClusterManager instance;

    /**
     * 提供给外界获取单例的方法
     * @return 单例对象
     */
    public static JedisClusterManager getInstance() {
        //第一层检测，在锁的外面判断是否为空，效率高
        if (instance == null) {
            //开始进入加锁创建对象
            synchronized (JedisClusterManager.class) {
                //第二层检测，因为第一层检测期间可能其他线程正则创建，但是未创建完成，所以需要在这里进行二次判断
                if (instance == null) {
                    //创建实例
                    instance = new JedisClusterManager();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造方法，让外界不能创建对象，在这里做初始化逻辑处理
     */
    private JedisClusterManager() {
        Set<String> socketSet = JedisConfig.getInstance().getHostAndPorts();
        String[] sockets = JedisConfig.getInstance().getHostAndPorts().toArray(new String[socketSet.size()]);
        Set<HostAndPort> hostAndPorts = JedisUtils.convertSocketToHostAndPort(sockets);

        this.jedisCluster = new JedisCluster(hostAndPorts, JedisConfig.getInstance().getConnectionTimeout(), JedisConfig.getInstance().getSoTimeout(), 3, JedisConfig.getInstance().getPassword(), JedisConfig.getInstance().getClientName(), getJedisPoolConfig());
    }

    private JedisPoolConfig getJedisPoolConfig() {
        int maxTotal = JedisConfig.getInstance().getMaxTotal();
        int maxIdle = JedisConfig.getInstance().getMaxIdle();
        int minIdle = JedisConfig.getInstance().getMinIdle();
        int maxWaitMillis = JedisConfig.getInstance().getMaxWaitMillis();

        return JedisUtils.createJedisPoolConfig(maxTotal, maxIdle, minIdle, maxWaitMillis, JedisConfig.getInstance().isTestOnBorrow(), JedisConfig.getInstance().isTestOnReturn(), JedisConfig.getInstance().isBlockWhenExhausted());
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

}
