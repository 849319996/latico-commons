package com.latico.commons.net.httpclient;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.codec.CodecUtils;
import com.latico.commons.common.util.string.StringUtils;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-03 0:45
 * @version: 1.0
 */
public abstract class AbstractHttpClient implements HttpClient {

    protected static final String DEFAULT_CHARSET = CharsetType.UTF8;

    /**
     * 当前状态码
     */
    protected int currentStatusCode;

    /** 连接超时, 默认1分钟 */
    protected final static int CONN_TIMEOUT = 60000;

    /** 响应/读取超时 , 默认15分钟 */
    protected final static int CALL_TIMEOUT = 900000;

    /** URL协议类型:HTTPS */
    protected final static String HTTPS = "https";

    /** SSL实例名称 */
    protected final static String TLS = "tls";

    /** GET请求方法名 */
    protected final static String METHOD_GET = "GET";

    /** POST请求方法名 */
    protected final static String METHOD_POST = "POST";

    public boolean isOkStatusCode() {
        return isOkStatusCode(currentStatusCode);
    }

    /**
     * 判断HTTP响应状态码是否为成功
     * @param httpStatus 响应状态码
     * @return
     */
    public static boolean isOkStatusCode(int httpStatus) {
        return (httpStatus == 200);
    }

    /**
     * 把请求参数转换成URL的KV串形式并进行编码
     * @param request 请求参数集
     * @return ?&key1=val1&key2=val2&key3=val3
     */
    public static String encodeRequests(Map<String, String> request) {
        return encodeRequests(request, DEFAULT_CHARSET);
    }

    /**
     * 把请求参数转换成URL的KV串形式并进行编码
     * @param request 请求参数集
     * @param charset 参数字符编码
     * @return ?key1=val1&key2=val2&key3=val3
     */
    public static String encodeRequests(
            Map<String, String> request, final String charset) {
        if(request == null || request.isEmpty() ||
                CodecUtils.isInvalid(charset)) {
            return "";
        }

        StringBuilder sb = new StringBuilder("?");
        Iterator<String> keyIts = request.keySet().iterator();
        while(keyIts.hasNext()) {
            String key = keyIts.next();
            String val = request.get(key);
            try {
                val = URLEncoder.encode(val, charset);
            } catch (Exception e) {
                val = "";
            }

            // 注意：
            //   第一个参数开头的&，对于POST请求而言是必须的
            //   但对于GET请求则是可有可无的（但存在某些网页会强制要求不能存在）
            if(StringUtils.isNoneEmpty(key, val)) {
                sb.append("&").append(key).append("=").append(val);
            }
        }
        return sb.toString();
    }

    /**
     * 拼接GET请求的URL和参数(对于第一个参数开头的&, 强制去除)
     * @param url GET请求URL
     * @param requestKVs GET请求参数表 (需通过{@link #encodeRequests}方法转码)
     * @return GET请求URL
     */
    protected static String concatGET(String url, String requestKVs) {
        url = StringUtils.isEmpty(url) ? "" : url;
        String _GETURL = url.concat(requestKVs);
        return _GETURL.replace(url.concat("?&"), url.concat("?"));	// 去掉第一个参数的&
    }
}
