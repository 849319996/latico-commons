package com.latico.commons.net.dhcp.server.examples;


import com.latico.commons.net.dhcp.DhcpUtils;
import com.latico.commons.net.dhcp.server.exception.DHCPServerInitException;
import com.latico.commons.net.dhcp.server.DhcpServerThread;

/**
 * <PRE>
 * 
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年7月17日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class ServerMainExample {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DhcpServerThread dhcpServer = DhcpUtils.createDhcpServerAndStart(new DhcpServiceHandlerImplExample());

			//停止
//			dhcpServer.stopServer();
		} catch (DHCPServerInitException e) {
			e.printStackTrace();
		}
	}

}
