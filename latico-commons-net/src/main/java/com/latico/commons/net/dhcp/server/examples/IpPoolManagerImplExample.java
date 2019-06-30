package com.latico.commons.net.dhcp.server.examples;

import com.latico.commons.net.dhcp.server.AbstractIpPoolManager;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.dhcp.server.common.DhcpDataPacket;


/**
 * <PRE>
 * 地址管理器，如果IP池是从数据库加载的话，那可以设置一个定时器，定时从数据库加载后更新IP池
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年7月18日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class IpPoolManagerImplExample extends AbstractIpPoolManager {
	private static final Logger LOG = LoggerFactory.getLogger(IpPoolManagerImplExample.class);
	private IpPoolManagerImplExample() {
		initDbIpPools();
	}

	public static final IpPoolManagerImplExample getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static final class SingletonHolder {
		private static final IpPoolManagerImplExample INSTANCE = new IpPoolManagerImplExample();
	}

	@Override
	public void refreshCache(DhcpDataPacket request) {
		initDbIpPools();
	}

	/**
	 * 初始化数据库IP池
	 */
	protected void initDbIpPools() {
		//TODO
	}

	public static void main(String[] args) {
		System.out.println(IpPoolManagerImplExample.getInstance().getIpPools());
	}
}
