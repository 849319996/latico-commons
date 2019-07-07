package com.latico.commons.net.httpclient.impl.httpclientv3;

import com.latico.commons.common.envm.HttpHead;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.system.SystemUtils;
import com.latico.commons.net.httpclient.AbstractHttpClient;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-26 23:31
 * @Version: 1.0
 */
public class HttpClient3Impl extends AbstractHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient3Impl.class);

    private HttpClient client;
    /**
     * 添加请求头参数参数
     * @param method
     * @param params
     */
    private static void addParamsToHeader(HttpMethod method, Map<String, String> params) {
        if(params != null) {
            Iterator<String> keyIts = params.keySet().iterator();
            while(keyIts.hasNext()) {
                String key = keyIts.next();
                String val = params.get(key);
                method.addRequestHeader(key, val);
            }
        }
    }

    /**
     * 添加post方法的请求参数
     * @param post
     * @param params
     */
    private static void addParamsToBody(PostMethod post, Map<String, String> params) {
        if(params != null) {
            Iterator<String> keyIts = params.keySet().iterator();
            while(keyIts.hasNext()) {
                String key = keyIts.next();
                String val = params.get(key);
                post.addParameter(key, val);
            }
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
        PostMethod post = new PostMethod(url);
        addParamsToHeader(post, header);
        addParamsToBody(post, request);	// POST的请求参数是在结构体中发过去的

        HttpClient client = createHttpClient(connTimeout, readTimeout);
        int status = client.executeMethod(post);
        String response = (!isOkStatusCode(status) ? "" :
                responseAsString(post, charset));
        post.releaseConnection();
        close(client);
        return response;
    }
    /**
     * 关闭HttpClient会话
     * @param httpClient
     */
    public static void close(HttpClient httpClient) {
        if(httpClient != null) {
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
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

        GetMethod get = new GetMethod(url);
        addParamsToHeader(get, header);

        HttpClient client = createHttpClient(connTimeout, readTimeout);
        int status = client.executeMethod(get);
        String response = (!isOkStatusCode(status) ? "" :
                responseAsString(get, charset));
        get.releaseConnection();
        close(client);
        return response;
    }

    /**
     *  提取HTTP连接的响应结果
     * @param method 请求方法
     * @param charset 字符集编码
     * @return HTTP返回的字符串（包括文本、json、xml等内容）
     */
    private static String responseAsString(HttpMethod method, String charset) {

        // 检测返回的内容是否使用gzip压缩过
        Header header = method.getResponseHeader(HttpHead.KEY.CONTENT_ENCODING);
        boolean isGzip = (header == null ? false :
                HttpHead.VAL.GZIP.equalsIgnoreCase(header.getValue()));

        String response = "";
        InputStreamReader is = null;
        try {
            is = isGzip ?
                    new InputStreamReader(new GZIPInputStream(method.getResponseBodyAsStream()), charset) :
                    new InputStreamReader(method.getResponseBodyAsStream(), charset);
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
     * @return
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
        PostMethod post = new PostMethod(url);
        addParamsToHeader(post, header);
        addParamsToBody(post, request);	// POST的请求参数是在结构体中发过去的

        HttpClient client = createHttpClient(connTimeout, readTimeout);
        int status = client.executeMethod(post);
        boolean isOk = (!isOkStatusCode(status) ? false :
                responseAsRes(post, savePath));
        post.releaseConnection();
        close(client);
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

        GetMethod get = new GetMethod(url);
        addParamsToHeader(get, header);

        HttpClient client = createHttpClient(connTimeout, readTimeout);
        int status = client.executeMethod(get);
        boolean isOk = (!isOkStatusCode(status) ? false :
                responseAsRes(get, savePath));
        get.releaseConnection();
        close(client);
        return isOk;
    }

    /**
     * 保存HTTP资源
     * @param method 请求方法
     * @param savePath 包括文件名的保存路径
     * @return
     */
    private static boolean responseAsRes(HttpMethod method, String savePath) {
        InputStream is = null;
        try {
            is = method.getResponseBodyAsStream();
            IOUtils.toFile(is, savePath);
            return true;
        } catch (Exception e) {
            LOG.error("保存资源 [{}] 失败", savePath, e);
        }finally {
            IOUtils.close(is);
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            close(client);
        }
    }


    /**
     * 创建HttpClient会话(不支持TLSv1.2)
     * @return
     */
    public static HttpClient createHttpClient() {
        return createHttpClient(CONN_TIMEOUT, CALL_TIMEOUT);
    }

    /**
     * 创建HttpClient会话(不支持TLSv1.2)
     * @param connTimeout
     * @param callTimeout
     * @return
     */
    public static HttpClient createHttpClient(int connTimeout, int callTimeout) {
        if(SystemUtils.IS_JAVA_1_6 || SystemUtils.IS_JAVA_1_7) {
            _supportTLSv12();	//  JDK1.6和JDK1.7追加TLSv1.2支持

        } else {
            // Undo JDK1.8以上默认支持TLSv1.2, 无需追加
        }

        HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
        managerParams.setConnectionTimeout(connTimeout);
        managerParams.setSoTimeout(callTimeout);

        HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.setParams(managerParams);

        HttpClient httpClient = new HttpClient(new HttpClientParams());
        httpClient.setHttpConnectionManager(httpConnectionManager);
        return httpClient;
    }
    /**
     * <pre>
     * 追加TLSv1.2支持 (适用于org.apache.commons.httpclientv3.MyHttpClient).
     * -------------------
     *  主要用于解决 JDK1.6 和 JDK1.7 不支持 TLSv1.2 的问题.
     *  注意此方法不能与绕过SSL校验 {@link #_bypassSSL()} 共用
     * </pre>
     */
    private static void _supportTLSv12() {
        Protocol sslProtocol = Protocol.getProtocol(HTTPS);
        int sslPort = sslProtocol.getDefaultPort();		// https的默认端口一般为443
        _TLS12_HttpClientSocketFactory sslSocketFactory = new _TLS12_HttpClientSocketFactory();
        Protocol https = new Protocol(HTTPS, sslSocketFactory, sslPort);
        Protocol.registerProtocol(HTTPS, https);
    }
}
