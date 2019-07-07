package com.latico.commons.net.httpclient.impl.jdknet;

import com.latico.commons.common.envm.HttpHead;
import com.latico.commons.common.util.codec.CodecUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.system.SystemUtils;
import com.latico.commons.net.httpclient.AbstractHttpClient;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-03 0:43
 * @Version: 1.0
 */
public class JdkHttpClientImpl extends AbstractHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(JdkHttpClientImpl.class);

    /** SSL实例名称 */
    private final static String TLS = "tls";

    private HttpURLConnection httpClientConn;

    @Override
    public void close() {
        try {
            close(httpClientConn);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * 关闭HTTP/HTTPS连接
     * @param httpClientConn
     */
    public static void close(HttpURLConnection httpClientConn) {
        if(httpClientConn != null) {
            httpClientConn.disconnect();
        }
    }

    /**
     * 提交POST请求
     * @param url 资源路径
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    @Override
    public String doPost(String url) {
        return doPost(url, null, null);
    }

    /**
     * 提交POST请求
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    public String doPost(String url, Map<String, String> header,
                                Map<String, String> request) {
        return doPost(url, header, request, DEFAULT_CHARSET);
    }

    /**
     * 提交POST请求
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param charset 字符集编码
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    public String doPost(String url, Map<String, String> header,
                                Map<String, String> request, String charset) {
        return doPost(url, header, request,
                CONN_TIMEOUT, CALL_TIMEOUT, charset);
    }

    /**
     * 提交POST请求
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param connTimeout 连接超时（ms）
     * @param readTimeout 读取超时（ms）
     * @param charset 字符集编码
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    public String doPost(String url,
                                Map<String, String> header, Map<String, String> request,
                                int connTimeout, int readTimeout, String charset) {
        String response = "";
        try {
            response = _doPost(url, header, request,
                    connTimeout, readTimeout, charset);

        } catch(Exception e) {
            LOG.error("提交{}请求失败: [{}]", METHOD_POST, url, e);
        }
        return response;
    }

    /**
     * 提交POST请求
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param connTimeout 连接超时（ms）
     * @param readTimeout 读取超时（ms）
     * @param charset 字符集编码
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     * @throws Exception
     */
    private String _doPost(String url,
                                  Map<String, String> header, Map<String, String> request,
                                  int connTimeout, int readTimeout, String charset) throws Exception {
        String response = "";
        HttpURLConnection conn = createHttpConn(url, METHOD_POST,
                header, connTimeout, readTimeout);

        // POST的请求参数是在结构体中发过去的
        String kvs = encodeRequests(request, charset);
        if (StringUtils.isNoneEmpty(kvs)) {
            byte[] bytes = CodecUtils.toBytes(kvs, charset);
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
        }

        response = responseAsString(conn, charset);
        close(conn);
        return response;
    }

    /**
     * 提交GET请求
     * @param url 资源路径
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    @Override
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * 提交GET请求
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    public String doGet(String url, Map<String, String> header,
                               Map<String, String> request) {
        return doGet(url, header, request, DEFAULT_CHARSET);
    }

    /**
     * 提交GET请求
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param charset 字符集编码
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    public String doGet(String url, Map<String, String> header,
                               Map<String, String> request, String charset) {
        return doGet(url, header, request,
                CONN_TIMEOUT, CALL_TIMEOUT, charset);
    }

    /**
     * 提交GET请求
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param connTimeout 连接超时（ms）
     * @param readTimeout 读取超时（ms）
     * @param charset 字符集编码
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    public String doGet(String url,
                               Map<String, String> header, Map<String, String> request,
                               int connTimeout, int readTimeout, String charset) {
        String response = "";
        try {
            response = _doGet(url, header, request,
                    connTimeout, readTimeout, charset);

        } catch(Exception e) {
            LOG.error("提交{}请求失败: [{}]", METHOD_GET, url, e);
        }
        return response;
    }

    /**
     * 提交GET请求
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param connTimeout 连接超时（ms）
     * @param readTimeout 读取超时（ms）
     * @param charset 字符集编码
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     * @throws Exception
     */
    private String _doGet(String url,
                                 Map<String, String> header, Map<String, String> request,
                                 int connTimeout, int readTimeout, String charset) throws Exception {
        String kvs = encodeRequests(request, charset);
        url = concatGET(url, kvs);	// GET的参数是拼在url后面的

        HttpURLConnection conn = createHttpConn(url, METHOD_GET,
                header, connTimeout, readTimeout);
        String response = responseAsString(conn, charset);
        close(conn);
        return response;
    }

    /**
     * 提取HTTP连接的响应结果
     * @param conn HTTP连接
     * @param charset 字符集编码
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    private String responseAsString(HttpURLConnection conn, String charset) {
        if(!isOkStatusCode(conn)) {
            return "";
        }

        // 检测返回的内容是否使用gzip压缩过
        String encode = conn.getContentEncoding();
        boolean isGzip = HttpHead.VAL.GZIP.equalsIgnoreCase(encode);

        String response = "";
        InputStreamReader is = null;
        try {
            is = isGzip ?
                    new InputStreamReader(new GZIPInputStream(conn.getInputStream()), charset) :
                    new InputStreamReader(conn.getInputStream(), charset);
            response = IOUtils.toString(is);

        } catch (Exception e) {
            LOG.error("获取HTTP响应结果失败", e);
        }finally {
            IOUtils.close(is);
        }
        return response;
    }

    /**
     * 下载资源，适用于返回类型是非文本的响应
     * @param savePath 包括文件名的保存路径
     * @param url 资源路径
     * @return 是否下载成功（下载成功会保存到savePath）
     */
    public boolean downloadByPost(String savePath, String url) {
        return downloadByPost(savePath, url, null, null);
    }

    /**
     * 下载资源，适用于返回类型是非文本的响应
     * @param savePath 包括文件名的保存路径
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @return 是否下载成功（下载成功会保存到savePath）
     */
    public boolean downloadByPost(String savePath, String url,
                                         Map<String, String> header, Map<String, String> request) {
        return downloadByPost(savePath, url, header, request,
                CONN_TIMEOUT, CALL_TIMEOUT, DEFAULT_CHARSET);
    }

    /**
     * 下载资源，适用于返回类型是非文本的响应
     * @param savePath 包括文件名的保存路径
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param connTimeout 连接超时（ms）
     * @param readTimeout 读取超时（ms）
     * @param charset 字符集编码
     * @return 是否下载成功（下载成功会保存到savePath）
     */
    public boolean downloadByPost(String savePath, String url,
                                         Map<String, String> header, Map<String, String> request,
                                         int connTimeout, int readTimeout, String charset) {
        boolean isOk = false;
        try {
            isOk = _downloadByPost(savePath, url, header, request,
                    connTimeout, readTimeout, charset);
        } catch (Exception e) {
            LOG.error("下载资源失败: [{}]", url, e);
        }
        return isOk;
    }

    /**
     * 下载资源，适用于返回类型是非文本的响应
     * @param savePath 包括文件名的保存路径
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param connTimeout 连接超时（ms）
     * @param readTimeout 读取超时（ms）
     * @param charset 字符集编码
     * @return 是否下载成功（下载成功会保存到savePath）
     * @throws Exception
     */
    private boolean _downloadByPost(String savePath, String url,
                                           Map<String, String> header, Map<String, String> request,
                                           int connTimeout, int readTimeout, String charset) throws Exception {
        boolean isOk = false;
        HttpURLConnection conn = createHttpConn(url, METHOD_POST,
                header, connTimeout, readTimeout);

        // POST的请求参数是在结构体中发过去的
        String kvs = encodeRequests(request, charset);
        if (StringUtils.isNoneEmpty(kvs)) {
            byte[] bytes = CodecUtils.toBytes(kvs, charset);
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
        }

        isOk = responseAsRes(conn, savePath);
        close(conn);
        return isOk;
    }

    /**
     * 下载资源，适用于返回类型是非文本的响应
     * @param savePath 包括文件名的保存路径
     * @param url 资源路径
     * @return 是否下载成功（下载成功会保存到savePath）
     */
    public boolean downloadByGet(String savePath, String url) {
        return downloadByGet(savePath, url, null, null);
    }

    /**
     * 下载资源，适用于返回类型是非文本的响应
     * @param savePath 包括文件名的保存路径
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @return 是否下载成功（下载成功会保存到savePath）
     */
    public boolean downloadByGet(String savePath, String url,
                                        Map<String, String> header, Map<String, String> request) {
        return downloadByGet(savePath, url, header, request,
                CONN_TIMEOUT, CALL_TIMEOUT, DEFAULT_CHARSET);
    }

    /**
     * 下载资源，适用于返回类型是非文本的响应
     * @param savePath 包括文件名的保存路径
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param connTimeout 连接超时（ms）
     * @param readTimeout 读取超时（ms）
     * @param charset 字符集编码
     * @return 是否下载成功（下载成功会保存到savePath）
     */
    public boolean downloadByGet(String savePath, String url,
                                        Map<String, String> header, Map<String, String> request,
                                        int connTimeout, int readTimeout, String charset) {
        boolean isOk = false;
        try {
            isOk = _downloadByGet(savePath, url, header, request,
                    connTimeout, readTimeout, charset);
        } catch (Exception e) {
            LOG.error("下载资源失败: [{}]", url, e);
        }
        return isOk;
    }

    /**
     * 下载资源，适用于返回类型是非文本的响应
     * @param savePath 包括文件名的保存路径
     * @param url 资源路径
     * @param header 请求头参数表
     * @param request 请求参数表
     * @param connTimeout 连接超时（ms）
     * @param readTimeout 读取超时（ms）
     * @param charset 字符集编码
     * @return 是否下载成功（下载成功会保存到savePath）
     * @throws Exception
     */
    private boolean _downloadByGet(String savePath, String url,
                                          Map<String, String> header, Map<String, String> request,
                                          int connTimeout, int readTimeout, String charset) throws Exception {
        String kvs = encodeRequests(request, charset);
        url = concatGET(url, kvs);	// GET的参数是拼在url后面的

        HttpURLConnection conn = createHttpConn(url, METHOD_GET,
                header, connTimeout, readTimeout);
        boolean isOk = responseAsRes(conn, savePath);
        close(conn);
        return isOk;
    }

    /**
     * 提取HTTP连接的响应资源
     * @param conn HTTP连接
     * @param savePath 包括文件名的保存路径
     * @return 是否下载成功（下载成功会保存到savePath）
     */
    private boolean responseAsRes(HttpURLConnection conn, String savePath) {
        if(!isOkStatusCode(conn)) {
            return false;
        }
        InputStream is = null;
        try {
            is = conn.getInputStream();
            IOUtils.toFile(is, savePath);
            return true;
        } catch (Exception e) {
            LOG.error("保存资源 [{}] 失败", savePath, e);
        } finally {
            IOUtils.close(is);
        }
        return false;
    }

    /**
     * 构造HTTP/HTTPS连接(支持TLSv1.2)
     * @param url 目标地址
     * @param method 请求方法：GET/POST
     * @return HTTP连接(失败返回null)
     */
    public static HttpURLConnection createHttpConn(String url, String method) {
        return createHttpConn(url, method, null, CONN_TIMEOUT, CALL_TIMEOUT);
    }

    /**
     * 构造HTTP/HTTPS连接(支持TLSv1.2)
     * @param url 目标地址
     * @param method 请求方法：GET/POST
     * @param header 请求头参数
     * @return HTTP连接(失败返回null)
     */
    public static HttpURLConnection createHttpConn(String url,
                                                   String method, Map<String, String> header) {
        return createHttpConn(url, method, header,
                CONN_TIMEOUT, CALL_TIMEOUT);
    }

    /**
     * 构造HTTP/HTTPS连接(支持TLSv1.2)
     * @param url 目标地址
     * @param method 请求方法：GET/POST
     * @param header 请求头参数
     * @param connTimeout 连接超时(ms)
     * @param readTimeout 读取超时(ms)
     * @return HTTP连接(失败返回null)
     */
    public static HttpURLConnection createHttpConn(String url, String method,
                                                   Map<String, String> header, int connTimeout, int readTimeout) {
        HttpURLConnection conn = null;
        try {
            conn = _createHttpConn(url, method, header, connTimeout, readTimeout);

        } catch(Exception e) {
            LOG.error("创建HTTP连接失败", e);
        }
        return conn;
    }

    /**
     * 构造HTTP/HTTPS连接(支持TLSv1.2)
     * @param url 目标地址
     * @param method 请求方法：GET/POST
     * @param header 请求头参数
     * @param connTimeout 连接超时(ms)
     * @param readTimeout 读取超时(ms)
     * @return HTTP连接(失败返回null)
     * @throws Exception
     */
    private static HttpURLConnection _createHttpConn(String url, String method,
                                                     Map<String, String> header, int connTimeout, int readTimeout) throws Exception {
        HttpURLConnection conn = null;
        if(StringUtils.isEmpty(url)) {
            return conn;
        }
        URL URL = new URL(url);

        // HTTPS连接(若依然报错 protocol_version， 则调用此方法的程序需切换到JDK1.8以上, JDK1.8默认使用TLSv1.2)
        if(HTTPS.equals(URL.getProtocol())) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) URL.openConnection();
            if(SystemUtils.IS_JAVA_1_6 || SystemUtils.IS_JAVA_1_7) {
                _supportTLSv12(httpsConn);	//  JDK1.6和JDK1.7追加TLSv1.2支持

            } else {
                _bypassSSL(httpsConn);		// 绕过SSL校验(可选, JDK1.8以上不绕过也可)
            }

            conn = httpsConn;

            // HTTP连接
        } else {
            conn = (HttpURLConnection) URL.openConnection();
        }

        // 设置固有请求参数
        conn.setRequestMethod(method);
        conn.setConnectTimeout(connTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        // 设置自定义请求头参数
        if(header != null) {
            Iterator<String> keyIts = header.keySet().iterator();
            while(keyIts.hasNext()) {
                String key = keyIts.next();
                String val = header.get(key);
                if(StringUtils.isNoneEmpty(key, val)) {
                    conn.setRequestProperty(key, val);
                }
            }
        }
        return conn;
    }

    /**
     * <pre>
     * 追加TLSv1.2支持 (适用于javax.net.ssl.HttpsURLConnection).
     * -------------------
     *  主要用于解决 JDK1.6 和 JDK1.7 不支持 TLSv1.2 的问题.
     *  注意此方法不能与绕过SSL校验 {@link #_bypassSSL()} 共用
     * </pre>
     * @param httpsConn HTTPS连接
     */
    private static void _supportTLSv12(HttpsURLConnection httpsConn) {
        httpsConn.setSSLSocketFactory(new _TLS12_HttpURLSocketFactory());
    }

    /**
     * <pre>
     * 绕过SSL校验.
     * -------------------
     *  若服务端使用的是TLSv1.2协议, 绕过也没有用的, 在建立握手连接时,
     *  服务端会认为客户端加密机制不安全而拒绝握手, 报错 Received fatal alert: protocol_version.
     *  由于 JDK1.6 和 JDK1.7 均不支持 TLSv1.2, 在这种情况下只能使用 JDK1.8
     * </pre>
     * @param httpsConn HTTPS连接
     * @throws Exception
     */
    private static void _bypassSSL(HttpsURLConnection httpsConn) throws Exception {

        // 绕过SSL证书校验
        SSLContext ssl = SSLContext.getInstance(TLS);
        ssl.init(new KeyManager[0], new TrustManager[] { new _X509TrustManager() }, new SecureRandom());
        httpsConn.setSSLSocketFactory(ssl.getSocketFactory());

        // 绕过SSL域名校验
        httpsConn.setHostnameVerifier(new _X509HostnameVerifier());
    }

    /**
     * 判断HTTP请求是否响应成功
     * @param conn
     * @return
     */
    public static boolean isOkStatusCode(HttpURLConnection conn) {
        boolean isOk = false;
        try {
            isOk = (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            LOG.error("提取HTTP状态码失败", e);
        }
        return isOk;
    }
}
