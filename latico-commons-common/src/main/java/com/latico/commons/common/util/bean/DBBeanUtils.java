package com.latico.commons.common.util.bean;

import com.latico.commons.common.util.bean.template.Placeholders;
import com.latico.commons.common.util.bean.template.Template;
import com.latico.commons.common.util.bean.template.TemplateFiles;
import com.latico.commons.common.util.collections.CollectionUtils;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.string.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <PRE>
 * 数据库javaBean工具
 *
 * 把数据库表生成javabean
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-06-18 14:45:36
 * @Version: 1.0
 */
public class DBBeanUtils {

    /**
     * java关键数组
     */
    private final static String[] JAVA_KEY_WORDS = {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while",
    };

    /**
     * java关键字列表
     */
    private final static List<String> JAVA_KEY_WORD_LIST =
            CollectionUtils.toList(JAVA_KEY_WORDS);

    /**
     * 从数据库中的表信息创建javabean
     *
     * @param conn        数据库连接
     * @param packageName 导出实体类所在的包名,如：com.latico.pub.db.po
     * @param expPath     导出实体类的路径,如：./src/main/java/com/latico/pub/db/po
     * @param tables      选择要导出的表，为空则导出所有表
     * @throws Exception
     */
    public static void getBeanFromDB(Connection conn, String packageName, String expPath, List<String> tables)
            throws Exception {

        Set<String> tableNames = new HashSet<String>();
        if (tables != null) {
            for (String tableName : tables) {
                tableNames.add(tableName.toLowerCase());
            }

        }
        DatabaseMetaData dmd = conn.getMetaData();
        ResultSet tableInfos = checkDBType(dmd);

        //迭代所有表，选择需要生成bean的表进行处理
        while (tableInfos.next()) {
            String tableName = tableInfos.getObject("TABLE_NAME").toString();
            if (tableName.toLowerCase().indexOf("bin$") != -1) {
                continue;
            }
            if (tableNames != null && !tableNames.contains(tableName.toLowerCase())) {
                continue;
            }

            //列名 - java类型
            Map<String, String> colTypeMap = new LinkedHashMap<String, String>();

            ResultSet colInfos = checkColumnsType(dmd, tableName);
            while (colInfos.next()) {
                String colType = dataBaseToJavaType(
                        colInfos.getString("TYPE_NAME"));
                String colName = colInfos.getString(4);

                colTypeMap.put(colName, colType);
            }

            //获取主键列名
            String pkColumnName = "";
            ResultSet pkRS = dmd.getPrimaryKeys(null, null, tableName);
            if (pkRS.next()) {
                pkColumnName = (String) pkRS.getObject(4);
            }

            //生成Bean内容
            String expData = createBeanData(tableName, colTypeMap,
                    pkColumnName, packageName);

            //生成bean类文件
            String expFilePath = expPath +
                    "/" + getHumpName(tableName) + ".java";
            File expFile = FileUtils.createFile(expFilePath);
            FileUtils.write(expFile, expData,
                    Template.DEFAULT_CHARSET, false);
        }
    }

    /**
     * @param conn
     * @param packageName
     * @param expPath
     * @param tables
     * @throws Exception
     */
    @Deprecated
    public static void getBeanFromDB2(Connection conn, String packageName, String expPath, List<String> tables)
            throws Exception {

        List<String> tableList = ((tables == null || tables.size() == 0) ? null : tables);

        DatabaseMetaData dmd = conn.getMetaData();
        ResultSet tableInfos = checkDBType(dmd);

        //迭代所有表，选择需要生成bean的表进行处理
        while (tableInfos.next()) {
            String tableName = tableInfos.getObject("TABLE_NAME").toString();
            if (tableName.toLowerCase().indexOf("bin$") != -1) {
                continue;
            }
            if (tableList != null && !tableList.contains(tableName)) {
                continue;
            }

            //列名 - java类型
            Map<String, String> colTypeMap = new LinkedHashMap<String, String>();

            ResultSet colInfos = checkColumnsType(dmd, tableName);
            while (colInfos.next()) {
                String colType = dataBaseToJavaType(
                        colInfos.getString("TYPE_NAME"));
                String colName = colInfos.getString(4);

                colTypeMap.put(colName, colType);
            }

            //获取主键列名
            String pkColumnName = "";
            ResultSet pkRS = dmd.getPrimaryKeys(null, null, tableName);
            if (pkRS.next()) {
                pkColumnName = (String) pkRS.getObject(4);
            }

            //生成Bean内容
            String expData = createBeanData(tableName, colTypeMap,
                    pkColumnName, packageName);

            //生成bean类文件
            String expFilePath = expPath +
                    "/" + getHumpTableName(tableName) + ".java";
            File expFile = FileUtils.createFile(expFilePath);
            FileUtils.write(expFile, expData,
                    Template.DEFAULT_CHARSET, false);
        }
    }

