package com.latico.commons.freemarker;

import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.system.classloader.ClassLoaderUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-04 14:25
 * @Version: 1.0
 */
public class FreemarkerUtils {

    /**
     * 通过资源文件创建Template
     *
     * @param filePath 资源文件路径格式如：com/latico/commons/log4j.properties、ftl/01.ftl
     * @return
     * @throws Exception
     */
    private static Template createTemplateByResourcesFile(String filePath) throws Exception {

        int lastIndexOf = filePath.lastIndexOf("/");
        String packagePath = "";
        String fileName = filePath;
        if (lastIndexOf != -1) {
            packagePath = PathUtils.formatResourcesPathForClassLoader(filePath.substring(0, lastIndexOf));
            fileName = filePath.substring(lastIndexOf + 1);
        }

//        这里的过时是因为，需要指定版本，目前要求不高，使用默认版本
        Configuration configuration = new Configuration();
        configuration.setClassLoaderForTemplateLoading(ClassLoaderUtils.getDefaultClassLoader(), packagePath);

        Template template = configuration.getTemplate(fileName);

        return template;

    }

    /**
     * 通过文件系统的文件创建Template
     *
     * @param filePath 文件系统路径格式
     * @return
     * @throws Exception
     */
    private static Template createTemplateByFile(String filePath) throws Exception {

        filePath = PathUtils.toLinux(filePath);

        File dirPath = PathUtils.getParentFile(filePath);
        String fileName = PathUtils.getFileName(filePath);

//        这里的过时是因为，需要指定版本，目前要求不高，使用默认版本
        Configuration configuration = new Configuration();
        configuration.setDirectoryForTemplateLoading(dirPath);

        Template template = configuration.getTemplate(fileName);

        return template;

    }

    /**
     * 生成返回结果成string
     *
     * @param template
     * @param dataModel
     * @return
     * @throws Exception
     */
    private static String generateToString(Template template, Object dataModel) throws Exception {
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        return writer.getBuffer().toString();
    }

    /**
     * 生成的结果写进文件
     *
     * @param template
     * @param dataModel
     * @param outFilePath
     * @throws Exception
     */
    private static void generateToFile(Template template, Object dataModel, String outFilePath) throws Exception {
// 通过一个文件输出流，就可以写到相应的文件中，此处用的是绝对路径
        FileWriter writer = null;
        try {
            File file = FileUtils.createFile(outFilePath);
            writer = new FileWriter(file);
            template.process(dataModel, writer);
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.close(writer);
        }

    }

    /**
     * @param filePath
     * @param dataModel
     * @return
     * @throws Exception
     */
    public static String generateByResourcesFile(String filePath, Object dataModel) throws Exception {
        Template template = createTemplateByResourcesFile(filePath);
        return generateToString(template, dataModel);
    }

    /**
     * @param filePath
     * @param dataModel
     * @param outFilePath
     * @throws Exception
     */
    public static void generateResultFileByResourcesFile(String filePath, Object dataModel, String outFilePath) throws Exception {
        Template template = createTemplateByResourcesFile(filePath);
        generateToFile(template, dataModel, outFilePath);
    }

    /**
     * @param filePath
     * @param dataModel
     * @return
     * @throws Exception
     */
    public static String generateByFile(String filePath, Object dataModel) throws Exception {
        Template template = createTemplateByFile(filePath);
        return generateToString(template, dataModel);
    }

    /**
     * @param filePath
     * @param dataModel
     * @param outFilePath
     * @throws Exception
     */
    public static void generateResultFileByFile(String filePath, Object dataModel, String outFilePath) throws Exception {
        Template template = createTemplateByFile(filePath);
        generateToFile(template, dataModel, outFilePath);
    }


}
