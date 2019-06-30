package com.latico.commons.db.redis.jedis;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <PRE>
 *     注意，假如是使用Jedis对象，那么每次用完要关闭，因为这个对象是相当于数据库连接，
 *     而其他的就相当于数据库连接池，JedisClusterManager的集群JedisCluster对象的使用不需要关闭，
 *     内部自带连接池；
 *
 * Jedis的常量定义可以参考{@link Protocol}
 *
 * jedis客户端操作redis主要三种模式：单台模式、分片模式（ShardedJedis）、
 * 集群模式（BinaryJedisCluster），分片模式用于分布式场景
 *
 * 分片模式也是分布式，数据按照hash算法平均分配到N个主机，
 * 集群模式就是主从，所有主机存储一样的数据，一台挂了另外一台顶上
 *
 * <p>
 * 关于切片池和非切片池的区别，  一般项目基本都使用非切片池；切片池主要用于分布式项目，
 * 可以设置主从Redis库。  
 * 如果需要指明Redis连接第几个库，需要在使用Redis进行数据操作之前使用如下语句：  
 * Jedis resource = jedisPool.getResource();  
 * resource.select(1);  
 * 注意：select(1)表示指定Redis库的第二个库（第一个用0）。  
 * 使用最新Redis jar包可以发现，释放redis连接的方法  jedisPool.returnResource(jedis);  
 * 在Redis3.0之后不再推荐使用，改为 jedis.close();
 *
 说一下redis的分布式，分布式，无疑，肯定不是一台redis服务器。假如说，我们有两台redis服务器，一个6379端口，一个6380端口。那么，我们存储一个数据，他会存在哪个redis服务器上呢？那我们要是取该如何取呢？这是我们需要关心的事情。
 下面这个工具类，解决了你上面的困扰，他会把数据尽可能的平均分配到每个redis服务器上面。然后你获取也不用纠结哪个服务器上获取。
 大体思路和上一篇单机redis差不多，无非是类不一样。
 既然是分布式，肯定是需要你获取一个一个redis连接，然后搞一个List放进去，然后写上你需要的一些hash算法，选取规则什么的，这就得到了一个redisPool
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-22 15:58
 * @Version: 1.0
 */
