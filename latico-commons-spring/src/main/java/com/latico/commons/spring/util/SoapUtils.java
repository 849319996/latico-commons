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

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2020-03-17 15:45
 * @version: 1.0
 */
public class SoapUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SoapUtils.class);

    /**
     * 默认的HTTP模式
     */
    private static RestTemplate defaultRestTemplateHttp = createRestTemplateHttp(900000);
    /**
     * 默认的HTTPS模式
     */
    private static RestTemplate defaultRestTemplateHttps = createRestTemplateHttps(900000);

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

        RestTemplate restTmpl = new SoapRestTemplateImpl(httpRequestFactory);

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
            RestTemplate restTemplate = new SoapRestTemplateImpl(requestFactory);
            return restTemplate;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * @param soapMethodName 比如采集CTP的方式：getContainedPotentialConnectionTerminationPoints
     * @return
     */
    public static HttpHeaders createHttpHeaders(String soapMethodName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/xml;charset=UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.TEXT_XML);
        mediaTypeList.add(MediaType.TEXT_PLAIN);
        mediaTypeList.add(MediaType.TEXT_HTML);
        headers.setAccept(mediaTypeList);
        headers.add("SOAPAction", soapMethodName);
        return headers;
    }


    /**
     * 调用
     *
     * @param url            能带?wsdl后缀，带了也可以，本方法会去除
     * @param soapMethodName 比如采集CTP的方式：getContainedPotentialConnectionTerminationPoints
     * @param requestXml
     * @return
     */
    public static ResponseEntity<String> call(String url, String soapMethodName, String requestXml) {
        if (isHttpsUrl(url)) {
            return call(defaultRestTemplateHttps, url, soapMethodName, requestXml);
        } else {
            return call(defaultRestTemplateHttp, url, soapMethodName, requestXml);
        }

    }

    /**
     * 判断是不是HTTPS
     * @param url
     * @return
     */
    private static boolean isHttpsUrl(String url) {
        return url.startsWith(HTTPS_PREFIX);
    }

    /**
     * 调用
     *
     * @param restTemplate
     * @param url            不能带?wsdl后缀，带了也可以，本方法会去除
     * @param soapMethodName 比如采集CTP的方式：getContainedPotentialConnectionTerminationPoints
     * @param requestXml
     * @return
     */
    public static ResponseEntity<String> call(RestTemplate restTemplate, String url, String soapMethodName, String requestXml) {
        HttpHeaders httpHeaders = createHttpHeaders(soapMethodName);
        HttpEntity<String> request = new HttpEntity<>(requestXml, httpHeaders);

        String wsdlSuffix = "?wsdl";
        //替换掉WSDL后缀
        if (url.endsWith(wsdlSuffix)) {
            url = url.replace(wsdlSuffix, "");
        }

        String wsdlSuffix2 = "?WSDL";
        if (url.endsWith(wsdlSuffix2)) {
            url = url.replace(wsdlSuffix2, "");
        }

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return responseEntity;
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
