package com.latico.commons.common.start;

import com.latico.commons.common.monitor.ThreadMonitor;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

/**
 * <PRE>
 *  程序启动入口类
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 13:39:21
 * @Version: 1.0
 */
public class MainExample extends AbstractMain {

	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(MainExample.class);

	/**
	 * 程序入口函数
	 *
	 * @param args 首个参数为配置文件路径
	 */
	public static void main(String[] args) {
        LogUtils.loadLogBackConfigDefault();
		AbstractMain main = new MainExample();
		main.startApp(args);

	}

	@Override
	protected void execute(String[] args) throws Exception {
		LOG.info("程序启动");
		ThreadMonitor.getInstance().startMonitor(getAppRunStatus());
		ThreadMonitor.getInstance().addThread(threadName, Thread.currentThread());
		System.out.println("文件存在:" + FileUtils.exists("config/Readme2.md"));
		//启动程序
		//主线程执行结束
		ThreadMonitor.getInstance().removeThread(threadName);
	}


	@Override
	protected void executeShutDownHook() {
		System.out.println("执行shutdownhook任务");
	}

	@Override
	protected void printVersionInfo() {
//		LOG.info(new Version().getVersionInfo());
		
	}

	@Override
	protected void loadConfig() {
		// 加载配置文件
	}

}
