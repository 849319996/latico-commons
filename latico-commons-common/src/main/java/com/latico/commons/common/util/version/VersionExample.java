package com.latico.commons.common.util.version;

/**
 * <PRE>
 * 版本更新记录
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @version <B>V1.0 2018年12月5日</B>
 * @since <B>JDK1.6</B>
 */
public class VersionExample extends AbstractVersion {
    private static final VersionExample version = new VersionExample();

    /**
     * 打印版本信息
     * @param args
     */
    public static void main(String[] args) {
        printVersionInfo();
    }

    /**
     * 执行方法，输出所有更新记录
     *
     * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
     */
    public static void printVersionInfo() {
        System.out.println(version.getVersionInfo());
    }


    /**
     * projectName 项目名称
     */
    private static final String projectName = "此处写项目名称";

    /**
     * projectDesc 项目描述
     */
    private static final String projectDesc = "此处写项目描述";

    public VersionExample() {
        super(projectName, projectDesc);
    }

    @Override
    protected void addUpdateHistory() {
        /////////////////////////////////升级记录////////////////////////////////////////
        addCurrentVersionInfo("0", "2018-12-05 18:00:00", "蓝鼎栋");
        addUpdateNode("20181205", "项目新建");

        ///////////////////////////////////////////////////////////////
        addCurrentVersionInfo("1.0", "2018-12-05 18:00:00", "蓝鼎栋");

        addUpdateNode("蓝鼎栋", "20181205", "项目提交到GitLab");
        addUpdateNode("蓝鼎栋", "20181205", "整理项目结构");
    }

}
