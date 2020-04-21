package com.latico.commons.net.mail;

import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 * 枚举模板示例
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-06 14:15:31
 * @version: 1.0
 */
public enum SmtpServerEnum {

    QQ("smtp.qq.com", 25, "@qq.com"),

    QQ_COMPANY("smtp.exmail.qq.com", 465, "@exmail.qq.com"),

    GMAIL("smtp.gmail.com", 587, "@gmail.com"),

    _163("smtp.163.com", 25, "@163.com"),

    _126("smtp.126.com", 25, "@126.com"),

    _139("SmtpEnum.139.com", 25, "@139.com"),

    _263("smtp.263.net", 25, "@263.net"),

    _263CN("263.net.cn", 25, "@263.net.cn"),

    X263("smtp.x263.net", 25, "@x263.net"),

    _21CN("smtp.21cn.com", 25, "@21cn.com"),

    SINA("smtp.sina.com", 25, "@sina.com"),

    SINA_VIP("smtp.vip.sina.com", 25, "@vip.sina.com"),

    SOHU("smtp.sohu.com", 25, "@sohu.com"),

    FOXMAIL("smtp.foxmail.com", 25, "@foxmail.com"),

    CHINA("smtp.china.com", 25, "@china.com"),

    TOM("smtp.tom.com", 25, "@tom.com"),

    ETANG("smtp.etang.com", 25, "@etang.com"),

    GDCATTSOFT("mail.gdcattsoft.com", 25, "@gdcattsoft.com"),
    ;

    /**
     * nameEnumMap 名字和枚举对象的映射
     */
    private final static Map<String, SmtpServerEnum> NAME_ENUM_MAP = new HashMap<>();
    private final static Map<String, SmtpServerEnum> SUFFIX_ENUM_MAP = new HashMap<>();


    static {
        SmtpServerEnum[] values = values();
        for (SmtpServerEnum value : values) {
            // 统一转换成小写
            NAME_ENUM_MAP.put(value.getSmtpHost(), value);
            SUFFIX_ENUM_MAP.put(value.getMailSuffix(), value);
        }
    }



    /**
     * SMTP服务主机
     */
    public String smtpHost;

    /**
     * 端口
     */
    public int port;
    /**
     * 邮箱后缀
     */
    public String mailSuffix;


    /**
     * 构造函数
     * 枚举参数初始化
     *
     * @param smtpHost
     * @param port
     */
    private SmtpServerEnum(String smtpHost, int port, String mailSuffix) {
        this.smtpHost = smtpHost;
        this.port = port;
        this.mailSuffix = mailSuffix;
    }

    public int getPort() {
        return port;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getMailSuffix() {
        return mailSuffix;
    }

    /**
     * 通过枚举的名称获取枚举对象
     *
     * @param name
     * @return
     */
    public static SmtpServerEnum getEnumByName(String name) {
        if (name != null) {
            //统一转换成小写
            return NAME_ENUM_MAP.get(name.toLowerCase());
        } else {
            return null;
        }
    }


    /**
     *
     * @param mailAddr 邮件地址
     * @return
     */
    public static SmtpServerEnum getEnumByMailAddress(String mailAddr) {
        if (mailAddr != null) {
            int index = mailAddr.indexOf("@");
            if (index >= 0) {
                return SUFFIX_ENUM_MAP.get(mailAddr.substring(index));
            }
        }

        return null;
    }


}
