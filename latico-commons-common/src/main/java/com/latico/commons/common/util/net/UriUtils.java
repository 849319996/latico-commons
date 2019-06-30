package com.latico.commons.common.util.net;

import java.io.File;
import java.net.URL;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-22 0:46
 * @Version: 1.0
 */
public class UriUtils {
    /**
     * 把文件路径转成成URL对象
     * @param filePath 文件的系统路径
     * @return
     * @throws Exception
     */
    public static URL convertFilePathToUrl(String filePath) throws Exception {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        return file.toURI().toURL();
    }

    /**
     * 把文件路径转成成URL对象
     * @param filePath 文件的系统路径
     * @return
     * @throws Exception
     */
    public static URL convertFilePathToJarUrl(String filePath) throws Exception {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        return convertFileUrlToJarUrl(file.toURI().toURL());
    }

    /**
     * 把文件URL转换成Jar的JRL
     * @param url
     * @return
     * @throws Exception
     */
    public static URL convertFileUrlToJarUrl(URL url) throws Exception {
        String urlStr = url.toString();
        if (urlStr.startsWith("jar:") && urlStr.contains("!/")) {
            return url;
        }
        return new URL("jar:" + urlStr + "!/");
    }
}
