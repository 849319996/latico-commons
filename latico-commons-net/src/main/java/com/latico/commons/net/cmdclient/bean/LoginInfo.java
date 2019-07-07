package com.latico.commons.net.cmdclient.bean;

import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import com.latico.commons.net.snmp.enums.VersionEnum;

import java.io.Serializable;

/**
 * <PRE>
 * SNMP、Telnet、SSH的登陆连接信息
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2017年3月23日</B>
 * @since <B>JDK1.6</B>
 */
public class LoginInfo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ip 登陆的IP
     */
    private String ip;

    /**
     * manufacturer 厂家 required
     */
    private String manufacturer;

    /**
     * model 型号
     */
    private String model;

    /**
     * port 登陆的端口
     */
    private int port;

    /**
     * username 登陆的用户名
     */
    private String username;

    /**
     * password 登陆的密码
     */
    private String password;

    /**
     * enable 是否需要执行enable命令，可以通过厂家判断设备是否需要enable
     */
    private boolean enable;

    /**
     * enablePwd 假如有enable并且需要密码（中兴）则需要输入的enable的密码，可以通过厂家知道该密码，不需要从外界动态获取
     */
    private String enablePwd;

    /**
     * charset 流的字符集
     */
    private String charset;

    /**
     * snmpReadCommunity SNMP读团体号
     */
    private String snmpReadCommunity;

    /**
     * snmpWriteCommunity SNMP写团体和
     */
    private String snmpWriteCommunity;

    /**
     * snmpVersion SNMP版本
     */
    private VersionEnum snmpVersion;

    /**
     * connectType 连接类型，有telnet和SSH等
     */
    private CmdClientTypeEnum connectType;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public CmdClientTypeEnum getConnectType() {
        return connectType;
    }

    public void setConnectType(CmdClientTypeEnum connectType) {
        this.connectType = connectType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getEnablePwd() {
        return enablePwd;
    }

    public void setEnablePwd(String enablePwd) {
        this.enablePwd = enablePwd;
    }

    public String getSnmpReadCommunity() {
        return snmpReadCommunity;
    }

    public void setSnmpReadCommunity(String snmpReadCommunity) {
        this.snmpReadCommunity = snmpReadCommunity;
    }

    public String getSnmpWriteCommunity() {
        return snmpWriteCommunity;
    }

    public void setSnmpWriteCommunity(String snmpWriteCommunity) {
        this.snmpWriteCommunity = snmpWriteCommunity;
    }

    public VersionEnum getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(VersionEnum snmpVersion) {
        this.snmpVersion = snmpVersion;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginInfo{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", port=").append(port);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", enable=").append(enable);
        sb.append(", enablePwd='").append(enablePwd).append('\'');
        sb.append(", charset='").append(charset).append('\'');
        sb.append(", snmpReadCommunity='").append(snmpReadCommunity).append('\'');
        sb.append(", snmpWriteCommunity='").append(snmpWriteCommunity).append('\'');
        sb.append(", snmpVersion=").append(snmpVersion);
        sb.append(", connectType=").append(connectType);
        sb.append('}');
        return sb.toString();
    }

}
