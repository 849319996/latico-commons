package com.latico.commons.net.trap.impl;

import com.adventnet.snmp.beans.*;
import com.adventnet.snmp.snmp2.SnmpAPI;
import com.adventnet.snmp.snmp2.SnmpPDU;
import com.adventnet.snmp.snmp2.SnmpVarBind;
import com.adventnet.snmp.snmp2.usm.USMUserEntry;
import com.latico.commons.net.snmp.enums.VersionEnum;
import com.latico.commons.net.snmp.mib.SnmpMibUtils;
import com.latico.commons.net.trap.bean.TrapResult;
import com.latico.commons.net.trap.bean.VariableBind;
import com.latico.commons.net.trap.test.HandleThread;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import net.percederberg.mibble.MibLoader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * <PRE>
 * 
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月9日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class AdventnetTrapReceiver extends AbstractTrapReceiver implements TrapParserListener,TrapListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdventnetTrapReceiver.class);
	
	/**
	 * trapReceiver trap接收器
	 */
	private SnmpTrapReceiver trapReceiver = null;

	/**
	 * 获取mib文件路径组成的字符串，用空格分开
	 * @return
	 */
	private static String getMibFilePaths() {
		File dir = new File(mibsDir);
		if(!dir.exists()){
			return "";
		}
		StringBuffer mibs = new StringBuffer();
		MibLoader ml = new MibLoader();
		ml.addDir(dir);
		File[] mibFiles = dir.listFiles();
		
		for(File mibFile : mibFiles){
			if(mibFile.isDirectory()){
				continue;
			}
			mibs.append(mibFile).append(" ");
				
		}
		return mibs.toString();
	}
	
	@Override
	public boolean startListen(){
		if(status){
			LOG.info("开始启动Trap监听服务...");
			trapReceiver.addTrapListener(this);
			LOG.info("启动Trap监听服务 [{}]", status?"成功":"失败");
		}else{
			LOG.error("启动Trap服务器失败，未初始化或初始化失败");
		}
		return status;
	}
	
	@Override
	public void stopListen(){
		if(status){
			trapReceiver.removeTrapListener(this);
			LOG.info("暂停Trap监听");
		}
	}
	/**
	 * receivedTrap
	 *
	 * @param event TrapEvent
	 * @todo Implement this com.adventnet.snmp.beans.TrapListener method
	 */
	@Override
	public void receivedTrap(TrapEvent event) {
		try {
			
			TrapResult result = new TrapResult();
			result.setName(getTrapName(event));
			result.setCommunity(event.getCommunity());
			result.setDescr(getTrapDescr(event));
			result.setEnterprise(event.getEnterprise());
			result.setRemoteHost(event.getRemoteHost());
			result.setRemotePort(event.getRemotePort());
			result.setUpTime(event.getUpTime());
			result.setReceiveTime(System.currentTimeMillis());
			result.setVersion(getTrapVersion(event));
			result.setVariableBinds(getVarBinds(event));
			
			this.trapResults.add(result);
		} catch (Exception e) {
			LOG.error("处理Trap报文异常", e);
		}
	}
	
	/**
	 * 获取变量绑定的值
	 * @param event
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<VariableBind> getVarBinds(TrapEvent event)
	        throws UnsupportedEncodingException {
		SnmpPDU pdu = event.getTrapPDU();
		Vector<?> vars = pdu.getVariableBindings();
		Iterator<?> it = vars.iterator();
		List<VariableBind> variableBinds = new ArrayList<VariableBind>();
		VariableBind variableBind = null;
		SnmpVarBind snmpVarBind = null;
		while (it.hasNext()) {
			snmpVarBind = (SnmpVarBind) it.next();
			variableBind = new VariableBind();
			variableBinds.add(variableBind);
			
			variableBind.setOid(snmpVarBind.getObjectID().toString());
			variableBind.setType(getTrapVarType(snmpVarBind));
			variableBind.setName(SnmpMibUtils.getNameByOidFromAdventnet(variableBind.getOid()));
			variableBind.setValue(getSnmpVarBindValue(snmpVarBind));
			
		}
		return variableBinds;
	}
	/**
	 * 
	 * @param snmpVarBind
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getTrapVarType(SnmpVarBind snmpVarBind) throws UnsupportedEncodingException {
		if (snmpVarBind.getVariable() != null && snmpVarBind.getVariable().getTypeString() != null) {
			return new String (snmpVarBind.getVariable().getTypeString().getBytes(this.origncharset),this.convertcharset);
		}else{
			return "";
		}
	}
	/**
	 * 
	 * @param event
	 * @return
	 */
	public String getTrapVersion(TrapEvent event) {
		int version = event.getVersion();
		
		if (version == SnmpTarget.VERSION1) {
			return VersionEnum.V1.getId() + "";
		}else if (version == SnmpTarget.VERSION2C) {
			return VersionEnum.V2C.getId() + "";
		} else if (version == SnmpTarget.VERSION3) {
			return VersionEnum.V3.getId() + "";
		}else{
			return "";
		}
			
	} 
	
	/**
	 * 
	 * @param trapEvent
	 * @return
	 */
	public String getTrapDescr(TrapEvent trapEvent) {
		if (trapEvent.getTrapDefinition() != null) {
			return trapEvent.getTrapDefinition().getDescription();
		} else {
			return "";
		}
	}
	/**
	 * 
	 * @param snmpVarBind
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getSnmpVarBindValue(SnmpVarBind snmpVarBind)
	        throws UnsupportedEncodingException {
		String value;
		if (snmpVarBind.getVariable() != null && snmpVarBind.getVariable().getVarObject() != null) {
//			value = new String(snmpVarBind.toTagString().getBytes(origncharset), convertcharset);
			value =	new String (snmpVarBind.getVariable().getVarObject().toString().getBytes(this.origncharset),this.convertcharset);
		}else{
			value = "";
		}
		return value;
	}


	/**
	 * 显示TRAP中携带的变量的信息
	 * @param pdu TRAP报文PDU
	 */
	public void printBindedVarInfo(SnmpPDU pdu) {
		Vector<?> vars = pdu.getVariableBindings();
		Iterator<?> it = vars.iterator();
		while (it.hasNext()) {
			SnmpVarBind vb = (SnmpVarBind) it.next();
			try {
				String value = "";
				if (vb.getVariable() != null
						&& vb.getVariable().getVarObject() != null) {
					value = vb.getVariable().getVarObject().toString();
				}
				System.out.println("       " + "VARIABLE OID   :"
						+ new String (vb.getObjectID().toString().getBytes(this.origncharset),this.convertcharset));
				System.out.println("       " + "VARIABLE TYPE  :"
						+ getTrapVarType(vb));
				System.out.println("       " + "VARIABLE VALUE :" + new String (value.getBytes(this.origncharset),this.convertcharset));
			} catch (Exception e) {
				LOG.error("", e);
			}
		}
	}

	@Override
	public void close() {
		if(trapReceiver != null){
			trapReceiver.stop();
			LOG.info("停止Trap监听");
		}
	}
	
	@Override
	public void eventParsed(ParsedTrapEvent arg0) {
		LOG.warn(arg0.toLogString());
	}
	
	private String getTrapName(TrapEvent trap) {
		String trapname = null;
		// v1
		if (trap.getTrapPDU().getCommand() == SnmpAPI.TRP_REQ_MSG) {
			if (trap.getTrapDefinition() != null)
				trapname = trap.getTrapDefinition().getName();
			// v2
		} else if (trap.getTrapPDU().getCommand() == SnmpAPI.TRP2_REQ_MSG
				|| trap.getTrapPDU().getCommand() == SnmpAPI.INFORM_REQ_MSG) {
			if (trap.getNotificationDefinition() != null)
				trapname = trap.getNotificationDefinition().getLabel();
		}

		return trapname;
	}
	
	@Override
	protected boolean initReceiver() {
		LOG.info("开始初始化Trap信息...");
		
		try {
			trapReceiver = new SnmpTrapReceiver();
			trapReceiver.setPortWithExceptionMsg(162);
			trapReceiver.setTrapAuthEnable(false);
			trapReceiver.setV3AuthEnable(true);
			this.trapReceiver.setCharacterEncoding(origncharset);

			this.trapReceiver.setCommunity(community);
			String mibs = getMibFilePaths();
			this.trapReceiver.loadMibs(mibs);
			
			this.trapReceiver.setAuthPassword(null);
			this.trapReceiver.setAuthProtocol(USMUserEntry.MD5_AUTH);
			this.trapReceiver.setPrivProtocol(USMUserEntry.CBC_DES);
			this.trapReceiver.setPrivPassword(null);
			this.trapReceiver.setPrincipal(null);
			status = true;
		} catch (Exception e) {
			status = false;
			LOG.error("初始化trap失败", e);
		}
		LOG.info("初始化Trap信息[{}]", status?"成功":"失败");
		
		return status;
	}
	
	public static void main(String[] args) {
		AdventnetTrapReceiver trap = new AdventnetTrapReceiver();
		if(trap.init(null, 0, null, "utf-8", 1000, null)){
			if(trap.startListen()){
				new HandleThread(trap).start();
			}
		}
//		trap.stopListen();
//		trap.startListen();
//		trap.stopListen();
	}
}
