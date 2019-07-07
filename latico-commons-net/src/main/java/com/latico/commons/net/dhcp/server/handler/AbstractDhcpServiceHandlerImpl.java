package com.latico.commons.net.dhcp.server.handler;


import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.Ipv4Utils;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.net.dhcp.DhcpUtils;
import com.latico.commons.net.dhcp.server.common.DhcpConstants;
import com.latico.commons.net.dhcp.server.DhcpdConfig;
import com.latico.commons.net.dhcp.server.common.LeaseTypeEnum;
import com.latico.commons.net.dhcp.server.bean.IpPool;
import com.latico.commons.net.dhcp.server.bean.Lease;
import com.latico.commons.net.dhcp.server.common.DhcpDataPacket;
import com.latico.commons.net.dhcp.server.common.DhcpOption;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Map;

import static com.latico.commons.net.dhcp.server.common.DhcpConstants.*;

/**
 * <PRE>
 * 实现大部分功能，然后只是预留了业务处理后的通知真正的实现类
 * DHCPd服务实现例子;
 * 1、IP分配优先级：MAC分配静态IP > 客户端请求IP > 客户端自身IP地址 > 动态分配IP；
 * 2、
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2017年6月28日</B>
 * @since <B>JDK1.6</B>
 */
public abstract class AbstractDhcpServiceHandlerImpl extends AbstractDhcpServiceHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDhcpServiceHandlerImpl.class);

    /**
     * leaseScaner 租期过期扫描器
     */
    private LeaseTimeoutScanThread leaseScaner = null;

    /**
     * DCHP的交互过程全自动，但是会通过该线程通知外界在做了什么
     */
    private NotificationOutsideThread notificationOutsideThread = null;

    /**
     * 刷新IP池缓存，比如实时从库里面加载新数据，DHCP服务器每次拿IP的时候都会调用一次，就可以利用它给实现类进行查库操作
     *
     * @param request
     * @return
     */
    protected abstract void refreshIpPoolCache(DhcpDataPacket request);

    /**
     * 初始化IP池等
     *
     * @param props a Properties containing parameters, as passed to <tt>DhcpServerThread</tt>
     */
    @Override
    public void init(java.util.Properties props) {
        leaseScaner = new LeaseTimeoutScanThread(this);
        leaseScaner.start();
        notificationOutsideThread = new NotificationOutsideThread(this);
        notificationOutsideThread.start();
    }

    @Override
    protected DhcpDataPacket doDiscover(DhcpDataPacket request) {
//        LOG.info("收到DHCP的DISCOVER报文:{}", request);

        String clientMac = request.getChaddrAsHex();
        IpPool ipPool = getIpPool(request);

        //没有MAC和IP池，再判断是否配置了默认的IP，如果没有直接拒绝返回
        if (StringUtils.isAnyEmpty(ipPool, clientMac)) {

            LOG.debug("没有找到IP池,客户端MAC:{}, 请求报文:\r\n{}", clientMac, request);
            return null;
        }

        InetAddress clientRequestAddr = request.getOptionAsInetAddr(DhcpConstants.DHO_DHCP_REQUESTED_ADDRESS);
        InetAddress clientIpAddr = request.getCiaddr();
        String clientIp = DhcpUtils.getClientIp(clientMac, clientRequestAddr, clientIpAddr, ipPool);
        if (StringUtils.isEmpty(clientIp)) {
            LOG.debug("找不到合适的客户端IP进行分配,{}", request);
            return null;
        }

        //消息，一般用于报文说明
        String message = ipPool.getMessage();
        int leaseTime = DhcpdConfig.getInstance().getDefaultLeaseTime();
        DhcpOption[] options = request.getOptionsArray();
        InetAddress serverAddr = Ipv4Utils.getInetAddress(ipPool.getServerIp());

        String domainName = ipPool.getDomainName();
        String netmask = ipPool.getNetmask();
        String clientHostName = request.getOptionAsString(DHO_HOST_NAME);

        DhcpDataPacket response = DhcpDataPacket.makeDHCPOffer(request,
                Ipv4Utils.getInetAddress(clientIp), netmask, domainName, leaseTime,
                serverAddr, message, options);

        //生成租期对象
        Lease lease = createLease(clientMac, ipPool, clientIp, leaseTime * 1000, netmask, clientHostName, response);
        ipPool.addLease(lease);
        lease.setLeaseType(LeaseTypeEnum.offerdLease);
        notificationOutsideThread.addLease(lease);
        return response;
    }


    @Override
    protected DhcpDataPacket doRequest(DhcpDataPacket request) {
//        LOG.info("收到DHCP的REQUEST报文:{}", request);
        String clientMac = request.getChaddrAsHex();
        IpPool ipPool = getIpPool(request);

        //没有MAC和IP池，直接拒绝
        if (StringUtils.isAnyBlank(ipPool, clientMac)) {
            LOG.debug("没有找到IP池，拒绝分配IP, MAC：{}, 请求报文:\r\n{}", clientMac, request);
            return DhcpDataPacket.makeDHCPNak(request, DhcpConstants.INADDR_BROADCAST, "refuse allocation");
        }

        InetAddress serverAddr = Ipv4Utils.getInetAddress(ipPool.getServerIp());

        InetAddress clientRequestAddr = request.getOptionAsInetAddr(DhcpConstants.DHO_DHCP_REQUESTED_ADDRESS);
        InetAddress clientIpAddr = request.getCiaddr();
        String clientIp = DhcpUtils.getClientIp(clientMac, clientRequestAddr, clientIpAddr, ipPool);
        if (StringUtils.isEmpty(clientIp)) {
            Lease lease = ipPool.deleteLease(clientMac);//服务端拒绝
            lease.setLeaseType(LeaseTypeEnum.serverDeclineLease);
            notificationOutsideThread.addLease(lease);
            return DhcpDataPacket.makeDHCPNak(request, serverAddr, "refuse allocation");
        }

        //消息，一般用于报文说明
        String message = ipPool.getMessage();
        int leaseTime = ipPool.getLeaseTime();
        DhcpOption[] options = request.getOptionsArray();


        String domainName = ipPool.getDomainName();
        String netmask = ipPool.getNetmask();
        String clientHostName = request.getOptionAsString(DHO_HOST_NAME);

        DhcpDataPacket response = DhcpDataPacket.makeDHCPAck(request, Ipv4Utils.getInetAddress(clientIp), netmask, domainName, leaseTime, serverAddr, message, options);
        Lease lease = createLease(clientMac, ipPool, clientIp, leaseTime * 1000, netmask, clientHostName, response);
        ipPool.addLease(lease);

        //更新数据库操作
        lease.setLeaseType(LeaseTypeEnum.ackLease);
        notificationOutsideThread.addLease(lease);
        return response;
    }

    @Override
    protected DhcpDataPacket doInform(DhcpDataPacket request) {
//        LOG.info("收到DHCP的INFORM报文:{}", request);
        String clientMac = request.getChaddrAsHex();
        IpPool ipPool = getIpPool(request);

        String clientIp = request.getCiaddr().getHostAddress();
        if (StringUtils.isEmpty(clientIp)) {
            return null;
        }

        //没有MAC和IP池，直接拒绝
        if (StringUtils.isAnyBlank(ipPool, clientMac)) {
            LOG.debug("没有找到IP池，忽略处理, MAC：{}, 请求报文:\r\n{}", clientMac, request);
            return null;
        }

        //消息，一般用于报文说明
        String message = null;
        try {
            message = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            LOG.error("", e);
        }

        int leaseTime = ipPool.getLeaseTime();
        DhcpOption[] options = request.getOptionsArray();
        InetAddress serverAddr = Ipv4Utils.getInetAddress(ipPool.getServerIp());

        String domainName = ipPool.getDomainName();
        String netmask = ipPool.getNetmask();

        DhcpDataPacket response = DhcpDataPacket.makeDHCPAck(request, Ipv4Utils.getInetAddress(clientIp), netmask, domainName, leaseTime, serverAddr, message, options);

        return response;
    }

    @Override
    protected DhcpDataPacket doDecline(DhcpDataPacket request) {
//        LOG.info("收到DHCP的DECLINE报文:{}", request);
        String clientMac = request.getChaddrAsHex();
        IpPool ipPool = getIpPool(request);

        //没有MAC和IP池，直接拒绝
        if (StringUtils.isAnyBlank(ipPool, clientMac)) {
            //使用默认的方式进行分配
            LOG.debug("没有找到IP池，忽略处理, MAC：{}, 请求报文:\r\n{}", clientMac, request);
            return null;
        }
        Lease lease = ipPool.deleteLease(clientMac);

        //更新数据库操作
//		clientDeclineLease(lease);
        lease.setLeaseType(LeaseTypeEnum.clientDeclineLease);
        notificationOutsideThread.addLease(lease);

        //        拒绝包，不用响应客户端
        return null;
    }

    /**
     * Process RELEASE request.
     *
     * @param request DHCP request received from client
     * @return DHCP response to send back, or <tt>null</tt> if no response.
     */
    @Override
    protected DhcpDataPacket doRelease(DhcpDataPacket request) {
//        LOG.info("收到DHCP的RELEASE报文:{}", request);
        String clientMac = request.getChaddrAsHex();
        IpPool ipPool = getIpPool(request);

        //没有MAC和IP池，直接拒绝
        if (StringUtils.isAnyBlank(ipPool, clientMac)) {
            //使用默认的方式进行分配
            LOG.debug("没有找到IP池，忽略处理, MAC：{}, 请求报文:\r\n{}", clientMac, request);
            return null;
        }
        Lease lease = ipPool.deleteLease(clientMac);

        //更新数据库操作
//		clientAbandonLease(lease);
        lease.setLeaseType(LeaseTypeEnum.clientAbandonLease);
        notificationOutsideThread.addLease(lease);

//        释放包，不用响应客户端
        return null;
    }

    /**
     * 根据给出的条件，创建一个租期对象
     *
     * @param clientMac
     * @param ipPool
     * @param clientIp
     * @param leaseTime      单位：毫秒
     * @param netmask
     * @param clientHostName
     * @param response
     * @return
     */
    public Lease createLease(String clientMac, IpPool ipPool, String clientIp,
                             int leaseTime, String netmask, String clientHostName,
                             DhcpDataPacket response) {
        Lease lease = ipPool.getLeaseByMac(clientMac);
        if (lease == null) {
            lease = new Lease(clientIp, leaseTime);
        }
        lease.setClientHostName(clientHostName);

        lease.setIpPoolId(ipPool.getId());
        lease.setDns(Ipv4Utils.byteToIp(response.getOptionRaw(DHO_DOMAIN_NAME_SERVERS)));
        lease.setGateway(Ipv4Utils.byteToIp(response.getOptionRaw(DHO_ROUTERS)));
        lease.setMac(clientMac);
        lease.setNetmask(netmask);
        lease.setOpt60(response.getOptionAsString(DHO_VENDOR_CLASS_IDENTIFIER));
        byte[] opt61 = response.getOptionRaw(DHO_DHCP_CLIENT_IDENTIFIER);
        if (opt61 != null) {
            try {
                lease.setOpt61(new String(opt61, CharsetType.BYTE_ENCODING));
            } catch (UnsupportedEncodingException e) {
                LOG.error("", e);
            }
        }
        DhcpOption opt82 = response.getOption(DHO_DHCP_AGENT_OPTIONS);
        if (opt82 != null) {
            lease.setOpt82(opt82.toString());
        }
        lease.refreshLease();
        return lease;
    }
    /**
     * 子类提供
     * 服务器需要用来进行租约过期扫描使用
     *
     * @return IP池实例，程序唯一，key是IP池ID
     */
    protected abstract Map<String, IpPool> getIpPoolInstance();

    /**
     * 每次获取IP池的时候刷新IP池缓存，实现让外界进行动态获取IP传递给内部使用
     * @param dhcpDataPacket
     * @return
     */
    protected abstract IpPool getIpPool(DhcpDataPacket dhcpDataPacket);
    /**
     * 通过响应discover报文提供的租期，响应offerdLease
     * 已经响应给了客户端，通知外界结果，实现者可以根据该结果进行入库等处理
     *
     * @param lease
     */
    protected abstract void discoverOfferdLease(Lease lease);

    /**
     * 通过响应request报文提供的租期，响应ACK报文
     * 已经响应给了客户端，通知外界结果，实现者可以根据该结果进行入库等处理
     *
     * @param lease
     */
    protected abstract void requestAckLease(Lease lease);

    /**
     * 客户端拒绝服务端提供报文
     * 已经响应给了客户端，通知外界结果，实现者可以根据该结果进行入库等处理
     *
     * @param lease
     */
    protected abstract void clientDeclineLease(Lease lease);

    /**
     * 服务端拒绝客户端请求报文
     * 已经响应给了客户端，通知外界结果，实现者可以根据该结果进行入库等处理
     *
     * @param lease
     */
    protected abstract void serverDeclineLease(Lease lease);

    /**
     * 到期，释放租期
     * 已经响应给了客户端，通知外界结果，实现者可以根据该结果进行入库等处理
     *
     * @param lease
     */
    protected abstract void expireLease(Lease lease);

    /**
     * 客户端放弃租期
     * 已经响应给了客户端，通知外界结果，实现者可以根据该结果进行入库等处理
     *
     * @param lease
     */
    protected abstract void clientAbandonLease(Lease lease);
}
