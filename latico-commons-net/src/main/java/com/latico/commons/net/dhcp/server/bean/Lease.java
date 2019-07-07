package com.latico.commons.net.dhcp.server.bean;


import com.latico.commons.net.dhcp.server.common.LeaseTypeEnum;
import com.latico.commons.common.util.net.Ipv4Utils;
import com.latico.commons.common.util.time.DateTimeUtils;

import java.util.Arrays;


/**
 * <PRE>
 * 租期对象
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年7月15日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class Lease {
	/**
	 * The time how long a lease should be valid.
	 * 租期大小，默认2天，单位毫秒
	 */
	private long leaseTime = 172800000;

	/** ipPoolId IP池的ID */
	private String ipPoolId;
	
	/** leaseType 租约对象类型 */
	private LeaseTypeEnum leaseType;
	
	/**
	 * The ip adress assigned to this lease.
	 */
	private byte[] ipByte;

	/**
	 * A string representation of {@link #ipByte}
	 */
	private String ipString;

	/**
	 * When was this lease given / renewed?
	 * 当前租期的起始时间点
	 */
	private long currentStartTime;

	/** startTime 第一个租期起租时间点 */
	private long startTime;

	private String opt60;

	private String opt61;
	
	private String opt82;

	private String mac;

	/** clientHostName 客户端主机名 */
	private String clientHostName;

	private String netmask;

	/** gateway 暂时不支持人工指定，通过程序逻辑判断获取 */
	private String gateway;

	/** dns 暂时不支持人工指定，通过程序逻辑判断获取 */
	private String dns;
	

	public String getIpPoolId() {
		return ipPoolId;
	}

	public void setIpPoolId(String ipPoolId) {
		this.ipPoolId = ipPoolId;
	}

	public long getCurrentStartTime() {
		return currentStartTime;
	}

	public void setCurrentStartTime(long currentStartTime) {
		this.currentStartTime = currentStartTime;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	/**
	 * Creates a new lease for the given ip adress.
	 * @param ip the ip adress to lease
	 */
	public Lease(byte[] ip) {
		ipByte = ip.clone();
		ipString = Ipv4Utils.byteToIp(ipByte);

		currentStartTime = System.currentTimeMillis() + 3000;
		startTime = currentStartTime;

	}

	/**
	 * Creates a new lease with a custom leased timestamp.
	 * 
	 * @param ip the ip adress to lease
	 * @param lease the timestamp when this lease was granted.
	 */
	public Lease(String ip, int lease) {
		ipByte = Ipv4Utils.ipToByte(ip);
		ipString = ip;
		this.leaseTime = lease;
		currentStartTime = System.currentTimeMillis() + 3000;
		startTime = currentStartTime;
	}

	/**
	 * @return the ip adress of this lease.
	 */
	public byte[] getIp() {
		return ipByte;
	}

	/**
	 * @return the string representation of the leased ip adress
	 */
	public String getIpString() {
		return ipString;
	}

	/**
	 * @return when was this lease granted?
	 */
	public long getLeasedTimestamp() {
		return currentStartTime;
	}

	/**
	 * 
	 * @return 租约大小，单位：毫秒
	 */
	public long getLeaseTime() {
		return leaseTime;
	}

	public void setOpt60(String opt60) {
		this.opt60 = opt60;
	}

	public String getOpt60() {
		return opt60;
	}

	public void setOpt61(String opt61) {
		this.opt61 = opt61;
	}

	public String getOpt61() {
		return opt61;
	}

	public String getOpt82() {
		return opt82;
	}

	public void setOpt82(String opt82) {
		this.opt82 = opt82;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMac() {
		return mac;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getClientHostName() {
		return clientHostName;
	}

	public void setClientHostName(String clientHostName) {
		this.clientHostName = clientHostName;
	}

	/**
	 * @return is this lease still valid?
	 */
	public boolean isValid() {
		return ((currentStartTime + leaseTime) >= System.currentTimeMillis());
	}

	/**
	 * Refreshes a lease (renew it)
	 */
	public void refreshLease() {
		currentStartTime = System.currentTimeMillis() + 3000;
	}

	public LeaseTypeEnum getLeaseType() {
		return leaseType;
	}

	public void setLeaseType(LeaseTypeEnum leaseType) {
		this.leaseType = leaseType;
	}

	public byte[] getIpByte() {
		return ipByte;
	}

	public void setIpByte(byte[] ipByte) {
		this.ipByte = ipByte;
	}

	public void setLeaseTime(int leaseTime) {
		this.leaseTime = leaseTime;
	}

	public void setIpString(String ipString) {
		this.ipString = ipString;
	}

	@Override
	public String toString() {
		return getIpString() + ", leased until: "
		        + DateTimeUtils.toStrDefault(currentStartTime + leaseTime) +  "Lease [lease_time=" + leaseTime + ", ipPoolId=" + ipPoolId
		        + ", leaseType=" + leaseType + ", ip_byte="
		        + Arrays.toString(ipByte) + ", ip_string=" + ipString
		        + ", currentStartTime=" + currentStartTime + ", startTime="
		        + startTime + ", opt60=" + opt60 + ", opt61=" + opt61
		        + ", opt82=" + opt82 + ", mac=" + mac + ", clientHostName="
		        + clientHostName + ", netmask=" + netmask + ", gateway="
		        + gateway + ", dns=" + dns + "]";
	}

}