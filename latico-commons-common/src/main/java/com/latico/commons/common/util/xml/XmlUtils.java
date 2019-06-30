package com.latico.commons.common.util.xml;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.codec.CodecUtils;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.io.flowreader.FileFlowReader;
import com.latico.commons.common.util.io.flowreader.StringFlowReader;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.string.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML工具,公共部分，默认Dom4jUtils，
 * 建议针对性使用，比如
 * 1、{@link Dom4jUtils}
 * 2、{@link JdomUtils}
 * 3、{@link DomUtils}
 * 4、{@link SaxUtils}
 *
 *
 1）DOM、JDOM、DOM4j都是把xml文档读取到内存中，生成dom对象进行遍历的；

 DOM是Java原生的，所以比较繁琐；

 JDOM是对DOM操作的封装，更加通俗、易记，操作也快了一点；

 DOM4j解析xml的函数上与JDOM差不多，只不过有几个相同功能的函数名字不同而已，过程都是一样的；但由于底层使用了Xpath等方法加快了索引，所以检索性能更快。

 2）SAX是基于事件驱动的，查询事件监听器继承自DefaultHandler，定义了检索xml过程中遇到开始标签、结束标签时执行的事件函数，从而查找需要的信息并返回而不是把整个文档都加载进来。

 * @Author: LanDingDong
 * @Date: 2018/12/09 3:48
 * @Version: 1.0
 */
public class XmlUtils {
    public static final String DEFAULT_CHARSET = CharsetType.UTF8;
    public static final Pattern encodingPattern = Pattern.compile(" encoding=\"([^\"]+)\"");
    /** 默认编码 */

    /**
     * 读取XML文件为String
     *
     * @param filePath 文件路径
     * @return
     */
    public static String readXmlFileToString(String filePath) throws IOException {
        File file = new File(filePath);
        return readXmlFileToString(file);
    }
    /**
     * 读取XML文件为String
     *
     * @param file 文件
     * @return
     */
    public static String readXmlFileToString(File file) throws IOException {
        String charset = getXmlHeadEncoding(file);
        //默认UTF-8
        if (StringUtils.isBlank(charset)) {
            charset = CharsetType.UTF8;
        }
        return FileUtils.readFileToString(file, charset);
    }

    /**
     * 读取XML文件为String
     *
     * @param filePath 文件路径
     * @param charset  指定字符集
     * @return
     * @throws IOException
     */
    public static String readXmlFileToString(String filePath, String charset) throws IOException {
        return FileUtils.readFileToString(filePath, charset);
    }

    /**
     * 读取资源目录的XML文件为String
     *
     * @param resourcesfilePath 资源文件路径，如果没有以/开头，此方法会自动补全
     * @return
     */
    public static String readResourcesXmlFileToString(String resourcesfilePath) throws IOException {
        String str = IOUtils.resourceToStringByClassLoader(resourcesfilePath, CharsetType.ISO);
        String charset = getXmlHeadEncoding(str);
        //默认UTF-8
        if (StringUtils.isBlank(charset)) {
            charset = CharsetType.UTF8;
        }
        return IOUtils.resourceToStringByClassLoader(resourcesfilePath, charset);
    }

    /**
     * 读取资源目录的XML文件为String
     *
     * @param resourcesfilePath 资源文件路径，如果没有以/开头，此方法会自动补全
     * @return
     */
    public static String readResourcesXmlFileToString(String resourcesfilePath, String charset) throws IOException {
        return IOUtils.resourceToStringByClassLoader(resourcesfilePath, charset);
    }

    /**
     * 移除xml中的空行
     *
     * @param xml     xml报文
     * @param charset xml编码
     * @return 移除空行后的xml
     */
    public static String removeEmptyLines(String xml, String charset) {
        StringBuilder sb = new StringBuilder();
        StringFlowReader sfr = new StringFlowReader(xml, charset);
        while (sfr.hasNextLine()) {
            String line = sfr.readLine('>');
            sb.append(line.trim());
        }
        sfr.close();
        return sb.toString();
    }

