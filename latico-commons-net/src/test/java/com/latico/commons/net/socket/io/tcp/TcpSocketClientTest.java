package com.latico.commons.net.socket.io.tcp;

import com.latico.commons.net.socket.SocketDataParser;
import com.latico.commons.net.socket.bean.StrEndTagResult;
import com.latico.commons.net.socket.io.tcp.client.TcpSocketClient;
import com.latico.commons.net.socket.io.tcp.common.SocketHandler;
import org.junit.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpSocketClientTest {

    @Test
    public void test() {
        TcpSocketClient tcpSocketClient = new TcpSocketClient("127.0.0.1", 7878);
        SocketHandler socketHandler = new SocketHandler(tcpSocketClient.getSocket(), true);
        socketHandler.startAsThread();

        final String endTag = "\0";
        socketHandler.sendData("你好!");

        StringBuilder sb = new StringBuilder();

        //一直从缓存拿数据，拿不到为止
        String str = socketHandler.getAndRemoveCurrentAllReceiveDataString();
        System.out.println(str);
        sb.append(str);


        List<String> responses = new ArrayList<>();

        StrEndTagResult strEndTagResult = SocketDataParser.parseByTag(sb.toString(), null, endTag);
        System.out.println(strEndTagResult);
        responses.addAll(strEndTagResult.getResults());

        System.out.println("第一次打印："+responses);

///////////////////////////////////
        socketHandler.sendData("abc!\n");

        sb = new StringBuilder();

        //一直从缓存拿数据，拿不到为止
        str = socketHandler.getAndRemoveCurrentAllReceiveDataString();
        System.out.println(str);
        sb.append(str);


        strEndTagResult = SocketDataParser.parseByTag(sb.toString(), null, endTag);
        System.out.println(strEndTagResult);
        responses.addAll(strEndTagResult.getResults());

        //发送结束标志
//        socketHandler.sendData("<<exit>>");

        socketHandler.close();
        tcpSocketClient.close();

        System.out.println("最终数据列表:" + responses);
    }

    @Test
    public void test2() {
        TcpSocketClient tcpSocketClient = new TcpSocketClient("127.0.0.1", 7878);

        Socket socket = tcpSocketClient.getSocket();
        SocketHandler socketHandler = new SocketHandler(socket, true);

        TransferBean bean = new TransferBean();
        bean.setName("小明abc");

        System.out.println("发送：" + bean);
        socketHandler.sendObjectData(bean);
        System.out.println("接收:"+socketHandler.getAndRemoveReceiveDataObject());

        //对象不能使用第二次，所以要new一个新对象，原因未明
        bean = new TransferBean();
        bean.setName("小红abc");
        bean.setClose(true);
        socketHandler.sendObjectData(bean);
        System.out.println("发送：" + bean);
        tcpSocketClient.close();
    }

}