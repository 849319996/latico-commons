package com.latico.commons.common.util.xml;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.math.NumberUtils;
import com.latico.commons.common.util.other.BooleanUtils;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.string.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <PRE>
 * Dom4j xml处理工具
 *
 Dom4j是目前最流行、最好用的XML解析工具，解析XML的速度最快。

 操作步骤：

 1：创建SAXReader：SAXReader reader = new SAXReader();

 2：创建文件输入流打开xml文件：InputStream in = new FileInputStream("XXX.xml");

 3：通过reader和输入流读取xml文件到内存创建Document对象：Document dom = reader.read(in);

 4：获取根节点：Element root=dom.getRootElement();

 5：获取子节点列表：List<Element> childNodes = root.elements();

 6：遍历子节点：Element node = childNodes.get(i);

 7：读取结点信息：

 1）结点属性值：node.attributeValue("属性名")；

 2）结点名：node.getName()；

 3）结点值：node.getValue();

 4）子结点文本值：node.elementText("子结点名")
 * </PRE>
 *
 * @author: latico
 * @date: 2018/12/16 03:03:43
 * @version: 1.0
 */
public class Dom4jUtils {

    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(Dom4jUtils.class);

    /**
     * 默认编码
     */
    private final static String DEFAULT_CHARSET = CharsetType.UTF8;

    /**
     * 空节点正则
     */
    private static final Pattern emptyNodePattern = Pattern.compile("^<[^>\r\n]+/>$");

    /**
     * 获取到文档对象
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document getDocument(String xml) throws DocumentException {
        if (xml == null) {
            return null;
        }
        return DocumentHelper.parseText(xml);
    }

    /**
     * 获取根节点
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Element getRootElement(String xml) throws DocumentException {
        Document doc = getDocument(xml);
        return getRootElement(doc);
    }

    /**
     * 获取根节点
     *
     * @param doc
     * @return
     */
    private static Element getRootElement(Document doc) {
        if (doc == null) {
            return null;
        }
        return doc.getRootElement();
    }

    /**
     * 检查节点下是否存在子节点
     *
     * @param e 指定节点
     * @return true:存在子节点; false:不存在子节点
     */
    @SuppressWarnings("unchecked")
    public static boolean hasChilds(Element e) {
        boolean hasChilds = false;
        if (e != null) {
            Iterator<Element> childs = e.elementIterator();
            hasChilds = childs.hasNext();
        }
        return hasChilds;
    }

    /**
     * 把当前元素下的所有子元素的名字和值转换成Map集合
     *
     * @param element DOM4J 元素对象
     * @return Map<String   ,       String> 子元素
     */
    public static Map<String, String> getChildsNameValueMap(Element element) {
        if (element == null) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<String, String>();
        List<Element> list = element.elements();
        if (list != null) {
            for (Element e : list) {
                map.put(e.getName(), e.getTextTrim());
            }
        }
        return map;
    }
    /**
     * 把当前元素下的所有子元素的值集合
     *
     * @param element DOM4J 元素对象
     * @return 子元素的值
     */
    public static List<String> getAllChildsValue(Element element) {
        if (element == null) {
            return null;
        }
        List<String> vals = new ArrayList<>();
        List<Element> list = element.elements();
        if (list != null) {
            for (Element e : list) {
                vals.add(e.getText());
            }
        }
        return vals;
    }
    /**
     * 把当前元素下的所有子元素的值集合
     *
     * @param element DOM4J 元素对象
     * @param childElementName 子节点名称
     * @return 子元素的值
     */
    public static List<String> getChildValues(Element element, String childElementName) {
        if (element == null) {
            return null;
        }
        List<String> vals = new ArrayList<>();
        List<Element> list = element.elements(childElementName);
        if (list != null) {
            for (Element e : list) {
                vals.add(e.getText());
            }
        }
        return vals;
    }

    /**
     * 获取第一个子节点
     *
     * @param father 父节点
     * @return 第一个子节点; 若父节点为null或无子节点，则返回null
     */
    public static Element getFirstChild(Element father) {
        Element child = null;
        if (father != null) {
            @SuppressWarnings("unchecked")
            Iterator<Element> childs = father.elementIterator();
            if (childs.hasNext()) {
                child = childs.next();
            }
        }
        return child;
    }

    /**
     * 获取元素名称（优先取带命名空间的名称）
     *
     * @param e 元素对象
     * @return 元素名称
     */
    public static String getName(Element e) {
        String name = "";
        if (e != null) {
            name = e.getQualifiedName();
            if (name == null || "".equals(name)) {
                name = e.getName();
            }
        }
        return (name == null ? "" : name);
    }

