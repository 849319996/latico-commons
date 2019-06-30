package com.latico.commons.common.util.io;

import org.junit.Test;

public class AIOUtilsTest {

    @Test
    public void readByFuture() throws Exception {
        System.out.println(AIOUtils.readToStringByFuture("config/logback.xml", "UTF-8"));
    }

    @Test
    public void readToStringByCompletionHandler() throws Exception {
        System.out.println(AIOUtils.readToStringByCompletionHandler("config/logback.xml", "UTF-8"));
    }

    @Test
    public void writeStringByFuture() throws Exception {
        AIOUtils.writeStringByFuture("./src\\test\\resources\\123.xml", "123","UTF-8");
    }

    @Test
    public void writeStringByCompletionHandler() throws Exception {
        AIOUtils.writeStringByCompletionHandler("./src\\test\\resources\\1234.xml", "123","UTF-8");
    }
}