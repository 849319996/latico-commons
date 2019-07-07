package com.latico.commons.net.dhcp.server.bean;


import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.Ipv4Utils;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * <PRE>
 * 某个IP段信息
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年7月15日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class IpPool {
	private static final Logger logger = LoggerFactory.getLogger(IpPool.class.getName().toLowerCase());

	/** id 该池的唯一ID，报文的giaddr字段,默认是0.0.0.0，也就是没有DHCP Relay的情况 */
	private String id = "0.0.0.0";

	/** domainName 区域名称 */
	private String domainName;

	/** message 消息 */
	private String message;

	/**
	 * 租期时间
	 */
	private int leaseTime;

	/**
	 * The first ip adress in this range
	 */
	private int[] ipStart;

	/**
	 * The last ip adress in this range
	 */
	private int[] ipEnd;

	private byte[] firstIp;

	/** serverIp 服务器IP，注意，假如是多网卡，或者多子接口的情况下，每个子网连接的服务器的IP就是所属的端口IP */
	private String serverIp;

	private String netmask;

	private String gateway;

	private String dnsServer;

	/** staticIps Key是物理地址 */
	private Map<String, String> staticIps = new ConcurrentHashMap<String, String>();

	/** leases Key是MAC地址,注意，涉及到这个对象所有操作都要加对象锁 */
	private Map<String, Lease> leases = new ConcurrentHashMap<String, Lease>();

	private Set<String> leasedIps = new ConcurrentSkipListSet<String>();

	/**
	 * Creates a new ip adress range between the given adresses. (including)
	 *
	 * @param from the first ip in this range
	 * @param till the last ip in this range
	 */
	public IpPool(byte[] from, byte[] till) {
		ipStart = Ipv4Utils.byteArrToIntArr(from);
		ipEnd = Ipv4Utils.byteArrToIntArr(till);

		firstIp = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public IpPool(String from, String end, String netmask, String serverIp){
		this(Ipv4Utils.ipToByte(from), Ipv4Utils.ipToByte(end), netmask, null, null, serverIp);
	}

	public IpPool(byte[] from, byte[] till, String netmask, String gateway, String dns_server, String serverIp) {
		this(from, till);

		this.netmask = Ipv4Utils.formatNetmaskToIpType(netmask);
		this.gateway = gateway;
		this.dnsServer = dns_server;
		this.serverIp = serverIp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getFirstIp() {
		return firstIp;
	}

	public boolean isStaticIp(byte ip[]) {
		String ipStr = Ipv4Utils.byteToIp(ip);

		return isStaticIp(ipStr);
	}

	/**
	 * Checks whether the given ip adress is in this range.
	 *
	 * @param ip the ip adress to check
	 * @return is the given ip adress in this range?
	 */
	public boolean inRange(byte[] ip) {
		return Ipv4Utils.inRange(Ipv4Utils.byteArrToIntArr(ip), this.ipStart, this.ipEnd);
	}

	public boolean inRange(String ip) {
		try {
			return inRange(Ipv4Utils.ip2Byte(ip));
		} catch (UnknownHostException e) {
			e.printStackTrace(System.err);
			return false;
		}
	}

	/**
	 * Returns the next ip adress in this range, starting from the given
	 * ip adress.
	 *
	 * @param ip the ip adress to start from.
	 * @return the next ip adress in this range.
	 */
	public byte[] nextIp(byte[] ip, boolean isExcludeStaticIp) {
		byte tempIp[] = Ipv4Utils.nextIp(ip);

		if (isExcludeStaticIp) {
			while (this.isStaticIp(tempIp)) {
				tempIp = Ipv4Utils.nextIp(tempIp);
				if (!inRange(tempIp)) {
					tempIp = new byte[]{0, 0, 0, 0};
				}
			}
		}

		return tempIp;
	}

	public int[] getIpStart() {
		return ipStart;
	}

	public void setIpStart(int[] ipStart) {
		this.ipStart = ipStart;
	}

	public int[] getIpEnd() {
		return ipEnd;
	}

	public void setIpEnd(int[] ipEnd) {
		this.ipEnd = ipEnd;
	}

	public void setFirstIp(byte[] firstIp) {
		this.firstIp = firstIp;
	}

	public Map<String, String> getStaticIps() {
		return staticIps;
	}

	public String getStaticIp(String mac){
		return staticIps.get(convertMacCase(mac));
	}
	public boolean isStaticMac(String mac){
		return staticIps.containsKey(convertMacCase(mac));
	}
	public boolean isStaticIp(String ip){
		return staticIps.containsValue(ip);
	}
	public void addStaticIp(String mac, String ip) {
		this.staticIps.put(convertMacCase(mac), ip);
	}

	public synchronized Map<String, Lease> getLeases() {
		return leases;
	}

	public synchronized void setLeases(Map<String, Lease> leases) {
		this.leases = leases;
	}
	public synchronized boolean isLeaseMac(String mac){
		return leases.containsKey(mac);
	}

	public synchronized Lease getLeaseByMac(String mac){
		if(mac == null){
			return null;
		}
		return this.leases.get(mac);
	}
	public boolean isLeaseIp(String ip){
		return leasedIps.contains(ip);
	}
	/**
	 * 统一的MAC地址大小写管理
	 * @param mac
	 * @return
	 */
	public static String convertMacCase(String mac){
		return mac == null ? null : mac.toUpperCase();
	}

	public void setStaticIps(Map<String, String> staticIps) {
		this.staticIps = staticIps;
	}

	public synchronized void addLease(Lease lease){
		if(lease == null){
			return;
		}

		//先移除旧的数据(防止某些MAC的以前被分配的IP跟本次的不一样而导致垃圾数据)
		if(leases.containsKey(lease.getMac())){
			leasedIps.remove(leases.remove(lease.getMac()).getIpString());
		}

		leases.put(lease.getMac(), lease);
		leasedIps.add(lease.getIpString());
	}

	public synchronized Lease deleteLease(String mac){
		Lease lease = null;
		if(mac != null){
			if(leases.containsKey(mac)){
				lease = leases.remove(mac);
				leasedIps.remove(lease.getIpString());
			}
		}
		return lease;
	}

	public synchronized void deleteLease(Lease lease){
		if(lease != null){
			if(leases.containsKey(lease.getMac())){
				leasedIps.remove(leases.remove(lease.getMac()).getIpString());
			}
			leasedIps.remove(lease.getIpString());
		}
	}

	/**
	 * 获取一个动态IP（非静态、非已被用）
	 * @return
	 */
	public byte[] getDynamicIp() {
		byte[] ip = firstIp;
		while(true){

			//如果被用了，就下一个
			if(isAlreadyUsedIp(ip)){
				ip = Ipv4Utils.nextIp(ip);
			}else{
				break;
			}
		}

		//最后判断是否是有效的
		if(inRange(ip)){
			return ip;
		}else{
			logger.error("IP Pool(Pool ID:" + id + ") already use full");
			return new byte[]{0, 0, 0, 0};
		}
	}

	public String getDynamicIpStr() {
		byte[] ip = getDynamicIp();
		if(ip == null){
			return null;
		}

		return Ipv4Utils.byteToIp(ip);
	}

	public boolean isAlreadyUsedIp(byte[] ip){
		return isAlreadyUsedIp(Ipv4Utils.byteToIp(ip));
	}

	/**
	 * IP是否被使用
	 * @param ip
	 * @return
	 */
	public boolean isAlreadyUsedIp(String ip){
		if(staticIps.containsValue(ip) || leasedIps.contains(ip)){
			return true;
		}else{
			return false;
		}
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
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

	public String getDnsServer() {
		return dnsServer;
	}

	public void setDnsServer(String dnsServer) {
		this.dnsServer = dnsServer;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("IpPool{");
		sb.append("id='").append(id).append('\'');
		sb.append(", domainName='").append(domainName).append('\'');
		sb.append(", message='").append(message).append('\'');
		sb.append(", leaseTime='").append(leaseTime).append('\'');
		sb.append(", ipStart=").append(Arrays.toString(ipStart));
		sb.append(", ipEnd=").append(Arrays.toString(ipEnd));
		sb.append(", firstIp=").append(Arrays.toString(firstIp));
		sb.append(", serverIp='").append(serverIp).append('\'');
		sb.append(", netmask='").append(netmask).append('\'');
		sb.append(", gateway='").append(gateway).append('\'');
		sb.append(", dnsServer='").append(dnsServer).append('\'');
		sb.append(", staticIps=").append(staticIps);
		sb.append(", leases=").append(leases);
		sb.append(", leasedIps=").append(leasedIps);
		sb.append('}');
		return sb.toString();
	}

	public int getLeaseTime() {
		return leaseTime;
	}

	public void setLeaseTime(int leaseTime) {
		this.leaseTime = leaseTime;
	}

	public Set<String> getLeasedIps() {
		return leasedIps;
	}

	public void setLeasedIps(Set<String> leasedIps) {
		this.leasedIps = leasedIps;
	}
}
