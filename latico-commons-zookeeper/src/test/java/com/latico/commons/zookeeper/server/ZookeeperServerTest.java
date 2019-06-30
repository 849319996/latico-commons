package com.latico.commons.zookeeper.server;

import com.latico.commons.common.util.thread.ThreadUtils;
import org.junit.Test;

public class ZookeeperServerTest {

    @Test
    public void getInstance() {
        boolean status = ZookeeperServer.getInstance().getStatus();
        System.out.println("启动完成：" + status);

        ThreadUtils.sleepSecond(10000);
    }
}