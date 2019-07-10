package com.latico.commons.net.cmdclient;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.net.cmdclient.bean.*;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import com.latico.commons.net.cmdclient.enums.KeyboardValue;
import com.latico.commons.net.cmdclient.enums.TerminalTypeEnum;
import com.latico.commons.net.cmdclient.portmaper.IpPortMaper;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


/**
 * <PRE>
 * 抽象的连接类，同一些公共的定义
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2017年3月18日</B>
 * @since <B>JDK1.6</B>
 */
public abstract class AbstractCmdClient implements CmdClient {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCmdClient.class);

    /**
     * skipCmds 跳过的步骤的命令集合,比如询问是否需要修改密码,跳过的命令集合,key为等待的字符，value为输入的命令，比如：key为(y/n?)，value为n，这样等待收到(y/n?)字符串后输入命令n
     */
    public final static List<String[]> skipStepCmds = new ArrayList<String[]>();

    static {
        skipStepCmds.add(new String[]{null, KeyboardValue.ENTER});//发送一次确认键，防止一些特殊情况
    }

    /**
     * dataReader 数据读取流//需要用字节流读取，用字符流会有中文乱码
     */
    protected InputStream dataReader;

    /**
     * dataWriter，数据输出流，打印输出流 ，方便发送
     */
    protected PrintStream dataWriter;

    /**
     * loginStatus 连接状态，当前是否在连接中
     */
    protected boolean loginStatus = false;

    /**
     * loginError 是否在登录的时候发生严重错误
     */
    protected boolean loginError = false;

    /**
     * 连接超时，默认15秒
     */
    protected int connTimeout = CmdClientConfig.getInstance().getCmdConnTimeout();

    /**
     * 读取返回数据超时，默认1分钟，建议对于返回时间有明确的命令的时候，请在调用读取返回数据的时候传入更适合的超时时间readData()方法中有对应的参数可传
     */
    protected int readTimeout = 30000;

    /**
     * waitBufferedTime 每次从缓冲中获取数据时等待的时间，毫秒
     */
    protected final int waitBufferedTime = 10;

    /**
     * printQuestionMaxLen 打印的问题最大长度
     */
    protected static final int printQuestionMaxLen = 30;

    /**
     * whenUnReadToEndWaitTime 默认100毫秒，当从缓冲中获取不到数据时，需要等待多久才允许判断是否达到结束,该参数视网络环境而定
     */
    protected int whenUnReadToEndWaitTime = CmdClientConfig.getInstance().getCmdAllowCheckLineEndTime();

    /**
     * specialCmdCheckLineEndTime 指定特殊命令允许校验行标识符的时间，跟whenUnReadToEndWaitTime功能一样
     */
    protected int specialCmdCheckLineEndTime = CmdClientConfig.getInstance().getSpecialCmdCheckLineEndTime();

    /**
     * connHostIp 当前连接的主机IP
     */
    protected String remoteIp = "127.0.0.1";

    /**
     * connHostPort 当前连接的主机端口，默认23，telnet端口
     */
    protected int remotePort = 23;

    /**
     * connUserName 连接的用户名,默认root
     */
    protected String remoteUsername = "root";

    /**
     * connPassword 连接的密码,默认root
     */
    protected String remotePassword = "root";

    /**
     * isEnable 是否enable
     */
    protected boolean enable = false;

    /**
     * enablePwd enable模式下，如果需要密码
     */
    protected String enablePwd = null;

    /**
     * currentCmd 当前执行的命令行
     */
    protected Object currentCmd = "";

    /**
     * lastReplyInfo 最后的响应信息
     */
    private String lastReplyInfo;

    /**
     * terminalType 终端协议类型
     */
    protected TerminalTypeEnum terminalType = TerminalTypeEnum.VT220;

    /**
     * dateCharset 解析数据流的字符集，默认是获取系统字符集
     */
    protected String dateCharset = CharsetType.DEFAULT;

    /**
     * isLogSrcData 是否把读取到的原始数据打印到日志
     */
    protected boolean isLogSrcData = false;

    /**
     * resetPtySize 是否设置PTP的大小，对于华为设备，设置此值会耗时十几二十秒，所以默认关闭
     */
    protected boolean resetPtySize = false;

    /**
     * execDetails 执行详情
     */
    protected List<String> execDetails = new ArrayList<String>();

    /**
     * cmdClientType 命令行客户端类型
     */
    protected CmdClientTypeEnum cmdClientType;

    /**
     * lastChar 最后一个字符
     */
    private String lastChar = "";

    /**
     * lastCmdReadTimeout 最后执行的命令是否读取超时
     */
    private boolean lastCmdReadTimeout = false;

    /**
     * loginCount 登录计数，从一个设备用命令登录另外一个设备，那就自增1
     */
    private int loginCount = 1;

    /**
     * manufacturer 设备厂家
     */
    private String manufacturer;

    /**
     * model 设备型号
     */
    private String model;

    /**
     * inputToDetail 输入内容添加到detail
     */
    private boolean inputToDetail = true;

    /**
     * 是否检测enable错误
     */
    private boolean checkEnableFail = false;

    protected AbstractCmdClient(TerminalTypeEnum terminalType, String dateCharset) {
        this.dateCharset = dateCharset;
        this.terminalType = terminalType;
    }

    protected AbstractCmdClient() {
    }

    /**
     * 发送指令，注意是要有换行符（确认输入）和用flush()刷出去来发送
     *
     * @param cmd 发送的字符串
     */
    @Override
    public boolean execCommand(Object cmd) {
        this.currentCmd = cmd;
        if (!loginStatus) {
            LOG.warn("[{}:{}] 请先登陆成功再执行命令 [{}]", remoteIp, remotePort, cmd);
            return false;
        }
        boolean succ = false;

        //为空时算失败
        if (cmd == null) {
            LOG.warn("[{}:{}] 当前要执行的命令为空", remoteIp, remotePort);
            succ = false;

            //不为空时发送执行命令
        } else {
            this.currentCmd = cmd;
            try {
                //如果是回车命令，直接发送回车
                LOG.info("{} 执行命令 [{}]", getLoginSocketInfo(), cmd);
                ThreadUtils.sleepMilliSeconds(waitBufferedTime);
                if (KeyboardValue.ENTER == cmd) {
                    //使用自定义的\n作为换行符
                    dataWriter.print(KeyboardValue.ENTER);
//					dataWriter.println();
                } else {
                    dataWriter.print(cmd);

                    //使用自定义的\n作为换行符
                    dataWriter.print(KeyboardValue.ENTER);
//					dataWriter.println();
                }
                dataWriter.flush();
                ThreadUtils.sleepMilliSeconds(20);
                succ = true;

                //添加执行的命令，由于比如中兴设备，不会在读取流产生
                if (inputToDetail) {
                    addExecCmdToExecDetail(cmd.toString());
                }
            } catch (Exception e) {
                LOG.error("发送命令 [{}] 到 {} 失败 {},关闭连接", cmd, getLoginSocketInfo(), e);
                close();
                succ = false;
            }
        }
        return succ;
    }

    @Override
    public void setWaitEndTimeWhenUnReadData(int waitTime) {
        if (waitTime >= 10) {
            this.whenUnReadToEndWaitTime = waitTime;
        }
    }

    @Override
    public String execCmdAndReceiveData(Object cmd) {
        return execCmdAndReceiveData(cmd, null);
    }

    /**
     * 发送和接收数据
     *
     * @return
     */
    @Override
    public String execCmdAndReceiveData(Object cmd, String endStrRegex) {
        if (execCommand(cmd)) {
            return readData(endStrRegex);
        } else {
            return null;
        }
    }

    @Override
    public String execCmdAndReceiveData(Object cmd, String endStrRegex, long timeout) {
        return execCmdAndReceiveData(cmd, endStrRegex, null, timeout);
    }

    @Override
    public String execCmdAndReceiveData(Object cmd, String endStrRegex, String notEndStrRegex, long timeout) {
        if (execCommand(cmd)) {
            return readData(endStrRegex, notEndStrRegex, timeout);
        } else {
            return null;
        }
    }

    @Override
    public String readData(String endStrRegex, String notEndStrRegex, long readTimeout) {
        return readData(endStrRegex, notEndStrRegex, false, 0, readTimeout);
    }

    protected String readAutoAnswerNo(String endStrRegex, String notEndStrRegex, boolean isLogReturnData) {
        return readRawAutoAnswerNo(endStrRegex, notEndStrRegex, isLogReturnData, readTimeout);
    }

    @Override
    public String readRawAutoAnswerNo(String endStrRegex, String notEndStrRegex, boolean isLogReturnData, long timeout) {
        return readRaw(endStrRegex, notEndStrRegex, isLogReturnData, true, false, timeout);
    }

    @Override
    public String readRawAutoAnswerYes(String endStrRegex, String notEndStrRegex, boolean isLogReturnData, long timeout) {
        return readRaw(endStrRegex, notEndStrRegex, isLogReturnData, true, true, timeout);
    }

    @Override
    public String readRaw(String endStrRegex, String notEndStrRegex, boolean isLogReturnData,
                          boolean isAutoAnswer, boolean isAutoAnswerYes, long timeout) {
        if (!loginStatus) {
            LOG.warn("{} 请先登陆成功再执行读取数据", getLoginInfo());
            return null;
        }

        //重置读取超时状态
        lastCmdReadTimeout = false;

        boolean isCheckNotEndStr = false;//是否校验不能以此结束的正则
        if (StringUtils.isNoneEmpty(notEndStrRegex)) {
            isCheckNotEndStr = true;
        }
        int curCmdWhenUnReadToEndWaitTime = whenUnReadToEndWaitTime;

        if (currentCmd.toString().matches(CmdClientConfig.getInstance().getSpecialCmdCheckLineEndRegex())) {
            curCmdWhenUnReadToEndWaitTime = specialCmdCheckLineEndTime;
            LOG.info("{} 匹配到指定时长命令，允许校验行标识符的时长从{}ms变为{}ms", getLoginSocketInfo(), whenUnReadToEndWaitTime, specialCmdCheckLineEndTime);
        }

        final int whenUnReadToEndWaitTimeCount = curCmdWhenUnReadToEndWaitTime / waitBufferedTime;
        boolean isGetedData = false;//当前获取数据时，是否获取到数据
        boolean isCanCheckEnd = false;//是否达到要求允许校验命令行结束
        int unReadDataCount = 0;//没有读取到数据的次数计数器
        StringBuffer rawByteData = new StringBuffer();//返回原始字节形式的总数据的字符串组拼形式

        //临时变量
        String yesOrNoQuestionAnswer = null;
        StringBuffer returnByteDataTmp = new StringBuffer();//当前批次的返回数据
        try {

            //由于缓冲区没有准备好数据，所以睡眠50毫秒
            ThreadUtils.sleepMilliSeconds(waitBufferedTime);//没有获取过数据就睡眠等待一段时间

            final long timeoutPoint = System.currentTimeMillis() + timeout;//读取的主动控制的超时时间点
            final long connTimeoutPoint = System.currentTimeMillis() + connTimeout;//读取的主动控制的超时时间点
            //在超时时间前，利用非阻塞式不断轮询从数据缓存中获取数据
            while (true) {

                //如果是在设备上执行了telnet、stelnet、ssh命令，并且达到了连接的超时，直接返回空
                if (System.currentTimeMillis() >= connTimeoutPoint) {
                    if (checkCmdConnFail(currentCmd, returnByteDataTmp.toString())) {
                        addExecDetail(returnByteDataTmp.toString());
                        printSrcContent(isLogReturnData, returnByteDataTmp.toString());
                        return null;
                    }
                }

                if (System.currentTimeMillis() >= timeoutPoint) {
                    LOG.error("[{}] {} 读取返回信息时,在限定时间({}ms)内无法检测到命令结束", Thread.currentThread().getName(), getLoginInfo(), timeout);
                    lastCmdReadTimeout = true;
                    if (StringUtils.isEmpty(rawByteData.toString()) && StringUtils.isEmpty(returnByteDataTmp.toString())) {
                        LOG.error("[{}] {} 本次未能接收到数据,如果确认发送过命令,可能是流发生了未知的通信异常情况,直接关闭连接", Thread.currentThread().getName(), getLoginInfo());
                        close();
                    }
                    break;
                }

                //非阻塞式，获取不到返回0
                int cacheCurrentGetedMaxLen = dataReader.available();

                if (cacheCurrentGetedMaxLen >= 1) {
                    //临时计数用
                    isGetedData = true;
                    unReadDataCount = 0;

                    //读取本次的数据到returnByteDataTmp
                    if (readDataToStrAndCheckIsEnd(returnByteDataTmp, cacheCurrentGetedMaxLen)) {
                        break;
                    }

                    //如果没有读取到数据，就开始判断是否达到结束
                } else {

                    //如果没有读取到数据达到指定次数，就允许检查是否结束
                    unReadDataCount++;
                    if (unReadDataCount >= whenUnReadToEndWaitTimeCount) {
                        isCanCheckEnd = true;
                    }
                    String dataTmp = returnByteDataTmp.toString();

                    //判断命令是否需要回答问题,同时获取问题的答案作为命令发送
                    yesOrNoQuestionAnswer = forceCheckHasYesNoQuestionWithAnswer(remoteIp, remotePort, dataTmp);
//					boolean forceCheckHasYesNoQuestion = false;
//					if(StringUtils.isNoneEmpty(yesOrNoQuestionAnswer)){
//						forceCheckHasYesNoQuestion = true;
//					}else{
//						yesOrNoQuestionAnswer = getYesNoQuestionAnswer(isGetedData, isAutoAnswerYes, dataTmp);
//					}

                    //判断是不是需要回答问题
                    if (hasAnswer(yesOrNoQuestionAnswer)) {

                        //强制回答，所以是否自动回答参数取消
//						if(!isAutoAnswer){
//							LOG.info("{} 不自动回答Yes|No问题,本轮接收结束", getLoginSocketInfo());
//							break;
//						}
                        rawByteData.append(returnByteDataTmp);
                        returnByteDataTmp = new StringBuffer();

                        //如果是自动回答问题就发送命令，否则跳出循环
                        LOG.info("{} 自动回答Yes|No问题,答案[{}]", getLoginSocketInfo(), yesOrNoQuestionAnswer);
                        execCommand(yesOrNoQuestionAnswer);


                        //判断是不是由于more标识符导致的读取不到数据，检查本次获取到的数据中是否有more标识符结尾就发送空格符，同时把获取的数据转移到正式串中并重置临时串
                    } else if (isGetedData && checkHasMore(remoteIp, remotePort, dataTmp)) {

                        rawByteData.append(returnByteDataTmp);
                        rawByteData.append(KeyboardValue.ENTER);
                        returnByteDataTmp = new StringBuffer();
                        execCommand(KeyboardValue.SPACE);

                    } else {

                        //获得过数据同时允许检查的时候才检查
                        if (isGetedData && isCanCheckEnd) {
                            if (isCheckNotEndStr && dataTmp.matches(notEndStrRegex)) {
                                printNotEndStrRegex(notEndStrRegex, dataTmp);
                            } else if (checkIsEnd(endStrRegex, dataTmp)) {
                                break;
                            }
                            //重置计数
                            isGetedData = false;
                            unReadDataCount = 0;
                            isCanCheckEnd = false;
                        }
                    }
                }

                //睡眠等待下一批数据
                ThreadUtils.sleepMilliSeconds(waitBufferedTime);//没有获取过数据就睡眠等待一段时间
            }

            //把最后未转移到正式串的部分添加进正式串中
            rawByteData.append(returnByteDataTmp);

        } catch (Exception e) {
            return readExceptionHandle(rawByteData, e);
        }

        //转换解释返回结果并返回
        String returnData = convertByteDataToStr(rawByteData);

        //是否打印原始内容到日志
        printSrcContent(isLogReturnData, returnData);

        // XXX 获取最后一个字符,用于命令行结束符不改变的命令判断结束符使用
        if (returnData.length() >= 1) {
            String s = cutDataToCheckRegex(returnData);
            s = s.trim();
            if (!"".equals(s)) {
                lastChar = s.substring(s.length() - 1);

            } else {
                lastChar = "";
            }
        }
        LOG.info("{}当前命令行结束符[{}]", getLoginSocketInfo(), lastChar);
        //如果不是结束符中的某一个
        if (!matchLineEndChar(lastChar)) {
            LOG.error("{}命令行结束符异常[{}]", getLoginSocketInfo(), lastChar);
        }

        //缓存最后响应数据
        lastReplyInfo = returnData;

        return returnData;
    }

    /**
     * @param lastChar
     * @return
     */
    private boolean matchLineEndChar(String lastChar) {
        return lastChar.matches("[:：#\\$>?\\]]");
    }

    /**
     * 校验命令行方式跳转设备失败
     *
     * @param cmd
     * @param returnData
     * @return
     */
    private boolean checkCmdConnFail(Object cmd, String returnData) {
        if (cmd != null && cmd.toString().trim().matches("^(tel|stel|ssh).+?")) {

            if (returnData.toString().matches(CmdClientConfig.getInstance().getCmdConnFailRegex())) {
                LOG.error("{} 登录命令[{}]执行异常, 返回报文:[{}]", getLoginSocketInfo(), cmd, returnData);
                return true;
            }
        }
        return false;
    }

    /**
     * 把返回数据结果的字节形式转换成字符串形式
     *
     * @param rawByteData 原始的字节形式
     * @return
     */
    public String convertByteDataToStr(StringBuffer rawByteData) {
        String returnData = "";
        try {
            if (!"".equals(rawByteData.toString())) {
                returnData = new String(rawByteData.toString().getBytes(CharsetType.ISO), dateCharset);
                addExecDetail(returnData);//保存返回所有数据记录细节
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error(getLoginInfo() + " 读取返回信息的时候发生异常", e);
        }

        return returnData;
    }

    /**
     * 根据本次缓冲区读取的长度，把本次的数据到readData,如果最后一个字节是-1，说明达到流结束
     *
     * @param readData   读取到的数据放到这里
     * @param readMaxLen 从流中读取的最大字节量
     * @return 是否达到结束，true：流结束
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public boolean readDataToStrAndCheckIsEnd(StringBuffer readData, int readMaxLen)
            throws Exception {
        byte[] buff;
        buff = new byte[readMaxLen];
        //读取非最后一个字节
        readMaxLen = dataReader.read(buff, 0, readMaxLen);
        readData.append(new String(buff, 0, readMaxLen, CharsetType.ISO));
        return false;
    }

    /**
     * 是否有答案
     *
     * @param answer 获取到的答案值
     * @return
     */
    private boolean hasAnswer(String answer) {
        if (StringUtils.isEmpty(answer)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 读取数据时发生异常的处理
     *
     * @param returnByteData
     * @param e
     * @return
     */
    public String readExceptionHandle(StringBuffer returnByteData, Exception e) {
        try {
            LOG.error("{} 发送读取异常前, 已读取到的内容有：\r\n{}", getLoginInfo(), new String(returnByteData.toString().getBytes(CharsetType.ISO), CharsetType.DEFAULT));
        } catch (Exception e1) {
            LOG.error("{} 解析返回信息的时候发生异常, {}", getLoginInfo(), e1);
        }

        //如果是读取流异常，那这个异常会导致读取流都会有问题，所以直接关闭它//20180326 //XXX
//		LOG.error("{} 读取返回信息的时候发生异常,该连接已不可用,被关闭", getLoginInfo(), e);
//		close();
        return null;//发生异常，返回null，已读取
    }

    /**
     * 获取Yes|No问题的答案
     *
     * @param isAutoAnswerYes
     * @param isGetedData
     * @param dataTmp
     * @return
     */
    public String getYesNoQuestionAnswer(boolean isGetedData, boolean isAutoAnswerYes, String dataTmp) {
        String answer = null;
        if (isGetedData) {
            //通用的强制问题回答
            answer = getAnswer(remoteIp, remotePort, isAutoAnswerYes, dataTmp);
        }
        return answer;
    }

    /**
     * 获取答案
     *
     * @param isAutoAnswerYes true：选择答案yes，false：选择答案no
     * @param dataTmp
     * @return
     */
    public static String getAnswer(String ip, int port, boolean isAutoAnswerYes, String dataTmp) {
        String answer = null;
        if (isAutoAnswerYes) {
            answer = checkHasYesNoQuestionWithAnswerYes(ip, port, dataTmp);

        } else {
            answer = checkHasYesNoQuestionWithAnswerNo(ip, port, dataTmp);
        }
        return answer;
    }

    /**
     * 强制的校验回答的问题类型
     *
     * @param ip
     * @param port
     * @param data
     * @return
     */
    public String forceCheckHasYesNoQuestionWithAnswer(String ip, int port, String data) {

        //netconf不需要检测
        if (isNetconf()) {
            return null;
        }
        String answer = null;
        if (data != null) {
            //切割字符串，为了提高匹配正则的效率
            data = cutDataToCheckRegex(data, 200);
            for (ForceQuestionWithAnswer pat : CmdClientConfig.getInstance().getForceCheckHasYesNoQuestionWithAnswerPatterns()) {
                Matcher mat = pat.getPattern().matcher(data);
                if (mat.find()) {
                    answer = mat.group(pat.getRegexGroupIndex()).trim();

                    data = getPrintData(data);
                    LOG.info("[{}:{}]  [匹配到强制回答问题标识符:{}], 选择答案为[{}]", ip, port, data, answer);
                    return answer;
                }
            }

        }
        return answer;
    }

    /**
     * @param isLogReturnData
     * @param returnData
     */
    private void printSrcContent(boolean isLogReturnData, String returnData) {
        if (isLogReturnData) {
            LOG.info("{} 本次返回的原始内容：\r\n{}", getLoginSocketInfo(), returnData);
        } else {
            LOG.debug("{} 本次返回的原始内容：\r\n{}", getLoginSocketInfo(), returnData);
        }
    }

    /**
     * 校验非结束符
     *
     * @param notEndStrRegex
     * @param dataTmp
     */
    public void printNotEndStrRegex(String notEndStrRegex, String dataTmp) {
        int startIndex = 0;
        int len = dataTmp.length();
        if (len >= 10) {
            startIndex = len - 10;
        }
        LOG.info("{} {} [匹配到非结束尾正则:{}]", getLoginSocketInfo(), dataTmp.substring(startIndex, len), notEndStrRegex);
    }

    /**
     * 1、如果是yes或者no的选择，默认回答no；
     * 2、[Yes/no] ?:
     * 3、(Y/N) ?:
     * agg[Y/N]:
     * agg[Y/N]
     *
     * @param s
     * @return yes or no
     */
    public static String checkHasYesNoQuestionWithAnswerNo(String ip, int port, String s) {
        String answer = null;
        if (s != null) {

            //切割字符串，为了提高匹配正则的效率
            s = cutDataToCheckRegex(s);
            Matcher mat = yesOrNoQuestionPattern.matcher(s);
            if (mat.find()) {
                answer = mat.group(3).trim();

                s = getPrintData(s);
                LOG.info("[{}:{}] [匹配到Yes/No问题标识符:{}], 选择答案为[{}]", ip, port, s, answer);
            }
        }
        return answer;
    }

    /**
     * @param s
     * @return
     */
    private static String getPrintData(String s) {
        return getPrintData(s, printQuestionMaxLen);
    }

    private static String getPrintData(String s, int maxLen) {
        int startIndex = 0;
        int len = s.length();
        if (len >= maxLen) {
            startIndex = len - maxLen;
        }
        s = s.substring(startIndex, len).trim();
        return s;
    }

    /**
     * 1、如果是yes或者no的选择，默认回答yes；
     * 2、[Yes/no] ?:
     * 3、(Y/N) ?:
     *
     * @param s
     * @return yes or no
     */
    public static String checkHasYesNoQuestionWithAnswerYes(String ip, int port, String s) {
        String answer = null;
        if (s != null) {
            //切割字符串，为了提高匹配正则的效率
            s = cutDataToCheckRegex(s);
            Matcher mat = yesOrNoQuestionPattern.matcher(s);
            if (mat.find()) {
                answer = mat.group(2).trim();

                s = getPrintData(s);
                LOG.info("[{}:{}]  [匹配到Yes/No问题标识符:{}], 选择答案为[{}]", ip, port, s, answer);
            }
        }
        return answer;
    }

    /**
     * 切割字符串，为了提升正则匹配效率
     *
     * @param s
     * @return
     */
    public static String cutDataToCheckRegex(String s) {
        //正则有问题，提升正则匹配效率
        return cutDataToCheckRegex(s, 100);
    }

    /**
     * 切割字符串，为了提升正则匹配效率
     *
     * @param s
     * @return
     */
    public static String cutDataToCheckRegex(String s, int maxLen) {
        //正则有问题，提升正则匹配效率
        if (StringUtils.isNoneEmpty(s)) {
            int len = s.length();
            if (len > maxLen) {
                s = s.substring(len - maxLen, len);
            }
        }
        return s;
    }

    /**
     * 判断是否达到结束
     *
     * @param endStrRegex
     * @param data
     * @return
     */
    public boolean checkIsEnd(String endStrRegex, String data) {
        boolean isEnd = false;

        if (StringUtils.isEmpty(data)) {
            return isEnd;
        }

        //检测结尾是否达到指定的字符串结尾
        if (checkIsEndByEndStr(endStrRegex, data)) {
            isEnd = true;
        }

        //检查是否达到行标识符结尾
        if (checkIsEndLineTag(remoteIp, remotePort, data)) {
            isEnd = true;
        }
        return isEnd;
    }

    /**
     * 检查是否包含more形式的内容，有的话返回true，提示上层需要发送空格符
     *
     * @param str
     * @return
     */
    public static boolean checkHasMore(String ip, int port, String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        //切割字符串，为了提高匹配正则的效率
        str = cutDataToCheckRegex(str);
        Matcher mat = moreTagPattern.matcher(str);
        if (mat.find()) {
            LOG.info("[{}:{}] [匹配到More标识符:{}]", ip, port, mat.group(1));
            return true;
        } else {
            return false;
        }

    }

    /**
     * XXX，判断行位是否达到结束的判断条件，智能处理结束符。
     * 程序解析有问题，一般是该方法需要不断优化支持场景
     *
     * @param ip
     * @param port
     * @param data
     * @return
     */
    private boolean checkIsEndLineTag(String ip, int port, String data) {
        boolean isEnd = false;
        //校验最后一行是不是以空格开头
        String lastLine = data.trim();

        if (isNetconf()) {
            if (lastLine.matches(CmdClientConfig.getInstance().getNetconfLineEndRegex())) {
                return true;
            } else {
                return false;
            }
        } else {

            //处理结束符不改变的命令，那就读取上一次最后一个结束符匹配本次最后一个可见符号
            if (currentCmd != null && currentCmd.toString().matches(CmdClientConfig.getInstance().getPromptUnChangeCmdRegex())) {
                if (StringUtils.isNoneEmpty(lastChar) && matchLineEndChar(lastChar)) {

                    //不是结束符，不能结束
                    if (!lastLine.endsWith(lastChar)) {
                        return false;
                    }
                }
//			if(lastLine.matchesPath("[\\s\\S]*:\\s*")){
//				return false;
//			}
//			
//			//show命令不能以]符号结束
//			if(currentCmd.toString().trim().startsWith("show")){
//				if(lastLine.matchesPath("[\\s\\S]*\\]\\s*")){
//					return false;
//				}
//			}
            }

            int len = lastLine.length();

            //获取最后一行，利用换行符获取最后一行
            int index = lastLine.lastIndexOf("\n");
            if (index >= 1 && len > index) {
                lastLine = lastLine.substring(index + "\n".length(), len).trim();
            } else {
                lastLine = null;
            }

            //优先使用最后一行进行判断
            if (StringUtils.isNoneEmpty(lastLine)) {

                if (lastLine.matches(cmdLastLineEndRegex)) {
                    isEnd = true;
                    LOG.info("[{}:{}] [尾行:{}] 匹配到 [行标识符] 结束字符串", ip, port, lastLine);
                }

            } else {

                //切割字符串，为了提高匹配正则的效率
                data = cutDataToCheckRegex(data);
                if (data != null && data.matches(LINE_TAGS_REGEX)) {

                    isEnd = true;

                    data = getPrintData(data, 15);
                    LOG.info("[{}:{}] [数据尾:{}] 匹配到 [行标识符] 结束字符串", ip, port,
                            data.replaceAll(LINE_SEPARATOR_REGEX, ""));
                }
            }

            return isEnd;
        }

    }

    /**
     * 校验字符串是否达到结尾
     *
     * @param endStrRegex 结尾字符串
     * @param data        判断字符串
     * @return
     */
    private boolean checkIsEndByEndStr(String endStrRegex, String data) {
        boolean isEnd = false;//是否符合结束条件
        //切割字符串，为了提高匹配正则的效率
        data = cutDataToCheckRegex(data);
        //判断是否以结束符结尾
        if (endStrRegex != null && !"".equals(endStrRegex) && data != null && data.matches(endStrRegex)) {
            data = getPrintData(data);
            LOG.info("{} 数据尾 [{}] 匹配到 [指定结束字符串:{}]", getLoginSocketInfo(),
                    data.replaceAll(LINE_SEPARATOR_REGEX, ""), endStrRegex);
            isEnd = true;
        }
        return isEnd;
    }

    @Override
    public boolean login(List<LoginInfo> loginInfos, List<String> execDetails) {
        if (execDetails != null) {
            this.execDetails = execDetails;
        }
        return login(loginInfos);
    }

    @Override
    public boolean login(List<LoginInfo> loginInfos) {
        if (loginInfos == null || loginInfos.size() == 0) {
            return false;
        }

        //拿第一个登陆信息作为telnet或者SSH，剩余的只能调用命令行的方式
        LoginInfo loginInfo = loginInfos.get(0);
        CmdClientTypeEnum connectType;
        boolean status = login(loginInfo);

        //登陆失败就直接返回
        if (!status) {
            return false;
        }

        //对于剩下的登陆跳转使用命令行方式进行
        for (int i = 1; i < loginInfos.size(); i++) {
            loginInfo = loginInfos.get(i);
            setCharset(loginInfo.getCharset());
            connectType = loginInfo.getConnectType();
            if (connectType == CmdClientTypeEnum.Telnet) {
                status = telnetCmdHandle(loginInfo);

            } else if (connectType == CmdClientTypeEnum.SSH) {
                status = sshCmdHandle(loginInfo);

            } else {
                status = false;
            }

            //执行跳过命令
            if (status) {
                executeSkipCmds();
            } else {
                //不成功时直接跳出循环
                break;
            }
        }

        return status;
    }

    /**
     * @param loginInfo
     * @return
     */
    @Override
    public boolean login(LoginInfo loginInfo) {
        String ip = loginInfo.getIp();
        int port = loginInfo.getPort();
        String username = loginInfo.getUsername();
        String charset = loginInfo.getCharset();
        String password = loginInfo.getPassword();
        setCharset(charset);
        boolean status = login(ip, port, username, password, loginInfo.isEnable(), loginInfo.getEnablePwd());
        return status;
    }

    @Override
    public boolean login(String ip, int port, String username, String password) {
        return login(ip, port, username, password, false, null);
    }

    /**
     * SSH命令行登录处理
     *
     * @param loginInfo
     * @return
     */
    private boolean sshCmdHandle(LoginInfo loginInfo) {
        String ip = loginInfo.getIp();
        int port = loginInfo.getPort();
        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();
        boolean status;

        //判断是否有端口就用什么模式的命令
        if (port >= 1) {
            status = execCommand(CmdClient.sshLoginCmdLine.replace("$ip$", ip).replace("$port$", port + "").replace("$username$", username));
        } else {
            status = execCommand(CmdClient.sshLoginCmdLineDefault.replace("$ip$", ip).replace("$username$", username));
        }
        if (status) {
            readData(null, null, true, 0);
            status = execCommand(password);
        }
        if (status) {
            readData(null, null, true, 0);
        }
        return status;
    }

    /**
     * telnet命令行登录处理
     *
     * @param loginInfo
     * @return
     */
    private boolean telnetCmdHandle(LoginInfo loginInfo) {
        String ip = loginInfo.getIp();
        int port = loginInfo.getPort();
        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();
        boolean status;


        //判断是否有端口就用什么模式的命令
        if (port >= 1) {
            status = execCommand(CmdClient.telnetLoginCmdLine.replace("$ip$", ip).replace("$port$", port + ""));
        } else {
            status = execCommand(CmdClient.telnetLoginCmdLineDefault.replace("$ip$", ip));
        }

        if (status) {
            readData(null, null, true, 0);
            status = execCommand(username);
        }
        if (status) {
            readData(null, null, true, 0);
            status = execCommand(password);
        }
        if (status) {
            readData(null, null, true, 0);
        }
        return status;
    }

    /**
     * 当多次登录时，要执行的跳过的命令，默认跟正式登录时一样
     */
    private void executeSkipCmds() {
        executeSkipStepCmds();
    }

    /**
     * 在这里只关闭这个抽象类管理的流对象
     * 所以子类关闭的时候需要再调用此方法来进行关闭
     */
    @Override
    public void logout() {
        loginStatus = false;
        //关闭子类管理的对象
        releaseSubResources();

        if (dataWriter != null) {
            try {
                dataWriter.close();
            } catch (Exception e) {
            }
            dataWriter = null;
        }
        if (dataReader != null) {
            try {
                dataReader.close();
            } catch (Exception e) {
                LOG.error("关闭流失败", e);
            }
            dataReader = null;
        }
//		LOG.info("[{}] 关闭连接 [{}:{}] 的输入输出流完成", Thread.currentThread().getName(), remoteIp, remotePort);

    }

    /**
     * 从返回数据中提取出真正返回的内容， 去掉返回头尾 （头：发送的命令单独一行） （尾：返回内容会多出一行命令输入行，例如[用户名@计算机名 ~]$）
     *
     * @param returnData      原始内容
     * @param command         执行的命令行
     * @param cutEndLineCount 截取掉后面的多少行
     * @return 截取到的真正报文
     */
    protected String extractReturnContent(String returnData, Object command, int cutEndLineCount) {

        if (StringUtils.isNoneEmpty(returnData)) {

            int tmpIndex = -1;
            int startIndex = -1;
            int endIndex = -1;

            String cmd = command == null ? null : command.toString();
            //获取起始索引点
            if (StringUtils.isNoneEmpty(cmd)) {
                tmpIndex = returnData.indexOf(cmd);
                if (tmpIndex != -1) {
                    startIndex = tmpIndex + cmd.length();
                }
            }
            //去掉全面部分
            if (startIndex != -1) {
                returnData = returnData.substring(startIndex, returnData.length());
            }

            while (cutEndLineCount-- >= 1) {
                //获取结束索引点
                endIndex = returnData.lastIndexOf("\n");
                if (endIndex != -1) {
                    returnData = returnData.substring(0, endIndex);
                }
            }
//			LOG.debug("[{}:{}] 截取--more--类型行之前的数据为：\r\n{}", remoteIp, remotePort, returnData);

            //去掉more控制字符， ---- More ----[42D                                          [42D  Administrator PW     : no
            returnData = returnData.replaceAll(hasMoreContralLineRegex, "").trim();

            //去掉more行字符,该行需要在前一行之后执行，防止more行和more控制字符在同一行
            returnData = returnData.replaceAll(hasMoreLineRegex, "").trim();
        }
        return returnData;
    }

    @Override
    public String readData() {
        return readData(readTimeout);
    }

    @Override
    public String readData(long readTimeout) {
        return readData(null, readTimeout);
    }

    @Override
    public String readData(String endStrRegex) {
        return readData(endStrRegex, readTimeout);
    }

    @Override
    public String readData(String endStrRegex, long readTimeout) {

        return readData(endStrRegex, null, isLogSrcData, 0, readTimeout);
    }

    @Override
    public String readData(String endStrRegex, String notEndStrRegex, boolean isLogSrcContent, int cutEndLineCount) {
        return readData(endStrRegex, notEndStrRegex, isLogSrcContent, cutEndLineCount, readTimeout);
    }

    @Override
    public String readData(String endStrRegex, String notEndStrRegex, boolean isLogSrcContent, int cutEndLineCount, long readTimeout) {
        String str = readRawAutoAnswerNo(endStrRegex, notEndStrRegex, isLogSrcContent, readTimeout);
        if (StringUtils.isNoneEmpty(str)) {
            str = extractReturnContent(str, currentCmd, cutEndLineCount);
        }
        return str;
    }

    /**
     * 执行跳过的步骤的命令
     *
     * @return 是否全部成功
     */
    protected boolean executeSkipStepCmds() {
        boolean execStatus = true;
        if (skipStepCmds != null) {
            String returnData = null;
            for (String[] cmd : skipStepCmds) {

                LOG.debug("{} 获取无关功能的目标内容 [{}]", getLoginSocketInfo(), cmd[0] == null ? "" : cmd[0]);
                returnData = readRawAutoAnswerNo(cmd[0], null, true, connTimeout);

                if (checkLoginProgressReturnDataIsSucc(returnData, true)) {
                    LOG.debug("{} 输入跳过无关功能的命令 [{}]", getLoginSocketInfo(), cmd[1]);
                    execStatus &= execCommand(cmd[1]);
                    if (!execStatus) {
                        break;
                    }

                } else {
                    LOG.error("登陆设备失败,无法获取到服务器响应, {}", getLoginInfo());
                    loginStatus = false;
                    execStatus = false;
                    break;

                }
            }
        }
        return execStatus;
    }

    @Override
    public boolean login(String ip, String username, String password) {
        return login(ip, remotePort, username, password);
    }

    @Override
    public void close() {
        boolean isLogPrint = loginStatus;
        loginStatus = false;
//		if(loginStatus){
        logout();
        if (isLogPrint) {
            LOG.info("[线程:{}] 关闭与 {} 的连接完成", Thread.currentThread().getName(), getLoginInfo());
        }
//		}
    }

    /**
     * 关闭剩余其它的对象，子类需要实现，以用于关闭子类管理的对象
     */
    protected abstract void releaseSubResources();

    @Override
    public void setCharset(String charset) {
        if (StringUtils.isNoneEmpty(charset)) {
            this.dateCharset = charset;
        }
    }

    @Override
    public boolean isLogin() {
        return this.loginStatus;
    }

    /**
     * 添加执行细节
     *
     * @param str
     */
    protected void addExecDetail(String str) {
        if (StringUtils.isEmpty(str)) {
            return;
        }
        execDetails.add(str.trim());
    }

    protected void addExecCmdToExecDetail(String cmd) {
        if (StringUtils.isEmpty(cmd)) {
            return;
        }
        int size = execDetails.size();
        if (size >= 1) {
            execDetails.set(size - 1, execDetails.get(size - 1) + cmd);
        } else {
            execDetails.add(cmd.trim());
        }
    }

    @Override
    public List<String> getExecDetail() {
        return execDetails;
    }

    @Override
    public void cleanExecDetail() {
        execDetails.clear();
    }

    @Override
    public String getIp() {
        return remoteIp;
    }

    @Override
    public int getPort() {
        return remotePort;
    }

    @Override
    public String getUsername() {
        return remoteUsername;
    }

    @Override
    public String getPassword() {
        return remotePassword;
    }

    /**
     * 校验是否登陆成功
     *
     * @param info
     * @return
     */
    public static boolean checkHasFailInfo(String info) {
        return checkHasFailInfo(info, Thread.currentThread().getName());
    }

    /**
     * 校验是否有失败信息
     *
     * @param info
     * @param producer
     * @return
     */
    public static boolean checkHasFailInfo(String info, String producer) {
        return CmdClientConfig.getInstance().checkHasFailInfo(info, producer);
    }

    @Override
    public String read(ReadParam param) {
        String str = null;
        if (param == null) {
            str = readRaw(null, null, false, true, false, readTimeout);

        } else {
            long timeout = param.getReadTimeout() >= 1 ? param.getReadTimeout() : readTimeout;
            str = readRaw(param.getEndStrRegex(), param.getNotEndStrRegex(), param.isLogSrcContent(), param.isAutoAnswer(), param.isAutoAnswerYes(), timeout);
        }
        //20180611 XXX 抽取算法存在问题
//		if(StringUtils.isNoneEmpty(str)){
//			str =  extractReturnContent(str, currentCmd, param.getCutEndLineCount());
//		}
        return str;
    }

    @Override
    public String execCmdAndReceive(Object cmd, ReadParam param) {
        if (execCommand(cmd)) {
            return read(param);
        } else {
            return null;
        }

    }

    @Override
    public void setIp(String ip) {
        this.remoteIp = ip;
    }

    @Override
    public void setPort(int port) {
        this.remotePort = port;
    }

    @Override
    public void setUsername(String username) {
        this.remoteUsername = username;
    }

    @Override
    public void setPassword(String password) {
        this.remotePassword = password;
    }

    /**
     * 校验登陆过程中的返回信息中有没有发生异常信息
     *
     * @param returnData
     * @param isCheckRedo 是否校验重试登录，比如瑞斯康达用户名密码错误时，没有提示错误，直接让你重新输入用户名
     * @return
     */
    protected boolean checkLoginProgressReturnDataIsSucc(String returnData, boolean isCheckRedo) {
        if (returnData == null || "".equals(returnData) || checkHasFailInfo(returnData, getLoginInfo()) || (!checkIsEnd(null, returnData) && returnData.matches("[\\s\\S]*?(?i)verification[\\s\\S]*?")) || (isCheckRedo && checkHasLoginRedo(returnData, getLoginInfo())))  {
            LOG.error("登陆设备失败,无法获取到服务器正确响应, {}", getLoginInfo());
            loginStatus = false;

        }
        return loginStatus;
    }

    /**
     * 校验是否直接让登录尝试
     *
     * @param returnData
     * @param loginInfo
     * @return
     */
    protected boolean checkHasLoginRedo(String returnData, String loginInfo) {
        if (returnData.matches("[\\s\\S]*(?i)(login:|username|user) *")) {
            return true;
        }
        return false;
    }

    @Override
    public String getLoginInfo() {
        return StringUtils.join("[", cmdClientType.getName(), "连接-", remoteIp, ":", remotePort, " 账密:", remoteUsername, "/", remotePassword, " 是否enable:", enable, " enable密码:", enablePwd, " 状态:", loginStatus ? "已连接" : "未连接", "]");
    }

    @Override
    public String getLoginSocketInfo() {
        return StringUtils.join("[", cmdClientType.getName(), "连接-", remoteIp, ":", remotePort, "]");
    }

    @Override
    public CmdClientTypeEnum getCmdClientType() {
        return cmdClientType;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void setEnablePwd(String enablePwd) {
        this.enablePwd = enablePwd;
    }


    /**
     * 校验登录状态
     *
     * @param loginResult
     */
    protected void checkLoginStatus(String loginResult) {
        if (loginStatus) {
            if (checkHasFailInfo(loginResult, getLoginInfo())) {

                LOG.error("登陆设备{}发生账号密码错误", getLoginInfo());
                loginStatus = false;
            } else {
                loginStatus = true;
            }
        }
    }

    @Override
    public boolean enableHandle(String enablePwd) {
        enableHandle(true, enablePwd);
        return loginStatus;
    }

    /**
     * enable的处理
     * 输入enable命令
     * 如果提示需要输入enable密码，同时enable密码不为空的时候就输入
     * 检查是否登录失败
     *
     * @param enable
     * @param enablePwd
     */
    protected void enableHandle(boolean enable, String enablePwd) {
        if (!loginStatus) {
            LOG.warn("{} 登录不成功，不进行enable模块处理", getLoginInfo());
            return;
        }
        String loginResult = null;
        String returnData;
        //enable模式处理
        if (enable || StringUtils.isNoneEmpty(enablePwd)) {
            LOG.info("{} 进行enable模式处理", getLoginInfo());
            this.enable = enable;
            //输入enable命令
            returnData = execCmdAndReceiveData(enableCmd, null, null, connTimeout);
            if (StringUtils.isNoneEmpty(enablePwd)) {

                //如果提示需要输入enable密码，同时enable密码不为空的时候就输入
                if (returnData != null && returnData.matches(pwdPromptRegex)) {
                    this.enablePwd = enablePwd;
                    loginResult = execCmdAndReceiveData(enablePwd, null, null, connTimeout);

                    //检查是否登录失败
                    if (returnData != null && loginResult != null) {
                        checkLoginStatus(returnData + loginResult);
                    } else if (returnData != null) {
                        checkLoginStatus(returnData);
                    } else if (loginResult != null) {
                        checkLoginStatus(loginResult);
                    }

                } else {
                    LOG.info("{} 没有提示需要输入enable密码模式", getLoginSocketInfo());
                }

            } else {

                if (checkEnableFail) {
                    //检查是否enable失败
                    if (returnData != null && loginResult != null) {
                        checkLoginStatus(returnData + loginResult);
                    } else if (returnData != null) {
                        checkLoginStatus(returnData);
                    } else if (loginResult != null) {
                        checkLoginStatus(loginResult);
                    }
                }

            }

        } else {
            this.enable = false;
        }
    }

    @Override
    public boolean modifyCmdOutputAllDataImmediate(String manufacturer) {
        return modifyCmdOutputAllDataImmediate(manufacturer, model);
    }

    @Override
    public boolean modifyCmdOutputAllDataImmediate(String manufacturer, String model) {
        DevPageBreakCmd devPageBreakCmd = CmdClientConfig.getInstance().getDevPageBreakCmd(manufacturer, model);
        if (devPageBreakCmd != null) {
            for (String pageBreakCmd : devPageBreakCmd.getPageBreakCmds()) {
                if (!execCommand(pageBreakCmd)) {
                    return false;
                }
                return hasError(readData(10000));
            }

        } else {
            LOG.warn("{} 暂不支持该厂家进入非分页输出模式", getLoginInfo());
        }
        return false;
    }

    @Override
    public void setCmdClientType(CmdClientTypeEnum cmdClientType) {
        this.cmdClientType = cmdClientType;
    }

    @Override
    public String getEnablePwd() {
        return enablePwd;
    }

    @Override
    public boolean isLoginError() {
        return loginError;
    }

    @Override
    public boolean lastCmdReadTimeout() {
        return lastCmdReadTimeout;
    }

    @Override
    public int getLoginCount() {
        return loginCount;
    }

    @Override
    public void incrementLoginCount() {
        loginCount++;
    }

    @Override
    public void closeWithQuitCmd() {
        execLogoutCmd(quit);
        close();
    }

    /**
     * 执行退出登录命令
     *
     * @param closeCmd
     */
    private void execLogoutCmd(String closeCmd) {
        if (isLogin()) {
            int count = 1;
            String result = null;
            while (count <= loginCount) {
                if (count == loginCount) {
                    execCommand(closeCmd);
                    break;
                } else {
                    result = execCmdAndReceiveData(closeCmd, null, 2000);
                    if (StringUtils.isEmpty(result)) {
                        break;
                    }
                }

                count++;

            }
        }
    }

    @Override
    public void closeWithExitCmd() {
        execLogoutCmd(exit);
        close();
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean login(String ip, int port, String username, String password, String manufacturer, String model) {
        boolean enable = false;
        String enablePwd = null;
        EnableModel enableModel = CmdClientFactory.getEnableModel(manufacturer, model);
        if (enableModel != null) {
            enable = enableModel.isEnable();
            enablePwd = enableModel.getEnablePwd();
        }

        loginStatus = login(ip, port, username, password, enable, enablePwd);
        if (loginStatus) {
            modifyCmdOutputAllDataImmediate(manufacturer, model);
        }
        return loginStatus;
    }

    @Override
    public void closeInputToDetail() {
        this.inputToDetail = false;
    }

    @Override
    public void openInputToDetail() {
        this.inputToDetail = true;
    }

    public boolean isInputToDetail() {
        return inputToDetail;
    }

    /**
     * 端口映射
     *
     * @author <B><a href="mailto:landingdong@gdcattsoft.com"> 蓝鼎栋 </a></B>
     */
    protected void portIpMap() {
        InetSocketAddress socketAddr = IpPortMaper.getInstance().getConvertedIpPort(remoteIp, remotePort);
        if (socketAddr != null) {
            LOG.info("端口映射配置:{}:{}-->{}:{}", remoteIp, remotePort, socketAddr.getAddress().getHostAddress(), socketAddr.getPort());
            remoteIp = socketAddr.getAddress().getHostAddress();
            remotePort = socketAddr.getPort();
        }
    }

    @Override
    public boolean isNetconf() {
        return false;
    }

    @Override
    public boolean hasError(String info) {
        return checkHasFailInfo(info);
    }

    @Override
    public boolean hasOk(String info) {
        return true;
    }

    @Override
    public boolean hasError() {
        return hasError(lastReplyInfo);
    }

    @Override
    public boolean hasOk() {
        return hasOk(lastReplyInfo);
    }

    @Override
    public String getLastReplyInfo() {
        return lastReplyInfo;
    }

    @Override
    public String getLastSendInfo() {
        if (currentCmd != null) {
            return currentCmd.toString();
        } else {
            return null;
        }
    }

    @Override
    public void checkEnableFail(boolean isCheck) {
        checkEnableFail = isCheck;
    }
}
