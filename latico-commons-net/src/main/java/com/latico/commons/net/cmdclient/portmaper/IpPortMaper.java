package com.latico.commons.net.cmdclient.portmaper;

import com.latico.commons.common.config.AbstractConfig;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <PRE>
 * IP和端口映射，转换成真正的IP端口
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月21日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class IpPortMaper extends AbstractConfig {
	private static final Logger LOG = LoggerFactory.getLogger(IpPortMaper.class);
	
	private static volatile IpPortMaper instance;

	/**
	 * 要访问的目的IP，获取到映射的IP和端口
	 */
	private Map<String, InetSocketAddress> ipPortMap = new ConcurrentHashMap<>();

	public static IpPortMaper getInstance() {
		if(instance == null) {
			synchronized (Class.class) {
				if(instance == null) {
					instance = new IpPortMaper();
				}
			}
		}
		return instance;
	}
	
	private IpPortMaper() {
		initOrRefreshConfig();
		LOG.info("读取IP端口映射关系:" + ipPortMap);
	}
	

	/**
	 * 获取转换后的IP地址和端口
	 * @param ip
	 * @param port
	 * @return
	 */
	public InetSocketAddress getConvertedIpPort(String ip, int port){
		if(ip == null || "".equals(ip.trim()) || port < 0){
			return null;
		}
		return ipPortMap.get(concat(ip, port));
	}
	
	/**
	 * 添加要映射的IP和端口
	 * @param beforeIp 转换前的IP
	 * @param beforePort 转换前的端口，大于0
	 * @param behindIp 转换后的IP
	 * @param behindPort 转换后的端口
	 * @return 是否添加成功，如果已经存在
	 */
	public boolean putIpPortToMap(String beforeIp, int beforePort, String behindIp, int behindPort){
		if(beforeIp == null || "".equals(beforeIp.trim()) || beforePort < 0){
			return false;
		}
		if(behindIp == null || "".equals(behindIp.trim()) || behindPort < 0){
			return false;
		}
		
		ipPortMap.put(concat(beforeIp, beforePort), new InetSocketAddress(behindIp, behindPort));
		return true;
		
	}
	/**
	 * 添加要映射的IP和端口
	 * @param beforeIp 转换前的IP
	 * @param beforePort 转换前的端口，大于0
	 * @return 是否添加成功，如果已经存在
	 */
	public InetSocketAddress removeIpPort(String beforeIp, int beforePort){
		if(beforeIp == null || "".equals(beforeIp.trim()) || beforePort < 0){
			return null;
		}

		return ipPortMap.remove(concat(beforeIp, beforePort));

	}

	/**
	 * 组装IP和端口成字符串
	 * @param ip
	 * @param port
	 * @return
	 */
	private String concat(String ip, int port) {
		return new StringBuffer(ip).append(":").append(port).toString();
	}
	
	public void clear(){
		ipPortMap.clear();
	}

    public Map<String, InetSocketAddress> getIpPortMap() {
        return ipPortMap;
    }

	@Override
	protected void initOtherConfig() {

	}

	@Override
	protected String getResourcesConfigFilePath() {
//		return RESOURCES_CONFIG_FILE_ROOT_DIR + "ipPortMap.properties";
		return null;
	}

	@Override
	protected String getConfigFilePath() {
		return CONFIG_FILE_ROOT_DIR + "ipPortMap.properties";
	}

	@Override
	protected boolean initConfig(String fileContent) throws Exception {
		String[] lines = StringUtils.splitByLine(fileContent);
		String key = null;
		String value = null;
		String[] arr1 = null;
		String[] arr2 = null;
		String[] arr = null;
		for(String line : lines){
			if (line.startsWith("#")) {
				continue;
			}
			arr = line.split("\\s*=\\s*");
			if(arr.length != 2){
				continue;
			}
			key = arr[0];
			value = arr[1];

			arr1 = key.split("\\s*:\\s*");
			if(arr1.length != 2){
				continue;
			}
			arr2 = value.split("\\s*:\\s*");
			if(arr2.length != 2){
				continue;
			}

			putIpPortToMap(arr1[0], Integer.parseInt(arr1[1]), arr2[0], Integer.parseInt(arr2[1]));
		}
		return false;
	}
}
