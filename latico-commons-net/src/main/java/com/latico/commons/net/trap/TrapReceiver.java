package com.latico.commons.net.trap;


/**
 * <PRE>
 * 默认监听162端口
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月11日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public interface TrapReceiver {
	
	/**
	 * 使用默认方式初始化
	 * @return
	 */
	public boolean init();
	

	/**
	 * 最全参数初始化
	 * @param ip
	 * @param port
	 * @param oriCharset 原字符集
	 * @param convertcharset
	 * @param community
	 * @return
	 */
	public boolean init(String ip, int port, String oriCharset, String convertcharset, String community);

	/**
	 * 启动trap监听
	 * @return
	 */
	public boolean startListen();
	
	/**
	 * 停止监听，不关闭，一般不建议使用，建议不用了直接调用close()进行完全关闭;
	 */
	public void stopListen();
	
	/**
	 * 关闭Trap系统
	 */
	public void close();
	
}

