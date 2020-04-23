package com.latico.commons.db.redis.jedis;

import com.latico.commons.common.config.AbstractConfig;
import com.latico.commons.common.util.xml.Dom4jUtils;
import org.dom4j.Element;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-24 22:23
 * @version: 1.0
 */
public class JedisConfig extends AbstractConfig {
    //池中最多可以有多少个jedis实例，默认值是8
    private int maxTotal = 8;
    //池中最多有多少个空闲的jedis实例，默认值是8
    private int maxIdle = 8;
    //池中最小有多少个空闲的jedis实例，默认值是0
    private int minIdle = 0;
    //获取jedis实例的最大等待毫秒数，默认值是-1
    private int maxWaitMillis = -1;
    //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的，默认false。
    private boolean testOnBorrow = false;
    //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的，默认false。
    private boolean testOnReturn = false;
    //连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。
    private boolean blockWhenExhausted = true;
    /**
     * 最大尝试次数
     */
    private int maxAttempts = 3;
    /**
     * 连接超时
     */
    private int connectionTimeout = 15000;
    /**
     * socket超时
     */
    private int soTimeout = 30000;

    /**
     * 哨兵用到，主节点名字
     */
    private String masterName = "master";
    /**
     * 哨兵用到，客户端名称
     */
    private String clientName = null;
    /**
     * 授权密码，假如redis配置了密码的时候用到
     */
    private String password = null;
    /**
     * 哨兵用到，数据库ID
     */
    private int database = 0;

    /**
     * 主机和端口，可以多个，socket字符串
     */
    private Set<String> hostAndPorts = new LinkedHashSet<>();
    /**
     * 私有单例对象，需要使用volatile，让其他线程直接可见
     */
    private static volatile JedisConfig instance;

    /**
     * 提供给外界获取单例的方法
     *
     * @return 单例对象
     */
    public static JedisConfig getInstance() {
        //第一层检测，在锁的外面判断是否为空，效率高
        if (instance == null) {
            //开始进入加锁创建对象
            synchronized (JedisConfig.class) {
                //第二层检测，因为第一层检测期间可能其他线程正则创建，但是未创建完成，所以需要在这里进行二次判断
                if (instance == null) {
                    //创建实例
                    instance = new JedisConfig();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造方法，让外界不能创建对象，在这里做初始化逻辑处理
     */
    private JedisConfig() {
        initOrRefreshConfig();
    }

    @Override
    protected void initOtherConfig() {

    }

    @Override
    protected String getResourcesConfigFilePath() {
        return RESOURCES_CONFIG_FILE_ROOT_DIR + "jedis.xml";
    }

    @Override
    protected String getConfigFilePath() {
        return CONFIG_FILE_ROOT_DIR + "jedis.xml";
    }

    @Override
    protected boolean initConfig(String fileContent) throws Exception {
        Element rootElement = Dom4jUtils.getRootElement(fileContent);
        initJedisPoolConfigEle(rootElement.element("JedisPoolConfig"));
        initHostAndPortsEle(rootElement.element("HostAndPorts"));
        return true;
    }

    private void initHostAndPortsEle(Element hostAndPortsEle) {
        if (hostAndPortsEle == null) {
            return;
        }
        List<String> list = Dom4jUtils.getChildValues(hostAndPortsEle, "HostAndPort");
        if (list != null) {
            for (String socket : list) {
                if (socket != null && !"".equals(socket)) {
                    this.hostAndPorts.add(socket);
                }
            }

        }
    }

    private void initJedisPoolConfigEle(Element jedisPoolConfig) throws Exception {
        if (jedisPoolConfig == null) {
            return;
        }
        Map<String, String> childsNameValueMap = Dom4jUtils.getChildsNameValueMap(jedisPoolConfig);
        writeFieldValueToCurrentInstance(childsNameValueMap);

    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public Set<String> getHostAndPorts() {
        return hostAndPorts;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public String getMasterName() {
        return masterName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JedisConfig{");
        sb.append("maxTotal=").append(maxTotal);
        sb.append(", maxIdle=").append(maxIdle);
        sb.append(", minIdle=").append(minIdle);
        sb.append(", maxWaitMillis=").append(maxWaitMillis);
        sb.append(", testOnBorrow=").append(testOnBorrow);
        sb.append(", testOnReturn=").append(testOnReturn);
        sb.append(", blockWhenExhausted=").append(blockWhenExhausted);
        sb.append(", maxAttempts=").append(maxAttempts);
        sb.append(", connectionTimeout=").append(connectionTimeout);
        sb.append(", soTimeout=").append(soTimeout);
        sb.append(", masterName='").append(masterName).append('\'');
        sb.append(", clientName='").append(clientName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", database=").append(database);
        sb.append(", hostAndPorts=").append(hostAndPorts);
        sb.append('}');
        return sb.toString();
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getDatabase() {
        return database;
    }
}