    /**
     * 判断数据库类型
     *
     * @param dmd DatabaseMetaData对象
     * @return ResultSet ResultSet对象
     * @throws SQLException 获取不到数据库类型
     */
    private static ResultSet checkDBType(DatabaseMetaData dmd)
            throws SQLException {
        String types[] = {"TABLE"};
        String dbName = dmd.getDatabaseProductName();
        ResultSet tabs = null;

        if (dbName.toLowerCase().equals("oracle")) {
            tabs = dmd.getTables(null, dmd.getUserName().toUpperCase(), null, types);

        } else if (dbName.toLowerCase().equals("mysql")) {
            tabs = dmd.getTables(null, null, null, types);

        } else if (dbName.toLowerCase().equals("sqlite")) {
            tabs = dmd.getTables(null, null, null, types);

        } else if (dbName.equals("Adaptive Server Enterprise")) {
            tabs = dmd.getTables(null, null, null, types);

        } else if (dbName.toLowerCase().equals("sqlserver")) {
            tabs = dmd.getTables(null, null, null, types);
        }
        return tabs;
    }

    /**
     * 检查每列
     *
     * @param dmd     DatabaseMetaData对象
     * @param tblName 表名
     * @return ResultSet集合
     * @throws SQLException 获取不到数据名
     */
    private static ResultSet checkColumnsType(DatabaseMetaData dmd,
                                              String tblName) throws SQLException {
        String dbName = dmd.getDatabaseProductName();
        ResultSet tabs = null;

        if (dbName.toLowerCase().equals("oracle")) {
            tabs = dmd.getColumns(null, dmd.getUserName().toUpperCase(), tblName, null);

        } else if (dbName.toLowerCase().equals("mysql")) {
            tabs = dmd.getColumns(null, "%", tblName, "%");

        } else if (dbName.toLowerCase().equals("sqlite")) {
            tabs = dmd.getColumns(null, "%", tblName, "%");

        } else if (dbName.equals("Adaptive Server Enterprise")) {
            tabs = dmd.getColumns(null, null, tblName, null);
        }
        return tabs;
    }

    /**
     * 数据库的数据类型转换java 数据类型
     *
     * @param source 数据库的数据类型
     * @return java 数据类型
     * @throws Exception
     */
    private static String dataBaseToJavaType(String source) throws Exception {
        String result = null;
        source = source.toLowerCase();

        if (source.equals("float")) {
            result = "float";
        } else if (source.startsWith("varchar") || source.startsWith("char")
                || source.startsWith("varchar2") || source.startsWith("nvarchar2")
                || source.startsWith("long") || source.startsWith("longtext")
                || source.startsWith("text") || "enum".equals(source)) {
            result = "String";
        } else if (source.equals("int") || source.equals("smallint")
                || source.equals("mediumint") || source.equals("tinyint")
                || source.matches("int\\([1-9]\\)")) {
            result = "Integer";
        } else if (source.equals("number") || source.matches("int\\(\\d{2,}\\)")) {
            result = "Long";
        } else if (source.equals("date")) {
            result = "java.util.Date";
        } else if (source.equals("time")) {
            result = "java.sql.Time";
        } else if (source.equals("datetime") || source.startsWith("timestamp")) {
            result = "java.sql.Timestamp";
        } else if (source.equals("double")) {
            result = "Double";
        } else if (source.equals("numeric")) {
            result = "BigDecimal";
        } else if (source.equals("bigint")) {
            result = "java.math.BigInteger";
        } else if (source.equals("decimal")) {
            result = "java.math.BigDecimal";
        } else if (source.equals("blob")) {
            result = "java.sql.Blob";
        } else if ("clob".equals(source)) {
            result = "java.sql.Clob";
        }
        if (StringUtils.isNotEmpty(result)) {
            return result;
        } else {
            throw new Exception("后台暂不支持该数据库类型转换成对应的java数据类型:" + source);
        }
    }

