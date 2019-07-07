package com.latico.commons.webservice.soap.wsdl;

/**
 * <PRE>
 * wsdl
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-24 21:22
 * @Version: 1.0
 */
public class WsdlInfo {
    /**
     * wsdl的URL或者是文件地址
     */
    private String wsdlUri;
    /**
     * WSDL文件内容
     */
    private String wsdlContent;
    /**
     * 请求模板
     */
    private WebServiceTemplate requestTemplate;
    /**
     * 响应模板
     */
    private WebServiceTemplate responseTemplate;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WsdlInfo{");
        sb.append("wsdlUri='").append(wsdlUri).append('\'');
        sb.append(", wsdlContent='").append(wsdlContent).append('\'');
        sb.append(", requestTemplate=").append(requestTemplate);
        sb.append(", responseTemplate=").append(responseTemplate);
        sb.append('}');
        return sb.toString();
    }

    public String getWsdlUri() {
        return wsdlUri;
    }

    public void setWsdlUri(String wsdlUri) {
        this.wsdlUri = wsdlUri;
    }

    public String getWsdlContent() {
        return wsdlContent;
    }

    public void setWsdlContent(String wsdlContent) {
        this.wsdlContent = wsdlContent;
    }

    public WebServiceTemplate getRequestTemplate() {
        return requestTemplate;
    }

    public void setRequestTemplate(WebServiceTemplate requestTemplate) {
        this.requestTemplate = requestTemplate;
    }

    public WebServiceTemplate getResponseTemplate() {
        return responseTemplate;
    }

    public void setResponseTemplate(WebServiceTemplate responseTemplate) {
        this.responseTemplate = responseTemplate;
    }
}
