package com.latico.commons.common.util.thread.pool;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>
 * 线程池(自定义回调值类型).
 * 一、线程任务提交。
 * 1、当提交一个新任务到线程池时首先线程池判断基本线程池(corePoolSize)是否已满？没满，
 * 创建一个工作线程来执行任务。满了，则进入下个流程；
 * 2、其次线程池判断工作队列(workQueue)是否已满？
 * 没满，则将新提交的任务存储在工作队列里。满了，则进入下个流程；
 * 3、最后线程池判断整个线程池(maximumPoolSize)是否已满？没满，则创建一个新的工作线程来执行任务，
 * 满了，则交给饱和策略来处理这个任务；
 * 4、如果线程池中的线程数量大于 corePoolSize 时，如果某线程空闲时间超过keepAliveTime，线程将被终止，
 * 直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，
 * 那么核心池中的线程空闲时间超过 keepAliveTime，线程也会被终止。
 *
 * 二、饱和策略(一般情况建议使用Abort策略，队列满了让外界接收异常处理，本线程池默认使用CallerRuns策略)：
 * Abort策略：默认策略，新任务提交时直接抛出未检查的异常RejectedExecutionException，该异常可由调用者捕获。
 * CallerRuns策略：为调节机制，既不抛弃任务也不抛出异常，而是将某些任务回退到调用者。不会在线程池的线程中执行新的任务，而是在调用exector的线程中运行新的任务。
 * Discard策略：新提交的任务被抛弃。
 * DiscardOldestPolicy ：抛弃旧的任务；
 *
 * 使用示例:
 * ==================================================================
 int maxWorkThreadSize = 10;
 BaseThreadPool<Object> threadPool = new BaseThreadPool<Object>(maxWorkThreadSize);

 //执行没有返回数据的任务
 threadPool.execute(new Runnable() {
@Override
public void run() {
System.out.println("线程任务");
}
});

 //安全关闭线程池，会等待任务执行完毕才关闭
 threadPool.shutdown();

 //等待线程池最大等待10s
 threadPool.awaitTerminationWithIntervalCheck(10000, 1000);
==================================================================
 BaseThreadPool<Object> threadPool = new BaseThreadPool<Object>(10,10,60, TimeUnit.SECONDS, 10000, new ThreadPoolExecutor.CallerRunsPolicy());

 //执行没有返回数据的任务
 threadPool.execute(new Runnable() {
@Override
public void run() {
System.out.println("线程任务");
}
});

 //安全关闭线程池，会等待任务执行完毕才关闭
 threadPool.shutdown();

 //等待线程池最大等待10s
 threadPool.awaitTerminationWithIntervalCheck(10000, 1000);

 ==================================================================

 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-01-15 15:38:40
 * @Version: 1.0
 */
