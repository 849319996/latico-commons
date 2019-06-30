package com.latico.commons.db.redis.jedis.redis.jedis.manager;

import com.latico.commons.db.redis.jedis.JedisUtils;
import com.latico.commons.db.redis.jedis.manager.JedisSentinelManager;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class JedisSentinelManagerTest {

    @Test
    public void getJedis() {
        Jedis jedis = JedisSentinelManager.getInstance().getJedis();

        jedis.setnx("abc", "123");
        jedis.setnx("abcd", "1233");
        jedis.setnx("abcf", "1235");

        System.out.println(jedis.keys("*"));
        JedisUtils.close(jedis);
    }
}