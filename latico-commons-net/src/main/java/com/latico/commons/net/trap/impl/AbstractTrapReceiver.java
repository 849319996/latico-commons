package com.latico.commons.net.trap.impl;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.net.trap.TrapReceiver;
import com.latico.commons.net.trap.bean.TrapResult;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * <PRE>
 * 
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月9日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public abstract class AbstractTrapReceiver implements TrapReceiver {

	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractTrapReceiver.class);
	
	/** origncharset 转化前的字符集 */
	public String origncharset = CharsetType.ISO;

	/** convertcharset 转换后的字符串 */
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
	protected String threadName = "SnmpTrap监听器";
	
	/** status 状态 */
	protected boolean status;
	
	/** receiveQueueSize 接收队列大小，默认10万 */
	protected int receiveQueueMaxSize = 100000;

	/** trapResults 接收的trap结果 */
	protected BlockingQueue<TrapResult> trapResults;
	
	@Override
	public boolean init() {
		return init(null, 0, null, null, 0, null);
	}
	@Override
	public boolean init(int receiveQueueMaxSize) {
		return init(null, 0, null, null, receiveQueueMaxSize, null);
	}
	
	@Override
	public boolean init(String ip, int port, String oriCharset, String destCharset, int receiveQueueMaxSize, String community) {
		if(StringUtils.isNotEmpty(ip)){
			this.listenIp = ip;
		}
		if(StringUtils.isNotEmpty(community)){
			this.community = community;
		}
		if(StringUtils.isNotEmpty(oriCharset)){
			this.origncharset = oriCharset;
		}
		if(StringUtils.isNotEmpty(destCharset)){
			this.convertcharset = destCharset;
		}
		if(port >= 1){
			this.listenPort = port;
		}
		if(receiveQueueMaxSize >= 1000){
			this.receiveQueueMaxSize = receiveQueueMaxSize;
		}
		this.trapResults = new ArrayBlockingQueue<TrapResult>(this.receiveQueueMaxSize);
		
		return initReceiver();
	}
	
	/**
	 * 初始化的接收器
	 * 由实现了处理
	 */
	protected abstract boolean initReceiver();
	
	/**
	 * 
	 * @param timeout 获取的超时时间，毫秒
	 * @return 如果没有获取到就返回null
	 */
	@Override
	public TrapResult getOneResult(long timeout){
		if(status){
			TrapResult result = null;
			try {
				result = this.trapResults.poll(timeout, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				LOG.error("从Trap队列中获取数据异常", e);
			}
			return result;
			
		}else{
			LOG.error("请先执行初始化方法成功");
			return null;
		}
	}
	
	@Override
	public List<TrapResult> getCurrentAllResult() {
		if(status){
			List<TrapResult> results = new ArrayList<TrapResult>();
			trapResults.drainTo(results);
			return results;
			
		}else{
			LOG.error("请先执行初始化方法成功");
			return null;
		}
	}
	
}
