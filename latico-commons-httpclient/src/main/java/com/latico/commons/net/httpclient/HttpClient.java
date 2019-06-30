package com.latico.commons.net.httpclient;

import java.io.Closeable;

/**
 * <PRE>
 * http客户端
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-03 0:31
 * @Version: 1.0
 */
public interface HttpClient extends Closeable {

    /**
     * GET方式，使用所有默认参数请求一个URL获取结果
     * @param url
     * @return
     */
    String doGet(String url);

    /**
     * POST方式，使用所有默认参数请求一个URL获取结果
     * @param url
     * @return
     */
    String doPost(String url);

}
