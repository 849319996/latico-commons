package com.latico.commons.net.socket.nio.tcp.server.handler;

import com.latico.commons.common.util.compare.CompareHelper;
import com.latico.commons.common.util.compare.CompareResult;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.thread.pool.ThreadPool;
import com.latico.commons.net.socket.nio.tcp.common.SelectionKeyHandler;
import com.latico.commons.net.socket.nio.tcp.server.TcpNioSocketServer;
import com.latico.commons.net.socket.nio.tcp.server.handler.service.TcpNioServerServiceTask;

import java.nio.channels.SelectionKey;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 * 不断轮询TcpNioSocketServer，从它那里拿到SelectionKey，然后放进线程池执行，
 * 因为只要SelectionKey一次交互未完成或者正在处理中，下轮的轮询还是会轮询到那个SelectionKey，
 * 为了防止多个线程同时操作同一个SelectionKey，所以要对SelectionKey的执行做统一管理，
 *
 * 有点像Proactor模式，轮询一次SelectionKey，不管上次的SelectionKey执行完成没有，都去轮询下一次
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-20 22:10
 * @version: 1.0
 */
public class TcpNioSocketServerHandler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(TcpNioSocketServerHandler.class);
    private final TcpNioSocketServer nioSocketServer;
    private AtomicBoolean status = new AtomicBoolean(false);
    private ThreadPool threadPool;

    private static final int TIMEOUT = 3000;

    private Class<? extends TcpNioServerServiceTask> serviceTaskClass;
    private String charset = "UTF-8";
    private int perTaskReceiveQueueSize = 10000;
    /**
     * 当前正在运行的SelectionKey和它的处理器的映射
     */
    private final Map<SelectionKey, TcpNioServerServiceTask> currentRuningSelectionKeys;

    public TcpNioSocketServerHandler(TcpNioSocketServer nioSocketServer, Class<? extends TcpNioServerServiceTask> nioServerServiceTaskClass) {
        this(nioSocketServer, nioServerServiceTaskClass, 10, 1000, "UTF-8", 10000);
    }

    public TcpNioSocketServerHandler(TcpNioSocketServer nioSocketServer, Class<? extends TcpNioServerServiceTask> nioServerServiceTaskClass, int threadPoolSize, String charset) {
        this(nioSocketServer, nioServerServiceTaskClass, threadPoolSize, 1000, charset, 10000);
    }

    /**
     * @param nioSocketServer           服务器Socket连接
     * @param nioServerServiceTaskClass 执行任务的Task类
     * @param threadPoolSize            线程池线程数量，建议10-100
     * @param threadPoolWorkQueueSize   线程池队列大小，建议10000-1000000
     * @param charset                   字符集，建议UTF-8
     * @param perTaskReceiveQueueSize   每个客户端连接跟服务端连接后，每次进行交互的时候缓存交互数据时的队列大小,建议1000
     */
    public TcpNioSocketServerHandler(TcpNioSocketServer nioSocketServer, Class<? extends TcpNioServerServiceTask> nioServerServiceTaskClass, int threadPoolSize, int threadPoolWorkQueueSize, String charset, int perTaskReceiveQueueSize) {
        this.charset = charset;
        this.perTaskReceiveQueueSize = perTaskReceiveQueueSize;

        this.nioSocketServer = nioSocketServer;
        this.serviceTaskClass = nioServerServiceTaskClass;
        this.currentRuningSelectionKeys = new ConcurrentHashMap<>();

        //创建执行任务的线程池
        this.threadPool = new ThreadPool(threadPoolSize, threadPoolSize, 30000, threadPoolWorkQueueSize);
    }

    @Override
    public void run() {
        LOG.info("启动TcpNioSocketServerHandler");
        try {
            status.set(true);
            startListenSelectionKey();
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            close();
        }
        LOG.info("TcpNioSocketServerHandler线程结束");
    }

    /**
     * 启动监听服务器的SelectionKey
     *
     * @throws Exception
     */
    private void startListenSelectionKey() throws Exception {
        //一分钟打印一次监控线程日志
        final long printLogTime = 60000;
        final long printLogInterval = TIMEOUT;
        final long printLogCount = printLogTime / printLogInterval - 1;
        long count = 0;

        long lastEmptyTime = 0;
        long lastHasTime = 0;
        while (true) {

            if (!isVaild()) {
                LOG.error("已关闭");
                break;
            }
            //检查当前列表，如果有完成的就移除
            checkFinished();

            Set<SelectionKey> selectionKeys = nioSocketServer.getAllSelectionKey(TIMEOUT);
            if (selectionKeys != null && selectionKeys.size() >= 1) {
                syncHandleNewSelectionKeys(selectionKeys);

                //处理完，清空
                selectionKeys.clear();
            } else {
                //没有取到任务就打印日志证明还线程在执行
                if (count == 0) {
                    LOG.info("TcpServerHandler监听TcpSocketServer队列,获取selectionKeys");
                }

                //判断是否打印日志
                if (count >= printLogCount) {
                    count = 0;
                } else {
                    count++;
                }

                //如果两次获取到数据为空的时间短与100ms，说明可能发生了JDK的NIO的连接异常后死循环问题，此时睡眠，防止CPU消耗殆尽
                if (System.currentTimeMillis() - lastEmptyTime <= 100) {
                    LOG.error("指定监听时间内获取SelectionKey无效，NIO可能发生了死循环，请检查");
                    Thread.sleep(1000);
                }
                lastEmptyTime = System.currentTimeMillis();
            }
        }
    }

    public boolean isVaild() {
        return status.get() && nioSocketServer.getStatus();
    }

    /**
     * 同步处理新的SelectionKey
     *
     * @param selectionKeys
     * @throws Exception
     */
    private void syncHandleNewSelectionKeys(Set<SelectionKey> selectionKeys) throws Exception {

        //为了防止一个任务被多个线程执行，需要判断是否存在
        CompareResult<SelectionKey> selectionKeyCompareResult = CompareHelper.diffCompareByHashCode(selectionKeys, currentRuningSelectionKeys.keySet());

        Collection<SelectionKey> newObjs = selectionKeyCompareResult.getNewObjs();
        if (newObjs.size() >= 1) {
            for (SelectionKey newObj : newObjs) {
                TcpNioServerServiceTask serviceHandler = createServiceTasks(newObj);
                if (serviceHandler != null) {
                    LOG.info("添加新任务:{}", serviceHandler.getTaskId());
                    //添加到正在运行队列和放进线程池中
                    currentRuningSelectionKeys.put(newObj, serviceHandler);
                    threadPool.execute(serviceHandler);
                }
            }

        }else{
            // XXX 因为差异比较后为空，说明队列有未执行完的SelectionKey，而且本次没有新的SelectionKey，避免浪费CPU轮询，本次睡眠50毫秒
            Thread.sleep(50);
        }

    }

    /**
     * 校验队列里面的任务是否有完成上次任务处理的，完成了就进行队列移除
     */
    private void checkFinished() {
        Iterator<Map.Entry<SelectionKey, TcpNioServerServiceTask>> iterator = currentRuningSelectionKeys.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<SelectionKey, TcpNioServerServiceTask> entry = iterator.next();
            TcpNioServerServiceTask task = entry.getValue();
            if (task.isFinished()) {
                LOG.info("任务完成,从队列移除:{}", task.getTaskId());
                iterator.remove();
            }
        }
    }

    /**
     * 创建任务
     *
     * @param newObj
     * @return
     * @throws Exception
     */
    private TcpNioServerServiceTask createServiceTasks(SelectionKey newObj) throws Exception {
        TcpNioServerServiceTask tcpServerServiceTask = serviceTaskClass.newInstance();
        SelectionKeyHandler selectionKeyHandler = new SelectionKeyHandler(newObj);
        tcpServerServiceTask.setSelectionKeyHandler(selectionKeyHandler);
        tcpServerServiceTask.setCharset(charset);
        tcpServerServiceTask.setReceiveQueueSize(perTaskReceiveQueueSize);
        return tcpServerServiceTask;
    }

    public void close() {
        status.set(false);
        threadPool.shutdownNow();
        nioSocketServer.close();
    }

    /**
     * 启动线程
     *
     * @return
     */
    public boolean startAsThread() {
        if (status.get()) {
            LOG.warn("TcpNioSocketServerHandler线程已经启动");
            return false;
        } else {
            new Thread(this).start();
            return true;
        }
    }
}
