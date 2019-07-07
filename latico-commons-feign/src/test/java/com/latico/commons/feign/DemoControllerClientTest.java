package com.latico.commons.feign;


import org.junit.Test;

/**
 * <PRE>
 *  DemoControllerClient的测试用例,调用的是springboot骨架latico-springboot-mix
 *  
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 10:54:20
 * @Version: 1.0
 */
public class DemoControllerClientTest {

    @Test
    public void serverTime() {

        DemoControllerClient serviceClient = FeignUtils.createProxyByJAXRSContractJson("http://127.0.0.1:8080/", DemoControllerClient.class);
        System.out.println(serviceClient.serverTime());
        System.out.println(serviceClient.serverTimeStr());

        serviceClient = FeignUtils.createProxyByJAXRSContract("http://127.0.0.1:8080/", DemoControllerClient.class);
        System.out.println(serviceClient.serverTimeStr());
    }
    @Test
    public void testRequestParam() {
        DemoControllerClient serviceClient = FeignUtils.createProxyByJAXRSContractJson("http://127.0.0.1:8080", DemoControllerClient.class);
        System.out.println(serviceClient.testRequestParam("abc"));
    }

    @Test
    public void testPathVariable() {
        DemoControllerClient serviceClient = FeignUtils.createProxyByJAXRSContractJson("http://127.0.0.1:8080", DemoControllerClient.class);
        System.out.println(serviceClient.testPathVariable("路径参数"));
    }

    @Test
    public void testMultiPathVariable() {
        DemoControllerClient serviceClient = FeignUtils.createProxyByJAXRSContractJson("http://127.0.0.1:8080", DemoControllerClient.class);
        System.out.println(serviceClient.testMultiPathVariable("路径参数key", "路径参数value"));
    }
}