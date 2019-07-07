package com.latico.commons.hessian.common;

import com.caucho.hessian.client.HessianProxyFactory;

import java.net.MalformedURLException;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-09 22:11
 * @Version: 1.0
 */
public class HessianUtils {
    private static final HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();


    /**
     * 创建Hessian Proxy
     * @param clazz
     * @param url
     * @param <T>
     * @return
     * @throws MalformedURLException
     */
    public static <T> T createHessianProxy(Class<T> clazz, String url) throws Exception {
        UrlPath urlAnn = clazz.getAnnotation(UrlPath.class);
        if (urlAnn != null) {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String subUrl = urlAnn.value();
            if (!subUrl.startsWith("/")) {
                subUrl = "/" + subUrl;
            }
            url += subUrl;
        }

        return (T) hessianProxyFactory.create(clazz, url);
    }
}
