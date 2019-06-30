package com.latico.commons.db.redis.jedis.redis.jedis.manager;

import com.latico.commons.db.redis.jedis.JedisUtils;
import com.latico.commons.db.redis.jedis.manager.JedisClusterManager;
import org.junit.Test;
import redis.clients.jedis.JedisCluster;

public class JedisClusterManagerTest {

    @Test
    public void getJedisCluster() {
        JedisCluster jedisCluster = JedisClusterManager.getInstance().getJedisCluster();
        System.out.println(jedisCluster.keys("*"));
        JedisUtils.close(jedisCluster);

    }
}