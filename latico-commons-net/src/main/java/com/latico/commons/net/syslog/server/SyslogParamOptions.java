package com.latico.commons.net.syslog.server;

/**
 *
 * @Author: latico
 * @Date: 2018/12/05 23:21:26
 * @Version: 1.0
 */
public class SyslogParamOptions {

    /**
     * The Protocol. 协议，支持：udp、tcp等
     */
    private String protocol = null;
    /**
     * The File name.把内容写进文件
     */
    private String fileName = null;
    /**
     * The Append. 是否追加，启动程序后，以追加的方式还是从新写的方式
     */
    private boolean append = false;
    /**
     * The Quiet.是否打印日志，true：安静的，不打印日志
     */
    private boolean quiet = false;

    /**
     * The Host. 主机名称或者IP
     */
    private String host = null;
    /**
     * The Port. 指定端口，默认514
     */
    private String port = null;
    /**
     * The Timeout. 超时时间
     */
    private String timeout = null;

    /**
     * The Usage. 参数配置使用情况
     */
    private String usage = null;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SyslogParamOptions{");
        sb.append("protocol='").append(protocol).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", append=").append(append);
        sb.append(", quiet=").append(quiet);
        sb.append(", host='").append(host).append('\'');
        sb.append(", port='").append(port).append('\'');
        sb.append(", timeout='").append(timeout).append('\'');
        sb.append(", usage='").append(usage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