    /**
     * 取[指定节点]的节点值（去除前后空字符）.
     *
     * @param e 指定节点
     * @return 节点值, 若为null则替换为""
     */
    public static String getValue(Element e) {
        String val = "";
        if (e != null) {
            val = e.getText();
            val = (val == null ? "" : val);
        }
        return val;
    }

    /**
     * 取[指定节点]下[指定路径]的节点值（去除前后空字符）.
     *
     * @param e     指定节点
     * @param ePath 指定路径, 以 "/" 作为路径分隔符
     * @return 节点值, 若为null则替换为""
     */
    public static String getValue(Element e, String ePath) {
        String val = "";
        if (e != null) {
            String[] paths = PathUtils.toLinux(ePath).split("/");
            if (paths != null) {
                Element child = e;

                for (String path : paths) {
                    if (path == null || "".equals(path)) {
                        continue;
                    }

                    child = child.element(path);
                    if (child == null) {
                        break;
                    }
                }
                val = (child == null ? "" : child.getText());
                val = (val == null ? "" : val);
            }
        }
        return val;
    }

    /**
     * 取[指定节点]的子节点的节点值（去除前后空字符）.
     *
     * @param e         指定节点
     * @param childName 子节点名称
     * @return 节点值, 若为null则替换为""
     */
    public static String getChildValue(Element e, String childName) {
        String val = "";
        if (e != null) {
            val = e.elementTextTrim(childName);
            val = (val == null ? "" : val);
        }
        return val;
    }

    /**
     * 取[指定节点]的[指定属性]的属性值（去除前后空字符）.
     *
     * @param e             指定节点
     * @param attributeName 指定属性名称
     * @return 属性值, 若为null则替换为""
     */
    public static String getAttribute(Element e, String attributeName) {
        String val = "";
        if (e != null) {
            val = e.attributeValue(attributeName);
            val = (val == null ? "" : val);
        }
        return val;
    }

    /**
     * 取[指定节点]的[指定属性]的属性值（去除前后空字符）.
     *
     * @param e             指定节点
     * @param attributeName 指定属性名称
     * @return 属性值, 若为null则替换为""
     */
    public static String getAttribute(Element e, String attributeName, String defaultVal) {
        String val = null;
        if (e != null) {
            val = e.attributeValue(attributeName);
        }
        if (StringUtils.isEmpty(val)) {
            val = defaultVal;
        }
        return val;
    }

    /**
     * 移除元素中的属性
     *
     * @param e             元素对象
     * @param attributeName 属性名称
     * @return true:移除成功; false:移除失败
     */
    public static boolean removeAttribute(Element e, String attributeName) {
        boolean isDel = false;
        Attribute attribute = e.attribute(attributeName);
        if (attribute != null) {
            e.remove(attribute);
            isDel = true;
        }
        return isDel;
    }

    /**
     * 获取某个节点自身所属的命名空间地址串
     *
     * @param element 节点
     * @return 命名空间地址串
     */
    public static String getSelfNamespace(Element element) {
        if (element == null) {
            return "";
        }
        return toNamespaceURL(element.getNamespace());
    }

