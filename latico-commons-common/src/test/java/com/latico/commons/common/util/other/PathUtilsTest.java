package com.latico.commons.common.util.other;

import org.junit.Test;

public class PathUtilsTest {

    @Test
    public void getProjectCompilePath() {
        String str = "D:\\eclipse\\eclipse-jee-photon-R-win32-x86_64\\eclipse-workspace\\ipran-confsrv\\target\\ipran-confsrv-2.0-SNAPSHOT\\WEB-INF\\classes";
        System.out.println(PathUtils.toLinux(str));
        System.out.println(PathUtils.isWebContainerPath(str));
    }

    @Test
    public void adapterFilePathSupportWebContainer() {
        System.out.println(PathUtils.getProjectCompilePath());
    }

    @Test
    public void formatResourcesPathForClassLoader() {
        System.out.println(PathUtils.formatResourcesPathForClassLoader("/com/latico/abc.xml"));
    }

    @Test
    public void getFileName() {
        System.out.println(PathUtils.getFileName("d:/abc/jajg.txt"));
    }

    @Test
    public void getParentFile() {
        System.out.println(PathUtils.getParentFile("d:/abc/jajg.txt"));
    }
}