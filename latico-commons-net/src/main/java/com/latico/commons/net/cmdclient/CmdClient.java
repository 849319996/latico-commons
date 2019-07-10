package com.latico.commons.net.cmdclient;


import com.latico.commons.net.cmdclient.bean.LoginInfo;
import com.latico.commons.net.cmdclient.bean.ReadParam;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;

import java.io.Closeable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <PRE>
 * 命令行连接工具类，Telnet和SSH等连接接口类
 * example:
 * <p>
 * CmdClient cmdClient = CmdClientFactory.getCmdClient(CmdClientTypeEnum.SSH);
 * if (cmdClient.login("172.168.10.7", 22, "laticosoft", "laticoosft")) {
 * cmdClient.execCmdAndReceiveData("ping www.baidu.com -c 5");
 * }
 * cmdClient.close();
 *
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2017年3月18日</B>
 * @since <B>JDK1.6</B>
 */
public interface CmdClient extends Closeable {

    /**
     * LINE_TAGS_REGEX 内容是否匹配到行结束符正则
     */
    public final static String LINE_TAGS_REGEX = CmdClientConfig.getInstance().getCmdLineEndRegex();

    /**
     * cmdLastLineEndRegex 命令行最后一行匹配用的正则
     */
    public final static String cmdLastLineEndRegex = CmdClientConfig.getInstance().getCmdLastLineEndRegex();

    public static final String quit = "quit";

    public static final String exit = "exit";

    /**
     * LINE_SEPARATOR_REGEX 匹配换行换页正则
     */
    public final static String LINE_SEPARATOR_REGEX = "[\r\n\f]";

    /**
     * yesOrNoQuestionRegex Yes/No问题的正则匹配
     */
    public final static String yesOrNoQuestionRegex = CmdClientConfig.getInstance().getYesOrNoQuestionRegex();

    /**
     * yesOrNoQuestionRegexPattern yes or no问题的正则 group(1)是获取第一个yes的回答字符，group(2)是获取no的回答字符，假如是n就返回n，假如是no就返回no
     */
    public final static Pattern yesOrNoQuestionPattern = Pattern.compile(yesOrNoQuestionRegex);

    /**
     * moreTagPattern 匹配More标识符
     */
    public final static Pattern moreTagPattern = Pattern.compile("(?i)(--\\s*more[^\\d]*?\\d*).*?$");

    /**
     * hasMoreLineRegex 匹配有more的行(连换行符也替换)
     */
    public final static String hasMoreLineRegex = "\\s+.*(?i)--\\s*more.*?--.*";

    /**
     * hasMoreContralLineRegex more控制字符的空闲字符
     */
    public final static String hasMoreContralLineRegex = "\\s.*?\\[42D.*?\\[42D";

    /**
     * sshLoginCmdLine SSH的命令行方式登陆模板
     */
    public final static String sshLoginCmdLine = "ssh $username$@$ip$ -p $port$";

    /**
     * sshLoginCmdLineDefault 默认的SSH登陆方式
     */
    public final static String sshLoginCmdLineDefault = "ssh $username$@$ip$";

    /**
     * telnetLoginCmdLine telnet命令行方式登陆的模板
     */
    public final static String telnetLoginCmdLine = "telnet $ip$ $port$";

    /**
     * telnetLoginCmdLineDefault 默认的telnet登陆方式
     */
    public final static String telnetLoginCmdLineDefault = "telnet $ip$";

    /**
     * enableCmd 执行enable模式用到的命令
     */
    public final static String enableCmd = "enable";

    /**
     * pwdPromptRegex 密码提示符正则
     */
    public final static String pwdPromptRegex = CmdClientConfig.getInstance().getPwdPromptRegex();

    /**
     * 只连接
     *
     * @param ip
     * @return
     */
    public boolean connect(String ip);

    /**
     * 只连接
     *
     * @param ip
     * @param port
     * @return
     */
    public boolean connect(String ip, int port);

    /**
     * 使用默认端口登陆
     *
     * @param ip       登陆的IP地址
     * @param username 用户名
     * @param password 密码
     * @return 是否登陆成功
     */
    public boolean login(String ip, String username, String password);

    /**
     * 登陆
     *
     * @param ip       登陆的IP地址
     * @param port     登陆的端口
     * @param username 用户名
     * @param password 密码
     * @return 是否登陆成功
     */
    public boolean login(String ip, int port, String username, String password);

    /**
     * 登录
     *
     * @param ip        登陆的IP地址
     * @param port      登陆的端口
     * @param username  用户名
     * @param password  密码
     * @param enable    是否需要使用enable模式， 非必填
     * @param enablePwd 使用enable模式下，可能还需要输入密码，非必填
     * @return 是否登陆成功
     */
    public boolean login(String ip, int port, String username, String password, boolean enable, String enablePwd);

    /**
     * 登录，该方法可以自动处理enable模式和进入非more模式
     *
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param manufacturer 厂家
     * @param model        型号
     * @return
     */
    public boolean login(String ip, int port, String username, String password, String manufacturer, String model);

