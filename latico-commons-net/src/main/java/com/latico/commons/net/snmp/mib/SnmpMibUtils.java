package com.latico.commons.net.snmp.mib;

import com.adventnet.snmp.mibs.MibNode;
import com.adventnet.snmp.mibs.MibOperations;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibValueSymbol;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 * 1、支持mibble加载mib；
 * 2、支持adventnet加载mib的方式，
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月10日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class SnmpMibUtils {
	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(SnmpMibUtils.class);
	private static final Map<String, Mib> mibs = new HashMap<String, Mib>();
	public static final String userMibDir = "./mibs/";
	private static final MibOperations mibOps = new MibOperations();
	private static boolean isLoadMibbleMode = false;//是否加载mibble模式
	static {
		initMibs();
	}

	/**
	 * 初始化mibs目录
	 */
	private static void initMibs() {
		
		/* mibble方式加载mib */
		LOG.info("AdventnetSnmpClient开始加载mib库目录：{}", userMibDir);
		File dir = new File(userMibDir);
		if(!dir.exists()){
			return;
		}
		StringBuffer sb = new StringBuffer();
		MibLoader ml = new MibLoader();
		ml.addDir(dir);
		File[] mibFiles = dir.listFiles();
		
		for(File mibFile : mibFiles){
			if(mibFile.isDirectory()){
				continue;
			}
			try {
				sb.append(mibFile).append(" ");
				
				if(isLoadMibbleMode){
					LOG.info("Mibble方式加载mib库");
					Mib mib = ml.load(mibFile);
					mibs.put(mibFile.getName(), mib);
				}
			} catch (Exception e) {
				LOG.error("", e);
			}
		}
		
		/* adventnet方式加载mib */
		try {
			LOG.info("adventnet方式加载mib库");
			mibOps.loadMibModules(sb.toString());
		} catch (Exception e) {
			LOG.error("adventnet方式加载mib异常", e);
		}
	}
	
	/**
	 * 建议使用此方式进行获取
	 * @param oid
	 * @return
	 */
	public static String getNameByOidFromAdventnet(String oid){
		String name = null;
		MibNode mibNode = mibOps.getMibNode(oid);
		if (mibNode != null) {
			name = mibNode.getLabel();
		} else {
			name = oid;
		}
		return name;
	}
	
	/**
	 * 获取OID对应的名称
	 * @param oid
	 * @return
	 */
	public static String getNameByOidFromMibble(String oid){
		MibValueSymbol mibValue = null;
		for(Map.Entry<String, Mib> mib : mibs.entrySet()){
			mibValue = mib.getValue().getSymbolByOid(oid);
			if(mibValue != null){
				return mibValue.getName();
			}
		}
		return oid;
	}
	
	/**
	 * 对于比较上层的节点，会存在不准确性，比如节点1.3.6.1.2.1.10，正常是transmission，而它返回frDLCIStatusChange
	 * @param oid
	 * @return
	 */
	public static MibValueSymbol getSymbolByOidFromMibble(String oid){
		MibValueSymbol mibValue = null;
		for(Map.Entry<String, Mib> mib : mibs.entrySet()){
			mibValue = mib.getValue().getSymbolByOid(oid);
			if(mibValue != null){
				break;
			}
		}
		return mibValue;
	}
	
	/**
	 * 获取mib操作对象
	 * @return
	 */
	public static MibOperations getMibOperations(){
		return mibOps;
	}
	
	public static void main(String[] args) {
//		frDLCIStatusChange
//		frErrIfIndex
//		frErrType
//		frErrData
//		frErrTime
//		------------------
//		transmission
//		frErrIfIndex
//		frErrType
//		frErrData
//		frErrTime

		System.out.println(SnmpMibUtils.getNameByOidFromMibble("1.3.6.1.2.1.10"));
		System.out.println(SnmpMibUtils.getNameByOidFromMibble("1.3.6.1.2.1.10.32.3.1.1"));
		System.out.println(SnmpMibUtils.getNameByOidFromMibble("1.3.6.1.2.1.10.32.3.1.2"));
		System.out.println(SnmpMibUtils.getNameByOidFromMibble("1.3.6.1.2.1.10.32.3.1.3"));
		System.out.println(SnmpMibUtils.getNameByOidFromMibble("1.3.6.1.2.1.10.32.3.1.4"));
		System.out.println("------------------");
		System.out.println(SnmpMibUtils.getNameByOidFromAdventnet("1.3.6.1.2.1.10"));
		System.out.println(SnmpMibUtils.getNameByOidFromAdventnet("1.3.6.1.2.1.10.32.3.1.1"));
		System.out.println(SnmpMibUtils.getNameByOidFromAdventnet("1.3.6.1.2.1.10.32.3.1.2"));
		System.out.println(SnmpMibUtils.getNameByOidFromAdventnet("1.3.6.1.2.1.10.32.3.1.3"));
		System.out.println(SnmpMibUtils.getNameByOidFromAdventnet("1.3.6.1.2.1.10.32.3.1.4"));
//		MibValueSymbol mibValue = SnmpMibUtils.getNameByOid("1.3.6.1.2.1.10");
//		System.out.println(mibValue.getType().getTag().getCategory());
//		System.out.println(mibValue.getType().getTag().getValue());
//		System.out.println(mibValue.getType().getTag().getNext());
	}
}
