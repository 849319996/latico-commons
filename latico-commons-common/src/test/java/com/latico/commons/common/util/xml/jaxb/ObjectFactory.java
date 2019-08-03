
package com.latico.commons.common.util.xml.jaxb;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the my package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Address_QNAME = new QName("", "address");
    private final static QName _AccoutId_QNAME = new QName("", "accoutId");
    private final static QName _AccountName_QNAME = new QName("", "accountName");
    private final static QName _Name_QNAME = new QName("", "name");
    private final static QName _TelNum_QNAME = new QName("", "telNum");
    private final static QName _AccountMoeny_QNAME = new QName("", "accountMoeny");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: my
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Bank }
     * 
     */
    public Bank createBank() {
        return new Bank();
    }

    /**
     * Create an instance of {@link Accounts }
     * 
     */
    public Accounts createAccounts() {
        return new Accounts();
    }

    /**
     * Create an instance of {@link Account }
     * 
     */
    public Account createAccount() {
        return new Account();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "address")
    public JAXBElement<String> createAddress(String value) {
        return new JAXBElement<String>(_Address_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "accoutId")
    public JAXBElement<String> createAccoutId(String value) {
        return new JAXBElement<String>(_AccoutId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "accountName")
    public JAXBElement<String> createAccountName(String value) {
        return new JAXBElement<String>(_AccountName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "telNum")
    public JAXBElement<String> createTelNum(String value) {
        return new JAXBElement<String>(_TelNum_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "accountMoeny")
    public JAXBElement<String> createAccountMoeny(String value) {
        return new JAXBElement<String>(_AccountMoeny_QNAME, String.class, null, value);
    }

}
