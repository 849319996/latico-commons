package com.latico.commons.net.snmp.client.impl;

import com.adventnet.snmp.beans.SnmpTarget;
import com.adventnet.snmp.snmp2.*;
import com.latico.commons.net.snmp.SnmpUtils;
import com.latico.commons.net.snmp.bean.SnmpLine;
import com.latico.commons.net.snmp.bean.SnmpRow;
import com.latico.commons.net.snmp.bean.SnmpTable;
import com.latico.commons.net.snmp.client.AbstractSnmpClient;
import com.latico.commons.net.snmp.enums.VersionEnum;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 * Adventnet 实现SNMP
 * 【注意：Adventnet的实现，OID的值必须前面带英文句号'.'，否则无法识别】
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月28日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class AdventnetSnmpClient extends AbstractSnmpClient {

	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(AdventnetSnmpClient.class);

	/**
	 * SnmpTarget对象
	 */
	private SnmpTarget target;

	@Override
	protected boolean connect() {
		return initSnmpTarget();
	}

	/**
	 * 初始化SNMP目标
	 * @return 是否初始化成功
	 */
	private boolean initSnmpTarget() {
		if (status) {
			return true;
		}
		try {
			target = new SnmpTarget();

			target.setTargetHost(ip);
			target.setTargetPort(getPort);
			target.setCommunity(readCommunity);
			target.setWriteCommunity(writeCommunity);
//			target.setTimeoutInMilliSec(timeout);//连接时的超时时间
//			target.setTimeToWait(timeout);//读取和关闭时需要等待的时间，默认是0
			target.setRetries(retries);//连接的重试次数
			target.setMaxRepetitions(0);//读取的重试次数

			if (version == VersionEnum.V1) {
				target.setSnmpVersion(SnmpTarget.VERSION1);

			} else if (version == VersionEnum.V2C || version == VersionEnum.V2) {
				target.setSnmpVersion(SnmpTarget.VERSION2C);

			} else if (version == VersionEnum.V3) {
				target.setSnmpVersion(SnmpTarget.VERSION3);
				target.setPrincipal(username);
				target.setAuthPassword(authPassword);
				target.setPrivPassword(privPassword);
				target.setContextName(contextName);
				target.setContextID(contextID);
				target.create_v3_tables();
				if (authProtocol == AuthProtocolEnum.SHA_AUTH) {
					target.setAuthProtocol(SnmpTarget.SHA_AUTH);

				} else if (authProtocol == AuthProtocolEnum.MD5_AUTH) {
					target.setAuthProtocol(SnmpTarget.MD5_AUTH);

				} else {
					target.setAuthProtocol(SnmpTarget.NO_AUTH);
				}
			}
			
			//加载私有mib库
//			LOG.info("AdventnetSnmpClient开始加载mib库：./mibs/*");
//			target.setMibOperations(SnmpMibUtils.getMibOperations());
			status = true;
			status = testSnmpConn();
			if(status){
				LOG.info("测试{}", getConnInfo());
			}else{
				LOG.error("测试{}", getConnInfo());
			}
		} catch (Exception e) {
			status = false;
			LOG.error("初始化SNMP对象失败", e);
		}
		return status;
	}

	@Override
	public void close() {
		if (target != null) {
			try {
				target.releaseResources();
			} catch (Exception e) {
			}
		}
		status = false;
	}

	@Override
	public Map<String, String> get(String oid) {
		if (!status) {
			return null;
		}
		oid = SnmpUtils.formatterOid(oid);
		Map<String, String> strTemp = null;
		String[] oids = new String[]{oid};
		target.setObjectIDList(oids);
		SnmpVarBind result = target.snmpGetVariableBinding(); 
		if (result == null) {
			strTemp = null;
		} else {
			strTemp = new LinkedHashMap<String, String>();
			strTemp.put(result.getObjectID().toString(), snmpVarSwith(result.getVariable()));
		}

		return strTemp;
	}

	/**
	 * get操作，获取一个详情对象
	 *
	 * @param oid
	 * @return
	 */
	@Override
	public SnmpRow getDetail(String oid) {
		if (!status) {
			return null;
		}
		oid = SnmpUtils.formatterOid(oid);
		SnmpRow snmpRow = new SnmpRow();
		String[] oids = new String[]{oid};
		target.setObjectIDList(oids);
		SnmpVarBind result = target.snmpGetVariableBinding();
		if (result == null) {
			snmpRow = null;
		} else {
			snmpRow.setOid(oid);
			snmpRow.setTypeInt(result.getVariable().getType());
			snmpRow.setTypeStr(result.getVariable().getTypeString());
			snmpRow.setValue(snmpVarSwith(result.getVariable()));
		}

		return snmpRow;
	}

	@Override
	public Map<String, String> getNext(String oid) {
		if (!status) {
			return null;
		}
		oid = SnmpUtils.formatterOid(oid);
		Map<String, String> strTemp = null;
		String[] oids = new String[]{oid};
		target.setObjectIDList(oids);
		SnmpVarBind result = target.snmpGetNextVariableBinding();
		if (result == null) {
			strTemp = null;
		} else {
			strTemp = new LinkedHashMap<String, String>();
			strTemp.put(SnmpUtils.getOidLastIndex(oid, result.getObjectID().toString()), snmpVarSwith(result.getVariable()));
		}
		
		return strTemp;
	}

	@Override
	public Map<String, String> get(List<String> oidList) {
		if (!status) {
			return null;
		}
		Map<String, String> valueList = null;
		String[] oids = new String[oidList.size()];
		for (int i = 0; i < oids.length; i++) {
			oids[i] = SnmpUtils.formatterOid(oidList.get(i));
		}
		target.setObjectIDList(oids);

		SnmpVarBind[] result = target.snmpGetVariableBindings();
		if (result == null) {
			LOG.error(target.getErrorString());
		} else {
			valueList = new LinkedHashMap<String, String>();
			String[] oidArr = target.getObjectIDList();
			for (int i = 0; i < result.length; i++) {
				valueList.put(oidArr[i], snmpVarSwith(result[i].getVariable()));
			}
		}

		return valueList;
	}
	
	@Override
	public Map<String, String> getNext(List<String> oidList) {
		if (!status) {
			return null;
		}
		Map<String, String> valueList = null;
		String[] oids = new String[oidList.size()];
		for (int i = 0; i < oids.length; i++) {
			oids[i] = SnmpUtils.formatterOid(oidList.get(i));
		}
		target.setObjectIDList(oids);
		
		SnmpVarBind[] result = target.snmpGetNextVariableBindings();
		if (result == null) {
			LOG.error(target.getErrorString());
		} else {
			valueList = new LinkedHashMap<String, String>();
			String[] oidArr = target.getObjectIDList();
			for (int i = 0; i < result.length; i++) {
				valueList.put(SnmpUtils.getOidLastIndex(oidList.get(i), oidArr[i]), snmpVarSwith(result[i].getVariable()));
			}
		}
		
		return valueList;
	}

	/**
	 * 获取已经加载的mib库的名称集合
	 * @return 
	 */
	public String getMibModules(){
		return target.getMibModules();
	}
	
	@Override
	public Map<String, String> walk(String oid) {
		if (!status) {
			return null;
		}
		oid = SnmpUtils.formatterOid(oid);
		Map<String, String> valueMap = new LinkedHashMap<String, String>();
		target.setObjectID(oid);
		int tryCount = 0;
		SnmpOID[] snmpOidList = target.getSnmpOIDList();
		SnmpOID rootoid = snmpOidList[0];
		String[] oids = null;
		SnmpVarBind[] result = null;
		String lastOid = oid;
		boolean finish = false;
		while (tryCount++ <= walkMaxLines) {
			result = target.snmpGetNextVariableBindings();
			if (result == null)
				break;

			// walk出了子树就跳出循环
			if (!SnmpTarget.isInSubTree(rootoid, target.getSnmpOID())) {
				break;
			}

			oids = target.getObjectIDList();
			for (int i = 0; i < result.length; i++) {
				String currentOid = SnmpUtils.formatterOid(oids[i]);
				if(StringUtils.equals(lastOid, currentOid)){
					finish = true;
					LOG.info("{}:遍历到相同OID,终止遍历, OID:{}", ip, currentOid);
					break;
				}
				lastOid = currentOid;

				valueMap.put(SnmpUtils.getOidLastIndex(oid, oids[i]), snmpVarSwith(result[i].getVariable()));
			}

			if (finish) {
				break;
			}
		}

		// 数据错误
		if (tryCount == 1 && !target.getErrorString().equalsIgnoreCase("No error")) {
			LOG.error("{}:{}", ip, target.getErrorString());
		}

		return valueMap;
	}

	/**
	 * SNMP walk 详细信息
	 *
	 * @param oid
	 * @return
	 */
	@Override
	public List<SnmpRow> walkDetail(String oid) {
		if (!status) {
			return null;
		}
		List<SnmpRow> snmpRows = new ArrayList<>();
		oid = SnmpUtils.formatterOid(oid);
		target.setObjectID(oid);
		int tryCount = 0;
		SnmpOID[] snmpOidList = target.getSnmpOIDList();
		SnmpOID rootoid = snmpOidList[0];
		String[] oids = null;
		SnmpVarBind[] result = null;
		String lastOid = oid;
		boolean finish = false;
		while (tryCount++ <= walkMaxLines) {
			result = target.snmpGetNextVariableBindings();
			if (result == null){
				break;
			}

			// walk出了子树就跳出循环
			if (!SnmpTarget.isInSubTree(rootoid, target.getSnmpOID())) {
				break;
			}

			oids = target.getObjectIDList();
			for (int i = 0; i < result.length; i++) {
				String currentOid = SnmpUtils.formatterOid(oids[i]);
				if(StringUtils.equals(lastOid, currentOid)){
					finish = true;
					LOG.info("{}:遍历到相同OID,终止遍历, OID:{}", ip, currentOid);
					break;
				}
				lastOid = currentOid;

				SnmpRow snmpRow = new SnmpRow();
				snmpRow.setOid(oids[i]);
				snmpRow.setTypeInt(result[i].getVariable().getType());
				snmpRow.setTypeStr(result[i].getVariable().getTypeString());
				snmpRow.setValue(snmpVarSwith(result[i].getVariable()));
				snmpRows.add(snmpRow);
			}
			if (finish) {
				break;
			}
		}

		// 数据错误
		if (tryCount == 1 && !target.getErrorString().equalsIgnoreCase("No error")) {
			LOG.error(target.getErrorString());
		}

		return snmpRows;
	}

	public Map<String, String> walkOriginal(String oid) {
	    if (!status) {
	        return null;
	    }
	    oid = SnmpUtils.formatterOid(oid);
	    Map<String, String> valueMap = new LinkedHashMap<String, String>();
	    target.setObjectID(oid);
	    int tryCount = 0;
	    SnmpOID[] snmpOidList = target.getSnmpOIDList();
	    SnmpOID rootoid = snmpOidList[0];
	    String[] oids = null;
	    SnmpVarBind[] result = null;
	    while (tryCount++ <= walkMaxLines) {
	        result = target.snmpGetNextVariableBindings();
	        if (result == null)
	            break;
	        
	        // walk出了子树就跳出循环
	        if (!SnmpTarget.isInSubTree(rootoid, target.getSnmpOID())) {
	            break;
	        }
	        
	        oids = target.getObjectIDList();
	        for (int i = 0; i < result.length; i++) {
	            valueMap.put(oids[i], snmpVarSwith(result[i].getVariable()));
	        }
	    }
	    
	    // 数据错误
	    if (tryCount == 1 && !target.getErrorString().equalsIgnoreCase("No error")) {
	        LOG.error(target.getErrorString());
	    }
	    
	    return valueMap;
	}

	@Override
	public Map<String, Map<String, String>> getTable(List<String> colOidList) {
		if (colOidList == null || colOidList.size() == 0)
			return null;
		// 设备节点信息
		List<Map<String, String>> valueMaps = new ArrayList<Map<String, String>>();
		int colOidSize = colOidList.size();
		String oid = null;
		Map<String, String> valueMap = null;
		for (int i = 0; i < colOidSize; i++) {
			oid = colOidList.get(i).toString();
			valueMap = walk(oid);
			if (valueMap == null) {
				LOG.error("{}:getTable时walk节点 [{}] 获取不到数据", ip, oid);
				valueMap = new LinkedHashMap<String, String>();
			}
			valueMaps.add(valueMap);
		}

		Map<String, Map<String, String>> oidColumsMap = new LinkedHashMap<String, Map<String, String>>();
		String colOid = null;
		for (int i = 0; i < colOidSize; i++) {
			valueMap = valueMaps.get(i);
			colOid = colOidList.get(i);
			oidColumsMap.put(colOid, valueMap);

		}
		return oidColumsMap;
	}

	@Override
	public Map<String, String> asyncWalk(String walkOid, int seconds) {
		return null;
		//TODO
	}
	
	/**
	 * 判断是否为IP地址
	 * @param macBytes
	 * @return
	 */
	protected static boolean isIpAddress(byte[] macBytes) {
		if (macBytes != null && macBytes.length == 4) {
			try {
				String value = "";
				value = bytes2HexString(macBytes);
				value = hexStringSwitchMac(value);
				value = value.replaceAll(":", ".");
				String regex = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(value);
				return m.matches();
			} catch (Exception e) {
				e.printStackTrace(System.err);
				return false;
			}
		}
		return false;

	}

	/**
	 * 十六进制数据转MAC地址 
	 * @param macStr
	 * @return
	 */
	protected static String hexStringSwitchMac(String macStr) {
		String ip = "";
		try {
			if (macStr.indexOf(":") > 0) {
				String tempStr = "";
				String[] tempStrs = macStr.split(":");
				String str = null;
				for (int i = 0; i < tempStrs.length; i++) {
					str = tempStrs[i];
					tempStr = tempStr + "." + Integer.parseInt(str, 16);
				}
				ip = tempStr.substring(1);
			} else if (macStr.indexOf("-") > 0) {
				String tempStr = "";
				String[] tempStrs = macStr.split("-");
				String str = null;
				for (int i = 0; i < tempStrs.length; i++) {
					str = tempStrs[i];
					tempStr = tempStr + "." + Integer.parseInt(str, 16);
				}
				ip = tempStr.substring(1);
			} else if (macStr.indexOf(" ") > 0) {
				String tempStr = "";
				String[] tempStrs = macStr.split(" ");
				for (int i = 0; i < tempStrs.length; i++) {
					String str = tempStrs[i];
					tempStr = tempStr + "." + Integer.parseInt(str, 16);
				}
				ip = tempStr.substring(1);
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return ip;
	}
	
	/**
	 * 解析SnmpVar对象 
	 * @param snmpVar
	 * @return
	 */
	private static String snmpVarSwith(SnmpVar snmpVar) {
		String strTemp = "";
		if (snmpVar instanceof SnmpInt) {
			SnmpInt snmpInt = (SnmpInt) snmpVar;
			strTemp = snmpInt.toString();

		} else if (snmpVar instanceof SnmpIpAddress) {
			SnmpIpAddress snmpIp = (SnmpIpAddress) snmpVar;
			strTemp = snmpIp.toString();

		} else if (snmpVar instanceof SnmpString) {
			SnmpString snmpStr = ((SnmpString) snmpVar);
			if (isMacAddByRegex(snmpStr.toBytes())) {
				strTemp = snmpStr.toByteString().toString().replace(" ", ":");
			} else {
				strTemp = snmpStr.toValue().toString();
			}

		} else if (snmpVar instanceof SnmpTimeticks) {
			strTemp = ((SnmpTimeticks) snmpVar).toValue().toString();
		} else if (snmpVar instanceof SnmpCounter) {
			strTemp = snmpVar.toValue().toString();
		} else if (snmpVar instanceof SnmpGauge) {
			strTemp = snmpVar.toValue().toString();
		} else if (snmpVar instanceof SnmpOID) {
			SnmpOID snmpOid = ((SnmpOID) snmpVar);
			strTemp = snmpOid.toString();
		} else if (snmpVar instanceof SnmpNull) {
			strTemp = "";
		} else if (snmpVar instanceof SnmpCounter64) {
			SnmpCounter64 snmpCounter64 = ((SnmpCounter64) snmpVar);
			strTemp = snmpCounter64.toBigInteger().toString();
		} else {
			strTemp = snmpVar.toTagString();
			LOG.error("未能解析的节点：snmpVar:" + snmpVar
			        + "snmpVar.getClass().getName():"
			        + snmpVar.getClass().getName());
		}
		return strTemp;
	}

	/**
	 * Determines whether this octet string contains non ISO control characters
	 * only.
	 * 
	 * @return <code>false</code> if this octet string contains any ISO control
	 *         characters as defined by
	 *         <code>Character.isISOControl(char)</code> except if these ISO
	 *         control characters are all whitespace characters as defined by
	 *         <code>Character.isWhitespace(char)</code>.
	 */
	protected static boolean isPrintable(byte[] value) {
		for (int i = 0; i < value.length; i++) {
			char c = (char) value[i];
			// if (
			// (Character.isISOControl(c) ||
			// ((value[i] & 0xFF) >= 0x80))
			// 不止判断大于0x80，还有包括控制字符的判断（0-31,127-159）
			// (Character.isISOControl(c) || (c >
			// 0x0000 && c <= 0x001F) || (c >=
			// 0x007F && c <= 0x009F))
			// && (!Character.isWhitespace(c))) {
			// return false;
			// }

			if (Character.isISOControl(c) || Character.isWhitespace(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否为MAC地址
	 * 
	 * @param macBytes
	 * @return
	 */
	protected static boolean isMacAddByRegex(byte[] macBytes) {
		if (macBytes != null && macBytes.length == 6) {
			try {
				String value = "";
				value = bytes2HexString(macBytes);
				String regex = "^[A-Fa-f0-9]{2}\\:[A-Fa-f0-9]{2}\\:[A-Fa-f0-9]{2}\\:[A-Fa-f0-9]{2}\\:[A-Fa-f0-9]{2}\\:[A-Fa-f0-9]{2}$";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(value);
				return m.matches();
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 把IP地址转换成MAC地址形式
	 * @param ip
	 * @return
	 */
	protected static String IpSwitch2Mac(String ip) {
		StringBuilder mac = new StringBuilder();
		if (ip != null) {
			try {
				String[] arr = ip.split("\\.");
				for (String s : arr) {
					s = Integer.toHexString(Integer.parseInt(s));
					if (s.length() == 1) {
						mac.append("0");
					}
					mac.append(s);
				}
			} catch (Exception e) {
				LOG.error("{},IP格式结果转成MAC出现异常", e, ip);
			}
		}
		return mac.toString().toUpperCase();
	}

	protected static String bytes2HexString(byte[] byteValue) {
		return bytes2HexString(byteValue, true);
	}

	/**
	 * 字节转成十六位进制字符
	 * 
	 * @param byteValue
	 * @param zeroFlag
	 * @return
	 */
	protected static String bytes2HexString(byte[] byteValue, boolean zeroFlag) {
		StringBuilder sb = new StringBuilder(byteValue.length * 3);
		for (int i = 0; i < byteValue.length; i++) {
			String s = Integer.toHexString(byteValue[i] & 0xFF);
			if (zeroFlag) {
				sb.append(s.length() != 1 ? s : "0" + s);
			} else {
				sb.append(s);
			}
			sb.append(" ");
		}
		return sb.toString().trim().toUpperCase()
		        .replaceAll(" ", ":");
	}

	@Override
	public SnmpTable getSnmpTable(String tableOid, List<Object> columnIndexs) {
		return getSnmpTableByWalk(tableOid, columnIndexs);
	}

}
