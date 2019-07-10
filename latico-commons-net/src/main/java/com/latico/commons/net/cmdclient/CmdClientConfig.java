package com.latico.commons.net.cmdclient;


import com.latico.commons.common.config.AbstractConfig;
import com.latico.commons.common.config.FieldConfigNameAnnotation;
import com.latico.commons.common.util.collections.CollectionUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.xml.Dom4jUtils;
import com.latico.commons.net.cmdclient.bean.DevDefaultUserPwd;
import com.latico.commons.net.cmdclient.bean.DevPageBreakCmd;
import com.latico.commons.net.cmdclient.bean.EnableModel;
import com.latico.commons.net.cmdclient.bean.ForceQuestionWithAnswer;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 * 命令行客户端工具配置类
 * 1、先加载资源目录的cmdClient.xml；
 * 2、再加载资源目录的cmdClient.xml，这个文件默认不存在，如果使用者的项目想要用到的话，就在项目的资源目录加上，程序加载的时候会自动覆盖cmdClient-default.xml的配置；
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2017年8月29日</B>
 * @since <B>JDK1.6</B>
 */
public class CmdClientConfig extends AbstractConfig {


    private static final Logger LOG = LoggerFactory.getLogger(CmdClientConfig.class);
    /**
     * instance 单例实例
     */
    private static volatile CmdClientConfig instance = null;

    /**
     * 默认配置文件
     */
    private final String cmdClientDefaultConfigFile = RESOURCES_CONFIG_FILE_ROOT_DIR + "cmdClient.xml";
    /**
     * 配置文件，会覆盖默认配置文件，该文件是实际项目需要使用的时候添加
     */
    private final String cmdClientConfigFile = PathUtils.adapterFilePathSupportWebContainer(CONFIG_FILE_ROOT_DIR + "cmdClient.xml");

    /**
     * cmdLineEndRegex 行结束正则
     */
    @FieldConfigNameAnnotation("cmdLineEndRegex")
    private String cmdLineEndRegex = "[\\s\\S]*?.+?(\\$ *|# *|> *|: *|\\] *)$";

    /**
     * cmdLastLineEndRegex 命令行最后一行结束正则
     */
    @FieldConfigNameAnnotation("cmdLastLineEndRegex")
    private String cmdLastLineEndRegex = "^\\S.+?(\\$ *|# *|> *|: *|\\] *)$";

    /**
     * netconfLineEndRegex
     */
    private String netconfLineEndRegex = "[\\s\\S]*?\\]\\]>\\]\\]>$";

    /**
     * cmdFailInfoRegex 命令行失败信息正则
     */
    private String cmdFailInfoRegex = "[\\s\\S]*?(?i)(incorrect|login\\S* +fail|error:|%Error |incomplete|denied| bad |reject|deny|unknow|closed|time out|unable|'\\^'| \\^|\\^ |% *Invalid|Unknow[\\s\\S]*command|Unrecognized +command)[\\s\\S]*?";

    /**
     * pattern
     */
    private Pattern cmdFailInfoPattern = null;

    /**
     * usernamePromptRegex 用户名正则
     */
    private String usernamePromptRegex = "(?i)[\\s\\S]*?username[\\s\\S]*?";

    /**
     * pwdPromptRegex 密码提示符正则
     */
    private String pwdPromptRegex = "(?i)[\\s\\S]*?Password[\\s\\S]*?";

    /**
     * promptUnChangeCmdRegex 结束符不会改变的命令正则
     */
    private String promptUnChangeCmdRegex = "(?i) *?(dis|show).*";

    /**
     * unCheckErrorCmdRegexWhenExecProcess 执行过程中，不校验是否执行失败的命令
     */
    private String unCheckErrorCmdRegexWhenExecProcess = "(?i) *?(dis|show).*";

    /**
     * cmdExecStopWhenCheckError 命令行执行报错时，是否进行终止
     */
    private boolean cmdExecStopWhenCheckError = false;

    /**
     * yesOrNoQuestionRegex  Yes/No问题的正则匹配
     */
    private String yesOrNoQuestionRegex = "[\\s\\S]*?((?i)[\\(\\[] *(y\\S*) *[/|] *(n[^ \\?|]*)[ \\?]*[\\)\\]][ \\?:]*)$";

    /**
     * quitCmdRegex 退出的命令正则集合
     */
    private String quitCmdRegex = "\\s*?(?i)(quit|exit|logout)\\s*?";

    /**
     * cmdConnTimeout 命令行连接登陆的超时时间，默认15秒
     */
    @FieldConfigNameAnnotation("cmdConnTimeout")
    private int cmdConnTimeout = 15000;