public class BaseThreadPool<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseThreadPool.class);

    /**
     * 线程池总状态，true:运行,false:已调用过关闭方法
     */
    private AtomicBoolean status = new AtomicBoolean(true);

    /**
     * <pre>
     * 核心线程数，线程池维护线程的最少数量。
     *
     * 核心线程会一直存活，即使没有任务需要处理。
     * 当线程数小于核心线程数时，即使现有的线程空闲，
     * 		线程池也会优先创建新线程来处理任务，而不是直接交给现有的线程处理。
     * 核心线程在allowCoreThreadTimeout被设置为true时会超时退出，默认情况下不会退出。
     * </pre>
     */
    private int corePoolSize;

    /**
     * <pre>
     * 线程池维护线程的最大数量
     *
     * 当线程数大于或等于核心线程，且任务队列已满时，
     * 		线程池会创建新的线程，直到线程数量达到maxPoolSize。
     * 如果线程数已等于maxPoolSize，且任务队列已满，
     * 		则已超出线程池的处理能力，线程池会拒绝处理任务而抛出异常。
     * </pre>
     */
    private int maxPoolSize;

    /**
     * <pre>
     * 线程池维护线程所允许的空闲时间
     *
     * 当线程空闲时间达到keepAliveTime，该线程会退出，直到线程数量等于corePoolSize。
     * 如果allowCoreThreadTimeout设置为true，则所有线程均会退出直到线程数量为0。
     * </pre>
     */
    private long keepAliveTime;

    /**
     * 线程池维护线程所允许的空闲时间的单位
     */
    private TimeUnit unit;

    /**
     * 线程池所使用的任务队列容量
     */
    private int workQueueSize;

    /**
     * 任务队列
     */
    private BlockingQueue<Runnable> workQueue;

    /**
     * <pre>
     * 线程池对拒绝任务的处理策略：
     * 	1、ThreadPoolExecutor.AbortPolicy：抛出异常；
     * 	2、ThreadPoolExecutor.CallerRunsPolicy：不会在线程池的线程中执行新的任务，而是在调用execute方法的线程中运行新的任务
     * 	3、ThreadPoolExecutor.DiscardOldestPolicy ：抛弃旧的任务；
     * 	4、ThreadPoolExecutor.DiscardPolicy：抛弃当前的任务；
     * </pre>
     */
    private RejectedExecutionHandler reHandler;

    /**
     * JDK线程池
     */
    private ThreadPoolExecutor threadPool;

    /**
     * <pre>
     * 构造函数
     *
     * 根据八二定律初始化线程池（80%的任务可以由20%的核心线程处理）
     * 作为基数的任务量 taskNum
     * 则：
     * 	核心线程数 corePoolSize = taskNum * 20%
     * 	最大线程数 maxPoolSize = taskNum
     * 	任务队列容量workQueueSize = taskNum * 80%
     * 计算值不足1的，按1处理。
     *
     * 另外：
     * 	线程允许空闲时间keepAliveTime = 5s
     * 	对拒绝任务的处理策略为CallerRunsPolicy（重试添加当前的任务）
     * </pre>
     *
     * @param maxWorkThreadSize 最大工作线程数量
     */
    public BaseThreadPool(int maxWorkThreadSize) {
        maxWorkThreadSize = (maxWorkThreadSize <= 0 ? 10 : maxWorkThreadSize);
        this.corePoolSize = (int) (maxWorkThreadSize * 0.2);
        this.corePoolSize = (this.corePoolSize <= 0 ? 1 : this.corePoolSize);
        this.maxPoolSize = maxWorkThreadSize;
        this.workQueueSize = maxWorkThreadSize - this.corePoolSize;
        this.workQueueSize = (this.workQueueSize <= 0 ? 1 : this.workQueueSize);

        this.keepAliveTime = 5;
        this.unit = TimeUnit.SECONDS;
        this.reHandler = new ThreadPoolExecutor.CallerRunsPolicy();

        this.workQueue = new ArrayBlockingQueue<Runnable>(workQueueSize);
        LOG.info("创建线程池[核心线程数量:{},最大线程数量:{},空闲时保持激活时间:{},时间单位:{},工作队列大小:{},饱和策略:{}]", corePoolSize, maxPoolSize,
                keepAliveTime, unit, this.workQueueSize, reHandler.getClass().getSimpleName());
        this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, unit, workQueue, reHandler);
    }

    /**
     * <pre>
     * 构造函数
     *
     * 线程允许空闲时间keepAliveTime = 5s
     * 对拒绝任务的处理策略为CallerRunsPolicy（重试添加当前的任务）
     * </pre>
     *
     * @param corePoolSize  核心线程数
     * @param maxPoolSize   线程池维护线程的最大数量
     * @param keepAliveTime 线程池维护线程所允许的空闲时间
     * @param workQueueSize 线程池所使用的任务队列容量
     */
    public BaseThreadPool(int corePoolSize, int maxPoolSize,
                          long keepAliveTime, int workQueueSize) {

        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = TimeUnit.SECONDS;
        this.workQueueSize = workQueueSize;
        this.reHandler = new ThreadPoolExecutor.CallerRunsPolicy();

        this.workQueue = new ArrayBlockingQueue<Runnable>(this.workQueueSize);
        LOG.info("创建线程池[核心线程数量:{},最大线程数量:{},空闲时保持激活时间:{},时间单位:{},工作队列大小:{}],饱和策略:{}", corePoolSize, maxPoolSize,
                keepAliveTime, unit, this.workQueueSize, reHandler.getClass().getSimpleName());
        this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, unit, workQueue, reHandler);
    }

    /**
     * <pre>
     * 构造函数
     * </pre>
     *
     * @param corePoolSize  核心线程数
     * @param maxPoolSize   线程池维护线程的最大数量
     * @param keepAliveTime 线程池维护线程所允许的空闲时间
     * @param unit          线程池维护线程所允许的空闲时间的单位
     * @param workQueueSize 线程池所使用的任务队列容量
     * @param reHandler     线程池对拒绝任务的处理策略
     */
    public BaseThreadPool(int corePoolSize, int maxPoolSize,
                          long keepAliveTime, TimeUnit unit, int workQueueSize,
                          RejectedExecutionHandler reHandler) {

        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueueSize = workQueueSize;
        this.reHandler = reHandler;

        this.workQueue = new ArrayBlockingQueue<Runnable>(workQueueSize);
        LOG.info("创建线程池[核心线程数量:{},最大线程数量:{},空闲时保持激活时间:{},时间单位:{},工作队列大小:{}],饱和策略:{}", corePoolSize, maxPoolSize,
                keepAliveTime, unit, this.workQueueSize, reHandler.getClass().getSimpleName());
        this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, unit, workQueue, reHandler);
    }

    /**
     * <pre>
     * 把任务线程放入线程池执行
     * </pre>
     *
     * @param command 任务线程
     */
    public void execute(Runnable command) {
        threadPool.execute(command);
    }

    /**
     * <pre>
     * 把任务线程放入线程池执行（有返回值）
     * </pre>
     *
     * @param command 任务线程
     * @return 线程返回值，通过Future.get()方法获取
     */
    public Future<T> submit(Callable<T> command) {
        return threadPool.submit(command);
    }

    /**
     * <pre>
     * 获取活动线程数
     * </pre>
     *
     * @return 活动线程数
     */
    public int getActiveCount() {
        return threadPool.getActiveCount();
    }

    /**
     * @return 统计执行任务总数，近似值，非准确值
     */
    public long getTaskCount() {
        return threadPool.getTaskCount();
    }

    /**
     * @return 统计任务队列里面的任务总数，统计期间不暂停任务执行导致的队列变化
     */
    public long getQueueSize() {
        return threadPool.getQueue().size();
    }

    /**
     * <pre>
     * 关闭线程池（会自动等待所有 [已经添加进队列的线程] 运行结束再关闭）
     *
     * 当线程池调用该方法时,线程池的状态则立刻变成SHUTDOWN状态。此时，则不能再往线程池中添加任何任务，否则将会抛出RejectedExecutionException异常。但是，此时线程池不会立刻退出，直到添加到线程池中的任务都已经处理完成，才会退出。
     * </pre>
     */
    public void shutdown() {
        status.set(false);
        threadPool.shutdown();
    }

    /**
     * <pre>
     * 立即关闭线程池（会等待所有 [正在运行的线程] 运行结束再关闭，对于已经在队列中但是未执行的任务进行丢弃处理）
     *
     * 根据JDK文档描述，大致意思是：执行该方法，线程池的状态立刻变成STOP状态，并试图停止所有正在执行的线程，不再处理还在池队列中等待的任务，当然，它会返回那些未执行的任务。
     * </pre>
     */
    public void shutdownNow() {
        status.set(false);
        threadPool.shutdownNow();
    }

    /**
     * 判断线程池中的任务是否全部执行完毕（该方法只有在shutdown执行后才会返回true）
     *
     * @return true:线程池已终止; false:存在线程运行中
     */
    public boolean isTerminated() {
        return threadPool.isTerminated();
    }

    /**
     * @return  当前使用的线程池
     */
    public ThreadPoolExecutor getCurrrentThreadPool() {
        return threadPool;
    }

    /**
     * 一直等待线程池到达Terminated状态
     *
     * @param maxWaitTime 最大等待时间，单位毫秒
     * @return true:线程池已终止; false:存在线程运行中
     */
    public boolean awaitTermination(long maxWaitTime) {
        try {
            return threadPool.awaitTermination(maxWaitTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * 与awaitTermination的区别在于，awaitTermination是不会间隔检测，
     *
     * @param maxWaitTime       最大等待时间，单位毫秒
     * @param checkTimeInterval 检测是否Terminated的过程中，隔多久检测一次，
     * @return
     */
    public boolean awaitTerminationWithIntervalCheck(long maxWaitTime, long checkTimeInterval) {
        boolean succFinished = false;
        final long startTime = System.currentTimeMillis();
        final long endTimePoint = startTime + maxWaitTime;
        while (status.get()) {

            //判断是否终结了
            if (threadPool.isTerminated()) {
                succFinished = true;
                LOG.info("线程池 [" + threadPool.toString() + "] 正常结束, 耗时:" + (System.currentTimeMillis() - startTime));
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
            if (endTimePoint <= System.currentTimeMillis()) {
                succFinished = false;
                LOG.info("线程池 [" + threadPool.toString() + "] 执行超时方式结束, 耗时:" + (System.currentTimeMillis() - startTime));
                break;
            }

            //等待
            awaitTermination(checkTimeInterval);
        }
        return succFinished;
    }

}
