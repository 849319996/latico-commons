package com.latico.commons.common.util.io.filemonitor;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.junit.Test;

public class FileMonitorUtilsTest {

    @Test
    public void createFileAlterationMonitor() {
        FileAlterationListener fileAlterationHandler = new FileAlterationListenerImplExample();
        try {
            FileAlterationMonitor fileAlterationMonitor = FileMonitorUtils.createFileAlterationMonitor("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\123", 2000, fileAlterationHandler);

            fileAlterationMonitor.start();

            Thread.sleep(600000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}