package com.latico.commons.net.trap.impl;

import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.net.trap.AbstractTrapReceiver;
import com.latico.commons.net.trap.bean.Snmp4jTrapResult;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.math.NumberUtils;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <PRE>
 * SNMP Trap工具
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月8日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class Snmp4jTrapReceiver extends AbstractTrapReceiver implements CommandResponder {
	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(Snmp4jTrapReceiver.class);

	private MultiThreadedMessageDispatcher dispatcher;

	private Snmp snmp = null;

	private Address listenAddress;

	private ThreadPool threadPool;

	/**
	 * 启动
	 * @return 
	 */
	@Override
	public boolean startListen() {
		
		if(status){
			LOG.info("开始启动Trap监听器...");
			snmp.addCommandResponder(this);
			LOG.info("Trap接收器成功启动");
		}else{
			LOG.warn("Trap监听器未成功初始化!");
		}
		return status;
	}

	/**
	 * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息
	 * 当接收到trap时，会自动进入这个方法
	 * 
	 * @param respEvnt
	 */
	@Override
	public void processPdu(CommandResponderEvent respEvnt) {
		Snmp4jTrapResult result = new Snmp4jTrapResult();
		result.setReceiveTime(System.currentTimeMillis());
		Map<String,String> oidValueMap = new HashMap<>();
		result.setOidVauleMap(oidValueMap);

		// 解析Response
        try {
			if (respEvnt != null && respEvnt.getPDU() != null) {
				List<? extends VariableBinding> recVBs = respEvnt.getPDU().getVariableBindings();
			   	for (VariableBinding recVB : recVBs) {
					oidValueMap.put(recVB.getOid().toString(), recVB.toValueString());
				}
			   	
			   	//组装Trap结果对象
			   	String[] socket = respEvnt.getPeerAddress().toString().split("/");
			   	if(socket.length == 2){
					result.setRemoteHost(socket[0]);
					result.setRemotePort(NumberUtils.toInt(socket[1]));
			   	}
			}

//			处理结果
			processResult(result);
        } catch (Exception e) {
			LOG.error("处理Trap报文异常:{}", e, respEvnt.getPeerAddress());
		}
	}

	/**
	 * TODO
	 * 继承后复写这个方法，可以把结果添加到缓存队列或者写到kafka中
	 * @param result
	 */
	protected void processResult(Snmp4jTrapResult result) throws Exception {
		LOG.debug("在这里处理收到的报文:{}", result);
	}

	/**
	 * 关闭监听器
	 */
	@Override
	public void stopListen(){
		if(snmp != null){
			snmp.removeCommandResponder(this);
			LOG.info("暂停Trap监听");
		}
	}
	/**
	 * 关闭监听器
	 */
	@Override
	public void close(){
		if(snmp != null){
			try {
				snmp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOG.info("Trap监听器成功关闭");
	}
	
	@Override
	protected boolean initReceiver() {
		status = false;
		LOG.info("开始初始化Trap信息...");
		try {
			threadPool = ThreadPool.create(threadName, 20);
			dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
			if (StringUtils.isEmpty(listenIp)) {
				listenAddress = null;
			} else {
				// 本地IP与监听端口
				listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", "udp:$IP$/$Port$".replace("$IP$", listenIp).replace("$Port$", String.valueOf(listenPort))));
			}

			TransportMapping<?> transport;

			if (listenAddress != null) {
				// 对TCP与UDP协议进行处理
				if (listenAddress instanceof UdpAddress) {
					transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
				} else {
					transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
				}
			} else {
				transport = new DefaultUdpTransportMapping();
			}
			
			snmp = new Snmp(dispatcher, transport);
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
			USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
			
			snmp.listen();
			status = true;
			
		} catch (Exception e) {
			status = false;
			LOG.error("SNMP Trap 初始化失败", e);
		}
		LOG.info("初始化Trap信息[{}]", status?"成功":"失败");
		return status;
	}
	
}