public class JedisUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JedisUtils.class);

    /**
     * 全量主动填入参数创建连接池配置
     * 配置jedis控制参数，redis.clients.jedis.JedisPoolConfig
     *
     * @param maxTotal           池中最多可以有多少个jedis实例，默认值是8
     * @param maxIdle            池中最多有多少个空闲的jedis实例，默认值是8
     * @param minIdle            池中最小有多少个空闲的jedis实例，默认值是0
     * @param maxWaitMillis      获取jedis实例的最大等待毫秒数，默认值是-1
     * @param testOnBorrow       在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
     * @param testOnReturn       在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
     * @param blockWhenExhausted 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。
     * @return
     */
    public static JedisPoolConfig createJedisPoolConfig(int maxTotal, int maxIdle, int minIdle, long maxWaitMillis, boolean testOnBorrow, boolean testOnReturn, boolean blockWhenExhausted) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(blockWhenExhausted);
        return config;
    }

    /**
     * 使用默认配置创建一个池
     *
     * @return
     */
    public static JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxWaitMillis(5000);
        return config;
    }

    /**
     * 创建非切片连接池，一般用这个
     *
     * @param config
     * @param socket
     * @return
     */
    public static JedisPool createJedisPool(JedisPoolConfig config, String socket, String password, int timeout, int database, String clientName) {
        HostAndPort hostAndPort = convertSocketToHostAndPort(socket);
        JedisPool jedisPool = new JedisPool(config, hostAndPort.getHost(), hostAndPort.getPort(), timeout, password, database, clientName);
        return jedisPool;
    }

    public static JedisPool createJedisPool(String host) {
        JedisPoolConfig config = createJedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, host);
        return jedisPool;
    }

    /**
     * 创建非切片连接池，一般用这个
     *
     * @param host
     * @param port
     * @return
     */
    public static JedisPool createJedisPool(String host, int port) {
        JedisPoolConfig config = createJedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, host, port);
        return jedisPool;
    }


    public static ShardedJedisPool createShardedJedisPool(List<JedisShardInfo> shards) {
        JedisPoolConfig config = createJedisPoolConfig();
        // 构造池
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, shards);
        return shardedJedisPool;
    }

    /**
     * 创建切片池，分布式用这个
     *
     * @param poolConfig 连接池控制参数
     * @param shards     redis分片（N个主机信息），每一个分片在redis.clients.jedis.JedisShardInfo中配置
     * @return
     */
    public static ShardedJedisPool createShardedJedisPool(GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards) {
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, shards);
        return shardedJedisPool;
    }

    /**
     * 创建一个切片信息，分布式中一个主机
     *
     * @param host
     * @param port
     * @param name 该切片的名字
     * @return
     */
    public static JedisShardInfo createJedisShardInfo(String host, int port, String name) {
        JedisShardInfo shardInfo = new JedisShardInfo(host, port, name);
        return shardInfo;
    }

    public static JedisShardInfo createJedisShardInfo(String host, int port) {
        JedisShardInfo shardInfo = new JedisShardInfo(host, port);
        return shardInfo;
    }

    public static JedisShardInfo createJedisShardInfo(String host) {
        JedisShardInfo shardInfo = new JedisShardInfo(host);
        return shardInfo;
    }


    /**
     * 创建多个切片信息，分布式中的主机集合，端口使用默认端口，
     * 如果要指定端口，请使用{@link JedisUtils#createJedisShardInfo(String, int, String)}
     *
     * @param hosts N个主机
     * @return
     */
    public static List<JedisShardInfo> createJedisShardInfos(String password, String... hosts) {
        List<JedisShardInfo> shardInfos = new ArrayList<>();
        for (String host : hosts) {
            JedisShardInfo shardInfo = new JedisShardInfo(host);
            shardInfo.setPassword(password);
            shardInfos.add(shardInfo);
        }
        return shardInfos;
    }

    public static List<JedisShardInfo> createJedisShardInfosBySocket(String password, String... sockets) {
        List<JedisShardInfo> shardInfos = new ArrayList<>();
        for (String socket : sockets) {
            HostAndPort hostAndPort = convertSocketToHostAndPort(socket);
            JedisShardInfo shardInfo = new JedisShardInfo(hostAndPort.getHost(), hostAndPort.getPort(), socket);
            shardInfo.setPassword(password);
            shardInfos.add(shardInfo);
        }
        return shardInfos;
    }

    public static void close(Jedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public static void close(JedisPool jedisPool) {
        try {
            if (jedisPool != null) {
                jedisPool.close();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public static void close(ShardedJedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public static void close(ShardedJedisPool jedisPool) {
        try {
            if (jedisPool != null) {
                jedisPool.close();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * 创建集群模式
     * @param jedisClusterNode
     * @return
     */
    public static JedisCluster createJedisCluster(Set<HostAndPort> jedisClusterNode) {
        JedisPoolConfig jedisPoolConfig = createJedisPoolConfig();
        return new JedisCluster(jedisClusterNode, jedisPoolConfig);
    }

    public static Set<HostAndPort> convertSocketToHostAndPort(String... sockets) {
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        for (String socket : sockets) {
            HostAndPort hostAndPort = convertSocketToHostAndPort(socket);
            hostAndPorts.add(hostAndPort);
        }
        return hostAndPorts;
    }

    public static HostAndPort convertSocketToHostAndPort(String socket) {
        String[] parts = HostAndPort.extractParts(socket);
        return new HostAndPort(parts[0], Integer.parseInt(parts[1]));
    }

    public static JedisCluster createJedisCluster(Set<HostAndPort> hostAndPorts, JedisPoolConfig jedisPoolConfig) {
        return new JedisCluster(hostAndPorts, jedisPoolConfig);
    }

    /**
     * 创建哨兵池
     * @param masterName
     * @param sentinels sockets
     * @param poolConfig
     * @param password 可以为空
     * @return
     */
    public static JedisSentinelPool createJedisSentinelPool(String masterName, Set<String> sentinels,
                             final GenericObjectPoolConfig poolConfig, final String password) {
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, poolConfig, password);
        return jedisSentinelPool;
    }

    public static JedisSentinelPool createJedisSentinelPool(String masterName, Set<String> sentinels,
                             final GenericObjectPoolConfig poolConfig, final int connectionTimeout, final int soTimeout,
                             final String password, final int database, final String clientName) {
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, password, database, clientName);
        return jedisSentinelPool;
    }

    public static void close(JedisCluster jedisCluster) {
        if (jedisCluster != null) {
            jedisCluster.close();
        }
    }
}