    private static String createBeanData(String tableName, Map<String, String> colTypeMap,
                                         String pkColumnName, String packageName) {
        StringBuilder sb = new StringBuilder();
        String colName = null;
        String colType = null;
        //取类模板
        Template beanClazz = new Template();
        beanClazz.read(TemplateFiles.bean_template);

        //设置年份和日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        beanClazz.set(Placeholders.year, date.substring(0, 4));
        beanClazz.set(Placeholders.date, date);

        //设置包路径
        beanClazz.set(Placeholders.package_path, packageName);

        //设置类名：去前缀的驼峰表名
        String clazzName = getHumpName(tableName);
        beanClazz.set(Placeholders.class_name, clazzName);

        //设置成员变量
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String humpColName = getHumpName(colName);
            sb.append("    /** ").append(colName).append(" */\r\n");
            sb.append("    private ").append(colType).append(" ");
            sb.append(humpColName).append(";\r\n\r\n");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 "\r\n"
        beanClazz.set(Placeholders.class_member, sb.toString());

        //设置getter setter
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String uppHumpColName = getHumpName(colName, true);
            String lowHumpColName = getHumpName(colName);

            //getter
            sb.append("    /**\r\n");
            sb.append("     * get").append(uppHumpColName).append("\r\n");
            sb.append("     * @return ").append(colType).append("\r\n");
            sb.append("     */\r\n");
            sb.append("    public ").append(colType);
            sb.append(" get").append(uppHumpColName).append("() {\r\n");
            sb.append("        return this.").append(lowHumpColName);
            sb.append(";\r\n    }\r\n\r\n");

            //setter
            sb.append("    /**\r\n");
            sb.append("     * set").append(uppHumpColName).append("\r\n");
            sb.append("     * @param ").append(lowHumpColName);
            sb.append(" ").append(lowHumpColName).append(" to set\r\n");
            sb.append("     */\r\n");
            sb.append("    public void");
            sb.append(" set").append(uppHumpColName);
            sb.append("(").append(colType).append(" ");
            sb.append(lowHumpColName).append(") {\r\n");
            sb.append("        this.").append(lowHumpColName).append(" = ");
            sb.append(lowHumpColName).append(";\r\n    }\r\n\r\n");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 "\r\n"
        beanClazz.set(Placeholders.getter_and_setter, sb.toString());

        //设置表名
        beanClazz.set(Placeholders.table_name, tableName);


        //设置 toString方法
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String uppHumpColName = getHumpName(colName, true);

            sb.append("        sb.append(\"\\t").append(colName).append("\").append(");
            sb.append("\" = \").append(this.get");
            sb.append(uppHumpColName).append("()).append(\"\\r\\n\");");
            sb.append("\r\n");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 "\r\n"
        beanClazz.set(Placeholders.to_string, sb.toString());

        //模板占位符替换完成，返回替换内容
        return beanClazz.getContent();
    }

