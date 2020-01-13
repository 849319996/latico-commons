package com.latico.commons.common.util.version;

import org.junit.Test;

import static org.junit.Assert.*;

public class VersionUtilsTest {

    @Test
    public void addVersionInfo() {
        VersionUtils.addVersionInfo("项目名称", "1.0", "latico", "20200113", "新增版本工具类");
        VersionUtils.addVersionInfo("项目名称", "1.0", "latico", "20200113", "新增版本工具类2");

        System.out.println(VersionUtils.getVersionInfosToHtml());
        System.out.println(VersionUtils.getVersionInfosToMarkdown());
    }
}