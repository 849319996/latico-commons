package com.latico.commons.common.util.io;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.other.PathUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * properties文件工具
 * @Author: LanDingDong
 * @Date: 2018/12/11 1:25
 * @Version: 1.0
 */
public class PropertiesUtils {

    private static final String defaultCharset = CharsetType.UTF8;

    /**
     * @param propsString
     * @return
     * @throws IOException
     */
    public static Properties fromString(String propsString) throws IOException {
        return fromString(propsString, defaultCharset);
    }

    /**
     * 通过字符串读成properties
     * @param propsString
     * @param encoding
     * @return
     * @throws IOException
     */
    public static Properties fromString(String propsString, String encoding) throws IOException {
        Properties out = new Properties();
        if (propsString != null) {
            byte[] bytes = propsString.getBytes(encoding);
            out.load(new ByteArrayInputStream(bytes));
        }
        return out;
    }

    /**
     * 通过文件路径读取properties文件，返回对应的properties对象
     *
     * @param path properties文件路径
     * @return
     */
    public static Properties readPropertiesFromFile(String path) throws IOException {
        return readPropertiesFromFile(path, defaultCharset);
    }
    /**
     * 通过文件路径读取properties文件，返回对应的properties对象
     *
     * @param path properties文件路径
     * @return
     */
    public static Properties readPropertiesFromFile(String path, String encoding) throws IOException {
        return fromString(FileUtils.readFileToString(path, encoding), encoding);
    }

    /**
     * 从资源文件中加载
     * @param path
     * @return
     * @throws IOException
     */
    public static Properties readPropertiesFromResources(String path) throws IOException {
        return readPropertiesFromResources(path, defaultCharset);
    }

    /**
     * 从资源文件中加载
     * @param path
     * @param encoding
     * @return
     * @throws IOException
     */
    public static Properties readPropertiesFromResources(String path, String encoding) throws IOException {
        path = PathUtils.formatResourcesPathForClassLoader(path);
        return fromString(IOUtils.resourceToStringByClassLoader(path, encoding), encoding);
    }

    public static Map<String, Object> toMapStrObj(Properties properties) {
        Map<String, Object> map = new HashMap<>();
        if (properties == null) {
            return map;
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue());
        }
        return map;
    }
}