    /**
     * 根据模板生成bean类的内容。
     *
     * @param tableName    表名
     * @param colTypeMap   每个列对应的类型映射表
     * @param pkColumnName 主键列名,根据是否为空影响update语句
     * @param packageName  类所属的包名
     * @return bean类的内容
     */
    @SuppressWarnings("unused")
    private static String createBeanData2(String tableName, Map<String, String> colTypeMap,
                                          String pkColumnName, String packageName) {
        StringBuilder sb = new StringBuilder();
        int colNum = colTypeMap.size();    //列数
        String colName = null;
        String colType = null;
        //取类模板
        Template beanClazz = new Template();
        beanClazz.read(TemplateFiles.bean_template);

        //设置年份和日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        beanClazz.set(Placeholders.year, date.substring(0, 4));
        beanClazz.set(Placeholders.date, date);

        //设置包路径
        beanClazz.set(Placeholders.package_path, packageName);

        //设置类名：去前缀的驼峰表名
        String clazzName = getHumpTableName(tableName);
        beanClazz.set(Placeholders.class_name, clazzName);

        //设置成员变量
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String humpColName = getHumpColumnName(colName, false);
            sb.append("    /** ").append(colName).append(" */\r\n");
            sb.append("    private ").append(colType).append(" ");
            sb.append(humpColName).append(";\r\n\r\n");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 "\r\n"
        beanClazz.set(Placeholders.class_member, sb.toString());

        //设置getter setter
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String uppHumpColName = getHumpColumnName(colName, true);
            String lowHumpColName = getHumpColumnName(colName, false);

            //getter
            sb.append("    /**\r\n");
            sb.append("     * get").append(uppHumpColName).append("\r\n");
            sb.append("     * @return ").append(colType).append("\r\n");
            sb.append("     */\r\n");
            sb.append("    public ").append(colType);
            sb.append(" get").append(uppHumpColName).append("() {\r\n");
            sb.append("        return this.").append(lowHumpColName);
            sb.append(";\r\n    }\r\n\r\n");

            //setter
            sb.append("    /**\r\n");
            sb.append("     * set").append(uppHumpColName).append("\r\n");
            sb.append("     * @param ").append(lowHumpColName);
            sb.append(" ").append(lowHumpColName).append(" to set\r\n");
            sb.append("     */\r\n");
            sb.append("    public void");
            sb.append(" set").append(uppHumpColName);
            sb.append("(").append(colType).append(" ");
            sb.append(lowHumpColName).append(") {\r\n");
            sb.append("        this.").append(lowHumpColName).append(" = ");
            sb.append(lowHumpColName).append(";\r\n    }\r\n\r\n");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 "\r\n"
        beanClazz.set(Placeholders.getter_and_setter, sb.toString());

        //设置表名
        beanClazz.set(Placeholders.table_name, tableName);

        //设置insert列
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            sb.append(colName).append(", ");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 ", "
        beanClazz.set(Placeholders.insert_column, sb.toString());

        //设置insert占位符
        sb.setLength(0);
        for (int i = 0; i < colNum; i++) {
            sb.append("?, ");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 ", "
        beanClazz.set(Placeholders.insert_column_placeholder, sb.toString());

        //设置update列
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            if (colNum > 1 && //当只有1列时，不做主键判断
                    pkColumnName != null && !"".equals(pkColumnName)) { //不update主键
                if (pkColumnName.equals(colName)) {
                    continue;
                }
            }
            sb.append(colName).append(" = ?, ");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 ", "
        beanClazz.set(Placeholders.update_column, sb.toString());

        //设置select列
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String lowHumpColName = getHumpColumnName(colName, false);

            sb.append(colName).append(" ");
            sb.append(lowHumpColName).append(", ");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 ", "
        beanClazz.set(Placeholders.select_column, sb.toString());

        //设置 insert用于替换占位符的参数
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String uppHumpColName = getHumpColumnName(colName, true);

            sb.append("                gxcloudpool.get").append(uppHumpColName);
            sb.append("(),\r\n");
        }
        sb.setLength(sb.length() - 3);    //移除最后一个 ",\r\n"
        beanClazz.set(Placeholders.insert_params, sb.toString());

        //设置 update用于替换占位符的参数
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            if (colNum > 1 && //当只有1列时，不做主键判断
                    pkColumnName != null && !"".equals(pkColumnName)) { //不update主键
                if (pkColumnName.equals(colName)) {
                    continue;
                }
            }
            String uppHumpColName = getHumpColumnName(colName, true);

            sb.append("                gxcloudpool.get").append(uppHumpColName);
            sb.append("(),\r\n");
        }
        sb.setLength(sb.length() - 3);    //移除最后一个 ",\r\n"
        beanClazz.set(Placeholders.update_params, sb.toString());

