package com.latico.commons.net.dhcp.server;

import com.latico.commons.common.config.AbstractConfig;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.xml.Dom4jUtils;
import com.latico.commons.net.dhcp.server.bean.ConfDhcpIpPool;
import com.latico.commons.net.dhcp.server.common.DhcpConstants;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <PRE>
 * 配置文件，所有单位都是秒
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年7月17日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class DhcpdConfig extends AbstractConfig {

	private static final Logger LOG = LoggerFactory.getLogger(DhcpdConfig.class);

	/**
	 * instance 单例实例
	 */
	private static volatile DhcpdConfig instance = null;

	public static DhcpdConfig getInstance() {
		//第一层检测，在锁的外面判断是否为空，效率高
		if (instance == null) {
			//开始进入加锁创建对象
			synchronized (DhcpdConfig.class) {
				//第二层检测，因为第一层检测期间可能其他线程正则创建，但是未创建完成，所以需要在这里进行二次判断
				if (instance == null) {
					//创建实例
					instance = new DhcpdConfig();
				}
			}
		}
		return instance;
	}
	/**
	 * 私有构造方法，让外界不能创建对象，在这里做初始化逻辑处理
	 */
	private DhcpdConfig() {
		initOrRefreshConfig();
	}
	/** confDhcpIpPools 配置文件配置的IP池 */
	private Map<String, ConfDhcpIpPool> confDhcpIpPools = new HashMap<>();
	/** default MTU for ethernet */
	public static final int PACKET_SIZE = DhcpConstants._DHCP_MAX_MTU;
	/**
	 * 默认租期
	 */
	private int defaultLeaseTime = 172800;
	private int threadPoolSize = 5;
	private int threadPoolMaxSize = 20;
	private int threadPoolQueueSize = 10000;
	private int threadPoolKeepAlive = 30000;

	private String serverListenSocket = "127.0.0.1:67";

	@Override
	protected void initOtherConfig() {

	}

	@Override
	protected String getResourcesConfigFilePath() {
		return null;
	}

	@Override
	protected String getConfigFilePath() {
		return CONFIG_FILE_ROOT_DIR + "dhcp-server.xml";
	}

	@Override
	protected boolean initConfig(String fileContent) throws Exception {
		Element rootElement = Dom4jUtils.getRootElement(fileContent);
		initCommonEle(rootElement.element("common"));
		initDhcpIpPoolsEle(rootElement.element("dhcpIpPools"));
		return true;
	}

	private void initCommonEle(Element common) throws Exception {
		if (common == null) {
			return;
		}

		Map<String, String> childsNameValueMap = Dom4jUtils.getChildsNameValueMap(common);
		writeFieldValueToCurrentInstance(childsNameValueMap);

	}

	private void initDhcpIpPoolsEle(Element dhcpIpPoolRootEle) throws Exception {
		if(dhcpIpPoolRootEle == null){
			return;
		}
		List<Element> dhcpIpPoolEles = dhcpIpPoolRootEle.elements("dhcpIpPool");
		if(dhcpIpPoolEles == null) {
			return;
		}
		for(Element dhcpIpPoolEle : dhcpIpPoolEles){
			Map<String, String> childsNameValueMap = Dom4jUtils.getChildsNameValueMap(dhcpIpPoolEle);
			ConfDhcpIpPool ipPool = new ConfDhcpIpPool();
			writeFieldValue(ipPool, childsNameValueMap);
			confDhcpIpPools.put(ipPool.getId(), ipPool);
		}
	}

	public static Logger getLOG() {
		return LOG;
	}

	public Map<String, ConfDhcpIpPool> getConfDhcpIpPools() {
		return confDhcpIpPools;
	}

	public int getDefaultLeaseTime() {
		return defaultLeaseTime;
	}

	public String getServerListenSocket() {
		return serverListenSocket;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public int getThreadPoolMaxSize() {
		return threadPoolMaxSize;
	}

	public int getThreadPoolQueueSize() {
		return threadPoolQueueSize;
	}

	public int getThreadPoolKeepAlive() {
		return threadPoolKeepAlive;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("DhcpdConfig{");
		sb.append("confDhcpIpPools=").append(confDhcpIpPools);
		sb.append(", defaultLeaseTime=").append(defaultLeaseTime);
		sb.append(", threadPoolSize=").append(threadPoolSize);
		sb.append(", threadPoolMaxSize=").append(threadPoolMaxSize);
		sb.append(", threadPoolQueueSize=").append(threadPoolQueueSize);
		sb.append(", threadPoolKeepAlive=").append(threadPoolKeepAlive);
		sb.append(", serverListenSocket='").append(serverListenSocket).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
