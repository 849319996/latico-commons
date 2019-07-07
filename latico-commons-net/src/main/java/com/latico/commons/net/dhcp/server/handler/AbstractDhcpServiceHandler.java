/*
 *	This file is part of dhcp4java, a DHCP API for the Java language.
 *	(c) 2006 Stephan Hadinger
 *
 *	This library is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU Lesser General Public
 *	License as published by the Free Software Foundation; either
 *	version 2.1 of the License, or (at your option) any later version.
 *
 *	This library is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *	Lesser General Public License for more details.
 *
 *	You should have received a copy of the GNU Lesser General Public
 *	License along with this library; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.latico.commons.net.dhcp.server.handler;

import com.latico.commons.common.util.net.Ipv4Utils;
import com.latico.commons.net.dhcp.server.common.DhcpConstants;
import com.latico.commons.net.dhcp.server.exception.DHCPBadPacketException;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.dhcp.server.common.DhcpDataPacket;
import com.latico.commons.net.dhcp.server.common.DhcpOption;
import com.latico.commons.net.dhcp.server.DhcpServerThread;

import java.net.DatagramPacket;
import java.net.InetAddress;

import static com.latico.commons.net.dhcp.server.common.DhcpConstants.*;

/**
 * <PRE>
 * 抽象的DHCP服务端业务类
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2018年8月3日</B>
 * @since <B>JDK1.6</B>
 */
