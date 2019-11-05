package com.latico.commons.net.trap.bean;

import java.util.Map;

/**
 * <PRE>
 * Trap接收的结果
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月8日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class Snmp4jTrapResult {


	/** receiveTime 收到报文时的时间毫秒值 */
	private long receiveTime;

	/** remoteHost 远端的主机/IP */
	private String remoteHost;

	/** remotePort Trap远程端的端口 */
	private int remotePort;

	/**
	 * OID值Map
	 */
	private Map<String, String> oidVauleMap;

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public Map<String, String> getOidVauleMap() {
		return oidVauleMap;
	}

	public void setOidVauleMap(Map<String, String> oidVauleMap) {
		this.oidVauleMap = oidVauleMap;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Snmp4jTrapResult{");
		sb.append("receiveTime=").append(receiveTime);
		sb.append(", remoteHost='").append(remoteHost).append('\'');
		sb.append(", remotePort=").append(remotePort);
		sb.append(", oidVauleMap=").append(oidVauleMap);
		sb.append('}');
		return sb.toString();
	}
}
