
package com.latico.commons.common.util.xml.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}accoutId"/>
 *         &lt;element ref="{}accountName"/>
 *         &lt;element ref="{}accountMoeny"/>
 *         &lt;element ref="{}telNum"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "accoutId",
    "accountName",
    "accountMoeny",
    "telNum"
})
@XmlRootElement(name = "account")
public class Account {

    @XmlElement(required = true)
    protected String accoutId;
    @XmlElement(required = true)
    protected String accountName;
    @XmlElement(required = true)
    protected String accountMoeny;
    @XmlElement(required = true)
    protected String telNum;

    /**
     * ��ȡaccoutId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccoutId() {
        return accoutId;
    }

    /**
     * ����accoutId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccoutId(String value) {
        this.accoutId = value;
    }

    /**
     * ��ȡaccountName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * ����accountName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountName(String value) {
        this.accountName = value;
    }

    /**
     * ��ȡaccountMoeny���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountMoeny() {
        return accountMoeny;
    }

    /**
     * ����accountMoeny���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountMoeny(String value) {
        this.accountMoeny = value;
    }

    /**
     * ��ȡtelNum���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelNum() {
        return telNum;
    }

    /**
     * ����telNum���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelNum(String value) {
        this.telNum = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("accoutId='").append(accoutId).append('\'');
        sb.append(", accountName='").append(accountName).append('\'');
        sb.append(", accountMoeny='").append(accountMoeny).append('\'');
        sb.append(", telNum='").append(telNum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
