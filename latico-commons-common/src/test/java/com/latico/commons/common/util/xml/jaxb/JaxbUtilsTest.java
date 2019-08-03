package com.latico.commons.common.util.xml.jaxb;

import com.latico.commons.common.util.xml.JaxbUtils;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

public class JaxbUtilsTest {

    @Test
    public void beanToXml() throws JAXBException {
        Bank bank = new Bank();
        Account account1 = new Account();
        Account account2 = new Account();
        account1.setAccoutId("1");
        account1.setAccountName("dengchao");
        account1.setAccountMoeny("100");
        account1.setTelNum("1333333333");

        account2.setAccoutId("2");
        account2.setAccountName("sunli");
        account2.setAccountMoeny("200");
        account2.setTelNum("14444444");

        Accounts accoun = new Accounts();
        List<Account> accounts = new ArrayList<Account>();
        accounts.add(account1);
        accounts.add(account2);

        accoun.setAccount(accounts);

        bank.setName("中国人民银行");
        bank.setAddress("地球");

        bank.setAccounts(accoun);

        String xml = JaxbUtils.beanToXml(bank, Bank.class);
        System.out.println(xml);
    }

    @Test
    public void xmlToBean() throws JAXBException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bank><name>中国人民银行</name><address>地球</address><accounts><account><accoutId>1</accoutId><accountName>dengchao</accountName><accountMoeny>100</accountMoeny><telNum>1333333333</telNum></account><account><accoutId>2</accoutId><accountName>sunli</accountName><accountMoeny>200</accountMoeny><telNum>14444444</telNum></account></accounts></bank>";
        Object bean = JaxbUtils.xmlToBean(xml, Bank.class);
        System.out.println(bean);
    }

}