        //设置 获取单个数据库字段域名称的方法
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String uppHumpColName = getHumpColumnName(colName, true);

            sb.append("    /**\r\n");
            sb.append("     * get column name\r\n");
            sb.append("     * @return ").append(colName).append("\r\n");
            sb.append("     */\r\n");
            sb.append("    public static String get").append(uppHumpColName);
            sb.append("$ColName() {\r\n");
            sb.append("        return \"").append(colName);
            sb.append("\";\r\n    }\r\n\r\n");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 "\r\n"
        beanClazz.set(Placeholders.get_column_name, sb.toString());

        //设置 获取单个类成员变量名称的方法
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String uppHumpColName = getHumpColumnName(colName, true);
            String lowHumpColName = getHumpColumnName(colName, false);

            sb.append("    /**\r\n");
            sb.append("     * get java name\r\n");
            sb.append("     * @return ").append(lowHumpColName);
            sb.append("\r\n     */\r\n");
            sb.append("    public static String get").append(uppHumpColName);
            sb.append("$JavaName() {\r\n");
            sb.append("        return \"").append(lowHumpColName);
            sb.append("\";\r\n    }\r\n\r\n");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 "\r\n"
        beanClazz.set(Placeholders.get_java_name, sb.toString());

        //设置 获取所有数据库字段域名称的方法
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            sb.append(colName).append(", ");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 ", "
        beanClazz.set(Placeholders.all_column_names, sb.toString());

        //设置 获取所有类成员变量名称的方法
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String lowHumpColName = getHumpColumnName(colName, false);
            sb.append(lowHumpColName).append(", ");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 ", "
        beanClazz.set(Placeholders.all_java_names, sb.toString());

        //设置 toString方法
        sb.setLength(0);
        for (Map.Entry<String, String> col : colTypeMap.entrySet()) {
            colName = col.getKey();
            colType = col.getValue();
            String uppHumpColName = getHumpColumnName(colName, true);
            String lowHumpColName = getHumpColumnName(colName, false);

            sb.append("        sb.append(\"\\t").append(colName);
            sb.append('/').append(lowHumpColName).append("\").append(");
            sb.append("\" = \").append(this.get");
            sb.append(uppHumpColName).append("()).append(\"\\r\\n\");");
            sb.append("\r\n");
        }
        sb.setLength(sb.length() - 2);    //移除最后一个 "\r\n"
        beanClazz.set(Placeholders.to_string, sb.toString());