    /**
     * 对象形式登陆
     *
     * @param loginInfo
     * @return
     */
    public boolean login(LoginInfo loginInfo);

    /**
     * 登陆，对第一个使用Telnet或者SSH进行登陆，之后那些使用命令行方式
     *
     * @param loginInfos 登陆信息，多级跳转
     * @return
     */
    public boolean login(List<LoginInfo> loginInfos);

    /**
     * 记录执行详情
     *
     * @param loginInfos
     * @param execDetails
     * @return
     */
    public boolean login(List<LoginInfo> loginInfos, List<String> execDetails);

    /**
     * 发送命令
     *
     * @param cmd 发送的命令
     * @return 是否发送成功，cmd为空时算失败，因为这样可以不让读取方法去监听读取返回数据
     */
    public boolean execCommand(Object cmd);

    /**
     * 读取返回数据
     *
     * @return
     */
    public String readData();

    /**
     * 读取一段数据，直到遇到 [行标识符] 或者 [指定终结字符串] 或者达到 [指定超时时间]为止
     *
     * @param endStrRegex
     * @return 读取的一段字符串
     */
    public String readData(String endStrRegex);

    /**
     * @param readTimeout
     * @return
     */
    public String readData(long readTimeout);

    /**
     * @param endStrRegex
     * @param readTimeout
     * @return
     */
    public String readData(String endStrRegex, long readTimeout);

    /**
     * @param endStrRegex
     * @param notEndStrRegex
     * @param readTimeout
     * @return
     */
    public String readData(String endStrRegex, String notEndStrRegex, long readTimeout);

    /**
     * @param endStrRegex
     * @param notEndStrRegex
     * @param isLogSrcContent
     * @param cutEndLineCount
     * @return
     */
    public String readData(String endStrRegex, String notEndStrRegex, boolean isLogSrcContent, int cutEndLineCount);

    /**
     * 读取数据
     *
     * @param param
     * @return
     */
    public String read(ReadParam param);

    /**
     * @param endStrRegex
     * @param notEndStrRegex
     * @param isLogSrcContent
     * @param cutEndLineCount
     * @param readTimeout
     * @return
     */
    public String readData(String endStrRegex, String notEndStrRegex, boolean isLogSrcContent, int cutEndLineCount, long readTimeout);

    /**
     * 发送和读取数据
     *
     * @param cmd         发送的数据或者命令
     * @param endStrRegex 指定的读取字符串尾部
     * @return 注意，返回null是代表可能发送命令失败和接收返回数据失败，而返回""是正常的情况
     */
    public String execCmdAndReceiveData(Object cmd, String endStrRegex);

    /**
     * 发送和读取数据
     *
     * @param cmd 发送的数据或者命令
     * @return 注意，返回null是代表可能发送命令失败和接收返回数据失败，而返回""是正常的情况
     */
    public String execCmdAndReceiveData(Object cmd);

    /**
     * 发送和读取数据
     *
     * @param cmd         发送的数据或者命令
     * @param endStrRegex 指定的读取字符串尾部正则
     * @param timeout     超时，单位毫秒
     * @return 注意，返回null是代表可能发送命令失败和接收返回数据失败，而返回""是正常的情况
     */
    public String execCmdAndReceiveData(Object cmd, String endStrRegex, long timeout);

    /**
     * 发送和读取数据
     *
     * @param cmd   执行的命令
     * @param param 读取时的识别参数
     * @return
     */
    public String execCmdAndReceive(Object cmd, ReadParam param);

    /**
     * @param cmd            发送的命令
     * @param endStrRegex    指定结尾字符串正则
     * @param notEndStrRegex 不能以此结尾正则
     * @param timeout
     * @return
     */
    public String execCmdAndReceiveData(Object cmd, String endStrRegex, String notEndStrRegex, long timeout);

    /**
     * 退出登录，释放相关资源
     */
    public void logout();

    /**
     * 是否登陆成功，判断当前连接是否已经登录并且是成功的
     *
     * @return
     */
    public boolean isLogin();

    /**
     * 是否在登录的时候发生严重错误，比如网络不通
     *
     * @return
     */
    public boolean isLoginError();

    /**
     * 关闭释放所有资源
     */
    public void close();

    /**
     * 设置字符集
     */
    public void setCharset(String charset);

    /**
     * 获取执行细节
     *
     * @return 执行过程每一步所获取的数据
     */
    public List<String> getExecDetail();

    /**
     * 清空当前的执行细节
     *
     * @return
     */
    public void cleanExecDetail();

    public String getIp();

    public int getPort();

    public String getUsername();

    public String getPassword();

    public void setIp(String ip);

    public void setCmdClientType(CmdClientTypeEnum cmdClientType);

    public void setPort(int port);

    public void setUsername(String username);

    public void setPassword(String password);

    /**
     * 获取命令行客户端类型
     */
    public CmdClientTypeEnum getCmdClientType();

    /**
     * 获取登陆信息
     *
     * @return 返回IP、端口、用户名、密码、是否已经登陆信息
     */
    public String getLoginInfo();

