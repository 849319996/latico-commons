package com.latico.commons.cephfs.libcephfs;

import com.ceph.fs.CephMount;
import com.ceph.fs.CephStat;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.io.*;

/**
 * <PRE>
 * LibCephFs工具类
 * 注意目前只支持在linux环境下运行，同时还要配置字LibCephFs所需要的底层类库
 * 底层是使用C/C++写的类库
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-09 15:08
 * @version: 1.0
 */
public class LibCephFsUtils {
    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(LibCephFsUtils.class);

    /**
     * 挂载ceph文件系统的目录
     * cephMount卸载目录后可以重新挂载
     * @param cephMount
     * @param path
     * @return
     */
    public static boolean mount(CephMount cephMount, String path) {
        if (cephMount != null) {
            cephMount.mount(path);
            return true;
        }
        return false;
    }

    /**
     * 卸载
     * 如果要重新用，可以从新挂载
     * @param cephMount
     */
    public static void unmount(CephMount cephMount) {
        if (cephMount != null) {
            cephMount.unmount();
        }
    }
    /**
     * 创建一个cephFS的连接
     * 不挂载
     * 要使用的时候
     * @param id           admin是ceph的admin用户
     * @param key          key来自于ceph环境的/etc/ceph/ceph.client.admin.keyring里面的key
     * @param mountDir     挂载目录，可以为空，那么就不挂载，建议创建的时候挂载到根目录（/），这样不用手工再调用挂载方法
     * @param monitorHosts 10.112.101.141;10.112.101.142;10.112.101.143是ceph集群的mon节点，有多少个写多少个
     * @return
     */
    public static CephMount createCephMount(String id, String key, String mountDir, String... monitorHosts) {
        CephMount mount = new CephMount(id);
        StringBuilder sb = new StringBuilder();
        sb.append(monitorHosts[0]);
        for (int i = 1; i < monitorHosts.length; i++) {
            sb.append(";").append(monitorHosts[i]);
        }
        mount.conf_set("mon_host", sb.toString());

        mount.conf_set("key", key);

        //挂载目录
        if (mountDir != null && mountDir.startsWith("/")) {
            mount.mount(mountDir);
        }

        return mount;
    }

