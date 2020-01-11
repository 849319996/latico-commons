package com.latico.commons.zookeeper.client.zookeeper;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.zookeeper.ZookeeperUtils;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * <PRE>
 * zookeeper客户端对象
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-05-23 13:53
 * @Version: 1.0
 */
public class ZookeeperClient {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperClient.class);
    private DefaultWatcher defaultWatcher;

    private ZooKeeper zookeeperClient;
    /**
     * socket连接字符串
     */
    private String[] socketStrs;
    /**
     * session超时时间，毫秒
     */
    private static final int defaultSessionTimeout = 10000;

    /**
     * 使用默认的回调方法，也就是简单打印回调节点
     *
     * @param socketStrs
     */
    public ZookeeperClient(String... socketStrs) {
        this.defaultWatcher = new DefaultWatcher(defaultSessionTimeout);
        this.socketStrs = socketStrs;
        this.zookeeperClient = ZookeeperUtils.createClient(defaultSessionTimeout, defaultWatcher, socketStrs);
        try {
            this.defaultWatcher.waitToConnected();
        } catch (InterruptedException e) {
            LOG.error("", e);
        }
    }

    /**
     * 指定回调类
     *
     * @param sessionTimeout 超时
     * @param watcher        回调类，当客户端订阅监听节点路径或者节点数据，当发生改变的时候就会触发，
     *                       而且每次订阅只会触发一次，后续想继续触发，需要重新订阅
     * @param socketStrs     连接的socket字符串，可以多个，多个就是集群模式
     */
    public ZookeeperClient(int sessionTimeout, Watcher watcher, String... socketStrs) {
        this.socketStrs = socketStrs;
        this.zookeeperClient = ZookeeperUtils.createClient(sessionTimeout, watcher, socketStrs);

    }

    /**
     * @return 客户端对象
     */
    public ZooKeeper getZookeeperClient() {
        return zookeeperClient;
    }

}
