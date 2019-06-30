package com.latico.commons.webservice.soap.wsdl;

import com.latico.commons.common.util.string.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * wsdl
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2018-12-24 21:21
 * @Version: 1.0
 */
public class WebServiceTemplate {

    /**
     * 分隔符
     */
    private String sep = "#";
    /**
     * soapMessage 模板,XML报文
     */
    private String soapMessage;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法描述
     */
    private String methodDesc;

    private Map<String, Map<String, Object>> methodName2InputParam;

    private String targetNameSpace;

    private String endPoint;

    private String targetXsd;

    private List<String> inputNames;

    private List<String> inputType;

    private List<String> inputDesc;

    private List<String> outputNames;

    private List<String> outputType;

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getInputNames() {
        return this.inputNames;
    }

    public void setInputNames(List<String> inputNames) {
        this.inputNames = inputNames;
    }

    public Map<String, Map<String, Object>> getMethodName2InputParam() {
        return this.methodName2InputParam;
    }

    public void setMethodName2InputParam(
            Map<String, Map<String, Object>> methodName2InputParam) {
        this.methodName2InputParam = methodName2InputParam;
    }

    public String getTargetNameSpace() {
        return this.targetNameSpace;
    }

    public void setTargetNameSpace(String targetNameSpace) {
        this.targetNameSpace = targetNameSpace;
    }

    public String getSoapMessage() {
        return soapMessage;
    }

    public void setSoapMessage(String soapMessage) {
        this.soapMessage = soapMessage;
    }

    public String getEndPoint() {
        return this.endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getTargetXsd() {
        return this.targetXsd;
    }

    public void setTargetXsd(String targetXsd) {
        this.targetXsd = targetXsd;
    }

    public List<String> getOutputNames() {
        return this.outputNames;
    }

    public void setOutputNames(List<String> outputNames) {
        this.outputNames = outputNames;
    }

    public List<String> getInputType() {
        return this.inputType;
    }

    public void setInputType(List<String> inputType) {
        this.inputType = inputType;
    }

    public List<String> getOutputType() {
        return this.outputType;
    }

    public void setOutputType(List<String> outputType) {
        this.outputType = outputType;
    }

    public List<String> getInputDesc() {
        return this.inputDesc;
    }

    public void setInputDesc(List<String> inputDesc) {
        this.inputDesc = inputDesc;
    }

    public String formatString() {
        StringBuilder su = new StringBuilder();
        su.append(this.methodName);
        su.append(this.sep);
        su.append(this.inputType == null
                ? ""
                : StringUtils.join(this.inputType.toArray(), "@"));
        su.append(this.sep);
        su.append(this.inputNames == null
                ? ""
                : StringUtils.join(this.inputNames.toArray(), "@"));
        su.append(this.sep);
        su.append(this.methodDesc == null ? "" : this.methodDesc);
        su.append(this.sep);
        su.append(this.sep);
        su.append(this.soapMessage == null ? "" : this.soapMessage);
        su.append(this.sep);
        su.append(this.outputType == null
                ? ""
                : StringUtils.join(this.outputType.toArray(), "@"));
        su.append(this.sep);
        su.append(this.targetXsd == null ? "" : this.targetXsd);
        return su.toString();
    }

    public String getMethodDesc() {
        return this.methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public String getSep() {
        return sep;
    }

    public void setSep(String sep) {
        this.sep = sep;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WebServiceTemplate{");
        sb.append("soapMessage='").append(soapMessage).append('\'');
        sb.append(", methodName='").append(methodName).append('\'');
        sb.append(", methodDesc='").append(methodDesc).append('\'');
        sb.append(", methodName2InputParam=").append(methodName2InputParam);
        sb.append(", targetNameSpace='").append(targetNameSpace).append('\'');
        sb.append(", endPoint='").append(endPoint).append('\'');
        sb.append(", targetXsd='").append(targetXsd).append('\'');
        sb.append(", inputNames=").append(inputNames);
        sb.append(", inputType=").append(inputType);
        sb.append(", inputDesc=").append(inputDesc);
        sb.append(", outputNames=").append(outputNames);
        sb.append(", outputType=").append(outputType);
        sb.append('}');
        return sb.toString();
    }
}
