package com.latico.commons.common.util.xml.xstream;

import com.latico.commons.common.util.xml.XstreamUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XstreamUtilsTest {

    @Test
    public void xmlToBeanByAnnotation() {
        String xml = "<bank>\n" +
                "  <name>中国人民银行</name>\n" +
                "  <address>地球</address>\n" +
                "  <accounts>\n" +
                "    <account>\n" +
                "      <accoutId>1</accoutId>\n" +
                "      <accountName>dengchao</accountName>\n" +
                "      <accountMoeny>100.0</accountMoeny>\n" +
                "      <telNum>1333333333</telNum>\n" +
                "    </account>\n" +
                "    <account>\n" +
                "      <accoutId>2</accoutId>\n" +
                "      <accountName>sunli</accountName>\n" +
                "      <accountMoeny>200.0</accountMoeny>\n" +
                "      <telNum>14444444</telNum>\n" +
                "    </account>\n" +
                "  </accounts>\n" +
                "</bank>";

        Bank bean = XstreamUtils.xmlToBeanByAnnotation(xml, Account.class, Bank.class);
        System.out.println(bean);
    }

    @Test
    public void beanToXmlByAnnotation() {
        Bank bank = new Bank();
        Account account1 = new Account();
        Account account2 = new Account();
        account1.setAccoutId(1);
        account1.setAccountName("dengchao");
        account1.setAccountMoeny(100);
        account1.setTelNum("1333333333");

        account2.setAccoutId(2);
        account2.setAccountName("sunli");
        account2.setAccountMoeny(200);
        account2.setTelNum("14444444");

        List<Account> accounts = new ArrayList<Account>();
        accounts.add(account1);
        accounts.add(account2);

        bank.setName("中国人民银行");
        bank.setAddress("地球");
        bank.setAccounts(accounts);

        String xml = XstreamUtils.beanToXmlByAnnotation(bank, Account.class, Bank.class);
        System.out.println(xml);
    }

    @Test
    public void xmlToBeanByAlias() {
        String xml = "<bankAlais>\n" +
                "  <name>中国人民银行</name>\n" +
                "  <address>地球</address>\n" +
                "  <accounts>\n" +
                "    <accountAlais>\n" +
                "      <accoutId>1</accoutId>\n" +
                "      <accountName>dengchao</accountName>\n" +
                "      <accountMoeny>100.0</accountMoeny>\n" +
                "      <telNum>1333333333</telNum>\n" +
                "    </accountAlais>\n" +
                "    <accountAlais>\n" +
                "      <accoutId>2</accoutId>\n" +
                "      <accountName>sunli</accountName>\n" +
                "      <accountMoeny>200.0</accountMoeny>\n" +
                "      <telNum>14444444</telNum>\n" +
                "    </accountAlais>\n" +
                "  </accounts>\n" +
                "</bankAlais>";

        Map<Class, String> map = new HashMap<>();
        map.put(Bank.class, "bankAlais");
        map.put(Account.class, "accountAlais");

        Bank bean = XstreamUtils.xmlToBeanByAlias(xml, map);
        System.out.println(bean);
    }

    @Test
    public void beanToXmlByAlias() {
        Bank bank = new Bank();
        Account account1 = new Account();
        Account account2 = new Account();
        account1.setAccoutId(1);
        account1.setAccountName("dengchao");
        account1.setAccountMoeny(100);
        account1.setTelNum("1333333333");

        account2.setAccoutId(2);
        account2.setAccountName("sunli");
        account2.setAccountMoeny(200);
        account2.setTelNum("14444444");

        List<Account> accounts = new ArrayList<Account>();
        accounts.add(account1);
        accounts.add(account2);

        bank.setName("中国人民银行");
        bank.setAddress("地球");
        bank.setAccounts(accounts);

        Map<Class, String> map = new HashMap<>();
        map.put(Bank.class, "bankAlais");
        map.put(Account.class, "accountAlais");

        String xml = XstreamUtils.beanToXmlByAlias(bank, map);
        System.out.println(xml);
    }
}