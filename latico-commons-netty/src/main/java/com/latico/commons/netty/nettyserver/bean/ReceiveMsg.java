package com.latico.commons.netty.nettyserver.bean;


import io.netty.channel.Channel;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-12-13 18:02
 * @Version: 1.0
 */
public class ReceiveMsg <MSG> {
    private String remoteIp;
    private int remotePort;
    private MSG msg;
    private Channel remoteChannel;

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public MSG getMsg() {
        return msg;
    }

    public void setMsg(MSG msg) {
        this.msg = msg;
    }

    public Channel getRemoteChannel() {
        return remoteChannel;
    }

    public void setRemoteChannel(Channel remoteChannel) {
        this.remoteChannel = remoteChannel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReceiveMsg{");
        sb.append("remoteIp='").append(remoteIp).append('\'');
        sb.append(", remotePort=").append(remotePort);
        sb.append(", msg=").append(msg);
        sb.append('}');
        return sb.toString();
    }
}
