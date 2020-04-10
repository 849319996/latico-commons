package com.latico.commons.hadoop;

import com.latico.commons.common.util.json.FastJsonUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * <PRE>
 * 一个FileSystem对象，可以打开多个不同的文件，使用完要关闭那个文件的流
 * </PRE>
 *
 * @author: latico
 * @date: 2020-03-23 17:06
 * @version: 1.0
 */
public class HdfsUtils {

    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(HdfsUtils.class);

    public static final String DEFAULT_HDFS_URI_KEY = "fs.defaultFS";
    public static final String MAPRED_JOB_TRACKER_KEY = "mapred.job.tracker";
    /**
     * 关闭HDFS文件系统对象
     * get方法不是每次都创建FileSystem对象，会从缓存中获取FileSystem对象，
     * 而newInstance方法则会每次都创建新对象。所以在使用该对象的API编程时，推荐使用get方法
     * @param fileSystem
     */
    public static void close(FileSystem fileSystem) {
        if (fileSystem != null) {
            try {
                fileSystem.close();
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
    }

    /**
     * 获取HDFS配置信息
     *
     * @param hdfsUrl
     * @return
     */
    public static Configuration getConfiguration(String hdfsUrl) {
        Configuration configuration = new Configuration();
        configuration.set(DEFAULT_HDFS_URI_KEY, hdfsUrl);
        configuration.set(MAPRED_JOB_TRACKER_KEY, hdfsUrl);
        return configuration;
    }


    /**
     * 获取HDFS文件系统对象
     * get方法不是每次都创建FileSystem对象，会从缓存中获取FileSystem对象，
     * 而newInstance方法则会每次都创建新对象。所以在使用该对象的API编程时，推荐使用get方法
     *
     * @param hdfsUrl
     * @param hdfsUsername
     * @return
     * @throws Exception
     */
    public static FileSystem getFileSystem(String hdfsUrl, String hdfsUsername) throws Exception {
        if (StringUtils.isBlank(hdfsUsername)) {
            return getFileSystem(hdfsUrl);
        }

        // 客户端去操作hdfs时是有一个用户身份的，默认情况下hdfs客户端api会从jvm中获取一个参数作为自己的用户身份
        // DHADOOP_USER_NAME=hadoop
        // 也可以在构造客户端fs对象时，通过参数传递进去
        return FileSystem.get(new URI(hdfsUrl), getConfiguration(hdfsUrl), hdfsUsername);
    }

    /**
     * 获取HDFS文件系统对象
     *
     * @param hdfsUrl
     * @return
     * @throws Exception
     */
    public static FileSystem getFileSystem(String hdfsUrl) throws Exception {
        // 客户端去操作hdfs时是有一个用户身份的，默认情况下hdfs客户端api会从jvm中获取一个参数作为自己的用户身份
        // DHADOOP_USER_NAME=hadoop
        // 也可以在构造客户端fs对象时，通过参数传递进去
        return FileSystem.get(new URI(hdfsUrl), getConfiguration(hdfsUrl));
    }

    /**
     * 获取HDFS文件系统对象
     *
     * @param hdfsUrl
     * @return
     * @throws Exception
     */
    public static FileSystem newInstanceFileSystem(String hdfsUrl) throws Exception {
        // 客户端去操作hdfs时是有一个用户身份的，默认情况下hdfs客户端api会从jvm中获取一个参数作为自己的用户身份
        // DHADOOP_USER_NAME=hadoop
        // 也可以在构造客户端fs对象时，通过参数传递进去
        return FileSystem.newInstance(new URI(hdfsUrl), getConfiguration(hdfsUrl));
    }


    /**
     * 获取HDFS文件系统对象
     * get方法不是每次都创建FileSystem对象，会从缓存中获取FileSystem对象，
     * 而newInstance方法则会每次都创建新对象。所以在使用该对象的API编程时，推荐使用get方法
     *
     * @param hdfsUrl
     * @param hdfsUsername
     * @return
     * @throws Exception
     */
    public static FileSystem newInstanceFileSystem(String hdfsUrl, String hdfsUsername) throws Exception {
        if (StringUtils.isBlank(hdfsUsername)) {
            return newInstanceFileSystem(hdfsUrl);
        }
        // 客户端去操作hdfs时是有一个用户身份的，默认情况下hdfs客户端api会从jvm中获取一个参数作为自己的用户身份
        // DHADOOP_USER_NAME=hadoop
        // 也可以在构造客户端fs对象时，通过参数传递进去
        return FileSystem.newInstance(new URI(hdfsUrl), getConfiguration(hdfsUrl), hdfsUsername);
    }


    /**
     * 在HDFS创建文件夹
     *
     * @param fs
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean mkdir(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        if (existPath(fs, path)) {
            return true;
        }
        // 目标路径
        Path srcPath = new Path(path);
        boolean isOk = fs.mkdirs(srcPath);
        return isOk;
    }

    /**
     * 判断HDFS文件是否存在
     *
     * @param fs
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean existPath(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        Path srcPath = new Path(path);
        return fs.exists(srcPath);
    }

    /**
     * 读取HDFS目录信息
     *
     * @param fs
     * @param path
     * @return
     * @throws Exception
     */
    public static FileStatus[] readPathInfo(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        if (!existPath(fs, path)) {
            return null;
        }
        // 目标路径
        Path newPath = new Path(path);
        FileStatus[] statusList = fs.listStatus(newPath);

        return statusList;
    }

    /**
     * HDFS创建文件
     *
     * @param fs
     * @param path
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void createFile(FileSystem fs, String path, String fileName, byte[] data) throws IOException {
        FSDataOutputStream outputStream = null;
        try {
            if (StringUtils.isBlank(path) || null == data) {
                return;
            }
            // 上传时默认当前目录，后面自动拼接文件的目录
            Path newPath = new Path(path + "/" + fileName);
            // 打开一个输出流
            outputStream = fs.create(newPath);
            outputStream.write(data);
        } finally {
            com.latico.commons.common.util.io.IOUtils.close(outputStream);
        }
    }


    /**
     * 读取HDFS文件列表
     *
     * @param fs
     * @param path
     * @return
     * @throws Exception
     */
    public static RemoteIterator<LocatedFileStatus> listFile(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        if (!existPath(fs, path)) {
            return null;
        }

        // 目标路径
        Path srcPath = new Path(path);
        // 递归找到所有文件
        RemoteIterator<LocatedFileStatus> filesList = fs.listFiles(srcPath, true);
        return filesList;
    }

    /**
     * 查询文件状态
     * @param fs
     * @param path
     * @return 不存在时，返回null
     */
    public static FileStatus getFileStatus(FileSystem fs, String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        Path srcPath = new Path(path);
        try {
            return fs.getFileStatus(srcPath);
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * HDFS重命名文件
     *
     * @param fs
     * @param oldName
     * @param newName
     * @return
     * @throws Exception
     */
    public static boolean renameFile(FileSystem fs, String oldName, String newName) throws Exception {
        if (StringUtils.isBlank(oldName) || StringUtils.isBlank(newName)) {
            return false;
        }
        // 原文件目标路径
        Path oldPath = new Path(oldName);
        // 重命名目标路径
        Path newPath = new Path(newName);
        boolean isOk = fs.rename(oldPath, newPath);
        return isOk;
    }

    /**
     * 删除HDFS路径
     *
     * @param fs
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean deleteFile(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        Path srcPath = new Path(path);
        boolean isOk = fs.deleteOnExit(srcPath);
        return isOk;
    }

    /**
     * 删除HDFS路径
     *
     *
     * @param fs
     * @param path
     * @param recursive 永久性删除指定的文件或目录，如果f是一个空目录或者文件，那么recursive的值就会被忽略。只有recursive＝true时，一个非空目录及其内容才会被删除。
     * @return
     * @throws Exception
     */
    public static boolean deleteFile(FileSystem fs, String path, boolean recursive) throws Exception {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        Path srcPath = new Path(path);
        boolean isOk = fs.delete(srcPath, recursive);
        return isOk;
    }

    /**
     * 删除目录
     * @param fs 文件系统
     * @param path 目录路径
     * @return
     * @throws Exception
     */
    public static boolean deleteDir(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        Path srcPath = new Path(path);
        boolean isOk = fs.delete(srcPath, true);
        return isOk;
    }

    /**
     * 上传HDFS文件
     *
     * @param fs
     * @param localFilePath
     * @param hdfsFilePath
     * @throws Exception
     */
    public static void uploadFile(FileSystem fs, String localFilePath, String hdfsFilePath) throws Exception {
        if (StringUtils.isBlank(localFilePath) || StringUtils.isBlank(hdfsFilePath)) {
            return;
        }
        // 上传路径
        Path localPath = new Path(localFilePath);
        // 目标路径
        Path serverPath = new Path(hdfsFilePath);

        // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
        fs.copyFromLocalFile(false, localPath, serverPath);
    }

    /**
     * 下载HDFS文件到本地文件
     *
     * @param fs
     * @param localFilePath
     * @param hdfsFilePath
     * @throws Exception
     */
    public static void downloadFile(FileSystem fs, String localFilePath, String hdfsFilePath) throws Exception {
        if (StringUtils.isBlank(localFilePath) || StringUtils.isBlank(hdfsFilePath)) {
            return;
        }
        // 上传路径
        Path clientPath = new Path(localFilePath);
        // 目标路径
        Path serverPath = new Path(hdfsFilePath);

        // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
        fs.copyToLocalFile(false, clientPath, serverPath);
    }

    /**
     * HDFS文件复制
     * 把HDFS中的一个文件复制到HDFS中的另外一个文件中
     *
     * @param fs
     * @param sourcePath
     * @param targetPath
     * @param bufferSize
     * @throws Exception
     */
    public static void copyFile(FileSystem fs, String sourcePath, String targetPath, int bufferSize) throws Exception {
        if (StringUtils.isBlank(sourcePath) || StringUtils.isBlank(targetPath)) {
            return;
        }
        // 原始文件路径
        Path oldPath = new Path(sourcePath);
        // 目标路径
        Path newPath = new Path(targetPath);

        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;
        try {
            inputStream = fs.open(oldPath);
            outputStream = fs.create(newPath);
            org.apache.hadoop.io.IOUtils.copyBytes(inputStream, outputStream, bufferSize, false);
        } finally {
            com.latico.commons.common.util.io.IOUtils.close(inputStream);
            com.latico.commons.common.util.io.IOUtils.close(outputStream);
        }
    }

    /**
     * 打开HDFS上的文件并返回byte数组
     *
     * @param fs
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] readFileDataToBytes(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        if (!existPath(fs, path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(srcPath);
            return org.apache.hadoop.io.IOUtils.readFullyToByteArray(inputStream);
        } finally {
            com.latico.commons.common.util.io.IOUtils.close(inputStream);
        }

    }

    /**
     * 读取HDFS文件内容
     *
     * @param fs
     * @param path
     * @return
     * @throws Exception
     */
    public static String readFileDataToString(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        if (!existPath(fs, path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(srcPath);
            // 防止中文乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = "";
            StringBuilder sb = new StringBuilder();
            while ((lineTxt = reader.readLine()) != null) {
                sb.append(lineTxt);
            }
            return sb.toString();
        }  finally {
            com.latico.commons.common.util.io.IOUtils.close(inputStream);
        }
    }


    /**
     * 打开HDFS上的文件并返回java对象
     *
     * @param fs
     * @param path
     * @param clazz
     * @return
     * @throws Exception
     * @param <T>
     */
    public static <T extends Object> T readFileDataToObject(FileSystem fs, String path, Class<T> clazz) throws Exception {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        if (!existPath(fs, path)) {
            return null;
        }
        String jsonStr = readFileDataToString(fs, path);
        return FastJsonUtils.jsonToObj(jsonStr, clazz);
    }

    /**
     * 获取某个文件在HDFS的集群位置
     *
     * @param fs
     * @param path
     * @return
     * @throws Exception
     */
    public static BlockLocation[] getFileBlockLocations(FileSystem fs, String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        if (!existPath(fs, path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        FileStatus fileStatus = fs.getFileStatus(srcPath);
        return fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    }

}
