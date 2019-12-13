package com.latico.commons.netty.nettyserver.impl.omc;

import com.latico.commons.common.util.math.NumberUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.netty.nettyclient.impl.omc.OmcProtocol;
import com.latico.commons.netty.nettyserver.NettyServer;
import com.latico.commons.netty.nettyserver.bean.ReceiveMsg;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class OmcNettyServerImplTest {
    /**
     *
     */
    @Test
    public void test() throws UnsupportedEncodingException {
        NettyServer<OmcProtocol> nettyServer = new OmcNettyServerImpl();
        System.out.println("初始化:" + nettyServer.init(7878, 1000));

        System.out.println("启动:" + nettyServer.startNettyServer());

        int count = 0;
        while (true) {
            ReceiveMsg<OmcProtocol> oneReceivedData = nettyServer.getOneReceivedData(5000);
            if (oneReceivedData == null) {

                System.out.println("为空，等待下一轮");
                ThreadUtils.sleepSecond(1);
                continue;
            }
            System.out.println("打印数据:" + oneReceivedData);

            OmcProtocol msg = oneReceivedData.getMsg();
            String data = new String(msg.getMsgBody(), OmcProtocol.charset);
            String data2;
            msg = new OmcProtocol();
            data2 = ++count + "回复:" + data;
            byte[] bytes = data2.getBytes(OmcProtocol.charset);
            short length = NumberUtils.toShort(bytes.length + "");
            msg.setMsgType(OmcProtocol.msgType_reqLoginAlarm);
            msg.setTimeStamp(NumberUtils.toInt(System.currentTimeMillis()/1000));
            msg.setLenOfBody(length);
            msg.setMsgBody(bytes);
            nettyServer.sendMsgToAllClient(msg);

            msg = new OmcProtocol();
            data2 = ++count + "回复:" + data;
            bytes = data2.getBytes(OmcProtocol.charset);
            length = NumberUtils.toShort(bytes.length + "");
            msg.setMsgType(OmcProtocol.msgType_reqLoginAlarm);
            msg.setTimeStamp(NumberUtils.toInt(System.currentTimeMillis()/1000));
            msg.setLenOfBody(length);
            msg.setMsgBody(bytes);
            nettyServer.sendMsgToAllClient(msg);

        }
    }
}