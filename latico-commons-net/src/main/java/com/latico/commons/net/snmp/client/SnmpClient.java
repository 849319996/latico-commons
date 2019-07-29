package com.latico.commons.net.snmp.client;


import com.latico.commons.net.snmp.bean.SnmpRow;
import com.latico.commons.net.snmp.bean.SnmpTable;
import com.latico.commons.net.snmp.enums.VersionEnum;

import java.util.List;
import java.util.Map;


/**
 * <PRE>
 * SNMP接口使用类
 *
 SnmpClient snmpClient = SnmpClientFactory.getSnmp4jClient();
 snmpClient.init("172.168.7.25", "public");
 System.out.println(snmpClient.getSnmpTable("1.3.6.1.2.1.4.22.1", "2"));
 snmpClient.close();

 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月28日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public interface SnmpClient {

	/** sysObjectIdOID SNMP系统公有节点OID */
	public static final String sysObjectIdOID = ".1.3.6.1.2.1.1.2.0";
	
	/**
	 * <PRE>
	 * 授权协议类型，参考开源包的SnmpTarget.java
	 * </PRE>
	 * <B>项	       目：</B>
	 * <B>技术支持：</B>
	 * @version   <B>V1.0 2017年3月28日</B>
	 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @since     <B>JDK1.6</B>
	 */
	public static enum AuthProtocolEnum {
		SHA_AUTH, MD5_AUTH, NO_AUTH
	};

	/**
	 * 其它参数使用默认
	 * @param ip
	 * @return
	 */
	public boolean init(String ip);

	/**
	 * 通过
	 * @param ip
	 * @param timeout
	 * @return
	 */
	public boolean init(String ip, int timeout);

	/**
	 * 使用默认的参数进行SNMP初始化，
	 * 其它参数使用默认
	 * @param ip 目标IP
	 * @param readCommunity 读团体名
	 * @return
	 */
	public boolean init(String ip, String readCommunity);

	/**
	 * 带上超时时间的初始化
	 * @param ip
	 * @param readCommunity
	 * @param timeout
	 * @return
	 */
	public boolean init(String ip, String readCommunity, int timeout);
	
	/**
	 * 
	 * @param ip
	 * @param readCommunity
	 * @param version
	 * @return
	 */
	public boolean init(String ip, String readCommunity, VersionEnum version);

	/**
	 * 初始化
	 * @param ip
	 * @param readCommunity
	 * @param version
	 * @param timeout
	 * @return
	 */
	public boolean init(String ip, String readCommunity, VersionEnum version, int timeout);

	/**
	 * 初始化
	 * @param ip
	 * @param port
	 * @param readCommunity
	 * @param version
	 * @param timeout
	 * @return
	 */
	public boolean init(String ip, int port, String readCommunity, VersionEnum version, int timeout);

	/**
	 * 初始化
	 * @param ip
	 * @param readCommunity
	 * @param version
	 * @return
	 */
	public boolean init(String ip, String readCommunity, String version);

	/**
	 * 初始化
	 * @param ip
	 * @param readCommunity
	 * @param version
	 * @param timeout
	 * @return
	 */
	public boolean init(String ip, String readCommunity, String version, int timeout);

	/**
	 * 初始化
	 * @param ip
	 * @param port
	 * @param readCommunity
	 * @param version
	 * @param timeout
	 * @return
	 */
	public boolean init(String ip, int port, String readCommunity, String version, int timeout);

	/**
	 * 全参数SNMP初始化方法
	 * @param ip 目标IP
	 * @param getPort 获取数据时使用的端口，如果小于1就使用默认值161
	 * @param trapPort Trap时使用的端口，如果小于1就使用默认值162
	 * @param readCommunity 读取数据的团体名
	 * @param writeCommunity 写数据的团体名
	 * @param timeout 超时时间
	 * @param version 版本枚举，在接口类ISnmp.java中
	 * @param username V3版本的用户名
	 * @param authProtocol V3版本的授权协议
	 * @param authPassword V3版本的授权密码
	 * @param privPassword V3版本的私有密码
	 * @param contextName V3版本的上下文名称
	 * @param contextID V3版本的上下文ID
	 * @return true初始化成功
	 */
	public boolean init(String ip, int getPort, int trapPort,
                        String readCommunity,
                        String writeCommunity, int timeout, VersionEnum version,
                        String username, AuthProtocolEnum authProtocol, String authPassword,
                        String privPassword, String contextName, String contextID);
	
	public boolean init(String ip, int getPort, int trapPort,
                        String readCommunity,
                        String writeCommunity, int timeout, String version,
                        String username, AuthProtocolEnum authProtocol, String authPassword,
                        String privPassword, String contextName, String contextID);

	/**
	 * SNMP get单个OID操作
	 * @param oid
	 * @return OID对应的值
	 */
	public Map<String, String> get(String oid);
	
	/**
	 * get一个值
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param oid
	 * @return
	 */
	public String getValue(String oid);

	/**
	 * get操作，获取一个详情对象
	 * @param oid
	 * @return
	 */
	public SnmpRow getDetail(String oid);

	/**
	 * SNMP get多个OID操作
	 * @param oids
	 * @return key为OID，value为对应的值
	 */
	public Map<String, String> get(List<String> oids);

	/**
	 * get一个OID的下一个节点的值
	 * @param oid
	 * @return key为OID，value为对应的值
	 */
	public Map<String, String> getNext(String oid);

	/**
	 * get多个个OID的下一个节点的值
	 * @param oids
	 * @return key为OID，value为对应的值
	 */
	public Map<String, String> getNext(List<String> oids);

	/**
	 * 同步walk一个节点
	 * @param oid
	 * @return key为OID，value为对应的值
	 */
	public Map<String, String> walk(String oid);

	/**
	 * SNMP walk 详细信息
	 * @param oid
	 * @return
	 */
	public List<SnmpRow> walkDetail(String oid);

	/**
	 * 异步walk一个节点
	 * @param oid 
	 * @param waitTime 异步等待时间，单位秒
	 * @return key为OID，value为对应的值
	 */
	public Map<String, String> asyncWalk(String oid, int waitTime);

	/**
	 * 一个table，
	 * Snmp返回某个表的多列值，如ifTable中 ifIndex,ifDescr,ifType 等,
	 * key为一条记录索引（索引是真实OID从前面截掉传入的采集OID部分），value为一条记录传入列OID对应的值。
	 * @param columnOids table的列的OID
	 * @return key为每一行的索引，value为一行中所有列数据的集合
	 */
	public Map<String, Map<String, String>> getTable(List<String> columnOids);
	
	/**
	 * 获取对象形式的SNMP Table
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param tableOid
	 * @param columnIndexs
	 * @return
	 */
	public SnmpTable getSnmpTable(String tableOid, List<Object> columnIndexs);

	/**
	 * 获取对象形式的SNMP Table
	 * @param tableOid 表节点OID
	 * @param columnIndexs 列节点的索引号
	 * @return
	 */
	public SnmpTable getSnmpTable(String tableOid, Object... columnIndexs);


	/**
	 * 通过walk方式进行SNMP Table采集
	 * 因为有些设备，使用getSnmpTable会采集不全，所以建议getSnmpTableByWalk，但是getSnmpTableByWalk效率会比getSnmpTable低
	 * @param tableOid
	 * @param columnIndexs
	 * @return
	 */
	public SnmpTable getSnmpTableByWalk(String tableOid, List<Object> columnIndexs);

	/**
	 * 因为有些设备，使用getSnmpTable会采集不全，所以建议getSnmpTableByWalk，但是getSnmpTableByWalk效率会比getSnmpTable低
	 * @param tableOid
	 * @param columnIndexs
	 * @return
	 */
	public SnmpTable getSnmpTableByWalk(String tableOid, Object... columnIndexs);

	/**
	 * 关闭snmp连接
	 */
	public void close();
	
	/**
	 * 获取设备系统OID，设备系统OID一般用于判断该SNMP的版本
	 * 原因：设备的型号不能唯一确定SNMP系统版本，导致虽然是同一个型号，但是SNMP结构可能不一样，但是差异不会很大，
	 * 所以使用设备系统OID作为SNMP版本的唯一确定因素。
	 * @return
	 */
	public String getSysOID();
	
	/**
	 * 测试SNMP连接
	 * @return true:成功  false:失败
	 */
	public boolean testSnmpConn();
	
	/**
	 * 获取连接信息
	 * @return
	 */
	public String getConnInfo();

	/**
	 * 获取连接状态
	 * @return true:连接成功 false:失败
	 */
	public boolean isConnectSucc();
}
