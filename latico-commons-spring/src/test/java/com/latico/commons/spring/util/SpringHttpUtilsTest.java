package com.latico.commons.spring.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SpringHttpUtilsTest {

    /**
     * 
     */
    @Test
    public void test(){
        // System.out.println(SpringHttpUtils.doGet("https://www.baodi.com/"));
        System.out.println(SpringHttpUtils.doGet("http://172.168.10.100:8081/nexus/index.html#welcome"));
    }

    /**
     *
     */
    @Test
    public void test2(){
        Map<String, String> headers = new HashMap<>();
        List<String> cookies = new ArrayList<>();
        String jsonBody = null;
        System.out.println(SpringHttpUtils.doPostJson(headers, cookies, "https://www.baidu.com", jsonBody));
    }
}