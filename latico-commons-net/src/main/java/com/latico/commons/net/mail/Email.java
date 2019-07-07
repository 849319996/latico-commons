package com.latico.commons.net.mail;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.envm.SmtpEnum;
import com.latico.commons.common.util.codec.CodecUtils;
import com.latico.commons.common.util.codec.sea.SymmetricEncryptAlgorithm;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmFactory;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmType;
import com.latico.commons.common.util.collections.CollectionUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.verify.VerifyUtils;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * <PRE>
 * Email发送工具.
 * <p>
 * 使用示例:
 * Email mail = new Email(SmtpEnum._126, "username@126.com", "password",
 * new String[] { recv1@qq.com, recv@163.com }, "KEY-TEST", CharsetType.UTF8);
 * mail.send("title-1", "content-abcdefg");
 * mail.send("title-2", "content-xyzzyx");
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-30 01:11:53
 * @Version: 1.0
 */
public class Email {

    /**
     * 日志器
     */
    private static final Logger LOG = LoggerFactory.getLogger(Email.class);

    /**
     * 默认SMTP服务器
     */
    private final static SmtpEnum DEFAULT_SMTP = SmtpEnum._126;

    /**
     * 默认SMTP端口
     */
    private final static int DEFAULT_SMTP_PORT = 25;

    /**
     * 默认加密密钥
     */
    private final static String DEFAULT_SECRET_KEY = "EXP-MAIL";

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
     * 邮箱密码
     */
    private String password;

    /**
     * 收件人标准邮箱地址
     */
    private Address[] receivers;

    /**
     * 安全加密密钥
     */
    private String secretKey;

    /**
     * 邮件内容编码
     */
    private String charset;

    /**
     * 邮箱会话
     */
    private Session session;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * des加密算法
     */
    private SymmetricEncryptAlgorithm des;

    /**
     * 构造函数
     *
     * @param smtp      SMTP服务器(发件服务器)
     * @param sender    发送人(邮箱账号)
     * @param password  邮箱密码
     * @param receiver  默认收件人邮箱地址
     * @param secretKey 邮件内容加密密钥,可以为空，发送的时候不加密就用不到
     * @param charset   邮件内容编码
     */
    public Email(SmtpEnum smtp, String sender, String password,
                 String receiver, String secretKey, String charset) throws Exception {
        smtp = (smtp == null ? DEFAULT_SMTP : smtp);
        init(smtp.SERVER, smtp.PORT, sender, password,
                new String[]{receiver}, secretKey, charset);
    }

    /**
     * 构造函数
     *
     * @param smtp      SMTP服务器(发件服务器)
     * @param sender    发送人(邮箱账号)
     * @param password  邮箱密码
     * @param receivers 默认收件人邮箱地址
     * @param secretKey 邮件内容加密密钥,可以为空，发送的时候不加密就用不到
     * @param charset   邮件内容编码
     */
    public Email(SmtpEnum smtp, String sender, String password,
                 String[] receivers, String secretKey, String charset) throws Exception {
        smtp = (smtp == null ? DEFAULT_SMTP : smtp);
        init(smtp.SERVER, smtp.PORT, sender, password,
                receivers, secretKey, charset);
    }

    /**
     * 构造函数
     *
     * @param smtpServer SMTP服务端口(发件服务器IP)
     * @param smtpPort   SMTP服务端口(发件服务器端口)
     * @param sender     发送人(邮箱账号)
     * @param password   邮箱密码
     * @param receiver   默认收件人邮箱地址
     * @param secretKey  邮件内容加密密钥
     * @param charset    邮件内容编码
     */
    public Email(String smtpServer, int smtpPort, String sender, String password,
                 String receiver, String secretKey, String charset) throws Exception {
        init(smtpServer, smtpPort, sender, password,
                new String[]{receiver}, secretKey, charset);
    }

    /**
     * 构造函数
     *
     * @param smtpServer SMTP服务端口(发件服务器IP)
     * @param smtpPort   SMTP服务端口(发件服务器端口)
     * @param sender     发送人(邮箱账号)
     * @param password   邮箱密码
     * @param receivers  默认收件人邮箱地址
     * @param secretKey  邮件内容加密密钥
     * @param charset    邮件内容编码
     */
    public Email(String smtpServer, int smtpPort, String sender, String password,
                 String[] receivers, String secretKey, String charset) throws Exception {
        init(smtpServer, smtpPort, sender, password, receivers, secretKey, charset);
    }

