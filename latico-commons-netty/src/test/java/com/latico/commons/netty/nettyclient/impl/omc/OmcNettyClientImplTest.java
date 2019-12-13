package com.latico.commons.netty.nettyclient.impl.omc;

import com.latico.commons.common.util.math.NumberUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.netty.nettyclient.NettyClient;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OmcNettyClientImplTest {
    /**
     *
     */
    @Test
    public void test2() {
        NettyClient<OmcProtocol> nettyClient = new OmcNettyClientImpl();
        try {
            boolean init = nettyClient.init("127.0.0.1", 7878, 1000);
            System.out.println("初始化:" + init);

            OmcProtocol loginOmc = new OmcProtocol();

            byte msgType = OmcProtocol.msgType_reqLoginAlarm;
            loginOmc.setMsgType(msgType);
            int timeStamp = NumberUtils.toInt(System.currentTimeMillis()/1000);
            loginOmc.setTimeStamp(timeStamp);
            String msgBody = "reqLoginAlarm;user=liumx;key=1234;type=msg>>>";
            byte[] bytes = msgBody.getBytes(OmcProtocol.charset);
            short length = NumberUtils.toShort(bytes.length + "");
            loginOmc.setLenOfBody(length);
            loginOmc.setMsgBody(bytes);

            System.out.println("发送登陆信息：" + nettyClient.sendMsg(loginOmc));

            List<OmcProtocol> oneReceivedDatas = nettyClient.getAllReceivedData(15000, 2000);
            System.out.println("接收到信息：" + oneReceivedDatas);

            msgBody = "reqLoginAlarm;你好啊;type=msg>>>";
            bytes = msgBody.getBytes(OmcProtocol.charset);
            length = NumberUtils.toShort(bytes.length + "");
            loginOmc = new OmcProtocol();
            timeStamp = NumberUtils.toInt(System.currentTimeMillis()/1000);
            loginOmc.setTimeStamp(timeStamp);
            loginOmc.setMsgType(OmcProtocol.msgType_ackHeartBeat);
            loginOmc.setLenOfBody(length);
            loginOmc.setMsgBody(bytes);
            System.out.println("继续发送信息：" + nettyClient.sendMsg(loginOmc));
            while (true) {
                OmcProtocol oneReceivedData = nettyClient.getOneReceivedData(5000);
                if (oneReceivedData == null) {
                    System.out.println("为空，等待下一轮");
                    ThreadUtils.sleepSecond(1);
                    continue;
                }
                System.out.println("接收到信息：" + oneReceivedData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            nettyClient.close();
        }

    }
}