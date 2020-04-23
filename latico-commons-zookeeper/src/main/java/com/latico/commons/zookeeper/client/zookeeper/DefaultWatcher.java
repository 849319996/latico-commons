package com.latico.commons.zookeeper.client.zookeeper;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2020-01-10 11:07
 * @version: 1.0
 */
public class DefaultWatcher implements Watcher {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWatcher.class);
    private long connectTimeout;

    /**
     * @param connectTimeout 连接超时，单位秒
     */
    public DefaultWatcher(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 为了等待连接完成事件
     */
    private CountDownLatch connectCountDownLatch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        LOG.info("zookeeper回调事件打印: [" + event + "]");
        if (Event.KeeperState.SyncConnected == event.getState()) {
            //如果收到了服务端的响应事件,连接成功
            LOG.info("zookeeper连接完成");
        }
        if (connectCountDownLatch.getCount() >= 1) {
            connectCountDownLatch.countDown();
        }
    }

    /**
     * 等待连接完成
     * @throws InterruptedException
     */
    public void waitToConnected() throws InterruptedException {
        connectCountDownLatch.await(connectTimeout, TimeUnit.SECONDS);
    }
}
