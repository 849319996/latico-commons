package com.latico.commons.net.trap.bean;

import java.util.List;

/**
 * <PRE>
 * Trap接收的结果
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月8日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class TrapResult {

	/** upTime trap接收时的报文自带的上报时间毫秒值 */
	private long upTime;
	
	/** receiveTime 收到报文时的时间毫秒值 */
	private long receiveTime;

	/** version trap版本 */
	private String version;

	/** remoteHost 远端的主机/IP */
	private String remoteHost;

	/** remotePort Trap远程端的端口 */
	private int remotePort;

	/** enterprise 厂家标识 */
	private String enterprise;

	/** name 本次trap的名称 */
	private String name;

	/** descr 描述 */
	private String descr;

	/** community 共同体 */
	private String community;

	private List<VariableBind> variableBinds;

	public long getUpTime() {
		return upTime;
	}

	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public List<VariableBind> getVariableBinds() {
		return variableBinds;
	}

	public void setVariableBinds(List<VariableBind> variableBinds) {
		this.variableBinds = variableBinds;
	}

	@Override
	public String toString() {
		return "TrapResult [upTime=" + upTime + ", receiveTime=" + receiveTime
		        + ", version=" + version + ", remoteHost=" + remoteHost
		        + ", remotePort=" + remotePort + ", enterprise=" + enterprise
		        + ", name=" + name + ", descr=" + descr + ", community="
		        + community + ", variableBinds=" + variableBinds + "]";
	}

}
