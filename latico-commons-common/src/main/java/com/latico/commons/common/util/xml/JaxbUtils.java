package com.latico.commons.common.util.xml;


import java.io.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * <PRE>
 * JAXB工具的XML和Bean互转
 *
 * 支持XML自动化生成JavaBean，生成的JavaBean支持命名空间，但是格式有点反人类设计，不太好用
 * 建议能使用xstream的就不要使用JAXB
 *
 *
 IDEA，利用xml文件生成JAXB格式的bean操作步骤：

 1.XML转XSD
 如果你是XSD的话,直接从 2 开始.
 如果你是非idea用户,可点此链接查看 链接:eclipse用户操作https://mp.csdn.net/postedit/85005209

 (1). 用idea打开xml文件,右击鼠标,选择 generate XSD from XML

 (2). 接下来是,选择自己想要的相关类型.如果想要生成的JavaBean属性和XML一致,记得选择Design type 为local elements type. 然后点击 OK 就可在当前文件夹生成了对应的XSD约束.

 2.XSD转JavaBean
 (1). 选中XSD,打开 并右键如下图,选中 generate javacode from XSD by JAXB
 (2). 进入如下界面,注意生成Java的路径,等问题.点击 OK.即可看到生成的JavaBean.注意一下生成 勾选:Make generated files read-only. 选项.
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-03 23:59:44
 * @Version: 1.0
 */
public class JaxbUtils {

    /**
     * java对象转换为xml文件
     *
     * @param obj
     * @param classesToBeBound java对象.Class
     * @return xml文件的String
     * @throws JAXBException
     */
    public static String beanToXml(Object obj, Class... classesToBeBound) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classesToBeBound);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }

    /**
     * xml文件配置转换为对象
     *
     * @param xmlPath xml文件路径
     * @param classesToBeBound    java对象.Class
     * @return java对象
     * @throws JAXBException
     * @param <T>
     * @throws FileNotFoundException
     */
    public static <T> T xmlToBeanByXmlFile(String xmlPath, Class... classesToBeBound) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(classesToBeBound);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new FileReader(xmlPath));
    }

    /**
     * JavaBean转换成xml
     * 默认编码UTF-8
     *
     * @param obj
     * @return
     * @throws JAXBException
     */
    public static String beanToXml(Object obj) throws JAXBException {
        return beanToXml(obj, "UTF-8");
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @param encoding
     * @return
     * @throws JAXBException
     */
    public static String beanToXml(Object obj, String encoding) throws JAXBException {
        String result = null;
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        result = writer.toString();

        return result;
    }

    /**
     * JavaBean转换成xml去除xml声明部分
     *
     * @param obj
     * @param encoding
     * @return
     */
    public static String beanToXmlIgnoreXmlHead(Object obj, String encoding) throws JAXBException {
        String result = null;
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        result = writer.toString();

        return result;
    }


    /**
     * xml转换成JavaBean
     *
     * @param xml
     * @param classesToBeBound
     * @return
     * @param <T>
     * @throws JAXBException
     */
    public static <T> T xmlToBean(String xml, Class... classesToBeBound) throws JAXBException {
        T t = null;
        JAXBContext context = JAXBContext.newInstance(classesToBeBound);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        t = (T) unmarshaller.unmarshal(new StringReader(xml));

        return t;
    }

}