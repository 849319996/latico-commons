package com.latico.commons.zookeeper;

import com.latico.commons.common.util.system.SystemUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import org.junit.Test;

import java.util.Properties;


/**
 * <PRE>
 *  jajg
 *
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-05-23 13:44:00
 * @Version: 1.0
 */
public class ZookeeperUtilsTest {

    @Test
    public void getPropertiesConfigSpringboot() {
        System.out.println(ZookeeperUtils.getPropertiesConfigSpringboot());
    }

    @Test
    public void startServer() {
        Properties properties = ZookeeperUtils.getPropertiesConfigSpringboot();
        boolean status = ZookeeperUtils.createServer(properties);
        System.out.println("启动完成：" + status);

        ThreadUtils.sleepSecond(1000);
    }

    /**
     *
     */
    @Test
    public void adapter(){
        System.out.println(SystemUtils.getAllLocalInetAddressIP());
    }

}