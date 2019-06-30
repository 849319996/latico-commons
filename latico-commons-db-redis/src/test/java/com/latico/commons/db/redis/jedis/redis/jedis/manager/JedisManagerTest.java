package com.latico.commons.db.redis.jedis.redis.jedis.manager;

import com.latico.commons.db.redis.jedis.JedisUtils;
import com.latico.commons.db.redis.jedis.manager.JedisManager;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class JedisManagerTest {

    @Test
    public void getJedis() {
        Jedis jedis = JedisManager.getInstance().getJedis();

        jedis.setnx("abc", "123");
        jedis.setnx("abcd", "1233");
        jedis.setnx("abcf", "1235");

        System.out.println(jedis.keys("*"));
        JedisUtils.close(jedis);
    }
}