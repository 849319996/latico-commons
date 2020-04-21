package com.latico.commons.net.mail;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * <PRE>
 * Email发送工具.
 * <p>
 * 使用示例:
 * Email mail = new Email("username@126.com", "password", new String[] { recv1@qq.com, recv@163.com });
 * mail.send("title-1", "content-abcdefg");
 * mail.send("title-2", "content-xyzzyx");
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-30 01:11:53
 * @Version: 1.0
 */
public class SmtpEmailClient {

    /**
     * 日志器
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailClient.class);

    /**
     * SMTP服务器
     */
    private String smtpServer;

    /**
     * SMTP端口
     */
    private int smtpPort;

    /**
     * 发件人(邮箱账号)
     */
    private String sender;

    /**
     * 邮箱密码（SMTP授权码）
     */
    private String password;

    /**
     * 收件人标准邮箱地址
     */
    private Address[] receivers;

    /**
     * 抄送人地址
     */
    private Address[] carbonCopyRecipients;

    /**
     * 邮件内容编码
     */
    private String charset = CharsetType.UTF8;

    /**
     * 邮箱会话
     */
    private Session session;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 一个连接
     */
    private Transport transport;

    /**
     * 构造函数
     *
     * @param sender    发送人(邮箱账号)
     * @param password  邮箱密码
     * @throws Exception
     */
    public SmtpEmailClient(String sender, String password) throws Exception {
        SmtpServerEnum smtpServerEnum = SmtpServerEnum.getEnumByMailAddress(sender);
        initSession(smtpServerEnum.smtpHost, smtpServerEnum.port, sender, password, charset, null, null);
    }

    /**
     * 构造函数
     *
     * @param sender    发送人(邮箱账号)
     * @param password  邮箱密码
     * @param receivers  收件人邮箱地址
     * @param carbonCopyRecipients 抄送人的邮件地址，可以为空
     * @throws Exception
     */
    public SmtpEmailClient(String sender, String password, String[] receivers, String[] carbonCopyRecipients) throws Exception {
        SmtpServerEnum smtpServerEnum = SmtpServerEnum.getEnumByMailAddress(sender);
        initSession(smtpServerEnum.smtpHost, smtpServerEnum.port, sender, password, charset, receivers, carbonCopyRecipients);
    }

    /**
     * 构造函数
     *
     * @param smtpServer      SMTP服务器(发件服务器)
     * @param sender    发送人(邮箱账号)
     * @param password  邮箱密码
     * @param receivers  收件人邮箱地址
     * @param carbonCopyRecipients 抄送人的邮件地址，可以为空
     * @throws Exception
     */
    public SmtpEmailClient(String smtpServer, String sender, String password, String[] receivers, String[] carbonCopyRecipients) throws Exception {
        SmtpServerEnum smtpServerEnum = SmtpServerEnum.getEnumByName(smtpServer);
        initSession(smtpServerEnum.smtpHost, smtpServerEnum.port, sender, password, charset, receivers, carbonCopyRecipients);
    }

    /**
     * 构造函数
     *
     * @param smtpServer      SMTP服务器(发件服务器)
     * @param sender    发送人(邮箱账号)
     * @param password  邮箱密码
     * @param charset   邮件内容编码
     * @param receivers 默认收件人邮箱地址
     * @param carbonCopyRecipients 抄送人的邮件地址，可以为空
     * @throws Exception
     */
    public SmtpEmailClient(SmtpServerEnum smtpServer, String sender, String password, String charset, String[] receivers, String[] carbonCopyRecipients) throws Exception {
        initSession(smtpServer.smtpHost, smtpServer.port, sender, password, charset, receivers, carbonCopyRecipients);
    }

