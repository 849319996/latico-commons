package com.latico.commons.common.util.thread;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 线程工具
 * @Author: latico
 * @Date: 2018/12/09 00:44:23
 * @Version: 1.0
 */
public class ThreadUtils extends org.apache.commons.lang3.ThreadUtils {

	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(ThreadUtils.class);
	/**
	 * 主线程名称
	 */
	static final String MAIN_TH_NAME = "main";

	/**
	 * 获取最合适的线程数量
	 * 处理器的数量减一，因为当前的线程占用了一个处理器，所以开线程池最好是处理器减一
	 * @return
	 */
	public static int getBestThreadSize() {
		return Runtime.getRuntime().availableProcessors() - 1;
	}

	public static ThreadGroup getMainThreadGroup() {
		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		while(!MAIN_TH_NAME.equals(threadGroup.getName()) && threadGroup.getParent() != null) {
			threadGroup = threadGroup.getParent();
		}
		return threadGroup;
	}
	/**
	 * 获得活动线程总数
	 * @return 线程总数
	 */
	public static int getMainThreadGroupActiveThreadCount() {
		return getMainThreadGroup().activeCount();
	}

	/**
	 * 获得活动线程总数
	 * @return 线程总数
	 */
	public static int getSystemThreadGroupActiveThreadCount() {
		return getSystemThreadGroup().activeCount();
	}
	
	/**
	 * 线程休眠(忙等)
	 * @param millis 休眠时间(ms)
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			LOG.error("线程休眠异常.", e);
		}
	}
	
	/**
	 * 线程阻塞(闲等)
	 * @param o 阻塞对象
	 * @param millis 阻塞时间(ms)
	 */
	public static void tWait(Object o, long millis) {
		if(o == null) {
			return;
		}
		
		try {
			synchronized (o) {
				o.wait(millis);
			}
		} catch (InterruptedException e) {
			LOG.error("线程阻塞异常.", e);
		}
	}
	
	/**
	 * 唤醒对象
	 * @param o 已阻塞对象
	 */
	public static void tNotify(Object o) {
		if(o == null) {
			return;
		}
		
		synchronized (o) {
			o.notify();;
		}
	}

