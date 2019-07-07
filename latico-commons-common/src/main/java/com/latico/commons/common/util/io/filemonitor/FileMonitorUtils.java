package com.latico.commons.common.util.io.filemonitor;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * <PRE>
 * 文件监控器工具
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-20 14:07
 * @Version: 1.0
 */
public class FileMonitorUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileMonitorUtils.class);

    /**
     * 创建一个apache实现的文件变更监控器
     *
     * apache使用了观察者模式，文件监控器监听到文件变更后，通知给所有的观察者，
     * 观察者执行调用者传入的所有业务逻辑对象
     *
     * @param filePath 所监听的文件/文件夹位置 (若监控的是文件夹，则该文件夹下所有文件和子目录均会被监控)
     * @param fileAlterationHandlers 业务处理器，该对象实现业务逻辑，对变更事件进行具体的处理
     * @param scanInterval 文件/文件夹扫描间隔(ms),如果小于等于0，那么就是1秒扫描一次
     * @return
     */
    public static FileAlterationMonitor createFileAlterationMonitor(String filePath, long scanInterval, FileAlterationListener... fileAlterationHandlers) throws Exception {
        if (fileAlterationHandlers == null || filePath == null) {
            throw new NullPointerException("传入数据有空");
        }
        //如果时间过小，就使用500毫秒扫描一次
        scanInterval = (scanInterval <= 0 ? 1000 : scanInterval);

        // 创建一个文件变更观察者
        FileAlterationObserver observer = new FileAlterationObserver(new File(filePath));
        //把文件变更事件处理逻辑添加到观察者中
        for (FileAlterationListener fileAlterationHandler : fileAlterationHandlers) {
            observer.addListener(fileAlterationHandler);
        }

        //指定扫描间隔创建监控器
        FileAlterationMonitor monitor = new FileAlterationMonitor(scanInterval);

        //把观察者注册到监控器中
        monitor.addObserver(observer);

        return monitor;
    }

    /**
     * 启动监听器
     */
    public static boolean start(FileAlterationMonitor monitor) {
        try {
            monitor.start();
            return true;
        } catch (Exception e) {
            LOG.error("启动文件监听器失败.", e);
        }
        return false;
    }

    /**
     * 停止监听器
     */
    public static boolean stop(FileAlterationMonitor monitor) {
        try {
            monitor.stop();
            return true;
        } catch (Exception e) {
            LOG.error("停止文件监听器失败.", e);
        }
        return false;
    }
}
