package com.latico.commons.net.dhcp.server.bean;

/**
 * <PRE>
 * 配置文件配置的DHCP IP池
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @version <B>V1.0 2018年8月1日</B>
 * @since <B>JDK1.6</B>
 */
public class ConfDhcpIpPool {

    private String id;
    private String serverIp;

    /**
     * 默认"0.0.0.0
     */
    private String relayIp = "0.0.0.0";

    private String startIp;

    private String endIp;

    private String netmask;
    private String serverName = "";
    private String serverMessage = "";
    private String serverDomainName = "";

    /**
     * 租期大小，默认172800，单位毫秒
     */
    private int leaseTime = 172800;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getRelayIp() {
        return relayIp;
    }

    public void setRelayIp(String relayIp) {
        this.relayIp = relayIp;
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public String getServerDomainName() {
        return serverDomainName;
    }

    public void setServerDomainName(String serverDomainName) {
        this.serverDomainName = serverDomainName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfDhcpIpPool{");
        sb.append("id='").append(id).append('\'');
        sb.append(", serverIp='").append(serverIp).append('\'');
        sb.append(", relayIp='").append(relayIp).append('\'');
        sb.append(", startIp='").append(startIp).append('\'');
        sb.append(", endIp='").append(endIp).append('\'');
        sb.append(", netmask='").append(netmask).append('\'');
        sb.append(", serverName='").append(serverName).append('\'');
        sb.append(", serverMessage='").append(serverMessage).append('\'');
        sb.append(", serverDomainName='").append(serverDomainName).append('\'');
        sb.append(", leaseTime=").append(leaseTime);
        sb.append('}');
        return sb.toString();
    }

    public void setLeaseTime(int leaseTime) {
        this.leaseTime = leaseTime;
    }

    public int getLeaseTime() {
        return leaseTime;
    }
}
