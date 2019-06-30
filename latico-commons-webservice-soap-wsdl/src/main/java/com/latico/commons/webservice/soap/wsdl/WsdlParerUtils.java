package com.latico.commons.webservice.soap.wsdl;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Log;
import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.common.util.xml.Dom4jUtils;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import org.apache.commons.io.LineIterator;
import org.dom4j.Element;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <PRE>
 *  WSDL解析工具，可以拿到WSDL地址的请求和响应XML报文
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-04-09 11:50:37
 * @Version: 1.0
 */
public class WsdlParerUtils {
    private static final Log LOG = LogUtils.getLog(WsdlParerUtils.class);

    private static final String HEAD_SOAP11 = "httpclient://schemas.xmlsoap.org/soap/envelope";

    private static final String HEAD_SOAP12 = "httpclient://www.w3.org/2003/05/soap-envelope";

    /**
     * 读取WSDL
     *
     * @param wsdlUri
     * @return
     */
    public static List<WsdlInfo> readWsdlUri(String wsdlUri) {
        List<WsdlInfo> wsdlInfos = new ArrayList<WsdlInfo>();
        WsdlProject project = null;
        WsdlInterface wsdl = null;
        try {
            project = new WsdlProject();

            WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, wsdlUri);
            if(wsdls == null || wsdls.length == 0){
                return wsdlInfos;
            }
            wsdl = wsdls[0];
            List<Operation> operationList = wsdl.getOperationList();

            for (Operation operation : operationList) {
                WsdlOperation op = (WsdlOperation) operation;

                WebServiceTemplate requestTemplate = new WebServiceTemplate();
                WebServiceTemplate responseTemplate = new WebServiceTemplate();


                requestTemplate.setMethodName(op.getName());
                responseTemplate.setMethodName(op.getName());

                //获取请求报文
                requestTemplate.setSoapMessage(op.createRequest(true));

                //获取响应报文
                responseTemplate.setSoapMessage(op.createResponse(true));

                //提取信息
                extractBodyParamInfo(requestTemplate);
                extractBodyParamInfo(responseTemplate);

                //组装一个请求和响应结果
                WsdlInfo wsdlInfo = new WsdlInfo();
                wsdlInfo.setWsdlUri(wsdlUri);
                wsdlInfo.setRequestTemplate(requestTemplate);
                wsdlInfo.setResponseTemplate(responseTemplate);
                wsdlInfos.add(wsdlInfo);
            }

        } catch (Throwable e) {
            LOG.error("WSDL读取出现异常:{}", e, wsdlUri);
        } finally {
            if (wsdl != null) {
                wsdl.release();
            }
            if (project != null) {
                project.release();
            }

        }
        return wsdlInfos;
    }

    /**
     * 读取WSDL文件
     * @param wsdlFile wsdl文件或者目录，如果是目录，会扫描下面所有的WSDL
     * @param recursive true:如果是目录的情况下，递归遍历查找
     * @return
     */
    public static List<WsdlInfo> readWsdlFile(String wsdlFile, boolean recursive) {
        List<WsdlInfo> wsdlInfos = new ArrayList<>();
        File file = new File(wsdlFile);
        if (!file.exists()) {
            return wsdlInfos;
        }
        if (file.isFile()) {
            return readWsdlUri(wsdlFile);
        }else{
            Collection<File> files = FileUtils.listFiles(file, new String[]{"wsdl", "WSDL"}, recursive);
            if (files == null) {
                return wsdlInfos;
            }

            for (File f : files) {
                wsdlInfos.addAll(readWsdlUri(f.getAbsolutePath()));
            }

        }
        return wsdlInfos;
    }

    /**
     * 抽取信息
     * @param template
     * @throws Exception
     */
    public static void extractBodyParamInfo(WebServiceTemplate template) throws Exception {
        if(template == null || template.getSoapMessage() == null){
            return;
        }
        List<String> inputType = new ArrayList<String>();
        List<String> inputNames = new ArrayList<String>();
        List<String> isparent = new ArrayList<String>();
        String methodName = template.getMethodName();
        String xmlString = template.getSoapMessage();
        // 处理targetNameSpace
        String qname = xmlString.substring(xmlString.lastIndexOf("\"httpclient://") + 1, xmlString.lastIndexOf("\">"));
        template.setTargetNameSpace(qname);

        InputStream inputStream = IOUtils.toInputStream(xmlString, CharsetType.UTF8);
        LineIterator lineIterator = IOUtils.lineIterator(inputStream, CharsetType.UTF8);
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            if (line != null) {
                if (line.indexOf(HEAD_SOAP11) >= 0) {
                    template.setTargetXsd("11");
                } else if (line.indexOf(HEAD_SOAP12) >= 0) {
                    template.setTargetXsd("12");
                }
            }
        }
        IOUtils.close(inputStream);

        //从XML中提取真正的方法
        Element rootElement = Dom4jUtils.getRootElement(xmlString);
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            if ("Body".equals(element.getName())) {
                List<Element> elements2 = element.elements();
                template.setMethodName(methodName);
                for (Element element2 : elements2) {
                    getParameter(element2, 1, 1, inputType, inputNames, isparent);
                    template.setInputNames(inputNames);
                    template.setInputType(inputType);
                    template.setOutputType(isparent);
                }
            }
        }

    }

    /**
     * 循环迭代子节点，获取所有参数信息
     * @param ele
     * @param gen
     * @param genParent
     * @param inputType
     * @param inputNames
     * @param isparent
     */
    private static void getParameter(Element ele, int gen, int genParent,
                                     List<String> inputType, List<String> inputNames,
                                     List<String> isparent) {
        if (ele == null) {
            return;
        }
        List<Element> eleChils = ele.elements();
        if (eleChils == null || eleChils.isEmpty()) {
            return;
        }

        for (Element eleChil : eleChils) {
            inputType.add(gen + "," + genParent);
            inputNames.add(eleChil.getQualifiedName());
            List<Element> eles = eleChil.elements();
            if (eles != null && !eles.isEmpty()) {
                isparent.add("1");
                int gen1 = gen + gen;
                getParameter(eleChil, gen1, gen, inputType,
                        inputNames, isparent);
            } else {
                isparent.add("0");
            }

        }
    }

}
