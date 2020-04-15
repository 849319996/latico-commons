package com.latico.commons.net.trap;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.apache.commons.lang3.StringUtils;


/**
 * <PRE>
 * 
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月9日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public abstract class AbstractTrapReceiver implements TrapReceiver {

	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractTrapReceiver.class);
	
	/** 转化前的字符集 */
	public String origncharset = CharsetType.ISO;

	/** 转换后的字符串 */
	public String convertcharset = CharsetType.UTF8;

	/** listenIp 监听的IP */
	public String listenIp = "127.0.0.1";
	
	/** listenPort 监听的端口 */
	public int listenPort = 162;
	
	/** community 监听的共同体 */
	public String community = "public";
	
	/** mibsDir mib库目录路径 */
	public static final String mibsDir = "./mibs/";
	
	/** trapName 线程名称 */
	protected String threadName = "TrapServer";
	
	/** status 状态 */
	protected boolean status;

	@Override
	public boolean init() {
		return init(null, 0, null, convertcharset, null);
	}

	@Override
	public boolean init(String ip, int port, String oriCharset, String convertcharset, String community) {
		this.listenIp = ip;
		if(StringUtils.isNotEmpty(community)){
			this.community = community;
		}
		if(StringUtils.isNotEmpty(oriCharset)){
			this.origncharset = oriCharset;
		}
		if(StringUtils.isNotEmpty(convertcharset)){
			this.convertcharset = convertcharset;
		}
		if(port >= 1){
			this.listenPort = port;
		}

		return initReceiver();
	}
	
	/**
	 * 初始化的接收器
	 * 由实现了处理
	 */
	protected abstract boolean initReceiver();
	

}
