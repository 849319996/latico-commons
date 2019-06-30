package com.latico.commons.db.redis.jedis.redis.jedis.manager;

import com.latico.commons.db.redis.jedis.JedisUtils;
import com.latico.commons.db.redis.jedis.manager.ShardedJedisManager;
import org.junit.Test;
import redis.clients.jedis.ShardedJedis;

public class ShardedJedisManagerTest {

    @Test
    public void getInstance() {
        ShardedJedis jedis = ShardedJedisManager.getInstance().getShardedJedis();

        jedis.setnx("abc", "123");
        jedis.setnx("abcd", "1233");
        jedis.setnx("abcf", "1235");

        System.out.println(jedis.exists("abc"));
        JedisUtils.close(jedis);
    }
}