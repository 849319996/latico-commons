package com.latico.commons.netty.client.impl.str;

import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.netty.client.NettyClient;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StringNettyClientImplTest {

    @Test
    public void  init() {
        NettyClient<String> nettyClient = new StringNettyClientImpl();
        boolean init = nettyClient.init("44.44.44.44", 23, 1000);
        // boolean init = nettyClient.init("127.0.0.1", 7878, 1000);
        System.out.println(init);
        ThreadUtils.sleepSecond(1);
        List<String> datas = nettyClient.getAllReceivedData(5000);
        String data = StringUtils.join(datas);
        System.out.println("第1次打印:\r\n" + data);
        if (data.contains("Username")) {
            System.out.println(nettyClient.sendData("admin\r\n"));
        }
        ThreadUtils.sleepSecond(2);

        datas = nettyClient.getAllReceivedData(5000);
        data = StringUtils.join(datas);
        System.out.println("第2次打印:\r\n" + data);
        if (data.contains("Password")) {
            System.out.println(nettyClient.sendData("admin\r\n"));
        }

        datas = nettyClient.getAllReceivedData(5000);
        data = StringUtils.join(datas);
        System.out.println("第3次打印:\r\n" + data);

        nettyClient.close();
    }
}