package com.latico.commons.net.trap.test;

import com.latico.commons.net.trap.impl.AdventnetTrapReceiver;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.List;
import java.util.Vector;


/**
 * <PRE>
 * 本类用于向管理进程发送Trap信息
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月8日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class TrapSenderTest {

	private Snmp snmp = null;

	private Address targetAddress = null;

	public void initComm() throws IOException {

		// 设置管理进程的IP和端口
		targetAddress = GenericAddress.parse("udp:127.0.0.1/162");
		TransportMapping<?> transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		transport.listen();

	}

	/**
	 * 向管理进程发送Trap报文
	 * 
	 * @throws IOException
	 */
	public void sendPDU() throws IOException {

		// 设置 target
		CommunityTarget target = new CommunityTarget();
		target.setAddress(targetAddress);

		// 通信不成功时的重试次数
		target.setRetries(2);
		// 超时时间
		target.setTimeout(1500);
		// snmp版本
		target.setVersion(SnmpConstants.version2c);

		// 创建 PDU
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(".1.3.6.1.2.3377.10.1.1.1.1"),
				new OctetString("SnmpTrap")));
		pdu.add(new VariableBinding(new OID(".1.3.6.1.2.3377.10.1.1.1.2"),
				new OctetString("你好")));
		pdu.setType(PDU.TRAP);

		// 向Agent发送PDU，并接收Response
		ResponseEvent respEvnt = snmp.send(pdu, target);

		// 解析Response
		if (respEvnt != null && respEvnt.getResponse() != null) {
			List<? extends VariableBinding> recVBs = respEvnt.getResponse()
			.getVariableBindings();
			for (int i = 0; i < recVBs.size(); i++) {
				VariableBinding recVB = recVBs.get(i);
				System.out.println(recVB.getOid() + " : " + recVB.getVariable());
			}
		}
	}

	public static void main(String[] args) {
		try {
			AdventnetTrapReceiver trap = new AdventnetTrapReceiver();
			TrapSenderTest util = new TrapSenderTest();
			util.initComm();
			
			trap.startListen();
			System.out.println("发送，能接收");
			util.sendPDU();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			trap.stopListen();
			System.out.println("发送，不能能接收");
			util.sendPDU();
			
			trap.startListen();
			System.out.println("发送，能接收");
			util.sendPDU();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			trap.stopListen();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}