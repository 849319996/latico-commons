package com.latico.commons.net.dhcp.server.handler;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.dhcp.server.bean.Lease;
import com.latico.commons.net.dhcp.server.common.LeaseTypeEnum;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 * 提供给外界进行实现处理租约的类，系统已经完成对IP的分配，然后把分配后的租约通知给外界使用
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2017年7月23日</B>
 * @since <B>JDK1.6</B>
 */
public class NotificationOutsideThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationOutsideThread.class);
    private LinkedBlockingDeque<Lease> leases = new LinkedBlockingDeque<Lease>(
            10000);

    private boolean status;
    private AbstractDhcpServiceHandlerImpl dhcpServiceHandler;

    public NotificationOutsideThread(AbstractDhcpServiceHandlerImpl dhcpServiceHandler) {
        this.dhcpServiceHandler = dhcpServiceHandler;
    }

    @Override
    public void run() {
        Lease lease = null;
        while (status) {
            try {
                lease = leases.poll(5, TimeUnit.SECONDS);
                if (lease != null) {
                    if (LeaseTypeEnum.offerdLease == lease.getLeaseType()) {
                        dhcpServiceHandler.discoverOfferdLease(lease);
                    } else if (LeaseTypeEnum.ackLease == lease
                            .getLeaseType()) {
                        dhcpServiceHandler.requestAckLease(lease);
                    } else if (LeaseTypeEnum.clientAbandonLease == lease
                            .getLeaseType()) {
                        dhcpServiceHandler.clientAbandonLease(lease);
                    } else if (LeaseTypeEnum.clientDeclineLease == lease
                            .getLeaseType()) {
                        dhcpServiceHandler.clientDeclineLease(lease);
                    } else if (LeaseTypeEnum.expireLease == lease
                            .getLeaseType()) {
                        dhcpServiceHandler.expireLease(lease);
                    } else if (LeaseTypeEnum.serverDeclineLease == lease
                            .getLeaseType()) {
                        dhcpServiceHandler.serverDeclineLease(lease);
                    } else {
                        LOG.warn(
                                "unknow the lease, so not give outside handle");
                    }
                }
            } catch (Exception e) {
                LOG.error("DHCP交互通知外界线程出现异常", e);
            }
        }
    }

    @Override
    public void start() {
        status = true;
        super.start();
    }

    public void stopThread() {
        status = false;
    }

    /**
     * 添加租约
     *
     * @param lease
     */
    public void addLease(Lease lease) {
        leases.add(lease);
    }

}