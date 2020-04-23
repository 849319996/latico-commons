package com.latico.commons.feign;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-03-05 17:10
 * @version: 1.0
 */
public class FeignUtils {

    /**
     * 使用默认的Contract，
     方法上面的注解格式使用：
     @RequestLine("GET /sys/menu/list")

     * @param serverUrl http服务器URL
     * @param clazz 要被代理的类
     * @param <T>
     * @return
     */
    public static <T> T createProxyDefault(String serverUrl, Class<T> clazz) {
//        建造者模式创建对象
        return Feign.builder()
                .options(new Request.Options(10000, 60000))
                .retryer(new Retryer.Default(3000, 5000, 3))
                .target(clazz, serverUrl);
    }

    /**
     * 使用默认的Contract，JSON传输格式
     方法上面的注解格式使用：
     @RequestLine("GET /sys/menu/list")

      * @param serverUrl http服务器URL
     * @param clazz 要被代理的类
     * @param <T>
     * @return
     */
    public static <T> T createProxyByJson(String serverUrl, Class<T> clazz) {
//        建造者模式创建对象
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(10000, 60000))
                .retryer(new Retryer.Default(3000, 5000, 3))
                .target(clazz, serverUrl);
    }

    /**
     * 使用JAXRSContract，
     方法上面的注解格式：
     @POST
     @Path("/serverTime")

     * @param serverUrl http服务器URL
     * @param clazz 要被代理的类
     * @param <T>
     * @return
     */
    public static <T> T createProxyByJAXRSContract(String serverUrl, Class<T> clazz) {
//        建造者模式创建对象
        return Feign.builder()
                .contract(new JAXRSContract())
                .options(new Request.Options(10000, 60000))
                .retryer(new Retryer.Default(3000, 5000, 3))
                .target(clazz, serverUrl);
    }

    /**
     * 使用JAXRSContract，
     方法上面的注解格式：
     @POST
     @Path("/serverTime")
     * @param serverUrl http服务器URL
     * @param clazz 要被代理的类
     * @param connectTimeoutMillis 连接的时候超时，毫秒
     * @param readTimeoutMillis 读取超时，毫秒
     * @param periodInterval 重试的时候间隔时间，毫秒
     * @param maxAttempts 最大尝试次数
     * @param <T>
     * @return
     */
    public static <T> T createProxyByJAXRSContract(String serverUrl, Class<T> clazz, int connectTimeoutMillis, int readTimeoutMillis, long periodInterval, int maxAttempts) {
//        建造者模式创建对象
        return Feign.builder()
                .contract(new JAXRSContract())
                .options(new Request.Options(connectTimeoutMillis, readTimeoutMillis))
                .retryer(new Retryer.Default(periodInterval, 5000, maxAttempts))
                .target(clazz, serverUrl);
    }

    /**
     * 使用JAXRSContract，JSON传输格式
     方法上面的注解格式：
     @POST
     @Path("/serverTime")
     @Produces({MediaType.APPLICATION_JSON})
     @Consumes({MediaType.APPLICATION_JSON})

      * @param serverUrl http服务器URL
     * @param clazz 要被代理的类
     * @param <T>
     * @return
     */
    public static <T> T createProxyByJAXRSContractJson(String serverUrl, Class<T> clazz) {
//        建造者模式创建对象
        return Feign.builder()
                .contract(new JAXRSContract())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(10000, 60000))
                .retryer(new Retryer.Default(3000, 5000, 3))
                .target(clazz, serverUrl);
    }

    /**
     * 使用JAXRSContract，JSON传输格式
     方法上面的注解格式：
     @POST
     @Path("/serverTime")
     @Produces({MediaType.APPLICATION_JSON})
     @Consumes({MediaType.APPLICATION_JSON})
      * @param serverUrl http服务器URL
     * @param clazz 要被代理的类
     * @param connectTimeoutMillis 连接的时候超时，毫秒
     * @param readTimeoutMillis 读取超时，毫秒
     * @param periodInterval 重试的时候间隔时间，毫秒
     * @param maxAttempts 最大尝试次数
     * @param <T>
     * @return
     */
    public static <T> T createProxyByJAXRSContractJson(String serverUrl, Class<T> clazz, int connectTimeoutMillis, int readTimeoutMillis, long periodInterval, int maxAttempts) {
//        建造者模式创建对象
        return Feign.builder()
                .contract(new JAXRSContract())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(connectTimeoutMillis, readTimeoutMillis))
                .retryer(new Retryer.Default(periodInterval, 5000, maxAttempts))
                .target(clazz, serverUrl);
    }
}