    /**
     * cmdAllowCheckEndTime 允许校验命令行结束的时间
     */
    private int cmdAllowCheckLineEndTime = 50;

    /**
     * specialCmdCheckLineEndRegex 指定命令格式的允许校验结束的
     */
    private String specialCmdCheckLineEndRegex = " *?(dis\\S*? +?cur|sh\\S*? +?run).*";

    /**
     * cmdConnFailRegex 命令行连接失败正则
     */
    private String cmdConnFailRegex = "(?i)[\\s\\S]*?(connection\\s+error\\.|Trying \\d+\\.\\d+\\.\\d+\\.\\d+\\s*\\.\\.\\.)[\\s\\S]*?";

    /**
     * specialCmdCheckLineEndTime 指定命令格式的允许校验结束的时间，单位毫秒
     */
    private int specialCmdCheckLineEndTime = 100;

    /**
     * cmdSessionTimeout 命令行会话无操作超时默认10分钟
     */
    private int cmdSessionTimeout = 600000;


    /**
     * devDefaultUserPwds 设备默认用户名密码,第一维是厂家，第二维是型号
     */
    private Map<String, Map<String, DevDefaultUserPwd>> devDefaultUserPwds = new HashMap<String, Map<String, DevDefaultUserPwd>>();

    /**
     * 设备非分页输出命令
     */
    private Map<String, Map<String, DevPageBreakCmd>> devPageBreakCmds = new HashMap<String, Map<String, DevPageBreakCmd>>();

    private Map<String, ForceQuestionWithAnswer> forceCheckHasYesNoQuestionWithAnswerPatterns = new HashMap<String, ForceQuestionWithAnswer>();

    private String loginRedoTagRegex;

    /**
     * enable的型号
     */
    private Map<String, Map<String, EnableModel>> enableModels = new HashMap<>();


    /**
     * 构造方法
     */
    private CmdClientConfig() {
        initOrRefreshConfig();
    }

    /**
     * 获取单例，同步双重校验的好处在于，兼顾了效率、支持延迟加载、可以再创建对象后，
     * 调用方法进行初始化，而不需要在构造方法初始化，因为有些参数的加载，需要对象创建成功后进行
     *
     * @return
     */
    public static CmdClientConfig getInstance() {
        if (instance == null) {
            synchronized (CmdClientConfig.class) {
                if (instance == null) {
                    instance = new CmdClientConfig();
                }
            }
        }
        return instance;
    }

    @Override
    protected void initOtherConfig() {

    }

    /**
     * 获取资源配置文件路径
     *
     * @return 资源配置文件路径
     */
    @Override
    protected String getResourcesConfigFilePath() {
        return cmdClientDefaultConfigFile;
    }

    /**
     * @return 配置文件路径
     */
    @Override
    protected String getConfigFilePath() {
        return cmdClientConfigFile;
    }
    /**
     * 初始化配置
     *
     * @param xml
     * @throws DocumentException
     */
    @Override
    protected boolean initConfig(String xml) throws Exception {
        Element rootElement = Dom4jUtils.getRootElement(xml);
        initCommonElement(rootElement.element("common"));
        initDevDefaultUserPwdsElement(rootElement.element("devDefaultUserPwds"));
        initDevPageBreakCmdsElement(rootElement.element("devPageBreakCmds"));
        initEnableModelsElement(rootElement.element("enableModels"));
        initForceCheckHasYesNoQuestionWithAnswerRegex(rootElement.element("forceCheckHasYesNoQuestionWithAnswerRegex"));
        return true;
    }

    private void initEnableModelsElement(Element ele) throws Exception {
        if (ele == null) {
            return;
        }
        List<Element> elements = ele.elements();
        if (elements == null) {
            return;
        }
        final String defaultName = "all";
        for (Element element : elements) {
            Map<String, String> allAttributeNameValueMap = Dom4jUtils.getAllAttributeNameValueMap(element);
            EnableModel enableModel = new EnableModel();
            writeFieldValue(enableModel, allAttributeNameValueMap);

            if (StringUtils.isBlank(enableModel.getManufacturer()) && StringUtils.isBlank(enableModel.getModel())) {
                continue;
            }

            enableModel.setManufacturer(unifyManufacturerHandle(enableModel.getManufacturer()));

            if (StringUtils.isBlank(enableModel.getModel())) {
                enableModel.setModel(defaultName);
            }

            Map<String, EnableModel> modelMap = enableModels.get(enableModel.getManufacturer());
            if (modelMap == null) {
                modelMap = new HashMap<>();
                enableModels.put(enableModel.getManufacturer(), modelMap);
            }

            modelMap.put(enableModel.getModel(), enableModel);

        }
    }