	/**
	 * 线程睡眠指定时长
	 * @param i 毫秒
	 */
	public static void sleepMilliSeconds(long i) {

		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * 睡眠，同时检测运行状态是否中断,默认3秒检测一次
	 * @param overallTime
	 * @param isRunning
	 */
	public static void sleepWithCheckStop(long overallTime, AtomicBoolean isRunning) {
		sleepWithCheckStop(overallTime, 3000, isRunning);
	}

	/**
	 * 睡眠，同时检测运行状态是否中断，
	 * 一般用于睡眠过程中同时检测线程状态或者系统状态是否关闭，
	 * 建议睡眠较长时间时使用。
	 * @param overallTime 睡眠总时长
	 * @param interval 检测间隔
	 * @param isRunning 是否中断
	 */
	public static void sleepWithCheckStop(final long overallTime, final int interval, final AtomicBoolean isRunning) {
		final long start = System.currentTimeMillis();
		while((System.currentTimeMillis() - start) <= overallTime){
			if(!isRunning.get()){
				break;
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 睡眠秒为单位
	 * @param i
	 */
	public static void sleepSecond(int i) {

		try {
			TimeUnit.SECONDS.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * 获取无限长度固定大小的线程池
	 * @param poolSize
	 * @return
	 */
	public static ExecutorService getFixedThreadPool(int poolSize){
		return Executors.newFixedThreadPool(poolSize);
	}

	/**
	 *
	 * @param threadPoolSize
	 * @param maxWaitTime
	 * @param runnables
	 * @return
	 */
	public static List<Runnable> threadPoolWaitEnd(int threadPoolSize, long maxWaitTime, Runnable...runnables){
		if(runnables != null && runnables.length >= 1){
			List<Runnable> rs = new ArrayList<Runnable>();
			for(Runnable runnable : runnables){
				rs.add(runnable);
			}
			return threadPoolWaitEnd(threadPoolSize, maxWaitTime, rs);
		}
		return null;
	}

	/**
	 * 带返回数据
	 * @param threadPoolSize
	 * @param maxWaitTime
	 * @param tasks
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> invokeAll(int threadPoolSize, long maxWaitTime, Callable<T>...tasks) throws Exception {
		if(tasks != null && tasks.length >= 1){
			List<Callable<T>> rs = new ArrayList<Callable<T>>();
			for(Callable<T> runnable : tasks){
				rs.add(runnable);
			}
			return invokeAll(threadPoolSize, maxWaitTime, rs);
		}else{
			return null;
		}
	}

	/**
	 * callable方式，带换回数据
	 * @param threadPoolSize
	 * @param maxWaitTime
	 * @param tasks
	 * @return
	 * @throws Exception
	 */
	public static <T, E extends Callable<T>> List<T> invokeAll(int threadPoolSize, long maxWaitTime, Collection<E> tasks) throws Exception {
		if(tasks == null || tasks.size() <= 0){
			return null;
		}

		if(threadPoolSize <= 0){
			threadPoolSize = 5;
		}

		//执行线程池
		ExecutorService threadPool = getFixedThreadPool(threadPoolSize);
		List<Future<T>> futures;
		try {
			futures = threadPool.invokeAll(tasks, maxWaitTime, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			throw e;
		}finally{
			// 关闭线程池
			threadPool.shutdownNow();
		}

		List<T> results = new ArrayList<T>();
		if(futures != null){
			for(Future<T> future : futures){
				if(future != null){

					T result = future.get();
					if(result != null){
						results.add(result);
					}
				}
			}
		}

		return results;
	}


	public static <T extends Runnable> List<Runnable> threadPoolWaitEnd(int threadPoolSize, long maxWaitTime, Collection<T> runnables){
		if(runnables == null || runnables.size() <= 0){
			return null;
		}

		if(threadPoolSize <= 0){
			threadPoolSize = 5;
		}

		//执行线程池
		ExecutorService threadPool = getFixedThreadPool(threadPoolSize);
		for(Runnable runnable : runnables){
			threadPool.execute(runnable);
		}

		// 关闭线程池
		threadPool.shutdown();
		threadPoolWaitEnd(threadPool, maxWaitTime);
		return closeThreadPoolNow(threadPool);
	}

	public static boolean threadPoolWaitEnd(ExecutorService threadPool, long maxWaitTime){
		long startTime = System.currentTimeMillis();
		try {
			threadPool.awaitTermination(maxWaitTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
		if(threadPool.isTerminated()){
			System.out.println("线程池 [" + threadPool.toString() + "] 终结方式结束, 耗时:" + (System.currentTimeMillis() - startTime));
			return true;
		}else{
			System.err.println("线程池 [" + threadPool.toString() + "] 执行超时方式结束, 耗时:" + (System.currentTimeMillis() - startTime));
			return false;
		}
	}


	public static void closeThreadPool(ExecutorService threadPool){
		if(threadPool != null && !threadPool.isShutdown()){
			threadPool.shutdown();
		}
	}

	/**
	 * 立即关闭线程池，返回未执行完成的
	 * @param threadPool
	 * @return 未执行的线程
	 */
	public static List<Runnable> closeThreadPoolNow(ExecutorService threadPool){
		if(threadPool != null && !threadPool.isTerminated()){
			return threadPool.shutdownNow();
		}
		return null;
	}

	/**
	 * 用一个线程池执行所给的线程，
	 * 不建议使用。请转移到threadPoolWaitEndNew
	 * @param threadPoolSize 默认是5
	 * @param runnables
	 */
	@Deprecated
	public static List<Runnable> threadPoolWaitEndAndCheck(int threadPoolSize, long maxWaitTime, long checkEndInterval, Runnable...runnables){
		if(runnables != null && runnables.length >= 1){
			List<Runnable> rs = new ArrayList<Runnable>();
			for(Runnable runnable : runnables){
				rs.add(runnable);
			}
			return threadPoolWaitEndAndCheck(threadPoolSize, maxWaitTime, checkEndInterval, rs);
		}
		return null;
	}

	/**
	 * 用一个线程池执行所给的线程，
	 * 不建议使用，请转移到threadPoolWaitEndNew
	 * @param threadPoolSize 默认是5
	 * @param checkTimeInterval 每次校验是否线程池执行结束的睡眠时间，单位毫秒，默认100
	 * @param runnables 要执行的任务
	 */
	@Deprecated
	public static List<Runnable> threadPoolWaitEndAndCheck(int threadPoolSize, long maxWaitTime, long checkTimeInterval, List<Runnable> runnables){
		if(runnables == null || runnables.size() <= 0){
			return null;
		}

		if(threadPoolSize <= 0){
			threadPoolSize = 5;
		}
		if(checkTimeInterval <= 0){
			checkTimeInterval = 100;
		}

		//执行线程池
		ExecutorService threadPool = getFixedThreadPool(threadPoolSize);
		for(Runnable runnable : runnables){
			threadPool.execute(runnable);
		}

		// 关闭线程池
		threadPool.shutdown();
		threadPoolWaitEnd(threadPool, maxWaitTime, checkTimeInterval);
		return closeThreadPoolNow(threadPool);
	}


	/**
	 * 等待线程池执行结束，改方法不会执行threadPool.shutdown()方法，
	 * 不建议使用，转移到threadPoolWaitEndNew
	 * @param threadPool 线程池
	 * @param maxWaitTime 最大等待的毫秒数
	 * @param checkTimeInterval 每次检查是否结束的毫秒数
	 * @return 是否正常结束
	 */
	@Deprecated
	public static boolean threadPoolWaitEnd(ExecutorService threadPool, long maxWaitTime, long checkTimeInterval){
		boolean succFinished = false;
//		int waiteforexecuThreadCount = -1;
//		int runningExecThreadCount = -1;

		final long startTime = System.currentTimeMillis();
		final long endTimePoint = startTime + maxWaitTime;
		while (!succFinished) {

			//判断是否终结了
			if(threadPool.isTerminated()){
				succFinished = true;
				System.out.println("线程池 [" + threadPool.toString() + "] 终结方式结束, 耗时:" + (System.currentTimeMillis() - startTime));
				break;
			}

			//判断执行的线程是否为0 注释原因：此判断方法有问题，任务未完成，这两个队列都为空，因为getActiveCount()方法是不可靠的
//			waiteforexecuThreadCount = ((ThreadPoolExecutor)threadPool).getQueue().size();
//			runningExecThreadCount = ((ThreadPoolExecutor)threadPool).getActiveCount();
//			 if(waiteforexecuThreadCount == 0 && runningExecThreadCount == 0){
//				 succFinished = true;
//				 System.out.println("线程池 [" + threadPool.toString() + "] 执行队列为空方式结束");
//				break;
//			 }

			//判断是否达到超时时间
			if(endTimePoint <= System.currentTimeMillis()){
				succFinished = false;
				System.err.println("线程池 [" + threadPool.toString() + "] 执行超时方式结束, 耗时:" + (System.currentTimeMillis() - startTime));
				break;
			}

			sleepMilliSeconds(checkTimeInterval);
		}
		return succFinished;
	}
}
