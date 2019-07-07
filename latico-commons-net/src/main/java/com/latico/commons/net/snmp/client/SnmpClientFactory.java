package com.latico.commons.net.snmp.client;

import com.latico.commons.net.snmp.client.impl.AdventnetSnmpClient;
import com.latico.commons.net.snmp.client.impl.Snmp4jClient;
import com.latico.commons.net.snmp.enums.SnmpClientTypeEnum;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;


/**
 * <PRE>
 * 连接器工厂根据类型判断用哪种类型的连接器
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月20日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class SnmpClientFactory {
	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(SnmpClientFactory.class);
	
	public static SnmpClientFactory getInstance() {
		return SingletonHolder.INSTANCE;
	}
	private static class SingletonHolder {
		private static final SnmpClientFactory INSTANCE = new SnmpClientFactory();
	}
	
	/**
	 * 获取连接器
	 * @param snmpTypeEnum
	 * @return
	 */
	public static SnmpClient getSnmpClient(SnmpClientTypeEnum snmpTypeEnum){
		SnmpClient snmp = null;
		
		if(SnmpClientTypeEnum.Snmp4j == snmpTypeEnum){
			snmp = getSnmp4jClient();
			
		}else if(SnmpClientTypeEnum.Adventnet == snmpTypeEnum){
			snmp = getAdventnetSnmpClient();
		}
		
		return snmp;
	}
	/**
	 * 
	 * @return
	 */
	public static SnmpClient getAdventnetSnmpClient() {
		return new AdventnetSnmpClient();
	}
	/**
	 * 
	 * @return
	 */
	public static SnmpClient getSnmp4jClient() {
		return new Snmp4jClient();
	}
	
}
