package com.latico.commons.net.httpclient.impl.httpclientv4;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.httpclient.AbstractHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-26 23:31
 * @Version: 1.0
 */
public class HttpClient4Impl extends AbstractHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient4Impl.class);
    /** httpClient 当已经登陆的时候使用这个 */
    private CloseableHttpClient httpClient;

    private HttpGet httpGet;

    private HttpPost httpPost;

    private CloseableHttpResponse httpResponse;

    private RequestConfig clientRequestConfig;

    private RequestConfig httpGetRequestConfig;

    private Set<Cookie> cookies;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";

    /** POOL_SIZE 最大的打开的连接总量 */
    private static final int POOL_SIZE = 120;

    public HttpClient4Impl() {
        initConfig();
    }
    /**
     * 初始化配置
     */
    private void initConfig() {

        LOG.info(Thread.currentThread().getName() + " 开始打开浏览器，并设置Get请求的各项超时时间");
        httpGetRequestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONN_TIMEOUT).setConnectTimeout(CONN_TIMEOUT)
                .setSocketTimeout(CALL_TIMEOUT).build();

        clientRequestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CALL_TIMEOUT)
                .setSocketTimeout(CALL_TIMEOUT)
                .setConnectTimeout(CONN_TIMEOUT).build();

        httpGet = new HttpGet();
        httpGet.setConfig(httpGetRequestConfig);// 设置httpGet的超时时间
        httpGet.setHeader("Referer", "httpclient://i.firefoxchina.cn/");
        httpGet.addHeader("Content-Type", "text/xml;charset=utf-8");
        httpGet.addHeader("Cache-Control", "no-cache");

        httpPost = new HttpPost();
        httpPost.setConfig(httpGetRequestConfig);// 设置httpGet的超时时间
        httpPost.setHeader("Referer", "httpclient://i.firefoxchina.cn/");
        httpPost.addHeader("Content-Type", "text/xml;charset=utf-8");
        httpPost.addHeader("Cache-Control", "no-cache");

        httpClient = HttpClients.custom().setUserAgent(USER_AGENT)
                .setMaxConnTotal(POOL_SIZE)
                .setMaxConnPerRoute(POOL_SIZE)
                .setDefaultRequestConfig(clientRequestConfig).build();
    }

    @Override
    public String doGet(String url) {


        try {
            httpGet.setURI(new URI(url));
            // 返回响应
            httpResponse = httpClient.execute(httpGet);

            // 获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();

            // 把响应实体转换成网页源代码
            return EntityUtils.toString(httpEntity, DEFAULT_CHARSET);
        } catch (Exception e) {
            LOG.error(e);
        }finally {
            // LOG.info("开始释放连接对象");
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            try {

                if (httpResponse != null) {
                    httpResponse.close();
                }

            } catch (Exception e) {

            }
        }

        return null;
    }

    @Override
    public String doPost(String url) {
        try {
            httpPost.setURI(new URI(url));
            // 返回响应
            httpResponse = httpClient.execute(httpPost);

            // 获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();

            // 把响应实体转换成网页源代码
            return EntityUtils.toString(httpEntity, DEFAULT_CHARSET);
        } catch (Exception e) {
            LOG.error(e);
        }finally {
            // LOG.info("开始释放连接对象");
            if (httpPost != null) {
                httpGet.releaseConnection();
            }
            try {

                if (httpResponse != null) {
                    httpResponse.close();
                }

            } catch (Exception e) {

            }
        }


        return null;
    }

    @Override
    public void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOG.error("httpclient浏览器关闭异常", e);
            }
        }
    }
}
