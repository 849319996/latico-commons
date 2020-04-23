package com.latico.commons.net.mail;

import org.junit.Test;

import static org.junit.Assert.*;

public class SmtpEmailClientTest {
    @Test
    public void send() {
        try {
            SmtpEmailClient mail = new SmtpEmailClient("latico@qq.com", "*", new String[] { "975298115@qq.com"}, null);
            mail.initConnect();
            System.out.println(mail.send("测试标题1", "测试邮件内容1"));
            mail.closeConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void send2() {
        try {
            SmtpEmailClient mail = new SmtpEmailClient("latico@qq.com", "*");
            mail.initConnect();
            System.out.println(mail.send("测试标题2", "测试邮件内容2", new String[]{"975298115@qq.com"}, new String[]{"1006923413@qq.com"}));
            mail.closeConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void send3() {
        try {
            long start = System.currentTimeMillis();
            SmtpEmailClient mail = new SmtpEmailClient("latico@qq.com", "*", new String[] { "975298115@qq.com"}, null);
            System.out.println("创建对象耗时:" + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            mail.initConnect();
            System.out.println("初始化连接耗时:" + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            System.out.println(mail.send("测试标题3", "测试邮件内容3"));
            System.out.println("第1次发送耗时:" + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            System.out.println(mail.send("测试标题4", "测试邮件内容4"));
            System.out.println("第2次发送耗时:" + (System.currentTimeMillis() - start));

            mail.closeConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void send4() {
        try {
            SmtpEmailClient mail = new SmtpEmailClient("landingdong@gdcattsoft.com", "Lan68341");
            mail.initConnect();
            System.out.println(mail.send("测试标题2", "测试邮件内容2", new String[]{"975298115@qq.com", "landingdong@gdcattsoft.com"}, null));
            mail.closeConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}