    /**
     * 构造函数
     *
     * @param smtpServer SMTP服务端口(发件服务器IP)
     * @param smtpPort   SMTP服务端口(发件服务器端口)
     * @param sender     发送人(邮箱账号)
     * @param password   邮箱密码
     * @param receivers  默认收件人邮箱地址
     * @param secretKey  邮件内容加密密钥
     * @param charset    邮件内容编码
     */
    private void init(String smtpServer, int smtpPort, String sender, String password,
                      String[] receivers, String secretKey, String charset) throws Exception {
        this.smtpServer = StringUtils.trim(smtpServer);
        this.smtpPort = (VerifyUtils.isPort(smtpPort) ? smtpPort : DEFAULT_SMTP_PORT);
        this.sender = StringUtils.trim(sender);
        this.receivers = toAddress(receivers);
        this.password = StringUtils.trim(password);
        this.secretKey = (StringUtils.isEmpty(secretKey) ? DEFAULT_SECRET_KEY : secretKey);
        this.charset = (CodecUtils.isInvalid(charset) ? CharsetType.UTF8 : charset);
        this.messageType = StringUtils.join("text/html;charset=", this.charset);
        this.des = SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.DES, this.secretKey, charset);
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
     * @param debug
     */
    public void debug(boolean debug) {
        this.session.setDebug(debug);
    }

    /**
     * 发送非加密邮件
     *
     * @param title   标题
     * @param content 正文
     * @return true:发送成功; false:发送失败
     */
    public boolean send(String title, String content) {
        return send(title, content, null, null, false);
    }

    /**
     * 发送邮件
     *
     * @param title   标题
     * @param content 正文
     * @param encrypt true:加密； false：不加密
     * @return true:发送成功; false:发送失败
     */
    public boolean send(String title, String content, boolean encrypt) {
        return send(title, content, null, null, encrypt);
    }

    /**
     * 发送邮件
     *
     * @param title   标题
     * @param content 正文
     * @param CCs     抄送人邮箱地址
     * @param encrypt true:加密； false：不加密
     * @return true:发送成功; false:发送失败
     */
    public boolean send(String title, String content,
                        String[] CCs, boolean encrypt) {
        return send(title, content, null, CCs, encrypt);
    }

    /**
     * 发送邮件
     *
     * @param title     标题
     * @param content   正文
     * @param receivers 临时收件人邮箱地址（若非空，则默认收件人不会受到此封邮件）
     * @param CCs       抄送人邮箱地址
     * @param encrypt   true:加密； false：不加密
     * @return true:发送成功; false:发送失败
     */
    public boolean send(String title, String content,
                        String[] receivers, String[] CCs, boolean encrypt) {
        boolean isOk = false;
        try {
            Transport ts = session.getTransport();
            ts.connect(smtpServer, sender, password);
            Message message = createMessage(title, content, receivers, CCs, encrypt);
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            isOk = true;

        } catch (Exception e) {
            LOG.error("使用SMTP服务 [{}] 发送邮件失败",
                    StringUtils.join(smtpServer, ":", smtpPort), e);
        }
        return isOk;
    }

    /**
     * 创建邮件
     *
     * @param title
     * @param content
     * @param receivers
     * @param CCs
     * @param encrypt
     * @return
     * @throws Exception
     */
    private MimeMessage createMessage(String title, String content,
                                      String[] receivers, String[] CCs, boolean encrypt) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setSubject(title);
        message.setFrom(new InternetAddress(sender));
        content = (encrypt ? des.encryptToStr(content) : content);
        message.setContent(content, messageType);

        message.setRecipients(Message.RecipientType.TO,
                (receivers != null ? toAddress(receivers) : this.receivers));
        if (CCs != null) {
            message.setRecipients(Message.RecipientType.CC, toAddress(CCs));
        }
        return message;
    }

    /**
     * 标准化邮箱地址
     *
     * @param mailAddr 邮箱地址
     * @return 标准化邮箱地址
     */
    private Address[] toAddress(String[] mailAddr) {
        mailAddr = (mailAddr == null || mailAddr.length <= 0 ?
                new String[]{sender} : mailAddr);
        int size = CollectionUtils.cutbackNull(mailAddr);

        Address[] address = new InternetAddress[size];
        for (int i = 0; i < size; i++) {
            try {
                address[i] = new InternetAddress(mailAddr[i]);
            } catch (AddressException e) {
                LOG.error("转换Email地址 [{}] 为标准格式失败.", receivers[i], e);
            }
        }
        return address;
    }

}
