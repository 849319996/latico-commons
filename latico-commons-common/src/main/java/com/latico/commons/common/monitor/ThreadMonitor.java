package com.latico.commons.common.monitor;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 * 线程监控心跳工具
 * </PRE>
 * @project   <B>项目</B>
 * @copyright <B>技术支持：latico</B>
 * @version   <B>V1.0 2016年7月22日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class ThreadMonitor extends Thread {

	/** 系统换行符 \r\n \n */
	public final static String LINE_SEPARATOR = System.getProperty("line.separator");

	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(ThreadMonitor.class);

	/** instance 单例实例对象 */
	private static ThreadMonitor instance;
	
	/** switchStatus 开关状态 */
	private boolean runStatus = true;
	
	/** appRunStatus APP运行状态，此值由外界调用startMonitor启动方式传入，一旦监控到APP关闭，此监控器也同时关闭自身，如果不传入，默认此状态一直为true */
	private AtomicBoolean appRunStatus;
	
	/** THREAD_MONITOR 线程队列，key最好为自定义线程名称，而value是具体的系统默认定义的线程名称代号 */
	private ConcurrentHashMap<String, Thread> monitorThreads = new ConcurrentHashMap<String, Thread>();

	/** 时间默认格式：yyyy-MM-dd HH:mm:ss */
	public static final String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";
	
	/** timeFormatter 时间格式化工具  */
	private SimpleDateFormat timeFormatter = new SimpleDateFormat(PATTERN_TIME);
	
	/** threadName 线程名称 */
	private String threadName = "线程监控器";
	
	/**
	 * 心跳获取的当前时间
	 */
	protected String hbTime = getNowTime();

	private ThreadMonitor() {
	}
	public static ThreadMonitor getInstance() {
		if(instance == null) {
			synchronized (Class.class) {
				if(instance == null) {
					instance = new ThreadMonitor();
				}
			}
		}
		return instance;
	}

	/**
	 * 心跳周期
	 * 默认一分钟打印一次被监控线程的状态
	 * 单位：毫秒
	 */
	protected int cycleTime = 60000;

	@Override
	public void run() {
		
		//监控器状态true和APP状态也是true时，线程继续运行
		boolean hasRunningThread = true;
		
		//如果程序还在运行或者有正在运行的线程，则继续监听
		while (isRunning() || hasRunningThread) {
			hasRunningThread = false;
			hbTime = getNowTime();
			StringBuffer hb = new StringBuffer();
			hb.append(LINE_SEPARATOR);
			hb.append("心跳时间：" + hbTime + " ,当前监控的线程总数量：" + monitorThreads.size() + LINE_SEPARATOR);
			for (Map.Entry<String, Thread> thread : monitorThreads.entrySet()) {
				hb.append("线程名称 [");
				hb.append(thread.getKey());
				hb.append("], 状态:");
				hb.append(thread.getValue().getState());
				if(thread.getValue().getState() == State.RUNNABLE){
					hasRunningThread = true;
				}
				hb.append(LINE_SEPARATOR);
			}
			LOG.warn(hb.toString());
			
			sleepWithCheckStop(cycleTime, 1000);
			
		}
		LOG.info("关闭线程监控线程完成");
	}
	
	/**
	 * 判断是否还在运行
	 * @return
	 */
	public boolean isRunning() {
		if(appRunStatus == null){
			return runStatus;
		}else{
			return runStatus && appRunStatus.get();
		}
	}
	
	/**
	 * 返回当前时间的yyyy-MM-dd HH:mm:ss格式字符串
	 * 
	 * @return str
	 */
	private String getNowTime() {
		Date nowDate = new Date();
		// Calendar now = Calendar.getInstance();
		// now.setTime(nowDate);
		String str = timeFormatter.format(nowDate);
		return str;
	}

	/**
	 * 停止监控，结束此工具，同时清空监控队列
	 */
	public void stopMonitor() {
		if(runStatus){
			runStatus = false;
			monitorThreads.remove(threadName);
		}
	}

	/**
	 * 建议使用startMonitor()方法启动此工具
	 */
	@Override
	public void start() {
		if(!runStatus){
			runStatus = true;
			addThread(threadName, this);
			LOG.info("线程监控队列添加监控线程");
			super.start();
		}
	}
	
	/**
	 * 启动线程监控器
	 * @param appRunStatus 可以传入null，APP状态值，此值用于判断APP是否已经运行结束，监控器以此条件也可以结束自身
	 */
	public void startMonitor(AtomicBoolean appRunStatus) {
		this.appRunStatus = appRunStatus;
		start();
	}
	
	/**
	 * 添加一个被监控的线程,被监控线程的名称使用输入线程的名称
	 * @param thread 被监控的线程
	 * @return 是否添加成功，假如队列中已经存在此线程，则添加失败
	 */
	public boolean addThread(Thread thread){
		return addThread(thread.getName(), thread);
	}
	
	/**
	 * 添加一个被监控的线程
	 * @param threadName 被监控线程的名称
	 * @param thread 被监控的线程
	 * @return 是否添加成功，假如队列中已经存在此线程，则添加失败
	 */
	public boolean addThread(String threadName, Thread thread){
		boolean b = false;
		if(monitorThreads.containsKey(threadName)){
			LOG.warn("线程监控器添加线程 [失败]，已经存在相同的线程名称:{}", thread);
			b = false;
		}else{
			monitorThreads.put(threadName, thread);
			b = true;
		}
		LOG.info("线程监控器添加线程 [{}] {}", threadName, b?"成功":"失败");
		return b;
	}
	
	/**
	 * 根据移除一个线程
	 * @param threadName 被移除监控的线程名称
	 * @return 假如不存在此线程名称的线程，那就返回false
	 */
	public boolean removeThread(String threadName){
		boolean b = false;
		Thread thread = monitorThreads.remove(threadName);
		if(thread != null){
			b = true;
		}else{
			b = false;
		}
		LOG.info("线程监控器移除线程 [{}] {}", threadName, b?"成功":"失败");
		return b;
	}
	
	/**
	 * 睡眠，同时检测运行状态是否中断，
	 * 一般用于睡眠过程中同时检测线程状态或者系统状态是否关闭，
	 * 建议睡眠较长时间时使用。
	 * @param overallTime 睡眠总时长
	 * @param interval 检测间隔
	 */
	private void sleepWithCheckStop(final long overallTime, final int interval) {
		final long start = System.currentTimeMillis();
		while((System.currentTimeMillis() - start) <= overallTime){
			if(!runStatus || !appRunStatus.get()){
				break;
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				LOG.error("", e);
			}
		}
	}
	
}