    /**
     * 初始化common节点，common节点下面的子节点的名称必须跟字段名称一致，然后程序会自动查找并注入
     *
     * @param commonEle
     * @throws IllegalAccessException
     * @throws ParseException
     */
    private void initCommonElement(Element commonEle) {
        try {
            if (commonEle == null) {
                return;
            }
            Map<String, String> commonChildMap = Dom4jUtils.getChildsNameValueMap(commonEle);
            writeFieldValueToCurrentInstance(commonChildMap);

            //提高效率生成失败匹配正则
            cmdFailInfoPattern = Pattern.compile(cmdFailInfoRegex);
        } catch (Exception e) {
            LOG.error("加载common节点配置存在异常", e);
        }
    }



    /**
     * 初始化强制回答问题
     *
     * @param element
     */
    private void initForceCheckHasYesNoQuestionWithAnswerRegex(Element element) {
        if (element == null) {
            return;
        }
        List<Element> questionWithAnswerRegexEles = element.elements("questionWithAnswerRegex");
        if (questionWithAnswerRegexEles == null) {
            return;
        }
        for (Element questionWithAnswerRegexEle : questionWithAnswerRegexEles) {
            String regex = questionWithAnswerRegexEle.getText();
            ForceQuestionWithAnswer forceQuestionWithAnswer = new ForceQuestionWithAnswer();

            forceQuestionWithAnswer.setRegex(regex);
            forceQuestionWithAnswer.setPattern(Pattern.compile(regex));
            forceQuestionWithAnswer.setRegexGroupIndex(Dom4jUtils.getAttributeInt(questionWithAnswerRegexEle, "regexGroupIndex", 1));
            forceCheckHasYesNoQuestionWithAnswerPatterns.put(regex, forceQuestionWithAnswer);
        }
    }

    /**
     * 初始化设备默认用户名密码配置
     *
     * @param devDefaultUserPwdsEle
     */
    private void initDevDefaultUserPwdsElement(Element devDefaultUserPwdsEle) throws Exception {
        if (devDefaultUserPwdsEle == null) {
            return;
        }
        List<Element> devDefaultUserPwdEles = devDefaultUserPwdsEle.elements("devDefaultUserPwd");
        if (devDefaultUserPwdEles == null) {
            return;
        }
        final String defaultName = "all";
        for (Element devDefaultUserPwdEle : devDefaultUserPwdEles) {
            String userPwdStr = devDefaultUserPwdEle.getTextTrim();
            String[] arr = userPwdStr.split("/");
            if (arr.length != 2) {
                continue;
            }
            DevDefaultUserPwd devDefaultUserPwd = new DevDefaultUserPwd();
            devDefaultUserPwd.setUsername(arr[0]);
            devDefaultUserPwd.setPassword(arr[1]);

            Map<String, String> allAttributeNameValueMap = Dom4jUtils.getAllAttributeNameValueMap(devDefaultUserPwdEle);
            writeFieldValue(devDefaultUserPwd, allAttributeNameValueMap);

            devDefaultUserPwd.setManufacturer(unifyManufacturerHandle(devDefaultUserPwd.getManufacturer()));

            Map<String, DevDefaultUserPwd> modelDevInfo = devDefaultUserPwds.get(devDefaultUserPwd.getManufacturer());
            if (modelDevInfo == null) {
                modelDevInfo = new HashMap<>();
                devDefaultUserPwds.put(devDefaultUserPwd.getManufacturer(), modelDevInfo);
            }
            String model = devDefaultUserPwd.getModel();
            if (StringUtils.isEmpty(model)) {
                model = defaultName;
            }
            modelDevInfo.put(model, devDefaultUserPwd);
        }
    }