    /**
     * 创建目录
     * @param cephMount 文件系统挂载对象
     * @param dirPath 要创建的路径目录
     * @param mode 文件权限，举例，如果是rwxrwxrwx对应的数字是:0777；如果是rw-rw-rw-对应的数字是:0666
     */
    public static boolean mkdirs(CephMount cephMount, String dirPath, int mode) {
        try {
            cephMount.mkdirs(dirPath, mode);
            return true;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return false;
    }

    /**
     * 默认权限创建目录
     * @param cephMount
     * @param dirPath
     * @throws IOException
     */
    public static boolean mkdirs(CephMount cephMount, String dirPath) {
        return mkdirs(cephMount, dirPath, 0666);
    }

    /**
     * @param cephMount
     * @param dirPath
     * @return
     */
    public static String[] listdir(CephMount cephMount, String dirPath)  {
        String[] paths = null;
        try {
            paths = cephMount.listdir(dirPath);
        } catch (Exception e) {
            LOG.error("", e);
        }
        return paths;
    }

    /**
     * @param cephMount
     * @param dirPath
     * @return
     */
    public static boolean rmdir(CephMount cephMount, String dirPath) {
        try {
            cephMount.rmdir(dirPath);
            return true;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return false;
    }

    /**
     * @param cephMount
     * @param filePath
     * @return
     */
    public static CephStat getFileStatus(CephMount cephMount, String filePath) {
        return getFileStatus(cephMount, filePath, true);
    }

    /**
     * @param cephMount
     * @param filePath
     * @param isPrintErrLog
     * @return
     */
    public static CephStat getFileStatus(CephMount cephMount, String filePath, boolean isPrintErrLog) {
        CephStat stat = null;
        try {
            stat = new CephStat();
            cephMount.stat(filePath, stat);
            return stat;
        } catch (Exception e) {
            if (isPrintErrLog) {
                LOG.error("", e);    
            }
        }
        return null;
    }

    /**
     * @param cephMount
     * @param filePath
     * @return
     */
    public static byte[] readFile(CephMount cephMount, String filePath) {
        CephStat stat = new CephStat();
        Integer fd = null;
        try {
            fd = cephMount.open(filePath, CephMount.O_RDONLY, 0);
            cephMount.fstat(fd, stat);
            byte[] buffer = new byte[(int) stat.size];
            cephMount.read(fd, buffer, stat.size, 0);
            return buffer;
        } catch (Exception e) {
            LOG.error("", e);
        }finally {
            if (cephMount != null && fd != null) {
                cephMount.close(fd);    
            }
        }
        return null;
    }

    /**
     * 上传文件
     * @param cephMount 
     * @param localFilePath 本地文件路径
     * @param cephFsFilePath 远端文件路径
     * @return 上传结果状态
     * @throws Exception
     */
    public static boolean uploadFile(CephMount cephMount, String localFilePath, String cephFsFilePath) {
        if (FileUtils.notExists(localFilePath)) {
            LOG.info("File not exist! {}", cephFsFilePath);
            return false;
        }
        Boolean fileExist = false;
        CephStat fileStatus = getFileStatus(cephMount, cephFsFilePath, false);
        if (fileStatus != null && fileStatus.isFile()) {
            fileExist = true;
        }

        //文件存在，就断点续传
        if (fileExist) {
            return uploadFileByBreakpoint(cephMount, localFilePath, cephFsFilePath);
        } else {
            return uploadFileByNew(cephMount, localFilePath, cephFsFilePath);
        }

    }

    /**
     * 断点续传方式上传文件
     * @param cephMount 
     * @param localFilePath
     * @param cephFsFilePath
     * @return
     */
    private static boolean uploadFileByBreakpoint(CephMount cephMount, String localFilePath, String cephFsFilePath) {

        FileInputStream fis = null;
        Integer fd = null;
        try {
            File file = new File(localFilePath);
            long fileLength = file.length();
            fis = new FileInputStream(file);
            // get file length
            CephStat stat = new CephStat();
            cephMount.stat(cephFsFilePath, stat);
            long uploadedLength = stat.size;
            fd = cephMount.open(cephFsFilePath, CephMount.O_RDWR, 0);

            // start transfer
            int length = 0;
            byte[] bytes = new byte[1024];
            fis.skip(uploadedLength);
            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                // write
                cephMount.write(fd, bytes, length, uploadedLength);

                // update length
                uploadedLength += length;

                // output transfer rate
                float rate = (float) uploadedLength * 100 / (float) fileLength;
                String rateValue = (int) rate + "%";
                LOG.info(rateValue);

                // complete flag
                if (uploadedLength == fileLength) {
                    break;
                }
            }
            LOG.info("断点文件传输成功！{}", cephFsFilePath);

            // chmod
            cephMount.fchmod(fd, 0666);

            return true;
        } catch (Exception e) {
            LOG.error("BreakPoint transfer failed!" + cephFsFilePath, e);
        }finally {
            IOUtils.close(fis);
            closeFile(cephMount, fd);
        }
        return false;
    }

    /**
     * 关闭文件
     * @param cephMount 
     * @param fileDescriptor
     */
    private static void closeFile(CephMount cephMount, Integer fileDescriptor) {
        if (cephMount != null && fileDescriptor != null) {
            cephMount.close(fileDescriptor);
        }
    }

    /**
     * 新文件方式上传
     * @param cephMount 
     * @param localFilePath
     * @param cephFsFilePath
     * @return
     */
    private static boolean uploadFileByNew(CephMount cephMount, String localFilePath, String cephFsFilePath) {
        FileInputStream fis = null;
        Integer fd = null;
        try {
            File file = new File(localFilePath);
            long uploadedLength = 0;
            long fileLength = file.length();
            fis = new FileInputStream(file);
            // create file and set mode WRITE
            cephMount.open(cephFsFilePath, CephMount.O_CREAT, 0);
            fd = cephMount.open(cephFsFilePath, CephMount.O_RDWR, 0);

            // start transfer
            int length = 0;
            byte[] bytes = new byte[1024];
            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                // write
                cephMount.write(fd, bytes, length, uploadedLength);

                // update length
                uploadedLength += length;

                // output transfer rate
                float rate = (float) uploadedLength * 100 / (float) fileLength;
                String rateValue = (int) rate + "%";
                LOG.info(rateValue);

                // complete flag
                if (uploadedLength == fileLength) {
                    break;
                }
            }
            LOG.info("文件传输成功！{}", cephFsFilePath);

            // chmod
            cephMount.fchmod(fd, 0666);

            return true;
        } catch (Exception e) {
            LOG.error("File transfer failed! " + cephFsFilePath, e);
        }finally {
            IOUtils.close(fis);
            closeFile(cephMount, fd);
        }
        return false;
    }

