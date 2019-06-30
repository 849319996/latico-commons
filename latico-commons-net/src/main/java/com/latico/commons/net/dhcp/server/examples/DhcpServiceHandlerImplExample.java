package com.latico.commons.net.dhcp.server.examples;

import com.latico.commons.net.dhcp.server.bean.IpPool;
import com.latico.commons.net.dhcp.server.bean.Lease;
import com.latico.commons.net.dhcp.server.handler.AbstractDhcpServiceHandlerImpl;
import com.latico.commons.net.dhcp.server.common.DhcpDataPacket;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.util.Map;


/**
 * <PRE>
 * 实现类，下面是一些系统处理好提供给外界使用的租约，外界可以利用它进行数据库操作
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @version <B>V1.0 2017年7月21日</B>
 * @since <B>JDK1.6</B>
 */
public class DhcpServiceHandlerImplExample extends AbstractDhcpServiceHandlerImpl {
	private static final Logger LOG = LoggerFactory.getLogger(DhcpServiceHandlerImplExample.class);
	@Override
	protected void requestAckLease(Lease lease) {
		LOG.info("处理一个 requestAckLease lease:\r\n{}", lease);
		//TODO
	}

	@Override
	protected void clientDeclineLease(Lease lease) {
		LOG.info("处理一个 clientDeclineLease lease:\r\n{}", lease);
		//TODO
	}

	@Override
	protected void serverDeclineLease(Lease lease) {
		LOG.info("处理一个 serverDeclineLease lease:\r\n{}", lease);
		//TODO
	}

	@Override
	protected void discoverOfferdLease(Lease lease) {
		LOG.info("处理一个 discoverOfferdLease lease:\r\n{}", lease);
		//TODO
	}

	@Override
	protected void expireLease(Lease lease) {
		LOG.info("处理一个 expireLease lease:\r\n{}", lease);
		//TODO
	}

	@Override
	protected void clientAbandonLease(Lease lease) {
		LOG.info("处理一个 clientAbandonLease lease:\r\n{}", lease);
		//TODO
	}

	/**
	 * 每次获取IP池的时候刷新IP池缓存，实现让外界进行动态获取IP传递给内部使用
	 *
	 * @param dhcpDataPacket
	 * @return
	 */
	@Override
	public IpPool getIpPool(DhcpDataPacket dhcpDataPacket) {

//        先从IP池拿，拿不到再刷新缓存重新拿
		IpPool ipPool = getIpPoolInstance().get(getIpPoolId(dhcpDataPacket));
		if (ipPool != null) {
			return ipPool;
		}
		refreshIpPoolCache(dhcpDataPacket);
		return getIpPoolInstance().get(getIpPoolId(dhcpDataPacket));
	}

	protected String getIpPoolId(DhcpDataPacket request) {
		return IpPoolManagerImplExample.getInstance().getIpPoolId(request);
	}

	@Override
	protected void refreshIpPoolCache(DhcpDataPacket request) {
//		因为服务器中的
		IpPoolManagerImplExample.getInstance().refreshCache(request);
	}

	@Override
	public Map<String, IpPool> getIpPoolInstance() {
		return IpPoolManagerImplExample.getInstance().getIpPools();
	}

}
