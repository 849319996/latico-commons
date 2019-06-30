package com.latico.commons.net.dhcp.server.handler;

import com.latico.commons.net.dhcp.server.DhcpServerThread;

import java.net.DatagramPacket;
import java.util.Map;
import java.util.Properties;

/**
 * <PRE>
 * 
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年7月22日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public interface DhcpServiceHandler {
    /**
     * read whatever parameters you need
     * 初始化操作，进行一些IP等数据的初始化加载
     * <p>
     * Initialize dhcpServiceHandler. Override this method to implement any initialization you may need.
     *
     * <p>This method is called once at stratup, before any request is passed to the dhcpServiceHandler.
     * A properties is passed to the dhcpServiceHandler to read whatever parameters it needs.
     *
     * <p>There is no default behaviour.
     *
     * @param props a Properties containing parameters, as passed to <tt>DhcpServerThread</tt>
     */
    void init(Properties props);

    /**
     * @return Returns the server.
     */
    public DhcpServerThread getServer();

    /**
     * @param server The server to set.
     */
    void setServer(DhcpServerThread server);

    /**
     * Low-level method for receiving a UDP Daragram and sending one back.
     *
     * <p>This methode normally does not need to be overriden and passes control
     * to <tt>serviceHandle()</tt> for DHCP packets handling. Howerever the <tt>serviceHandle()</tt>
     * method is not called if the DHCP request is invalid (i.e. could not be parsed).
     * So overriding this method gives you control on every datagram received, not
     * only valid DHCP packets.
     *
     * @param requestDatagram the datagram received from the client
     * @return response the datagram to send back, or <tt>null</tt> if no answer
     */
    DatagramPacket makeResponseDatagram(DatagramPacket requestDatagram);

}