    /**
     * 初始化设备默认用户名密码配置
     *
     * @param devDefaultUserPwdsEle
     */
    private void initDevPageBreakCmdsElement(Element devDefaultUserPwdsEle) throws Exception {
        if (devDefaultUserPwdsEle == null) {
            return;
        }
        List<Element> devPageBreakCmdEles = devDefaultUserPwdsEle.elements("devPageBreakCmd");
        if (devPageBreakCmdEles == null) {
            return;
        }
        final String defaultName = "all";
        for (Element devDefaultUserPwdEle : devPageBreakCmdEles) {
            DevPageBreakCmd devPageBreakCmd = new DevPageBreakCmd();
            Map<String, String> allAttributeNameValueMap = Dom4jUtils.getAllAttributeNameValueMap(devDefaultUserPwdEle);
            writeFieldValue(devPageBreakCmd, allAttributeNameValueMap);

            if (devPageBreakCmd.getPageBreakCmd().contains(";")) {

                String[] cmds = devPageBreakCmd.getPageBreakCmd().split("\\s*;\\s*");
                List<String> list = CollectionUtils.toList(cmds);
                devPageBreakCmd.setPageBreakCmds(list);
            } else {
                List<String> cmds = new ArrayList<>();
                cmds.add(devPageBreakCmd.getPageBreakCmd());
                devPageBreakCmd.setPageBreakCmds(cmds);
            }

            devPageBreakCmd.setManufacturer(unifyManufacturerHandle(devPageBreakCmd.getManufacturer()));
            Map<String, DevPageBreakCmd> modelDevInfo = devPageBreakCmds.get(devPageBreakCmd.getManufacturer());
            if (modelDevInfo == null) {
                modelDevInfo = new HashMap<>();
                devPageBreakCmds.put(devPageBreakCmd.getManufacturer(), modelDevInfo);
            }
            String model = devPageBreakCmd.getModel();
            if (StringUtils.isEmpty(model)) {
                model = defaultName;
            }
            modelDevInfo.put(model, devPageBreakCmd);
        }
    }

    public String getCmdClientDefaultConfigFile() {
        return cmdClientDefaultConfigFile;
    }

    public String getCmdClientConfigFile() {
        return cmdClientConfigFile;
    }

    public String getCmdLineEndRegex() {
        return cmdLineEndRegex;
    }

    public String getCmdLastLineEndRegex() {
        return cmdLastLineEndRegex;
    }

    public String getNetconfLineEndRegex() {
        return netconfLineEndRegex;
    }

    public String getCmdFailInfoRegex() {
        return cmdFailInfoRegex;
    }

    public String getUsernamePromptRegex() {
        return usernamePromptRegex;
    }

    public String getPwdPromptRegex() {
        return pwdPromptRegex;
    }

    public String getPromptUnChangeCmdRegex() {
        return promptUnChangeCmdRegex;
    }

    public String getUnCheckErrorCmdRegexWhenExecProcess() {
        return unCheckErrorCmdRegexWhenExecProcess;
    }

    public boolean isCmdExecStopWhenCheckError() {
        return cmdExecStopWhenCheckError;
    }

    public String getYesOrNoQuestionRegex() {
        return yesOrNoQuestionRegex;
    }

    public String getQuitCmdRegex() {
        return quitCmdRegex;
    }

    public int getCmdConnTimeout() {
        return cmdConnTimeout;
    }

    public int getCmdAllowCheckLineEndTime() {
        return cmdAllowCheckLineEndTime;
    }

    public String getSpecialCmdCheckLineEndRegex() {
        return specialCmdCheckLineEndRegex;
    }

    public String getCmdConnFailRegex() {
        return cmdConnFailRegex;
    }

    public int getSpecialCmdCheckLineEndTime() {
        return specialCmdCheckLineEndTime;
    }

    public int getCmdSessionTimeout() {
        return cmdSessionTimeout;
    }

    public Map<String, Map<String, DevDefaultUserPwd>> getDevDefaultUserPwds() {
        return devDefaultUserPwds;
    }

    public Collection<ForceQuestionWithAnswer> getForceCheckHasYesNoQuestionWithAnswerPatterns() {
        return forceCheckHasYesNoQuestionWithAnswerPatterns.values();
    }

    /**
     * 获取设备默认的用户名密码
     *
     * @param manufacturer
     * @param model
     * @return
     */
    public DevDefaultUserPwd getDevDefaultUserPwd(String manufacturer, String model) {

        manufacturer = unifyManufacturerHandle(manufacturer);
        Map<String, DevDefaultUserPwd> manuMap = devDefaultUserPwds.get(manufacturer);
        if (manuMap == null) {
            manuMap = devDefaultUserPwds.get("all");
        }
        if (manuMap == null) {
            return null;
        }
        if (StringUtils.isEmpty(model)) {
            model = "all";
            return manuMap.get(model);
        } else {
            DevDefaultUserPwd pwd = manuMap.get(model);
            if (pwd == null) {
                model = "all";
                pwd = manuMap.get(model);
            }
            return pwd;
        }
    }

    /**
     * 统一厂家字符串
     * @param manufacturer
     * @return
     */
    private String unifyManufacturerHandle(String manufacturer) {
        if (manufacturer == null) {
            manufacturer = "all";
        } else {
            manufacturer = manufacturer.toLowerCase();
        }
        return manufacturer;
    }

