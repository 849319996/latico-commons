package com.latico.commons.net.dhcp.server;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.dhcp.server.bean.ConfDhcpIpPool;
import com.latico.commons.net.dhcp.server.bean.IpPool;
import com.latico.commons.net.dhcp.server.common.DhcpDataPacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <PRE>
 * 地址管理器，如果IP池是从数据库加载的话，那可以设置一个定时器，定时从数据库加载后更新IP池
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @version <B>V1.0 2017年7月18日</B>
 * @since <B>JDK1.6</B>
 */
public abstract class AbstractIpPoolManager {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractIpPoolManager.class);

    /**
     * ipPools 地址范围，key是giaddr,value是IP池的范围
     */
    protected final Map<String, IpPool> ipPools = new ConcurrentHashMap<>();

    /**
     * 自动初始化
     */
    public AbstractIpPoolManager() {
        init();
    }

    /**
     * 刷新缓存，可以在这个方法会控制刷新IP池的频率
     *
     * @param request
     */
    public abstract void refreshCache(DhcpDataPacket request);

    /**
     * 全量初始化方法，包括初始化默认IP池
     * 客户端可能需要调用此
     */
    private void init() {
        initConfigFilePool();
    }
    /**
     * 初始化配置文件IP池
     */
    private void initConfigFilePool() {
        Map<String, ConfDhcpIpPool> confDhcpIpPools = DhcpdConfig.getInstance().getConfDhcpIpPools();
        for (Map.Entry<String, ConfDhcpIpPool> entry : confDhcpIpPools.entrySet()) {
            ConfDhcpIpPool pool = entry.getValue();
            IpPool ipPool = new IpPool(pool.getStartIp(), pool.getEndIp(), pool.getNetmask(), pool.getServerIp());
            //把服务器自身IP添加到静态IP列表中
            ipPool.addStaticIp("-1", pool.getServerIp());
            //中继IP作为池ID
            ipPool.setId(pool.getRelayIp());
            ipPool.setDomainName(pool.getServerDomainName());
            ipPool.setMessage(pool.getServerMessage());
            ipPool.setLeaseTime(pool.getLeaseTime());
            addIpPool(ipPool);
        }

    }

    /**
     * 增加一个池
     *
     * @param ipPool
     */
    protected void addIpPool(IpPool ipPool) {
        ipPools.put(ipPool.getId(), ipPool);
    }

    /**
     * 是否包含池
     *
     * @param poolId
     * @return
     */
    public boolean isContainPood(String poolId) {
        if (poolId == null) {
            return false;
        }
        if (ipPools.containsKey(poolId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过池ID拿到IP池
     *
     * @param poolId
     * @return
     */
    public IpPool getIpPool(String poolId) {
        return ipPools.get(poolId);
    }

    /**
     * 获取IP池
     *
     * @param request
     * @return
     */
    public IpPool getIpPool(DhcpDataPacket request) {
        String poolId = getPoolId(request);
        IpPool ipPool = getIpPool(poolId);
        if (ipPool == null) {
            LOG.warn("(MAC:" + request.getChaddrAsHex() + ") Request's IP Pool(pool's id:" + poolId + ") not find");
        }
        return ipPool;
    }

    /**
     * 获得一个IP池的ID值，暂时的需求是通过giaddr字段来区别
     *
     * @param request
     * @return
     */
    private String getPoolId(DhcpDataPacket request) {
        return getIpPoolId(request);
    }

    /**
     * @return 所有IP池
     */
    public Map<String, IpPool> getIpPools() {
        return ipPools;
    }

    /**
     * 拿到池ID,默认是从Giaddr拿
     *
     * @param request
     * @return
     */
    public String getIpPoolId(DhcpDataPacket request) {
        if (request == null) {
            return "0.0.0.0";
        } else {
            return request.getGiaddr().getHostAddress();
        }
    }

}
