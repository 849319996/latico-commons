package com.latico.commons.common.util.system.cmd;


import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.envm.LineSeparator;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.system.SystemUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 *  系统命令行操作工具.
 * </PRE>
 * @Author: latico
 * @Date: 2019-07-07 18:45:41
 * @Version: 1.0
 */
public class CmdUtils {

    /**
     * LOG 日志工具
     */
    private static final Logger LOG =
            LoggerFactory.getLogger(CmdUtils.class);

    /**
     * 私有化构造函数
     */
    protected CmdUtils() {
    }

    /**
     * 执行控制台命令
     *
     * @param cmd 控制台命令
     * @return 执行结果
     */
    public static CmdResult execute(String cmd) {
        return execute(cmd, false);
    }

    /**
     * <PRE>
     * 执行控制台命令.
     * 若启动debug模式, 则命令会阻塞等待异常码返回.
     * </PRE>
     *
     * @param cmd   控制台命令
     * @param debug 调试模式. true:返回包含异常码和异常信息的结果(会阻塞); false:只返回命令执行结果
     * @return 执行结果
     */
    public static CmdResult execute(String cmd, boolean debug) {
        CmdResult cmdRst = CmdResult.DEFAULT;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            cmdRst = execute(process, debug);
            process.destroy();

        } catch (Exception e) {
            LOG.error("执行控制台命令失败: {}", cmd, e);
        }
        return cmdRst;
    }

    private static CmdResult execute(Process process, boolean debug) {
        CmdResult cmdRst = new CmdResult();
        try {
            InputStream infoIs = process.getInputStream();
            cmdRst.setInfo(readProcessLine(infoIs));

            if (debug == true) {
                InputStream errIs = process.getErrorStream();
                cmdRst.setErr(readProcessLine(errIs));

                // 此方法会阻塞, 直到命令执行结束
                int errCode = process.waitFor();
                cmdRst.setErrCode(errCode);

                errIs.close();
            }
            infoIs.close();

        } catch (Exception e) {
            cmdRst = CmdResult.DEFAULT;
            LOG.error("执行控制台命令失败", e);
        }
        return cmdRst;
    }

    /**
     * 读取(同时打印)命令行的信息流
     *
     * @param is 信息流/异常流
     * @return 执行结果
     * @throws IOException
     */
    private static String readProcessLine(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, CharsetType.DEFAULT));

        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(LineSeparator.CRLF);
            // 实时打印命令行执行结果
            LOG.info(line);
        }
        return sb.toString();
    }

    /**
     * 通过命令行进行文件/文件夹复制
     *
     * @param srcPath 源文件路径
     * @param snkPath 目标文件路径
     * @return 执行结果
     */
    public static String copy(String srcPath, String snkPath) {
        String cmd = "";
        File file = new File(srcPath);
        if (SystemUtils.IS_OS_WINDOWS) {
            if (file.isFile()) {
                cmd = StringUtils.join("copySurface ", srcPath, " ", snkPath, " /Y");

            } else {
                srcPath = srcPath.trim().replaceAll("\\\\$", "");
                cmd = StringUtils.join("xcopy ", srcPath, " ", snkPath, " /e /y");
            }
        } else {
            srcPath = srcPath.trim().replaceAll("/$", "");
            cmd = StringUtils.join("cp -r ", srcPath, " ", snkPath);
        }
        return execute(cmd).getInfo();
    }

    /**
     * 打开DOS控制台（只支持win系统）
     *
     * @return 执行结果
     */
    public static String openDosUI() {
        String result = "";
        if (!SystemUtils.IS_OS_WINDOWS) {
            result = "Unsupport except windows-system.";

        } else {
            String cmd = "cmd /c start";
            result = execute(cmd).getInfo();
        }
        return result;
    }

    /**
     * 打开文件（只支持win系统）
     *
     * @param filePath 文件路径
     * @return 执行结果
     */
    public static String openFile(String filePath) {
        String result = "";
        if (!SystemUtils.IS_OS_WINDOWS) {
            result = "Unsupport except windows-system.";

        } else if (StringUtils.isEmpty(filePath)) {
            result = StringUtils.join("Invaild file: ", filePath);

        } else {
            File file = new File(filePath);
            if (!file.exists()) {
                result = StringUtils.join("Invaild file: ", filePath);

            } else {
                String cmd = StringUtils.join(
                        "rundll32 url.dll FileProtocolHandler ",
                        file.getAbsolutePath());
                result = execute(cmd).getInfo();
            }
        }
        return result;
    }

    /**
     * 打开网页（只支持win系统）
     *
     * @param url 网页地址
     * @return 执行结果
     */
    public static String openHttp(String url) {
        String result = "";
        if (!SystemUtils.IS_OS_WINDOWS) {
            result = "Unsupport except windows-system.";

        } else if (StringUtils.isEmpty(url)) {
            result = StringUtils.join("Invaild url: ", url);

        } else {
            String cmd = StringUtils.join("cmd /c start ", url);
            result = execute(cmd).getInfo();
        }
        return result;
    }

    /**
     * 运行bat脚本（只支持win系统）
     *
     * @param batFilePath 批处理脚本路径
     * @return 执行结果
     */
    public static String runBat(String batFilePath) {
        String result = "";
        if (!SystemUtils.IS_OS_WINDOWS) {
            result = "Unsupport except windows-system.";

        } else if (StringUtils.isEmpty(batFilePath)) {
            result = StringUtils.join("Invaild bat: ", batFilePath);

        } else {
            String cmd = StringUtils.join("cmd /c start ", batFilePath);
            result = execute(cmd).getInfo();
        }
        return result;
    }

    /**
     * 终止进程（只支持win系统）
     *
     * @param processName 进程名称
     */
    public static void kill(String processName) {
        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }

        Pattern ptn = Pattern.compile(StringUtils.join(processName, " +?(\\d+) "));
        String tasklist = execute("tasklist").getInfo();
        String[] tasks = tasklist.split("\n");

        for (String task : tasks) {
            if (task.startsWith(StringUtils.join(processName, " "))) {
                Matcher mth = ptn.matcher(task);
                if (mth.find()) {
                    String pid = mth.group(1);
                    execute(StringUtils.join("taskkill /f /t /im ", pid));
                }
            }
        }
    }

}