    /**
     * @param manufacturer
     * @param model
     * @return
     */
    public DevPageBreakCmd getDevPageBreakCmd(String manufacturer, String model) {
        manufacturer = unifyManufacturerHandle(manufacturer);

        Map<String, DevPageBreakCmd> manuMap = devPageBreakCmds.get(manufacturer);
        if (manuMap == null) {
            manuMap = devPageBreakCmds.get("all");
        }
        if (manuMap == null) {
            return null;
        }
        if (StringUtils.isEmpty(model)) {
            model = "all";
            return manuMap.get(model);
        } else {
            DevPageBreakCmd pwd = manuMap.get(model);
            if (pwd == null) {
                model = "all";
                pwd = manuMap.get(model);
            }
            return pwd;
        }
    }

    /**
     * 校验是否有失败的标志信息
     *
     * @param info
     * @param producer
     * @return
     */
    public boolean checkHasFailInfo(String info, String producer) {
        if (info == null) {
            return false;
        }
        Matcher matcher = cmdFailInfoPattern.matcher(info);
        if (matcher.find()) {
            String failInof = matcher.group(1);
            LOG.error("{} 有失败的标志信息:[{}]", producer, failInof);
            return true;
        } else {
            return false;
        }

    }

    public String getLoginRedoTagRegex() {
        return loginRedoTagRegex;
    }

    /**
     * 拿enable配置
     * @param manufacturer
     * @param model
     * @return
     */
    public EnableModel getEnableModel(String manufacturer, String model) {
        final String defaultName = "all";
        manufacturer = unifyManufacturerHandle(manufacturer);
        if (StringUtils.isBlank(model)) {
            model = defaultName;
        }

        Map<String, EnableModel> enableModelMap = enableModels.get(manufacturer);
        if (enableModelMap == null) {
            return null;
        }
        EnableModel enableModel = enableModelMap.get(model);
        if (enableModel != null) {
            return enableModel;
        }
        if (!defaultName.equals(model)) {
            return enableModelMap.get(defaultName);
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CmdClientConfig{");
        sb.append("cmdClientDefaultConfigFile='").append(cmdClientDefaultConfigFile).append('\'');
        sb.append(", \ncmdClientConfigFile='").append(cmdClientConfigFile).append('\'');
        sb.append(", \ncmdLineEndRegex='").append(cmdLineEndRegex).append('\'');
        sb.append(", \ncmdLastLineEndRegex='").append(cmdLastLineEndRegex).append('\'');
        sb.append(", \nnetconfLineEndRegex='").append(netconfLineEndRegex).append('\'');
        sb.append(", \ncmdFailInfoRegex='").append(cmdFailInfoRegex).append('\'');
        sb.append(", \nusernamePromptRegex='").append(usernamePromptRegex).append('\'');
        sb.append(", \npwdPromptRegex='").append(pwdPromptRegex).append('\'');
        sb.append(", \npromptUnChangeCmdRegex='").append(promptUnChangeCmdRegex).append('\'');
        sb.append(", \nunCheckErrorCmdRegexWhenExecProcess='").append(unCheckErrorCmdRegexWhenExecProcess).append('\'');
        sb.append(", \ncmdExecStopWhenCheckError=").append(cmdExecStopWhenCheckError);
        sb.append(", \nyesOrNoQuestionRegex='").append(yesOrNoQuestionRegex).append('\'');
        sb.append(", \nquitCmdRegex='").append(quitCmdRegex).append('\'');
        sb.append(", \ncmdConnTimeout=").append(cmdConnTimeout);
        sb.append(", \ncmdAllowCheckLineEndTime=").append(cmdAllowCheckLineEndTime);
        sb.append(", \nspecialCmdCheckLineEndRegex='").append(specialCmdCheckLineEndRegex).append('\'');
        sb.append(", \ncmdConnFailRegex='").append(cmdConnFailRegex).append('\'');
        sb.append(", \nspecialCmdCheckLineEndTime=").append(specialCmdCheckLineEndTime);
        sb.append(", \ncmdSessionTimeout=").append(cmdSessionTimeout);
        sb.append(", \ndevDefaultUserPwds=").append(devDefaultUserPwds);
        sb.append(", \nforceCheckHasYesNoQuestionWithAnswerPatterns=").append(forceCheckHasYesNoQuestionWithAnswerPatterns);
        sb.append(", \ndevPageBreakCmds=").append(devPageBreakCmds);
        sb.append(", \nloginRedoTagRegex=").append(loginRedoTagRegex);
        sb.append(", \nenableModels=").append(enableModels);
        sb.append('}');
        return sb.toString();
    }
}
