package com.latico.commons.spring.util;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-07 13:59
 * @version: 1.0
 */
public class SpringHttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SpringHttpUtils.class);

    /**
     * 默认的HTTP模式
     */
    protected static RestTemplate defaultRestTemplateHttp = createRestTemplateHttp(900000);
    /**
     * 默认的HTTPS模式
     */
    protected static RestTemplate defaultRestTemplateHttps = createRestTemplateHttps(900000);

    /**
     * 默认的接收响应头
     */
    protected static HttpHeaders defaultHttpHeaders = createDefaultHttpHeaders();

    /**
     * https的前缀
     */
    public static final String HTTPS_PREFIX = "https://";
    /**
     * 创建一个模板
     *
     * @param readTimeout
     * @return
     */
    public static RestTemplate createRestTemplateHttp(int readTimeout) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        //从连接池获取连接的timeout
        httpRequestFactory.setConnectionRequestTimeout(15000);
        //指客户端和服务器建立连接的timeout
        httpRequestFactory.setConnectTimeout(15000);
        //读取数据的timeout 15分钟
        httpRequestFactory.setReadTimeout(readTimeout);

        RestTemplate restTmpl = new MyRestTemplateImpl(httpRequestFactory);

        return restTmpl;
    }

    /**
     * @param readTimeout
     * @return
     */
    public static RestTemplate createRestTemplateHttps(int readTimeout) {
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();

            //从连接池获取连接的timeout
            requestFactory.setConnectionRequestTimeout(15000);
            //指客户端和服务器建立连接的timeout
            requestFactory.setConnectTimeout(15000);
            //读取数据的timeout 15分钟
            requestFactory.setReadTimeout(readTimeout);

            requestFactory.setHttpClient(httpClient);
            RestTemplate restTemplate = new MyRestTemplateImpl(requestFactory);
            return restTemplate;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * @return
     */
    protected static HttpHeaders createDefaultHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.TEXT_XML);
        mediaTypeList.add(MediaType.TEXT_PLAIN);
        mediaTypeList.add(MediaType.TEXT_HTML);
        mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(mediaTypeList);
        return headers;
    }
    
    /**
     * @param contentType   本端是内容类型
     * @param acceptContentTypes 能接收对端的内容类型
     * @param headers 自定义添加内容头
     * @param cookies cookies
     * @return
     */
    public static HttpHeaders createHttpHeaders(MediaType contentType, List<MediaType> acceptContentTypes, Map<String, String> headers, List<String> cookies) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(contentType);
        httpHeaders.setAccept(acceptContentTypes);
        if (cookies != null && !cookies.isEmpty()) {
            httpHeaders.put(HttpHeaders.COOKIE, cookies);
        }

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }
        return httpHeaders;
    }

    /**
     *
     * @param headers
     * @param cookies
     * @return
     */
    public static HttpHeaders createJsonHttpHeaders(Map<String, String> headers, List<String> cookies) {
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        mediaTypeList.add(MediaType.parseMediaType("text/html;charset=UTF-8"));
        mediaTypeList.add(MediaType.TEXT_XML);
        mediaTypeList.add(MediaType.TEXT_PLAIN);

        return createHttpHeaders(MediaType.APPLICATION_JSON_UTF8, mediaTypeList, headers, cookies);
    }


    /**
     * 调用
     *
     * @param url            能带?wsdl后缀，带了也可以，本方法会去除
     * @return
     */
    public static ResponseEntity<String> doGet(String url) {
        if (isHttpsUrl(url)) {
            return defaultRestTemplateHttps.getForEntity(url, String.class);
        } else {
            return defaultRestTemplateHttp.getForEntity(url, String.class);
        }
    }

    /**
     * 调用
     *
     * @param url
     * @return
     */
    public static ResponseEntity<String> doPost(String url, String body) {
        if (isHttpsUrl(url)) {
            return doPost(defaultRestTemplateHttps, url, body);
        } else {
            return doPost(defaultRestTemplateHttp, url, body);
        }
    }

    /**
     * POST请求
     * @param restTemplate
     * @param url
     * @param body
     * @return
     */
    public static ResponseEntity<String> doPost(RestTemplate restTemplate, String url, String body) {
        HttpEntity<String> request = new HttpEntity<>(body, defaultHttpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return responseEntity;
    }

    /**
     * @param restTemplate
     * @param httpHeaders
     * @param url
     * @param body
     * @return
     */
    public static ResponseEntity<String> doPost(RestTemplate restTemplate, HttpHeaders httpHeaders, String url, String body) {
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return responseEntity;
    }

    /**
     * @param restTemplate
     * @param contentType
     * @param acceptContentTypes
     * @param headers
     * @param cookies
     * @param url
     * @param body
     * @return
     */
    public static ResponseEntity<String> doPost(RestTemplate restTemplate, MediaType contentType, List<MediaType> acceptContentTypes, Map<String, String> headers, List<String> cookies, String url, String body) {
        HttpHeaders httpHeaders = createHttpHeaders(contentType, acceptContentTypes, headers, cookies);
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return responseEntity;
    }

    /**
     * @param restTemplate
     * @param headers
     * @param cookies
     * @param url
     * @param body
     * @return
     */
    public static ResponseEntity<String> doPostJson(RestTemplate restTemplate, Map<String, String> headers, List<String> cookies, String url, String body) {
        HttpHeaders httpHeaders = createJsonHttpHeaders(headers, cookies);
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return responseEntity;
    }

    /**
     * @param headers
     * @param cookies
     * @param url
     * @param body
     * @return
     */
    public static ResponseEntity<String> doPostJson(Map<String, String> headers, List<String> cookies, String url, String body) {
        if (isHttpsUrl(url)) {
            return doPostJson(defaultRestTemplateHttps, headers, cookies, url, body);
        } else {
            return doPostJson(defaultRestTemplateHttp, headers, cookies, url, body);
        }
    }

    /**
     * 判断是不是HTTPS
     * @param url
     * @return
     */
    protected static boolean isHttpsUrl(String url) {
        return url.startsWith(HTTPS_PREFIX);
    }

    /**
     * 是URL
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (url == null) {
            return false;
        }
        return url.startsWith("http://") || url.startsWith("https://");
    }

    /**
     * 是200的状态
     *
     * @param responseEntity
     * @return
     */
    public static boolean isOkStatus(ResponseEntity responseEntity) {
        if (responseEntity == null) {
            return false;
        }
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