    /**
     * 下载文件
     * @param cephMount
     * @param localFilePath
     * @param cephFsFilePath
     * @return
     */
    public static boolean downloadFile(CephMount cephMount, String localFilePath, String cephFsFilePath) {

        if (FileUtils.notExists(localFilePath)) {
            return downloadFileByNew(cephMount, localFilePath, cephFsFilePath);
        } else {
            return downloadFileByBreakpoint(cephMount, localFilePath, cephFsFilePath);
        }
    }

    /**
     * 断点续传方式下载
     * @param cephMount
     * @param localFilePath
     * @param cephFsFilePath
     * @return
     */
    private static boolean downloadFileByBreakpoint(CephMount cephMount, String localFilePath, String cephFsFilePath) {
        // download file
        int length = 10240;
        byte[] bytes = new byte[length];
        File file = new File(localFilePath);
        Long filePoint = file.length();
        long downloadedLength = 0;
        // IO
        RandomAccessFile raf = null;

        // new file object

        CephStat stat = getFileStatus(cephMount, localFilePath);
        long fileLength = stat.size;
        if (fileLength <= 0) {
            return false;
        }

        Integer fd = null;
        try {
            fd = cephMount.open(cephFsFilePath, CephMount.O_RDONLY, 0);
            raf = new RandomAccessFile(file, "rw");
            raf.seek(filePoint);
            downloadedLength = filePoint;
            float rate = 0;
            String rateValue = "";
            while ((fileLength - downloadedLength) >= length && (cephMount.read(fd, bytes, (long) length, downloadedLength)) != -1) {
                raf.write(bytes, 0, length);
                downloadedLength += (long) length;

                // output transfer rate
                rate = (float) downloadedLength * 100 / (float) fileLength;
                rateValue = (int) rate + "%";
                LOG.info("{}下载过程:{}", cephFsFilePath, rateValue);

                if (downloadedLength == fileLength) {
                    break;
                }
            }
            if (downloadedLength != fileLength) {
                cephMount.read(fd, bytes, fileLength - downloadedLength, downloadedLength);
                raf.write(bytes, 0, (int) (fileLength - downloadedLength));
                downloadedLength = fileLength;

                // output transfer rate
                rate = (float) downloadedLength * 100 / (float) fileLength;
                rateValue = (int) rate + "%";
                LOG.info("{}下载过程:{}", cephFsFilePath, rateValue);
            }

            LOG.info("Cut Point Download Success! {}", cephFsFilePath);
            return true;
        } catch (Exception e) {
            LOG.error("Continue download fail!" + cephFsFilePath, e);
        } finally {
            IOUtils.close(raf);
            closeFile(cephMount, fd);
        }
        return false;
    }

    /**
     * 全新方式下载
     * @param cephMount
     * @param localFilePath
     * @param cephFsFilePath
     * @return
     */
    private static boolean downloadFileByNew(CephMount cephMount, String localFilePath, String cephFsFilePath) {
        // download file
        int length = 10240;
        byte[] bytes = new byte[length];
        Integer fd = null;
        long downloadedLength = 0;
        FileOutputStream fos = null;

        CephStat stat = getFileStatus(cephMount, localFilePath);
        long fileLength = stat.size;
        if (fileLength <= 0) {
            return false;
        }

        try {
            File file = new File(localFilePath);
            fd = cephMount.open(cephFsFilePath, CephMount.O_RDONLY, 0);
            fos = new FileOutputStream(file);
            float rate = 0;
            String rateValue = "";
            while ((fileLength - downloadedLength) >= length && (cephMount.read(fd, bytes, (long) length, downloadedLength)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
                downloadedLength += (long) length;

                // output transfer rate
                rate = (float) downloadedLength * 100 / (float) fileLength;
                rateValue = (int) rate + "%";
                LOG.info("{}下载过程:{}", cephFsFilePath, rateValue);

                if (downloadedLength == fileLength) {
                    break;
                }
            }
            if (downloadedLength != fileLength) {
                cephMount.read(fd, bytes, fileLength - downloadedLength, downloadedLength);
                fos.write(bytes, 0, (int) (fileLength - downloadedLength));
                fos.flush();
                downloadedLength = fileLength;

                // output transfer rate
                rate = (float) downloadedLength * 100 / (float) fileLength;
                rateValue = (int) rate + "%";
                LOG.info("{}下载过程:{}", cephFsFilePath, rateValue);
            }

            LOG.info("Download Success! {}", cephFsFilePath);
            return true;
        } catch (Exception e) {
            LOG.error("First download fail!" + cephFsFilePath, e);
        } finally {
            IOUtils.close(fos);
            closeFile(cephMount, fd);
        }
        return false;
    }
}