public abstract class AbstractDhcpServiceHandler implements DhcpServiceHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDhcpServiceHandler.class);

    /**
     * the server instance running this dhcpServiceHandler
     */
    protected DhcpServerThread server = null;


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
    @Override
    public DatagramPacket makeResponseDatagram(DatagramPacket requestDatagram) {

        if (requestDatagram == null) {
            return null;
        }

        try {
            // parse DHCP request
            DhcpDataPacket request = DhcpDataPacket.convertToDhcpPacket(requestDatagram);

            if (request == null) {
                return null;
            }    // nothing much we can do

            String dhcpType = null;
            for (DhcpOption opt : request.getOptionsCollection()) {
                if (opt.getCode() == DhcpConstants.DHO_DHCP_MESSAGE_TYPE) {
                    dhcpType = opt.toStringDHCPDatagram();
                    break;
                }
            }


            LOG.info("DHCP请求包：数据来源:[{}:{}], DHCP Client(MAC:{} 中继IP:{} DHCP类型:{} CiAddr:{})", request.getAddress(), request.getPort(), request.getChaddrAsHex(), request.getGiaddr(), dhcpType, request.getCiaddr().getHostAddress());

//对于不是中继模式的，打印debug级别
            boolean giaddrIsValidIpV4Addr = Ipv4Utils.isValidIpV4Addr(request.getGiaddr());
            if (giaddrIsValidIpV4Addr) {
                LOG.info("DHCP请求包：数据来源:[{}:{}], DHCP Client(MAC:{}) 请求报文如下:\r\n{}", request.getAddress(), request.getPort(), request.getChaddrAsHex(), request.toString());
            } else {
                LOG.debug("DHCP请求包：数据来源:[{}:{}], DHCP Client(MAC:{}) 请求报文如下:\r\n{}", request.getAddress(), request.getPort(), request.getChaddrAsHex(), request.toString());
            }

            // do the real work 处理真正的业务
            DhcpDataPacket response = serviceHandle(request);

            // done 业务处理完成后
            LOG.info("DHCP响应(数据来源:[{}:{}], MAC:{} 中继IP:{} DHCP类型:{}) 处理完成", request.getAddress(), request.getPort(), request.getChaddrAsHex(), request.getGiaddr(), dhcpType);

            if (response == null) {
                return null;
            }
            if (giaddrIsValidIpV4Addr) {
                LOG.info("DHCP响应包：(数据来源:[{}:{}], MAC:{}) 响应包:\r\n{}", request.getAddress(), request.getPort(), request.getChaddrAsHex(), response.toString());

            } else {
                LOG.debug("DHCP响应包：(数据来源:[{}:{}], MAC:{}) 响应包:\r\n{}", request.getAddress(), request.getPort(), request.getChaddrAsHex(), response.toString());
            }

            // check address/port
            InetAddress address = response.getAddress();
            if (address == null) {
                LOG.warn("Response client(MAC:" + request.getChaddrAsHex() + ") address needed in response");
                return null;
            }

//            创建一个响应包
            // we have something to send back
            byte[] responseBuf = response.serialize();
            int port = response.getPort();
            DatagramPacket responseDatagram = new DatagramPacket(responseBuf, responseBuf.length, address, port);
            this.postProcess(requestDatagram, responseDatagram);

            return responseDatagram;
        } catch (DHCPBadPacketException e) {
            LOG.error("Invalid DHCP packet received", e);
        } catch (Exception e) {
            LOG.error("Unexpected Exception", e);
        }

        // general fallback, we do nothing
        return null;
    }

    /**
     * General method for parsing a DHCP request.
     *
     * <p>Returns the DHCPPacket to send back to the client, or null if we
     * silently ignore the request.
     *
     * <p>Default behaviour: ignore BOOTP packets, and dispatch to <tt>doXXX()</tt> methods.
     *
     * @param request DHCP request from the client
     * @return response DHCP response to send back to client, <tt>null</tt> if no response
     */
    private DhcpDataPacket serviceHandle(DhcpDataPacket request) {
        Byte dhcpMessageType;

        if (request == null) {
            return null;
        }

        if (!request.isDhcp()) {
            LOG.info("BOOTP packet rejected");
            return null;        // skipping old BOOTP
        }

        dhcpMessageType = request.getDHCPMessageType();

        if (dhcpMessageType == null) {
            LOG.info("no DHCP message type");
            return null;
        }

        if (request.getOp() == BOOTREQUEST) {
            switch (dhcpMessageType) {
                case DHCPDISCOVER:
                    return this.doDiscover(request);
                case DHCPREQUEST:
                    return this.doRequest(request);
                case DHCPINFORM:
                    return this.doInform(request);
                case DHCPDECLINE:
                    return this.doDecline(request);
                case DHCPRELEASE:
                    return this.doRelease(request);

                default:
                    LOG.info("Unsupported message type " + dhcpMessageType);
                    return null;
            }
        } else if (request.getOp() == BOOTREPLY) {
            // receiving a BOOTREPLY from a client is not normal
            LOG.info("BOOTREPLY received from client");
            return null;
        } else {
            LOG.warn("Unknown Op: " + request.getOp());
            return null;    // ignore
        }
    }

    /**
     * Process DISCOVER request.
     * extends to do //TODO
     * DHCP客户端请求地址时，并不知道DHCP服务器的位置，因此DHCP客户端会在本地网络内以广播方式发送请求报文，
     * 这个报文成为Discover报文，目的是发现网络中的DHCP服务器，所有收到Discover报文的DHCP服务器都会发送回应报文，
     * DHCP客户端据此可以知道网络中存在的DHCP服务器的位置。
     * <p>
     * DHCP服务器收到Discover报文后，就会在所配置的地址池中查找一个合适的IP地址，
     * 加上相应的租约期限和其他配置信息（如网关、DNS服务器等），构造一个Offer报文，发送给用户，
     * 告知用户本服务器可以为其提供IP地址。< 只是告诉client可以提供，是预分配，还需要client通过ARP检测该IP是否重复>
     *
     * @param request DHCP request received from client
     * @return DHCP response to send back, or <tt>null</tt> if no response.
     */
    protected abstract DhcpDataPacket doDiscover(DhcpDataPacket request);

    /**
     * Process REQUEST request.
     * extends to do //TODO
     * 如果DHCP服务器收到Request报文后，没有发现有相应的租约记录或者由于某些原因无法正常分配IP地址，
     * 则发送NAK报文作为回应，通知用户无法分配合适的IP地址。
     * <p>
     * DHCP客户端可能会收到很多Offer，所以必须在这些回应中选择一个。
     * Client通常选择第一个回应Offer报文的服务器作为自己的目标服务器，并回应一个广播Request报文，通告选择的服务器。
     * DHCP客户端成功获取IP地址后，在地址使用租期过去1/2时，会向DHCP服务器发送单播Request报文续延租期，
     * 如果没有收到DHCP ACK报文，在租期过去3/4时，发送广播Request报文续延租期。
     *
     * @param request DHCP request received from client
     * @return DHCP response to send back, or <tt>null</tt> if no response.
     */
    protected abstract DhcpDataPacket doRequest(DhcpDataPacket request);

    /**
     * Process INFORM request.
     * extends to do //TODO
     * DHCP客户端如果需要从DHCP服务器端获取更为详细的配置信息，则发送Inform报文向服务器进行请求，
     * 服务器收到该报文后，将根据租约进行查找，找到相应的配置信息后，发送ACK报文回应DHCP客户端。< 极少用到>
     *
     * @param request DHCP request received from client
     * @return DHCP response to send back, or <tt>null</tt> if no response.
     */
    protected abstract DhcpDataPacket doInform(DhcpDataPacket request);

    /**
     * Process DECLINE request.
     * extends to do //TODO
     * DHCP客户端收到DHCP服务器回应的ACK报文后，通过地址冲突检测发现服务器分配的地址冲突或者由于其他原因导致不能使用，
     * 则发送Decline报文，通知服务器所分配的IP地址不可用。
     *
     * @param request DHCP request received from client
     * @return DHCP response to send back, or <tt>null</tt> if no response.
     */
    protected abstract DhcpDataPacket doDecline(DhcpDataPacket request);

    /**
     * Process RELEASE request.
     * extends to do //TODO
     * 当用户不再需要使用分配IP地址时，就会主动向DHCP服务器发送Release报文，告知服务器用户不再需要分配IP地址，
     * DHCP服务器会释放被绑定的租约。
     *
     * @param request DHCP request received from client
     * @return DHCP response to send back, or <tt>null</tt> if no response.
     */
    protected abstract DhcpDataPacket doRelease(DhcpDataPacket request);

    /**
     * 对响应包的通用预处理
     * You have a chance to catch response before it is sent back to client.
     * extends to do //TODO
     * <p>This allows for example for last minute modification (who knows?)
     * or for specific logging.
     *
     * <p>Default behaviour is to do nothing.
     *
     * <p>The only way to block the response from being sent is to raise an exception.
     *
     * @param requestDatagram  datagram received from client
     * @param responseDatagram datagram sent back to client
     */
    protected void postProcess(DatagramPacket requestDatagram, DatagramPacket responseDatagram) {
        // default is nop
    }

    /**
     * @return Returns the server.
     */
    @Override
    public DhcpServerThread getServer() {
        return server;
    }

    /**
     * @param server The server to set.
     */
    @Override
    public void setServer(DhcpServerThread server) {
        this.server = server;
    }

}