    /**
     * 从xml声明头中截取编码信息
     *
     * @param xmlFile xml文件对象
     * @return xml编码(若未声明编码 ， 则返回默认编码UTF - 8)
     */
    public static String getXmlHeadEncoding(File xmlFile) {
        String charset = DEFAULT_CHARSET;
        FileFlowReader ffr = new FileFlowReader(xmlFile, CharsetType.ISO);
        if (ffr.hasNextLine()) {
            String headLine = ffr.readLine();
            Matcher mth = encodingPattern.matcher(headLine);
            if (mth.find()) {
                charset = mth.group(1);
                if (!CodecUtils.isVaild(charset)) {
                    charset = DEFAULT_CHARSET;
                }
            }
        }
        ffr.close();
        return charset;
    }

    /**
     * 从xml声明头中截取编码信息
     *
     * @param xmlContent xml内容
     * @return xml编码(若未声明编码 ， 则返回默认编码UTF - 8)
     */
    public static String getXmlHeadEncoding(String xmlContent) {
        String charset = DEFAULT_CHARSET;
        StringFlowReader sfr = new StringFlowReader(xmlContent, "ISO-8859-1");
        ;
        try {
            while (sfr.hasNextLine()) {
                Matcher mth = encodingPattern.matcher(sfr.readLine());
                if (mth.find()) {
                    charset = mth.group(1);
                    if (!CodecUtils.isVaild(charset)) {
                        charset = DEFAULT_CHARSET;
                    }
                }
            }
        } finally {
            sfr.close();
        }
        return charset;
    }

    /**
     * 构造命名空间地址
     *
     * @param prefix 地址前缀
     * @param _URI   唯一地址标识值
     * @return 命名空间地址
     */
    public static String toNamespaceURL(String prefix, String _URI) {
        StringBuilder sb = new StringBuilder();
        if ((prefix != null) && (prefix.length() > 0)) {
            sb.append(" xmlns:");
            sb.append(prefix);
            sb.append("=\"");

        } else {
            sb.append(" xmlns=\"");
        }

        sb.append(_URI);
        sb.append("\"");
        return sb.toString();
    }

    /**
     * <PRE>
     * 构造xml完整路径对应的压缩路径.
     * 如完整路径为 /root/test/one
     * 则压缩路径为/r/t/one
     * </PRE>
     *
     * @param path xml完整路径
     * @return xml压缩路径
     */
    public static String toCompressPath(String path) {
        String cmpPath = "";
        if (path != null && !"".equals(path)) {
            String[] nodes = PathUtils.toLinux(path).split("/");
            StringBuilder sb = new StringBuilder();

            if (nodes.length > 0) {
                for (int i = 0; i < nodes.length - 1; i++) {
                    if (!"".equals(nodes[i])) {
                        sb.append(nodes[i].charAt(0));
                    }
                    sb.append('/');
                }
                sb.append(nodes[nodes.length - 1]);

            } else {
                sb.append('/');
            }
            cmpPath = sb.toString();
        }
        return cmpPath;
    }

    /**
     * 校验xml完整路径和压缩路径是否匹配
     *
     * @param path         xml完整路径
     * @param compressPath xml压缩路径
     * @return true:匹配; false:不匹配
     */
    public static boolean matchesPath(String path, String compressPath) {
        boolean isMatches = false;
        if (path != null && compressPath != null) {
            String[] nodes = PathUtils.toLinux(path).split("/");
            String[] cmpNodes = PathUtils.toLinux(compressPath).split("/");

            if (nodes.length == cmpNodes.length) {
                isMatches = true;
                int size = nodes.length - 1;

                if (size >= 0) {
                    for (int i = 0; i < size; i++) {
                        isMatches &= (nodes[i].startsWith(cmpNodes[i]));
                    }
                    isMatches &= (nodes[size].equals(cmpNodes[size]));
                }
            }
        }
        return isMatches;
    }

}
