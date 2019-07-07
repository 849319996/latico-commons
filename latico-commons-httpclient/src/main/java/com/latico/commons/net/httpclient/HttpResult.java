package com.latico.commons.net.httpclient;

/**
 * <PRE>
 * HTTP响应结果
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-03 0:34
 * @Version: 1.0
 */
public class HttpResult {
    /**
     * 状态，true：成功，false:失败
     */
    private boolean status;
    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 请求的URL
     */
    private String url;
    /**
     * 字符集
     */
    private String charset;
    /**
     * 返回内容
     */
    private String content;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpResult{");
        sb.append("status=").append(status);
        sb.append(", statusCode=").append(statusCode);
        sb.append(", url='").append(url).append('\'');
        sb.append(", charset='").append(charset).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
