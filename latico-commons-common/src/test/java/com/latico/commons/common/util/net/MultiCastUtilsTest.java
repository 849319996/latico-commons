package com.latico.commons.common.util.net;

import com.latico.commons.common.util.thread.ThreadUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class MultiCastUtilsTest {

    /**
     * 这里利用一个方法，同时启动监听端和发送端，正常来说是要分开启动的
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final String groupIp = "224.10.10.10";
        final int port = 10000;
        String charset = "UTF-8";
        final AtomicBoolean switchStatus = new AtomicBoolean(true);
        final ArrayBlockingQueue<DatagramPacket> receiveQueue = new ArrayBlockingQueue(1000);


//        启动监听端，利用一个线程去启动监听
        Thread reciver = new Thread(){
            @Override
            public void run() {
                try {
                    MultiCastUtils.startReceive(groupIp, port, 1500, switchStatus, receiveQueue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("接收端关闭");
            }
        };

        reciver.start();

//        等接收端启动完成
        ThreadUtils.sleep(2000);

        MulticastSocket sender = MultiCastUtils.createMulticastSocket(10);

        for (int i = 0; i < 3; i++) {
            MultiCastUtils.send(sender, groupIp, port, "你好" + i, charset);
            ThreadUtils.sleep(1000);
        }
        MultiCastUtils.send(sender, groupIp, port, "end", charset);

        while (true) {
            DatagramPacket obj = receiveQueue.poll(1, TimeUnit.SECONDS);
            String str = UdpSocketUtils.readDatagramPacketToString(obj, charset);
            System.out.println("从队列提取：" + str);
            if ("end".equals(str)) {
                System.out.println("提示关闭");
                //        关闭
                switchStatus.set(false);
                break;
            }
        }

        System.out.println("发送端关闭");
    }
}