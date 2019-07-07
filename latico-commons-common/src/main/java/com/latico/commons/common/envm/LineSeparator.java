package com.latico.commons.common.envm;

/**
 * <PRE>
 * 枚举类：行分隔符
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-02-12 13:26:51
 * @Version: 1.0
 */
public class LineSeparator {

    /**
     * 当前操作平台所用的默认行分隔符
     */
    public static final String DEFAULT = System.getProperty("line.separator");

    /**
     * 回车符
     */
    public static final String CR = "\r";

    /**
     * 换行符
     */
    public static final String LF = "\n";

    /**
     * 回车换行符
     */
    public static final String CRLF = CR.concat(LF);

    /**
     * 无操作平台，分隔符为\0
     */
    public static final String NUL = "\0";

    /**
     * WINDOWS操作平台，分隔符为\r\n
     */
    public static final String WINDOWS = CRLF;

    /**
     * DOS操作平台，分隔符为\r\n
     */
    public static final String DOS = CRLF;

    /**
     * LINUX操作平台，分隔符为\n
     */
    public static final String LINUX = LF;

    /**
     * UNIX操作平台，分隔符为\n
     */
    public static final String UNIX = LF;

    /**
     * MAC操作平台，分隔符为\r
     */
    public static final String MAC = CR;
    /**
     * MAC OS X操作平台，分隔符为\n
     */
    public static final String MACX = LF;

}