    /**
     * 获取在某个节点上定义的所有命名空间地址串
     *
     * @param element 节点
     * @return 所有命名空间地址串
     */
    public static String getAllNamespace(Element element) {
        if (element == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int size = element.nodeCount();
        for (int i = 0; i < size; i++) {
            Node node = element.node(i);

            if (node instanceof Namespace) {
                Namespace ns = (Namespace) node;
                sb.append(toNamespaceURL(ns));
            }
        }
        return sb.toString();
    }

    /**
     * 构造命名空间地址
     *
     * @param namespace 命名空间
     * @return 命名空间地址
     */
    private static String toNamespaceURL(Namespace namespace) {
        if (namespace == null) {
            return "";
        }
        return XmlUtils.toNamespaceURL(namespace.getPrefix(), namespace.getURI());
    }


    /**
     * 获取属性，并返回boolean类型
     *
     * @param e
     * @param attributeName
     * @return
     */
    public static Boolean getAttributeBoolean(Element e, String attributeName, Boolean defaultVal) {
        Boolean val = null;
        if (e != null) {
            val = BooleanUtils.toBooleanObject(e.attributeValue(attributeName));
        }
        if (val == null) {
            val = defaultVal;
        }
        return val;
    }

    /**
     * 获取属性，并返回boolean类型
     *
     * @param e
     * @param attributeName
     * @return
     */
    public static Integer getAttributeInt(Element e, String attributeName, int defaultVal) {
        Integer val = null;
        if (e != null) {
            val = NumberUtils.toInt(e.attributeValue(attributeName), defaultVal);
        }
        if (val == null) {
            val = defaultVal;
        }
        return val;
    }

    /**
     * 获取属性，并返回boolean类型
     *
     * @param e
     * @param attributeName
     * @return
     */
    public static Long getAttributeLong(Element e, String attributeName, long defaultVal) {
        Long val = null;
        if (e != null) {
            val = NumberUtils.toLong(e.attributeValue(attributeName), defaultVal);
        }
        if (val == null) {
            val = defaultVal;
        }
        return val;
    }

    /**
     * 获取属性，并返回boolean类型
     *
     * @param e
     * @param attributeName
     * @return
     */
    public static Double getAttributeDouble(Element e, String attributeName, double defaultVal) {
        Double val = null;
        if (e != null) {
            val = NumberUtils.toDouble(e.attributeValue(attributeName), defaultVal);
        }
        if (val == null) {
            val = defaultVal;
        }
        return val;
    }

    /**
     * 获取属性，并返回boolean类型
     *
     * @param e
     * @param attributeName
     * @return
     */
    public static Float getAttributeFloat(Element e, String attributeName, float defaultVal) {
        Float val = null;
        if (e != null) {
            val = NumberUtils.toFloat(e.attributeValue(attributeName), defaultVal);
        }
        if (val == null) {
            val = defaultVal;
        }
        return val;
    }

    /**
     * 获取所有的熟悉
     * @param e
     * @return
     */
    public static List<Attribute> getAllAttribute(Element e){
        if (e == null) {
            return null;
        }
        return e.attributes();
    }

    /**
     * 获取所有的属性的名字和值的Map
     * @param e
     * @return
     */
    public static Map<String, String> getAllAttributeNameValueMap(Element e){
        if (e == null) {
            return null;
        }
        List<Attribute> allAttribute = getAllAttribute(e);
        if (allAttribute == null) {
            return null;
        }

        Map<String, String> map = new HashMap<>();

        for (Attribute attribute : allAttribute) {
            map.put(attribute.getName(), attribute.getValue());
        }
        return map;
    }
    /**
     * <PRE>
     * 格式化xml.
     * (缩进、换行、删除子节点中多余的命名空间等)
     * </PRE>
     *
     * @param xml        xml报文
     * @param linePrefix 在每一行前添加的前缀，亦即缩进符
     * @param newLine    是否为行尾增加换行符
     * @param charset    xml编码
     * @return 格式化的xml报文
     */
    public static String formatXml(final String xml, final String linePrefix, boolean newLine, String charset) {
        String fmtXml = xml;
        if (fmtXml != null) {
            try {
                SAXReader reader = new SAXReader();
                Document doc = reader.read(new StringReader(fmtXml));

                if (doc != null) {
                    StringWriter sWriter = new StringWriter();
                    OutputFormat format = new OutputFormat(linePrefix, newLine, charset);
                    XMLWriter xmlWriter = new XMLWriter(sWriter, format);
                    xmlWriter.write(doc);
                    xmlWriter.flush();
                    fmtXml = sWriter.getBuffer().toString();
                    xmlWriter.close();
                }
            } catch (Exception e) {
                LOG.error("格式化xml失败: {}", xml, e);
            }
        }
        return fmtXml;
    }
    /**
     * 是否为非法的xml格式字符串
     *
     * @param xml xml格式字符串
     * @return true:非法; false:合法
     */
    public static boolean isInvaild(String xml) {
        return !isVaild(xml);
    }
    /**
     * 是否为合法的xml格式字符串
     *
     * @param xml xml格式字符串
     * @return true:合法; false:非法
     */
    public static boolean isVaild(String xml) {
        boolean isVaild = true;
        try {
            DocumentHelper.parseText(xml);
        } catch (Throwable e) {
            isVaild = false;
        }
        return isVaild;
    }

    /**
     * 格式化，默认方式，UTF-8,4个空格缩进
     * @param xml
     * @return
     */
    public static String formatDefault(String xml){
        return formatXml(xml, "    ", true, DEFAULT_CHARSET);
    }

    /**
     * 对于值，当值不能被解析出数据的时候，映射成XML
     * @param element
     * @return
     */
    public static Map<String, String> getChildsNameValueAsXmlWhenValueCannotParseMap(Element element) {
        if (element == null) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<String, String>();
        List<Element> list = element.elements();
        if (list != null) {
            for (Element e : list) {
                String value = e.getTextTrim();
                if (StringUtils.isBlank(value)) {
                    String asXML = e.asXML();
                    //如果匹配是空节点，就不转换
                    if (!emptyNodePattern.matcher(asXML).find()) {
                        value = asXML;
                    }
                }
                map.put(e.getName(), value);
            }
        }
        return map;
    }

}
