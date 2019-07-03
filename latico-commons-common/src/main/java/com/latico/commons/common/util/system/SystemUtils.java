package com.latico.commons.common.util.system;

import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.string.StringUtils;
import com.sun.management.OperatingSystemMXBean;
import sun.misc.Launcher;
import sun.misc.URLClassPath;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;

/**
 * <PRE>
 * OS工具类
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2018/12/16 03:02:29
 * @Version: 1.0
 */
public class SystemUtils extends org.apache.commons.lang3.SystemUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SystemUtils.class);
    /**
     * 操作系统字符集编码
     */
    protected final static String OS_ENCODING =
            System.getProperty("sun.jnu.encoding").toUpperCase();

    /**
     * 程序入口命令.
     * 用于判断程序运行环境：
     * 1.通过tomcat运行的J2EE项目为固定值 org.apache.catalina.startup.Bootstrap start
     * 2.通过main运行的J2SE项目为main入口类的类名
     */
    private final static String SJC = System.getProperty("sun.java.command");
    protected final static String RUN_EVN = (SJC == null ? "" : SJC);

    private final static boolean RUN_BY_TOMCAT =
            RUN_EVN.startsWith("org.apache.catalina.startup.Bootstrap");

    /**
     * <PRE>
     * 判断当前操作系统位宽是否为64位.
     * （主要针对win, linux由于兼容32和64, 只能用64位）.
     * <p>
     * system 32位： x86
     * system 64位：amd64
     * linux 32位: i386
     * linux 64位：amd64
     * <PRE>
     *
     * @return true:64; false:32
     */
    public static boolean isX64() {
        return OS_ARCH.contains("64");
    }

    /**
     * <PRE>
     * 判断当前操作系统位宽是否为32位.
     * <PRE>
     *
     * @return true:64; false:32
     */
    public static boolean isX32() {
        return !isX64();
    }

    /**
     * 获取操作系统字符集编码
     *
     * @return 操作系统字符集编码
     */
    public static String getSysEncoding() {
        return OS_ENCODING;
    }

    /**
     * 检查当前程序是否通过tomcat启动
     *
     * @return true:通过tomcat启动; false:通过main启动
     */
    public static boolean isRunByTomcat() {
        return RUN_BY_TOMCAT;
    }

    /**
     * 复制文本到剪切板
     *
     * @param txt 文本内容
     */
    public static void copyToClipboard(final String txt) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(txt), null);
    }

    /**
     * 从剪切板获得文字
     *
     * @return 文本内容
     */
    public static String pasteFromClipboard() {
        String txt = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tf = sysClip.getContents(null);
        if (tf != null) {
            if (tf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    txt = (String) tf.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                }
            }
        }
        return txt;
    }

    /**
     * 获取当前系统可用的字体列表
     *
     * @return 可用字体列表
     */
    public static String[] getSysFonts() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return env.getAvailableFontFamilyNames();
    }

    /**
     * 获取进程启动锁(文件锁定方式).
     * --------------------------------------------------
     * 当要求程序只能在操作系统中运行一个进程时, 可使用此方法获取启动锁.
     * 当获取启动锁失败时, 配合System.exit终止程序即可.
     * --------------------------------------------------
     * 原理：
     * 程序每次运行时, 均在系统临时目录创建固有名称的临时文件, 该临时文件在程序终止时自动删除.
     * 由于文件不能被重复创建, 这样就确保了在系统中只能存在一个进程.
     * <p>
     * 缺陷:
     * 若使用非正常方式结束进程(如: kill -9), 会导致程序之后无法启动.
     *
     * @param processName 进程名（任意即可，但不能为空）
     * @return true:获取启动锁成功(程序只运行了一次); false:获取启动锁失败(程序被第运行了2次以上)
     */
    public static boolean getStartlock(String processName) throws IOException {
        boolean isOk = false;
        if (StringUtils.isNotBlank(processName)) {
            String tmpPath = PathUtils.combine(
                    PathUtils.getSysTmpDir(), "LOCK_".concat(processName));
            if (!FileUtils.exists(tmpPath)) {
                File tmpFile = FileUtils.createFile(tmpPath);
                isOk = (tmpFile != null);
                tmpFile.deleteOnExit(); // 程序终止时删除该临时文件
            }
        }
        return isOk;
    }

    /**
     * @return java启动命令
     */
    public static String getJavaStartCommand() {
        return SJC;
    }

    /**
     * @return 系统环境变量
     */
    public static String sysEnvPath() {
        return JAVA_CLASS_PATH;
    }

    /**
     * @return BootStrap ClassLoader加载的文件列表信息
     */
    public static String getBootStrapClassLoaderLoadFileInfo() {
        return System.getProperty("sun.boot.class.path");
    }


    /**
     * @return ExtClassLoader加载的文件列表信息
     */
    public static String getExtClassLoaderLoadFileInfo() {
        return JAVA_EXT_DIRS;
    }

    /**
     * @return 所有java类文件路径信息
     */
    public static String getJavaClassPath() {
        return JAVA_CLASS_PATH;
    }

    /**
     * 获得根类加载器所加载的核心类库,并会看到本机安装的Java环境变量指定的jdk中提供的核心jar包路径
     *
     * @return
     */
    public static URLClassPath getBootstrapClassPath() {
        URLClassPath bootstrapClassPath = Launcher.getBootstrapClassPath();

        return bootstrapClassPath;
    }

    /**
     * 操作系统磁盘可用空间大小（一般跟空闲空间大小相对）
     *
     * @return 单位B
     */
    public static long getOsDiskUsableSpaceSize() {
        File[] disks = File.listRoots();
        long size = 0;
        for (File file : disks) {
            size += file.getUsableSpace();
        }
        return size;
    }


    /**
     * 操作系统磁盘已用空间大小
     *
     * @return 单位B
     */
    public static long getOsDiskUsedSpaceSize() {

        File[] disks = File.listRoots();
        long freeSpaceSize = 0;
        long totalSpaceSize = 0;
        for (File file : disks) {
            freeSpaceSize += file.getFreeSpace();
            totalSpaceSize += file.getTotalSpace();
        }
        return totalSpaceSize - freeSpaceSize;
    }

    /**
     * 操作系统磁盘空闲空间大小
     *
     * @return 单位B
     */
    public static long getOsDiskFreeSpaceSize() {
        File[] disks = File.listRoots();
        long size = 0;
        for (File file : disks) {
            size += file.getFreeSpace();
        }
        return size;
    }

    /**
     * 操作系统磁盘总空间大小
     *
     * @return 单位B
     */
    public static long getOsDiskTotalSpaceSize() {
        File[] disks = File.listRoots();
        long size = 0;
        for (File file : disks) {
            size += file.getTotalSpace();
        }
        return size;
    }

    /**
     * 操作系统空闲的物理内存大小
     * 换算单位1024
     *
     * @return 字节数量，单位B
     */
    public static long getOsFreePhysicalMemorySize() {
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return mem.getFreePhysicalMemorySize();
    }

    /**
     * 操作系统总物理内存大小
     * 换算单位1024
     *
     * @return 字节数量，单位B
     */
    public static long getOsTotalPhysicalMemorySize() {
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return mem.getTotalPhysicalMemorySize();
    }

    /**
     * 操作系统总交换区空间大小
     * 换算单位1024
     *
     * @return 字节数量，单位B
     */
    public static long getOsTotalSwapSpaceSize() {
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return mem.getTotalSwapSpaceSize();
    }

    /**
     * 操作系统交换区空闲空间大小
     * 换算单位1024
     *
     * @return 字节数量，单位B
     */
    public static long getOsFreeSwapSpaceSize() {
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return mem.getFreeSwapSpaceSize();
    }

    /**
     * @return 字节数量，单位B
     */
    public static long getJvmFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * @return 字节数量，单位B
     */
    public static long getJvmTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * @return 字节数量，单位B
     */
    public static long getJvmMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    public static String getLocalMac() throws Exception {
        StringBuffer sb = new StringBuffer();
        InetAddress ia = InetAddress.getLocalHost();
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 获取本地网卡的IP
     *
     * @return
     */
    public static List<String> getAllLocalInetAddressIP() {
        List<String> ips = new ArrayList<>();
        List<InetAddress> allLocalRealInetAddress = getAllLocalRealInetAddress();
        for (InetAddress localRealInetAddress : allLocalRealInetAddress) {
            ips.add(localRealInetAddress.getHostAddress());
        }

        return ips;
    }

    /**
     * 获取本地网卡的主机名
     *
     * @return
     */
    public static Set<String> getAllLocalInetAddressHostName() {
        Set<String> hostnames = new HashSet<>();
        List<InetAddress> allLocalRealInetAddress = getAllLocalRealInetAddress();
        for (InetAddress localRealInetAddress : allLocalRealInetAddress) {
            hostnames.add(localRealInetAddress.getHostName());
        }

        return hostnames;
    }

    /**
     * 本地主机名
     *
     * @return
     */
    public static String getLocalHostName() {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostName();
            return hostAddress;
        } catch (UnknownHostException e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 本机的主机，当前是使用了本机的IP
     *
     * @return
     */
    public static String getLocalHost() {
        return getLocalHostAddress();
    }

    /**
     * 本机的IP
     *
     * @return
     */
    public static String getLocalHostAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error(e);
        }

        return null;
    }

    /**
     * 获取本机所有IP（排除掉了127.0.0.1和链路本地IP）
     *
     * @return
     */
    public static List<InetAddress> getAllLocalRealInetAddress() {
        List<InetAddress> inetAddresses = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        inetAddresses.add(inetAddress);
                    }
                }
            }
        } catch (SocketException e) {
            LOG.error(e);
        }

        return inetAddresses;
    }


}
