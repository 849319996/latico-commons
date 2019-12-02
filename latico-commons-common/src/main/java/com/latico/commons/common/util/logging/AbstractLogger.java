package com.latico.commons.common.util.logging;


/**
 * <PRE>
 *  抽象的日志对象
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 11:47:28
 * @Version: 1.0
 */
public abstract class AbstractLogger implements Logger {

    /**
     * 日志占位符
     */
    private static final String placeholder = "{}";
    /**
     * 日志占位符正则
     */
    private static final String placeholderRegex = toRegexESC(placeholder);
    /**
     * 日志名称
     */
    protected String loggerName = "";
    protected long errorCount;
    protected long warnCount;
    protected long infoCount;
    protected long debugCount;

    @Override
    public long getErrorCount() {
        return errorCount;
    }

    @Override
    public long getWarnCount() {
        return warnCount;
    }

    @Override
    public long getInfoCount() {
        return infoCount;
    }

    @Override
    public long getDebugCount() {
        return debugCount;
    }

    @Override
    public void resetCount() {
        errorCount = 0;
        warnCount = 0;
        infoCount = 0;
        debugCount = 0;
    }

    /**
     * 获取日志名称
     *
     * @return
     */
    @Override
    public String getLoggerName() {
        return loggerName;
    }

    /**
     * 实现类似slf4j的连接多个参数功能，使用{}作为占位符
     * @param msg
     * @param argArray
     * @return
     */
    protected static Object concatArgs(Object msg, Object... argArray) {
        //占位符是{}
        return replaceByPlaceholder(msg, argArray);
    }
    /**
     * 实现类似slf4j的连接多个参数功能，使用{}作为占位符
     * @param msg
     * @param argArray
     * @return
     */
    protected static String concatArgsToStr(Object msg, Object... argArray) {
        //占位符是{}
        Object obj = replaceByPlaceholder(msg, argArray);
        if(obj == null){
            return null;
        }else{
            return obj.toString();
        }
    }

    /**
     * object 转成 string
     * @param obj
     * @return
     */
    protected static String getString(Object obj){
        if (obj == null) {
            return null;
        }else{
            return obj.toString();
        }
    }
    /**
     * @return 错误计数
     */
    @Override
    public long incrementErrorCount() {
        return ++errorCount;
    }

    /**
     * @return 警告日志计数
     */
    @Override
    public long incrementWarnCount() {
        return ++warnCount;
    }

    /**
     * @return 信息日志计数
     */
    @Override
    public long incrementInfoCount() {
        return ++infoCount;
    }

    /**
     * @return 调试等级日志计数
     */
    @Override
    public long incrementDebugCount() {
        return ++debugCount;
    }

    /**
     * 通过占位符的形式替换，实现类型slf4j的日志占位符功能
     * @param str
     * @param params
     * @return
     */
    public static Object replaceByPlaceholder(Object str, Object... params){
        if(str != null){
            if(params == null || params.length == 0){
                return str;
            }
            String[] arr = str.toString().split(placeholderRegex, -1);
            if(arr.length == 1){
                return str;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]);
                if (i < params.length) {
                    sb.append(params[i].toString());
                }
            }
            return sb.toString();
        }else{
            return null;
        }
    }

    /**
     * <PRE>
     * 为正则表达式中所有特殊字符添加前置反斜杠, 使其转义为普通字符
     *
     * 	[ \ ] -> [ \\ ]
     * 	[ ( ] -> [ \( ]
     *  [ ) ] -> [ \) ]
     *  [ [ ] -> [ \[ ]
     *  [ ] ] -> [ \] ]
     *  [ { ] -> [ \{ ]
     *  [ } ] -> [ \} ]
     *  [ + ] -> [ \+ ]
     *  [ - ] -> [ \- ]
     *  [ . ] -> [ \. ]
     *  [ * ] -> [ \* ]
     *  [ ? ] -> [ \? ]
     *  [ ^ ] -> [ \^ ]
     *  [ $ ] -> [ \$ ]
     * </PRE>
     *
     * @param regex 正则表达式
     * @return 转义后的正则表达式
     */
    public static String toRegexESC(final String regex) {
        String str = "";
        if(regex != null) {
            str = regex;
            str = str.replace("\\", "\\\\");
            str = str.replace("(", "\\(");
            str = str.replace(")", "\\)");
            str = str.replace("[", "\\[");
            str = str.replace("]", "\\]");
            str = str.replace("{", "\\{");
            str = str.replace("}", "\\}");
            str = str.replace("+", "\\+");
            str = str.replace("-", "\\-");
            str = str.replace(".", "\\.");
            str = str.replace("*", "\\*");
            str = str.replace("?", "\\?");
            str = str.replace("^", "\\^");
            str = str.replace("$", "\\$");
        }
        return str;
    }
}
