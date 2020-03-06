package com.latico.commons.common.util.version;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <PRE>
 * 把版本记录通过 addVersionInfo写进 VersionUtils 中，然后通过 VersionUtils.getVersionInfosToMarkdown()读取
 * </PRE>
 *
 * @Author: latico
 * @Date: 2020-01-13 14:52
 * @Version: 1.0
 */
public class VersionUtils {
    public final static String LINE_SEPARATOR = "\r\n";
    /**
     * 存储的版本信息
     */
    private static final Queue<VersionInfo> versionInfos = new ConcurrentLinkedQueue<>();

    /**
     * 增加一个版本信息
     *
     * @param version    版本号
     * @param author     作者
     * @param updateTime 更新时间
     * @param updateInfo 更新信息
     */
    public static void addVersionInfo(String projectName, String version, String author, String updateTime, String updateInfo) {
        versionInfos.add(VersionInfo.build(projectName, version, author, updateTime, updateInfo));
    }

    /**
     * @return 所有版本信息
     */
    public static Queue<VersionInfo> getVersionInfos() {
        return versionInfos;
    }


    /**
     * markdown的格式
     *
     * @return
     */
    public static String getVersionInfosToMarkdown() {
        return toVersionInfosToMarkdownStr(versionInfos);
    }

    /**
     * HTML的格式
     *
     * @return
     */
    public static String getVersionInfosToHtml() {
        return toVersionInfosToHtmlTable(versionInfos);
    }
    /**
     * @return 字符串打印方式
     */
    public static String toVersionInfosToMarkdownStr(Queue<VersionInfo> versionInfos ) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEPARATOR).append("——————————————————————————————————————————————").append(LINE_SEPARATOR);
        sb.append("| 项目名称 | 版本号 | 作者 | 更新时间 | 更新内容 |").append(LINE_SEPARATOR);
        sb.append("|:------:|:------:|:------:|:------:|").append(LINE_SEPARATOR);
        for (VersionInfo versionInfo : versionInfos) {
            sb.append("| ").append(versionInfo.getProjectName());
            sb.append(" | ").append(versionInfo.getVersion());
            sb.append(" | ").append(versionInfo.getAuthor());
            sb.append(" | ").append(versionInfo.getUpdateTime());
            sb.append(" | ").append(versionInfo.getUpdateInfo());
            sb.append(" |").append(LINE_SEPARATOR);
        }
        sb.append("——————————————————————————————————————————————").append(LINE_SEPARATOR);
        return sb.toString();
    }

    /**
     * @return HTML的table方式
     */
    public static String toVersionInfosToHtmlTable(Queue<VersionInfo> versionInfos ) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEPARATOR).append("<html>\n<body>\n<table border=\"1\">");

        sb.append("<tr>").append(LINE_SEPARATOR);
        sb.append("<th>").append("项目名称").append("</th>").append(LINE_SEPARATOR);
        sb.append("<th>").append("版本号").append("</th>").append(LINE_SEPARATOR);
        sb.append("<th>").append("作者").append("</th>").append(LINE_SEPARATOR);
        sb.append("<th>").append("更新时间").append("</th>").append(LINE_SEPARATOR);
        sb.append("<th>").append("更新内容").append("</th>").append(LINE_SEPARATOR);
        sb.append("</tr>");

        for (VersionInfo versionInfo : versionInfos) {
            sb.append("<tr>").append(LINE_SEPARATOR);
            sb.append("<td>").append(versionInfo.getProjectName()).append("</td>");
            sb.append("<td>").append(versionInfo.getVersion()).append("</td>");
            sb.append("<td>").append(versionInfo.getAuthor()).append("</td>");
            sb.append("<td>").append(versionInfo.getUpdateTime()).append("</td>");
            sb.append("<td>").append(versionInfo.getUpdateInfo()).append("</td>");
            sb.append("</tr>");
            sb.append(LINE_SEPARATOR);
        }
        sb.append("</table>\n</body>\n</html>");
        return sb.toString();
    }

}
