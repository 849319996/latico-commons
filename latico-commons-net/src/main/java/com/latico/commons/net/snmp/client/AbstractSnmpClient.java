package com.latico.commons.net.snmp.client;


import com.latico.commons.net.snmp.bean.SnmpTable;
import com.latico.commons.net.snmp.enums.VersionEnum;
import com.latico.commons.common.util.string.StringUtils;

import java.util.Arrays;
import java.util.Map;


/**
 * <PRE>
 * 
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月28日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public abstract class AbstractSnmpClient implements SnmpClient {
	
	/** status 连接状态 */
	protected boolean status;
	
	/**
	 * SNMP的IP地址
	 */
	protected String ip;

	/**
	 * Snmp主动获取数据端口，get和walk等的端口，默认为161
	 */
	protected int getPort = 161;
	
	/** trapPort trap被动接收数据时使用的端口，默认是162 */
	protected int trapPort = 162;

	/**
	 * 读权限的团体名，默认为public
	 */
	protected String readCommunity = "public";

	/**
	 * 写权限的团体名，默认为public
	 */
	protected String writeCommunity = "public";

	/**
	 * 版本，默认为v2c
	 */
	protected VersionEnum version = VersionEnum.V2C;

	/**
	 * 超时时间，默认为5秒
	 */
	protected int timeout = 30000;
	
	/** 通信不成功时的重连次数，默认1 */
	protected int retries = 1;
	
	/**
	 * V3版本账户
	 */
	protected String username;

	/**
	 * V3版本授权协议
	 */
	protected AuthProtocolEnum authProtocol = AuthProtocolEnum.NO_AUTH;

	/**
	 * V3版本授权密码
	 */
	protected String authPassword;

	/**
	 * V3版本的私有密码
	 */
	protected String privPassword;

	/**
	 * V3版本的上下文名称
	 */
	protected String contextName;

	/**
	 * V3版本的上下文ID
	 */
	protected String contextID;
	
	/** walkMaxLines 使用walk的时候读取的最大行数（防止数据溢出） */
	protected int walkMaxLines = 1000000;

	/**
	 * 设备系统OID的值
	 */
	protected String sysOid;
	
	@Override
	public boolean init(String ip) {
		return init(ip, getPort, trapPort, readCommunity, writeCommunity, timeout, version, username, authProtocol, authPassword, privPassword, contextName, contextID);
	}
	
	@Override
	public boolean init(String ip, String readCommunity) {
		return init(ip, getPort, trapPort, readCommunity, writeCommunity, timeout, version, username, authProtocol, authPassword, privPassword, contextName, contextID);
	}
	
	@Override
	public boolean init(String ip, String readCommunity, VersionEnum version){
		return init(ip, getPort, trapPort, readCommunity, writeCommunity, timeout, version, username, authProtocol, authPassword, privPassword, contextName, contextID);
	}
	
	@Override
	public boolean init(String ip, int getPort, int trapPort,
	        String readCommunity, String writeCommunity, int timeout,
	        VersionEnum version, String username, AuthProtocolEnum authProtocol,
	        String authPassword, String privPassword, String contextName, String contextID) {
		this.ip = ip;
		this.getPort = getPort;
		this.trapPort = trapPort;
		if(StringUtils.isNotEmpty(readCommunity)){
			this.readCommunity = readCommunity;
		}
		if(StringUtils.isNotEmpty(writeCommunity)){
			this.writeCommunity = writeCommunity;
		}
		
		if(timeout >= 1000){
			this.timeout = timeout;
		}
		if(version != null){
			this.version = version;
		}
		
		this.username = username;
		this.authProtocol = authProtocol;
		this.authPassword = authPassword;
		this.privPassword = privPassword;
		this.contextName = contextName;
		this.contextID = contextID;
		
		//连接
		status = connect();
		
		return status;
	}
	
	/**
	 * 初始化的第二步,子类实现
	 * @return
	 */
	protected abstract boolean connect();
	
	@Override
	public String getValue(String oid) {
		Map<String, String> map = get(oid);
		if(map != null){
			for(Map.Entry<String, String> entry : map.entrySet()){
				return entry.getValue();
			}
		}
		return "";
	}
	
	@Override
	public String getSysOID() {
		if (sysOid == null || "".equals(sysOid)) {
			sysOid = getValue(sysObjectIdOID);
		}
		return sysOid;
	}
	
	@Override
	public boolean init(String ip, String readCommunity, String version) {
		VersionEnum ve = VersionEnum.getEnumByName(version);
		return init(ip, readCommunity, ve);
	}

	@Override
	public boolean init(String ip, int getPort, int trapPort,
	        String readCommunity, String writeCommunity, int timeout,
	        String version, String username, AuthProtocolEnum authProtocol,
	        String authPassword, String privPassword, String contextName,
	        String contextID) {
		VersionEnum ve = VersionEnum.getEnumByName(version);
		return init(ip, getPort, trapPort, readCommunity, writeCommunity, timeout, ve, username, authProtocol, authPassword, privPassword, contextName,
		        contextID);
	}
	
	@Override
	public boolean init(String ip, int timeout) {
		return init(ip, getPort, trapPort, readCommunity, writeCommunity, timeout, version, username, authProtocol, authPassword, privPassword, contextName, contextID);
	}

	@Override
	public boolean init(String ip, String readCommunity, int timeout) {
		return init(ip, getPort, trapPort, readCommunity, writeCommunity, timeout, version, username, authProtocol, authPassword, privPassword, contextName, contextID);
	}

	@Override
	public boolean init(String ip, String readCommunity, VersionEnum version,
	        int timeout) {
		return init(ip, getPort, trapPort, readCommunity, writeCommunity, timeout, version, username, authProtocol, authPassword, privPassword, contextName, contextID);
	}
	@Override
	public boolean init(String ip, int port, String readCommunity, VersionEnum version, int timeout) {
		if(port >= 1) {
			getPort = port;
		}
		return init(ip, getPort, trapPort, readCommunity, writeCommunity, timeout, version, username, authProtocol, authPassword, privPassword, contextName, contextID);
	}

	@Override
	public boolean init(String ip, String readCommunity, String version,
	        int timeout) {
		VersionEnum ve = VersionEnum.getEnumByNameOrId(version);
		return init(ip, readCommunity, ve, timeout);
	}
	
	@Override
	public boolean init(String ip, int port, String readCommunity, String version, int timeout) {
		VersionEnum ve = VersionEnum.getEnumByNameOrId(version);
		return init(ip, port, readCommunity, ve, timeout);
	}
	
	@Override
	public boolean testSnmpConn() {
		String sysOid = getSysOID();
		if(sysOid == null || sysOid.length() == 0){
			close();
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public String getConnInfo(){
		return StringUtils.join("[SNMP连接:", ip, "/", getPort, " 读写团体名:", readCommunity, "/", writeCommunity, " 版本:", version.getName(), " 状态:", status ? "连接成功":"连接失败", "]");
	}
	
    @Override
    public SnmpTable getSnmpTable(String tableOid, Object... columnIndexs) {
       if(columnIndexs != null) {
           return getSnmpTable(tableOid,  Arrays.asList(columnIndexs));
       }else {
           return null;
       }
    }

	/**
	 * 获取连接状态
	 *
	 * @return true:连接成功 false:失败
	 */
	@Override
	public boolean isConnectSucc() {
		return status;
	}
}