        //模板占位符替换完成，返回替换内容
        return beanClazz.getContent();
    }

    private static String getHumpName(String name) {
        return name;
    }

    /**
     * 把含下划线的表名构造为驼峰结构，并剔除诸如以"T_ZHWG_"或"T_"开头的规范部分(需配置,默认不剔除).
     * 不带下划线的表名,剔除前缀并将首字母大写后返回.
     * <p>
     * 若处理完得到的名称是java关键字，则前面补 $
     *
     * @param tableName 原表名
     * @return 若表名不含下划线则原样返回；否则返回驼峰结构的表名
     */
    private static String getHumpTableName(
            String tableName) {
        String tmpName = tableName;

        //含下划线的表名
        if (tmpName != null && tmpName.contains("_")) {
            StringBuffer sb = new StringBuffer();

            //若表名以下划线开头，剔除之
            while (tmpName.startsWith("_")) {
                tmpName = tmpName.substring(1);
            }

            //若表名以下划线结尾，剔除之，防止下面操作数组越界
            while (tmpName.endsWith("_")) {
                tmpName = tmpName.substring(0, tmpName.length() - 1);
            }

            //若表名以下划线开头，剔除之
            while (tmpName.startsWith("_")) {
                tmpName = tmpName.substring(1);
            }

            tmpName = tmpName.toLowerCase();
            char[] charArray = tmpName.toCharArray();
            sb.append((char) (charArray[0] - 32));    //表名首字母大写

            //把下划线删除，其后的字母转为大写
            for (int i = 1; i < charArray.length; i++) {
                if (charArray[i] == '_') {
                    i++;
                    sb.append((char) (charArray[i] - 32));
                } else {
                    sb.append(charArray[i]);
                }
            }
            tmpName = sb.toString();

            //不含下划线的表名
        } else {

            //首字母大写
            tmpName = firstWordToUpp(tmpName);
        }

        if (true == isJavaKeyWord(tmpName)) {
            tmpName = "$" + tmpName;
        }
        return tmpName;
    }

    /**
     * 转换字符串的第一个字母大写
     *
     * @param str 要转换字符串
     * @return 转换后的结果 ，如： string==>String
     */
    private static String firstWordToUpp(String str) {
        String first = str.substring(0, 1);
        String suffix = str.substring(1, str.length());
        return first.toUpperCase() + suffix;
    }

    /**
     * 转换字符串的第一个字母为小写
     *
     * @param str 要转换字符串
     * @return 转换后的结果 ，如： String==>string
     */
    private static String firstWordToLow(String str) {
        String first = str.substring(0, 1);
        String suffix = str.substring(1, str.length());
        return first.toLowerCase() + suffix;
    }

    /**
     * 检查字符串是否为java关键字
     *
     * @param str 待检查字符串
     * @return true:是 ; false:不是
     */
    private static boolean isJavaKeyWord(String str) {
        boolean isKeyWord = false;
        if (str != null && !"".equals(str.trim())) {
            isKeyWord = JAVA_KEY_WORD_LIST.contains(str.trim());
        }
        return isKeyWord;
    }

    private static String getHumpName(String columnName, boolean firstUpper) {
        if (firstUpper == true) {
            return firstWordToUpp(columnName);
        } else {
            return firstWordToLow(columnName);
        }
    }

    /**
     * 把含下划线的列名构造为驼峰结构.
     * 不带下划线的列名则只改变首字母大小写.
     * <p>
     * 若处理完得到的名称是java关键字，则前面补 _
     *
     * @param columnName 列名
     * @param firstUpper 开头字母是否需要大写
     * @return 驼峰形式列名
     */
    private static String getHumpColumnName(
            String columnName, boolean firstUpper) {
        String tmpName = columnName;

        //含下划线的列名
        if (tmpName != null && tmpName.contains("_")) {
            StringBuilder sb = new StringBuilder();

            //若列名以下划开头，剔除之
            while (tmpName.startsWith("_")) {
                tmpName = tmpName.substring(1);
            }

            //删除字段类型前缀 I_ 、 S_ 、D_ 等
            if (tmpName.charAt(1) == '_') {
                tmpName = tmpName.substring(2);
            }

            //若列名以下划结尾，剔除之，防止下面操作数组越界
            while (tmpName.endsWith("_")) {
                tmpName = tmpName.substring(0, tmpName.length() - 1);
            }

            tmpName = tmpName.toLowerCase();
            char[] charArray = tmpName.toCharArray();

            //首字母大写
            if (firstUpper == true) {
                sb.append((char) (charArray[0] - 32));

                //首字母小写
            } else {
                sb.append(charArray[0]);
            }

            //把下划线删除，其后的字母转为大写
            for (int i = 1; i < charArray.length; i++) {
                if (charArray[i] == '_') {
                    i++;
                    sb.append((char) (charArray[i] - 32));
                } else {
                    sb.append(charArray[i]);
                }
            }
            tmpName = sb.toString();

            //不含下划线的列名
        } else {
            if (firstUpper == true) {
                tmpName = firstWordToUpp(tmpName);
            } else {
                tmpName = firstWordToLow(tmpName);
            }
        }

        if (true == isJavaKeyWord(tmpName)) {
            tmpName = "_" + tmpName;
        }
        return tmpName;
    }

}