    /**
     * 构造函数
     *
     * @param smtpServer SMTP服务端口(发件服务器IP)
     * @param smtpPort   SMTP服务端口(发件服务器端口)
     * @param sender     发送人(邮箱账号)
     * @param password   邮箱密码
     * @param charset    邮件内容编码，可以为空
     * @param receivers  默认收件人邮箱地址
     * @param carbonCopyRecipients 抄送人的邮件地址，可以为空
     * @throws Exception
     */
    private void initSession(String smtpServer, int smtpPort, String sender, String password, String charset, String[] receivers, String[] carbonCopyRecipients) throws Exception {
        this.smtpServer = StringUtils.trim(smtpServer);
        this.smtpPort = smtpPort;
        this.sender = StringUtils.trim(sender);
        this.receivers = toMailAddress(receivers);
        this.carbonCopyRecipients = toMailAddress(carbonCopyRecipients);
        this.password = StringUtils.trim(password);
        if (StringUtils.isNotBlank(charset)) {
            this.charset = charset;
        }
        this.messageType = StringUtils.join("text/html;charset=", this.charset);
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.host", this.smtpServer);
        prop.setProperty("mail.smtp.port", String.valueOf(this.smtpPort));
        prop.setProperty("mail.smtp.auth", "true");
        this.session = Session.getInstance(prop);
        this.session.setDebug(false);
    }

    /**
     * 开关debug模式，用于查看程序发送Email的详细状态
     *
     * @param debugMode
     */
    public void changeDebugMode(boolean debugMode) {
        this.session.setDebug(debugMode);
    }

    /**
     * 发送邮件
     *
     * @param title   标题
     * @param content 正文
     * @return true:发送成功; false:发送失败
     */
    public boolean send(String title, String content) {
        return send(title, content, null, null);
    }

    /**
     * 发送邮件
     *
     * @param title     标题
     * @param content   正文
     * @param receivers 临时收件人邮箱地址（若非空，则默认收件人不会受到此封邮件）
     * @param CCs       抄送人邮箱地址
     * @return true:发送成功; false:发送失败
     */
    public boolean send(String title, String content, String[] receivers, String[] CCs) {
        if (!isConnected()) {
            LOG.warn("没有初始化服务器连接");
            return false;
        }
        boolean isOk = false;
        try {
            Message message = createMessage(title, content, receivers, CCs);
            transport.sendMessage(message, message.getAllRecipients());
            isOk = true;
        } catch (Exception e) {
            LOG.error("使用SMTP服务 [{}:{}] 发送邮件失败", e,
                    smtpServer, smtpPort);
        }
        return isOk;
    }

    /**
     * 初始化连接
     * @return
     * @throws Exception
     */
    public boolean initConnect() {
        if (transport != null && transport.isConnected()) {
            return true;
        }
        closeConnect();
        try {
            transport = session.getTransport();
            transport.connect(smtpServer, sender, password);
            return transport.isConnected();
        } catch (Exception e) {
            LOG.error("", e);
        }
        return false;

    }

    public boolean isConnected() {
        if (transport != null && transport.isConnected()) {
            return true;
        }
        return false;
    }

    public void closeConnect() {
        if (transport != null) {
            try {
                transport.close();
            } catch (MessagingException e) {
                LOG.error("", e);
            }
        }
    }

    /**
     * 创建邮件
     *
     * @param title 邮件标题
     * @param content 邮件内容
     * @param receivers 收件人邮件地址，如果为空就会使用初始化传入的
     * @param carbonCopyRecipients 抄送人邮件地址，如果为空就会使用初始化传入的
     * @return
     * @throws Exception
     */
    private MimeMessage createMessage(String title, String content, String[] receivers, String[] carbonCopyRecipients) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setSubject(title);
        message.setFrom(new InternetAddress(sender));
        message.setContent(content, messageType);

        if (receivers != null && receivers.length >= 1) {
            message.setRecipients(Message.RecipientType.TO, toMailAddress(receivers));
        }else{
            message.setRecipients(Message.RecipientType.TO, this.receivers);
        }

        if (carbonCopyRecipients != null && carbonCopyRecipients.length >= 1) {
            message.setRecipients(Message.RecipientType.CC, toMailAddress(carbonCopyRecipients));
        } else {
            message.setRecipients(Message.RecipientType.CC, this.carbonCopyRecipients);
        }
        return message;
    }

    /**
     * 标准化邮箱地址
     *
     * @param mailAddr 邮箱地址
     * @return 标准化邮箱地址
     */
    private Address[] toMailAddress(String[] mailAddr) {
        if (mailAddr == null || mailAddr.length == 0) {
            return null;
        }

        Address[] address = new InternetAddress[mailAddr.length];
        for (int i = 0; i < mailAddr.length; i++) {
            try {
                address[i] = new InternetAddress(mailAddr[i]);
            } catch (AddressException e) {
                LOG.error("转换Email地址 [{}] 为标准格式失败.", receivers[i], e);
            }
        }
        return address;
    }

}
