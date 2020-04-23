package com.latico.commons.common.start;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 *  主函数的抽象类
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 13:39:32
 * @version: 1.0
 */
public abstract class AbstractMain {

	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMain.class);
	
	/** appRunStatus APP总运行状态,默认false,由外界控制总状态，一般为false后，其它线程应该要感知此状态以此关闭自身线程 */
	private final static AtomicBoolean APP_RUN_STATUS = new AtomicBoolean(false);
	
	protected final static String threadName = "[主线程]";
	/**
	 * 启动程序的入口函数
	 * 
	 * @param args 程序启动参数
	 */
	public void startApp(String[] args) {
		
		APP_RUN_STATUS.set(true);
		
		LOG.info("程序启动");
		
		loadConfig();
		
		//打印版本信息
		printVersionInfo();
		
		//启动钩子函数
		startShutDownHook();
		
		//执行流程
		try {
			execute(args);
		} catch (Throwable e) {
			LOG.error("程序业务执行方法出现错误，程序退出", e);
			closeApp();
			System.exit(1);
		}
		
	}

	/**
	 * 获取APP运行总状态对象
	 * @return
	 */
	public static AtomicBoolean getAppRunStatus(){
		return APP_RUN_STATUS;
	}
	
	/**
	 * 获取APP总状态是否还在运行
	 * @return
	 */
	public static boolean isRunning(){
		return APP_RUN_STATUS.get();
	}
	
	/**
	 * 关闭APP，把APP总运行状态设置成false
	 */
	public static void closeApp(){
		APP_RUN_STATUS.set(false);
	}
	
	/**
	 * 加载日志配置
	 * 默认值加载logback
	 */
	protected abstract void loadConfig();

	/**
	 * 防止程序意外退出没有关闭资源，通过钩子函数来清空资源，主要关闭连接，释放列表等。
	 */
	public void startShutDownHook() {

		LOG.info("程序启动钩子函数监听功能...");
		//注册一个新的虚拟机关闭钩子
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					LOG.info("开始执行钩子函数的任务...");
					closeApp();
					executeShutDownHook();
				} catch (Throwable e) {
					LOG.error("钩子函数执行处理任务时操作异常...");
				}
				LOG.info("钩子函数执行完毕，系统已完全关闭！");
			}
		});
		LOG.info("程序启动钩子函数监听功能完毕");
	}
	
	/**
	 * 程序的启动处理流程
	 * 只需要实现该方法即可
	 * @throws Exception
	 * @param args
	 */
	protected abstract void execute(String[] args) throws Exception;
	
	/**
	 * 在此处执行具体处理的资源操作。
	 */
	protected abstract void executeShutDownHook();

	/**
	 * 打印版本信息
	 */
	protected abstract void printVersionInfo();
}
