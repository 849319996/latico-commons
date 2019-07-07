package com.latico.commons.net.dhcp.server.handler;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.net.dhcp.server.bean.IpPool;
import com.latico.commons.net.dhcp.server.bean.Lease;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * IP池过期检测线程，对过期的调用expireLease()方法
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2017年7月22日</B>
 * @since <B>JDK1.6</B>
 */
public class LeaseTimeoutScanThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(LeaseTimeoutScanThread.class);
    private boolean status = false;

    /**
     * sleepSecond 3秒检测一次
     */
    private int sleepSecond = 3;
    /**
     * 多久打印一次日志
     */
    private int printSecondOneTime = 150;

    private String threadName = "[租约过期扫描器]";
    private AbstractDhcpServiceHandlerImpl dhcpServiceHandler;

    LeaseTimeoutScanThread(AbstractDhcpServiceHandlerImpl dhcpServiceHandler) {
        this.dhcpServiceHandler = dhcpServiceHandler;
    }

    @Override
    public void run() {
        LOG.info(threadName + "启动...");
        if (dhcpServiceHandler.getIpPoolInstance() == null) {
            LOG.warn(threadName + " 需要扫描的IP Pool为空，描器关闭");
            return;
        }
        LOG.info(threadName + " 启动成功");
        String poolId = null;
        IpPool poolTmp = null;
        Lease leaseTmp = null;
        Map<String, Lease> leases = null;
        List<String> expireMacs = new ArrayList<String>();

        int printLogCount = 0;
        final int printCount = printSecondOneTime / sleepSecond;
        while (status) {
            ThreadUtils.sleepSecond(sleepSecond);
            printLogCount++;
            if (printLogCount >= printCount) {
                printLogCount = 0;
                LOG.info(threadName + " 继续扫描中...");
            }
            for (Map.Entry<String, IpPool> ipPool : dhcpServiceHandler.getIpPoolInstance().entrySet()) {
                poolId = ipPool.getKey();
                poolTmp = ipPool.getValue();
                leases = poolTmp.getLeases();
                expireMacs.clear();
                for (Map.Entry<String, Lease> lease : leases.entrySet()) {
                    leaseTmp = lease.getValue();

                    if (leaseTmp.isValid()) {
                        continue;
                    }
                    expireMacs.add(lease.getKey());
                }

                //过期释放，移除过期
                for (String mac : expireMacs) {
                    leaseTmp = poolTmp.deleteLease(mac);
                    dhcpServiceHandler.expireLease(leaseTmp);
                    LOG.warn(threadName + " IP pool(ID:" + poolId + ") 过期释放expire lease[MAC:" + mac + " IP:" + leaseTmp.getIpString() + "]");
                }
            }
        }

        LOG.info(threadName + " 关闭完成");
    }

    @Override
    public synchronized void start() {
        status = true;
        super.start();
    }

    public void stopThread() {
        this.status = false;
    }
}