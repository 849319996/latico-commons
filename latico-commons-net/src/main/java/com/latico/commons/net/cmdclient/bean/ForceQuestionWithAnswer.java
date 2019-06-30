package com.latico.commons.net.cmdclient.bean;

import java.util.regex.Pattern;

/**
 * <PRE>
 * 强制回答问题
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年11月19日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class ForceQuestionWithAnswer {

    /**
     * 要自动回答问题的正则
     */
    private String regex;

    /**
     * 要自动回答问题的正则的Pattern
     */
    private Pattern pattern;

    /**
     * 取正则的Matcher的第几个组号
     */
    private int regexGroupIndex;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public int getRegexGroupIndex() {
        return regexGroupIndex;
    }

    public void setRegexGroupIndex(int regexGroupIndex) {
        this.regexGroupIndex = regexGroupIndex;
    }

    @Override
    public String toString() {
        return "ForceQuestionWithAnswer [regex=" + regex + ", pattern=" + pattern + ", regexGroupIndex=" + regexGroupIndex + "]";
    }

}
