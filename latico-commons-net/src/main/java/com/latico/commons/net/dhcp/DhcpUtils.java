package com.latico.commons.net.dhcp;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.Ipv4Utils;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.net.dhcp.server.DhcpServerThread;
import com.latico.commons.net.dhcp.server.bean.IpPool;
import com.latico.commons.net.dhcp.server.bean.Lease;
import com.latico.commons.net.dhcp.server.exception.DHCPServerInitException;
import com.latico.commons.net.dhcp.server.handler.DhcpServiceHandler;

import java.net.InetAddress;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-22 9:46
 * @Version: 1.0
 */
public class DhcpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DhcpUtils.class);


    /**
     * 创建和启动DHCP 服务器
     * @param dhcpServiceHandler 业务处理器，客户端实现
     * @return
     * @throws DHCPServerInitException
     */
    public static DhcpServerThread createDhcpServerAndStart(DhcpServiceHandler dhcpServiceHandler) throws DHCPServerInitException {
        LOG.info("开始DHCP服务器创建和启动");
        DhcpServerThread server = new DhcpServerThread(dhcpServiceHandler);
        server.startAsThread();
        return server;
    }

    /**
     * IP分配优先级：MAC分配静态IP > 客户端请求IP > 动态分配IP；
     *
     * @param clientMac
     * @param clientRequestAddr
     * @param clientIpAddr
     * @param ipPool
     * @return
     */
    public static String getClientIp(String clientMac, InetAddress clientRequestAddr,
                                     InetAddress clientIpAddr, IpPool ipPool) {
        String clientIp = null;
        String staticIp = ipPool.getStaticIp(clientMac);

        //静态IP优先
        if (staticIp != null) {
            clientIp = staticIp;
        } else {

            //第二优先级是客户端请求过来的IP，如果请求的IP不符合要求，就使用一个动态地址
            if (Ipv4Utils.isValidIpV4Addr(clientRequestAddr) && !ipPool.isStaticIp(clientRequestAddr.getHostAddress())) {
                clientIp = clientRequestAddr.getHostAddress();
            } else if (Ipv4Utils.isValidIpV4Addr(clientIpAddr) && !ipPool.isStaticIp(clientIpAddr.getHostAddress())) {
                clientIp = clientIpAddr.getHostAddress();
            }

            //检查clientIP是否有效，没有就迭代获取有效的动态IP
            Lease lease = null;
            boolean isGetdIp = false;
            while (true) {

                //如果租期中没有该MAC，并且请求的IP不能已经出租，可以直接出租
                if ((lease = ipPool.getLeaseByMac(clientMac)) == null) {
                    if (ipPool.inRange(clientIp) && !ipPool.isAlreadyUsedIp(clientIp)) {
                        isGetdIp = true;
                    }

                    //下面是假如租期列表中已经存在相同的MAC的情况判断
                } else {
                    if (StringUtils.isEmpty(clientIp)) {
                        clientIp = lease.getIpString();
                    }
                    //如果相同MAC下，请求的IP与当前已经存在的租期中的IP一样，那直接使用
                    if (StringUtils.equals(lease.getIpString(), clientIp)) {
                        isGetdIp = true;

                        //请求的IP与当前已经存在的租期中的IP不一样，并且当前IP没有被租用，那直接使用
                    } else if (!ipPool.isAlreadyUsedIp(clientIp) && ipPool.inRange(clientIp)) {
                        isGetdIp = true;
                    }
                }

                //如果已经获得IP就跳出循环
                if (isGetdIp) {
                    break;
                }

                //获取一个动态地址
                clientIp = ipPool.getDynamicIpStr();

                //从IP池里面未能获取到IP，直接返回空
                if (Ipv4Utils.isInValidIpV4Addr(clientIp)) {
                    LOG.warn("(MAC:" + clientMac + ") can not find IP from ip pool");
                    return null;
                }
            }

        }
        return clientIp;
    }


}