    /**
     * 设置当读取不到流的数据的时候，还要等多久才可以结束
     *
     * @param waitTime
     */
    public void setWaitEndTimeWhenUnReadData(int waitTime);

    /**
     * 读取字符，直到指定的行结束字符串或者默认的行结束符;
     * 1、有判断是否需要回答yes/no的问题；
     * 2、判断本次获取的数据是否有more命令行，然后发送空格；
     * 3、在进行行结束判断前需要等待一段时间判断是否继续有数据传来，以确保数据不会在中间部分有行结束符等；
     * 4、每次获取数据，判断最后一个字节是否是流结束符。
     * 5、使用了非阻塞队列，以达到超时中断。
     * 6、解析返回数据时，需要先转换成字节，然后再转换成指定的编码格式（解决中文乱码问题）。
     *
     * @param endStrRegex     指定结束字符串正则
     * @param notEndStrRegex  指定非结束字符串正则
     * @param isLogReturnData 是否打印返回的原始内容到日志
     * @param isAutoAnswerYes 是否自动回答Yes，否则回答no
     * @param timeout         超时，单位毫秒
     * @return 注意，返回null是代表可能发送命令失败或者接收返回数据失败异常，而返回""是正常的情况
     */
    public String readRaw(String endStrRegex, String notEndStrRegex, boolean isLogReturnData, boolean isAutoAnswer, boolean isAutoAnswerYes, long timeout);

    /**
     * 自动回答问题的no选项
     *
     * @param endStrRegex     指定结束字符串正则
     * @param notEndStrRegex  指定非结束字符串正则
     * @param isLogReturnData 是否打印返回的原始内容到日志
     * @param timeout
     * @return
     */
    public String readRawAutoAnswerNo(String endStrRegex, String notEndStrRegex, boolean isLogReturnData, long timeout);

    /**
     * 自动回答问题的yes选项
     *
     * @param endStrRegex     指定结束字符串正则
     * @param notEndStrRegex  指定非结束字符串正则
     * @param isLogReturnData 是否打印返回的原始内容到日志
     * @param timeout
     * @return
     */
    public String readRawAutoAnswerYes(String endStrRegex, String notEndStrRegex, boolean isLogReturnData, long timeout);

    /**
     * 获取登录的Socket信息
     *
     * @return 登录类型，IP和端口
     */
    public String getLoginSocketInfo();

    /**
     * 修改命令行输出的时候，直接输出所有数据，而不需要敲击more命令。
     * 进入非执行去掉more模式的命令
     *
     * @param manufacturer 厂商
     * @return 是否执行成功
     */
    public boolean modifyCmdOutputAllDataImmediate(String manufacturer);

    /**
     * 修改命令行输出的时候，直接输出所有数据，而不需要敲击more命令。
     * 进入非执行去掉more模式的命令
     *
     * @param manufacturer 厂商
     * @param model        设备型号
     * @return 是否执行成功
     */
    public boolean modifyCmdOutputAllDataImmediate(String manufacturer, String model);

    /**
     * 进入enable模式
     *
     * @param enablePwd
     */
    public boolean enableHandle(String enablePwd);

    /**
     * 获取enable密码
     *
     * @return
     */
    public String getEnablePwd();

    /**
     * 重置PTY的大小，当某些厂家显示的行列信息过于小，调用此方法重置，
     * 但是SSH方式的华为设备，调用此方法会导致耗时15秒左右
     */
    public void resetPtySize();

    public boolean isEnable();

    public void setEnable(boolean enable);

    public void setEnablePwd(String enablePwd);

    /**
     * 最后执行的命令读取超时
     *
     * @return
     */
    public boolean lastCmdReadTimeout();

    /**
     * 登录计数，用于后期执行quit命令的次数
     *
     * @return
     */
    public int getLoginCount();

    /**
     * 登录计数自增1
     */
    public void incrementLoginCount();

    /**
     * 关闭的时候同时执行quit命令
     */
    public void closeWithQuitCmd();

    /**
     * 关闭的时候同时执行exit命令
     */
    public void closeWithExitCmd();

    public String getManufacturer();

    void setManufacturer(String manufacturer);

    public String getModel();

    public void setModel(String model);

    /**
     * 关闭输入内容添加到detail功能
     */
    public void closeInputToDetail();

    /**
     * 打开输入内容添加到detail功能
     */
    public void openInputToDetail();

    /**
     * 是不是netconf协议
     *
     * @return
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     */
    public boolean isNetconf();

    /**
     * 是否有错误信息
     *
     * @param info
     * @return
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     */
    public boolean hasError(String info);

    public boolean hasError();

    /**
     * 是否有正确信息
     *
     * @param info
     * @return
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     */
    public boolean hasOk(String info);

    public boolean hasOk();

    /**
     * 获取最后的响应信息
     *
     * @return
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     */
    public String getLastReplyInfo();

    /**
     * 最后的发送信息
     *
     * @return
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     */
    public String getLastSendInfo();

    /**
     * 是否校验enable错误，默认：false
     *
     * @param isCheck true:校验，false不校验
     */
    void checkEnableFail(boolean isCheck);
}
