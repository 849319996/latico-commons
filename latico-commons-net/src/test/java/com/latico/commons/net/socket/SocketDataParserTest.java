package com.latico.commons.net.socket;

import org.junit.Test;

public class SocketDataParserTest {

    @Test
    public void parseByEndTag() {
        String data = "服务端<start>处理了:你好!\0<end> \n" +
                "\n" +
                "服务<start>端处理完成\0<end> \n" +
                "\n" +
                "服务<start>端处理完成2\0 \n" +
                "\0 服务端处理<end>了:abc!\n" +
                "\0 \n" +
                "\n" +
                "服务<start>端处理完成\0 \n" +
                "\n" +
                "服务<start>端处理<end>完成2\0 125";


        System.out.println(SocketDataParser.parseByTag(data, "<start>", "<end>"));
    }
}