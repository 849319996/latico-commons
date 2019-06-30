package com.latico.commons.common.util.db.dao;

import com.latico.commons.common.util.collections.CollectionUtils;
import com.latico.commons.common.util.db.DBUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.reflect.GenericUtils;
import com.latico.commons.common.util.string.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * 继承该类，复写获取数据库连接方法
 * 对于数据库表名，假如没有使用 DBTableNameAnnotation 或者javax.persistence.Table注解，那么就是使用类名，
 * 对于数据库字段名，假如没有使用DBFieldNameAnnotation或者 javax.persistence.Column注解，那么就是使用bean对象的字段名称
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-24 1:17
 * @Version: 1.0
 */
public abstract class BaseDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

    /**
     * 泛型参数指定的类
     */
    protected final Class<T> genricClass = GenericUtils.getSuperClassGenricType(this.getClass());

    /**
     * 泛型参数指定的类的字段
     */
    protected final List<Field> genricClassFields = getGenricClassDeclaredFields();

    /**
     * 泛型参数指定的类的字段的名称
     */
    protected final List<String> genricClassFieldNames = getGenricClassFieldNames();

    /**
     * 表名
     */
    protected final String genricClassTableName = getGenricClassTableName();

    /**
     * 字段名称和字段的Map
     */
    protected final Map<String, Field> genricClassFieldNameFieldMap = getGenricClassFieldNameFieldMap();

    /**
     * 通用查询SQL，如：select fieldName1,fieldName2 from genricClassTableName
     * select id, name, nick_name, age from Compare_Bean
     */
    public final String SQL_SELECT = DBUtils.makeSelectSql(genricClassTableName, genricClassFieldNames);

    /**
     * 通用插入SQL，预处理PreparedStatement模式的
     * insert into Compare_Bean (id, name, nick_name, age) values(?, ?, ?, ?)
     */
    public final String SQL_INSERT = DBUtils.makeInsertPreparedSql(genricClassTableName, genricClassFieldNameFieldMap.keySet());

    /**
     * 通用删除SQL，示例如：
     * delete from Compare_Bean
     */
    public final String SQL_DELETE = DBUtils.makeDeleteSql(genricClassTableName);

    /**
     * update Compare_Bean set id = ?, name = ?, nick_name = ?, age = ?
     */
    public final String SQL_UPDATE = DBUtils.makeUpdatePreparedSql(genricClassTableName, genricClassFieldNameFieldMap.keySet());

    /**
     * 默认UTF-8
     */
    protected String charset = null;

    /**
     * 默认字符集
     */
    protected final String charsetDefault = "UTF-8";

    /**
     * 是否把LOB类型字段转换成字符串类型，默认true
     */
    protected boolean lobToStr = true;

    /**
     * 子类实现数据库连接
     *
     * @return
     */
    protected abstract Connection getConnection();

    /**
     * @return
     */
    public String setCharset(String charset) {
        return this.charset = charset;
    }

    /**
     * 是否把LOB类型字段转换成字符串类型，默认true,子类可以复写该方法
     *
     * @return
     */
    public boolean isLobToStr() {
        return true;
    }

    /**
     * 是否把LOG字段类型自动转换成String，默认true，方便做数据处理
     *
     * @param lobToStr
     */
    public void setLobToStr(boolean lobToStr) {
        this.lobToStr = lobToStr;
    }

    /**
     * 如果有特殊字符集要求，请覆盖此方法
     * 不复写，默认UTF-8
     *
     * @return
     */
    public String getCharset() {
        if (StringUtils.isNotBlank(this.charset)) {
            return this.charset;
        }

        return this.charsetDefault;
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<T> selectAll() {
        Connection conn = null;
        try {
            String sql = SQL_SELECT;
            conn = getConnection();
            return DBUtils.queryObject(genricClass, genricClassFieldNameFieldMap, conn, sql, getCharset(), isLobToStr());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }

        return null;
    }

    /**
     * @param whereCondition where条件（要含有where），可以为空
     * @return
     */
    public List<T> selectByWhere(String whereCondition) {
        Connection conn = null;
        try {
            String sql = SQL_SELECT;
            if (whereCondition != null && !"".equals(whereCondition.trim())) {
                sql += " " + whereCondition;
            }
            conn = getConnection();
            return DBUtils.queryObject(genricClass, genricClassFieldNameFieldMap, conn, sql, getCharset(), isLobToStr());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }

        return null;
    }

    /**
     * 通过一个字段名和值查询所有记录
     * @param key   where 条件的key，where条件的vlaue
     * @param value
     * @return
     */
    public List<T> selectByWhereKeyValue(String key, Object value) {
        Connection conn = null;
        try {
            String sql = addWhereCondition(SQL_SELECT, key, value);
            conn = getConnection();
            return DBUtils.queryObject(genricClass, genricClassFieldNameFieldMap, conn, sql, getCharset(), isLobToStr());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }

        return null;
    }

    /**
     * @param whereConditions where条件的key-value,多个
     * @return
     */
    public List<T> selectByWhereKeyValues(Map<String, Object> whereConditions) {
        Connection conn = null;
        try {
            String sql = addWhereConditions(SQL_SELECT, whereConditions);
            conn = getConnection();
            return DBUtils.queryObject(genricClass, genricClassFieldNameFieldMap, conn, sql, getCharset(), isLobToStr());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }

        return null;
    }

    /**
     * 返回第一个
     * @param whereConditions 条件
     * @return
     */
    public T selectFirstByWhereKeyValues(Map<String, Object> whereConditions) {
        return first(selectByWhereKeyValues(whereConditions));
    }

    /**
     * @param whereCondition where条件（要含有where），可以为空
     * @return 返回第一个
     */
    public T selectFirstByWhere(String whereCondition) {
        return first(selectByWhere(whereCondition));
    }
    /**
     * 返回第一个
     * @param key
     * @param value
     * @return
     */
    public T selectFirstByWhereKeyValue(String key, Object value) {
        List<T> list = selectByWhereKeyValue(key, value);
        return first(list);
    }
    /**
     * @param sql 指定SQL
     * @return
     */
    public List<T> selectBySql(String sql) {
        Connection conn = null;
        try {
            conn = getConnection();
            return DBUtils.queryObject(genricClass, genricClassFieldNameFieldMap, conn, sql, getCharset(), isLobToStr());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }

        return null;
    }

    /**
     * @param sql 指定SQL
     * @return
     */
    public T selectFirstBySql(String sql) {
        return first(selectBySql(sql));
    }

    /**
     * @param obj
     * @return 异常返回-1
     */
    public int insert(T obj) {
        Connection conn = null;
        try {
            Map<String, Object> fieldNameValueMap = getFieldNameValueMap(obj);

            String sql = SQL_INSERT;

            conn = getConnection();
            return DBUtils.executeUpdate(conn, sql, fieldNameValueMap.values());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return -1;
    }

    /**
     * 逐条入库，非批量入库
     *
     * @param objs
     * @return 异常返回-1
     */
    public int insert(List<T> objs) {
        Connection conn = null;
        int i = 0;
        try {
            conn = getConnection();
            String sql = SQL_INSERT;
            for (T obj : objs) {
                Map<String, Object> fieldNameValueMap = getFieldNameValueMap(obj);

                int update = DBUtils.executeUpdate(conn, sql, fieldNameValueMap.values());
                if (update >= 1) {
                    i += update;
                }
            }
            return i;

        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return -1;
    }

    /**
     * 批量入库，一次性执行
     *
     * @param objs
     * @return 异常返回-1
     */
    public int[] insertBatch(List<T> objs) {
        Connection conn = null;
        int i = 0;
        try {
            conn = getConnection();
            String sql = SQL_INSERT;
            List<Object[]> paramList = new ArrayList<>();
            for (T obj : objs) {
                Map<String, Object> fieldNameValueMap = getFieldNameValueMap(obj);

                paramList.add(fieldNameValueMap.values().toArray());
            }

            return DBUtils.executeUpdateBatch(conn, sql, paramList);

        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return new int[0];
    }
    /**
     * 插入数据到Oracle的Blob字段，SQL格式必须是：select BLOB字段名称 from 表名
     * @param selectBlobFieldSql SQL格式必须是：select BLOB字段名称 from 表名
     * @param bytes 数据
     * @return
     */
    public boolean insertOneBlobFieldOracle(String selectBlobFieldSql, byte[] bytes) {
        Connection conn = getConnection();

        //处理发现过程字段
        try {
            DBUtils.insertOneBlobFieldOracle(conn, selectBlobFieldSql, bytes);
            return true;
        } catch (Exception e) {
            LOG.error(selectBlobFieldSql, e);
        }finally{

            DBUtils.close(conn);
        }
        return false;
    }

    /**
     * 插入数据到Oracle的Clob字段
     * @param selectClobFieldSql SQL格式必须是：select CLOB字段名称 from 表名
     * @param data 数据
     * @return
     */
    public boolean insertOneClobFieldOracle(String selectClobFieldSql, String data) {
        Connection conn = getConnection();

        //处理发现过程字段
        try {
            DBUtils.insertOneClobFieldOracle(conn, selectClobFieldSql, data);
            return true;
        } catch (Exception e) {
            LOG.error(selectClobFieldSql, e);
        }finally{

            DBUtils.close(conn);
        }
        return false;
    }
    /**
     * 更新对象所有字段
     *
     * @param obj
     * @param whereCondition where条件（要含有where），防止操作失误，强制不能为空
     * @return 异常返回-1
     */
    public int updateByWhere(T obj, String whereCondition) {
        if (whereCondition == null || "".equals(whereCondition.trim())) {
            return -1;
        }
        Connection conn = null;
        try {
            Map<String, Object> fieldNameValueMap = getFieldNameValueMap(obj);

            String sql = SQL_UPDATE;
            sql += " " + whereCondition;

            conn = getConnection();
            return DBUtils.executeUpdate(conn, sql, fieldNameValueMap.values());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return -1;
    }

    /**
     * @param obj
     * @param key
     * @param value
     * @return
     */
    public int updateByWhereKevValue(T obj, String key, Object value) {
        String sql = addWhereCondition(SQL_UPDATE, key, value);
        Connection conn = null;
        try {
            Map<String, Object> fieldNameValueMap = getFieldNameValueMap(obj);
            conn = getConnection();
            return DBUtils.executeUpdate(conn, sql, fieldNameValueMap.values());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return -1;
    }

    /**
     * @param obj
     * @param whereKevValues
     * @return
     */
    public int updateByWhereKevValues(T obj, Map<String, Object> whereKevValues) {
        String sql = addWhereConditions(SQL_UPDATE, whereKevValues);
        Connection conn = null;
        try {
            Map<String, Object> fieldNameValueMap = getFieldNameValueMap(obj);
            conn = getConnection();
            return DBUtils.executeUpdate(conn, sql, fieldNameValueMap.values());
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return -1;
    }

    /**
     * 更新所有字段，批量一次性执行
     *
     * @param objs
     * @return 异常返回-1
     */
    public int[] updateBatch(List<T> objs, List<Map<String, Object>> WhereKevValues) {
        if (objs == null) {
            return new int[0];
        }

        int size = objs.size();
        int[] ints = new int[size];
        for (int i = 0; i < size; i++) {
            T obj = objs.get(0);
            if (WhereKevValues != null) {
                Map<String, Object> stringObjectMap = WhereKevValues.get(0);
                ints[i] = updateByWhereKevValues(obj, stringObjectMap);
            }
        }

        return new int[0];
    }

    /**
     * @param whereCondition where条件（要含有where），防止操作失误，强制不能为空
     * @return
     */
    public int delete(String whereCondition) {
        if (whereCondition == null || "".equals(whereCondition.trim())) {
            return -1;
        }
        Connection conn = null;
        try {

            String sql = SQL_DELETE;
            sql += " " + whereCondition;

            conn = getConnection();
            return DBUtils.executeUpdate(conn, sql);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return -1;
    }

    /**
     * 数据库连接拿一次，逐个执行
     *
     * @param whereConditions where条件（要含有where），防止操作失误，强制不能为空
     * @return
     */
    public int delete(List<String> whereConditions) {

        Connection conn = null;
        int i = 0;
        try {
            conn = getConnection();
            final String sql = SQL_DELETE;
            for (String whereCondition : whereConditions) {
                if (whereCondition == null || "".equals(whereCondition.trim())) {
                    continue;
                }
                int update = DBUtils.executeUpdate(conn, sql + " " + whereCondition);
                if (update >= 1) {
                    i += update;
                }
            }

            return i;
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return -1;
    }

    /**
     * 批量删除，一次性执行
     *
     * @param whereConditions where条件（要含有where），防止操作失误，强制不能为空
     * @return
     */
    public int[] deleteBatch(List<String> whereConditions) {

        List<String> sqls = new ArrayList<>();

        Connection conn = null;
        try {
            String sql = SQL_DELETE;
            for (String whereCondition : whereConditions) {
                if (whereCondition == null || "".equals(whereCondition.trim())) {
                    continue;
                }
                sqls.add(sql + " " + whereCondition);
            }

            conn = getConnection();
            return DBUtils.executeUpdateBatch(conn, sqls);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return new int[0];
    }

    /**
     * 执行更新SQL
     *
     * @param sql
     * @return
     */
    public int executeUpdate(String sql) {

        Connection conn = null;
        try {
            conn = getConnection();
            return DBUtils.executeUpdate(conn, sql);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return -1;
    }

    /**
     * 批量更新，一次性执行
     *
     * @param sqls
     * @return
     */
    public int[] executeUpdateBatch(List<String> sqls) {

        Connection conn = null;
        try {
            conn = getConnection();
            return DBUtils.executeUpdateBatch(conn, sqls);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            DBUtils.close(conn);
        }
        return new int[0];
    }

    private Map<String, Object> getFieldNameValueMap(T obj) throws IllegalAccessException {

        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Field> entry : genricClassFieldNameFieldMap.entrySet()) {
            Field field = entry.getValue();
            map.put(entry.getKey(), field.get(obj));
        }

        return map;
    }

    /**
     * 获取字段的值
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    private List<Object> getGenricClassFieldValues(T obj) throws IllegalAccessException {

        List<Object> list = new ArrayList<>();
        for (Map.Entry<String, Field> entry : genricClassFieldNameFieldMap.entrySet()) {
            Field field = entry.getValue();
            list.add(field.get(obj));
        }

        return list;
    }

    /**
     * @return 字段名称和字段的Map
     */
    private Map<String, Field> getGenricClassFieldNameFieldMap() {
        Map<String, Field> map = new LinkedHashMap<>();
        for (Field field : genricClassFields) {
            DBFieldNameAnnotation fieldAnnotation = field.getAnnotation(DBFieldNameAnnotation.class);
            Column column = field.getAnnotation(Column.class);
            String fieldName;
            if (fieldAnnotation != null && StringUtils.isNotBlank(fieldAnnotation.value())) {
                fieldName = fieldAnnotation.value();
            } else if(column != null && StringUtils.isNotBlank(column.name())){
                fieldName = column.name();
            } else {
                fieldName = field.getName();
            }
            map.put(fieldName, field);
        }

        return map;
    }

    /**
     * @return
     */
    private List<String> getGenricClassFieldNames() {

        List<String> fieldNames = new ArrayList<>();
        for (Field field : genricClassFields) {

            DBFieldNameAnnotation fieldAnnotation = field.getAnnotation(DBFieldNameAnnotation.class);
            Column column = field.getAnnotation(Column.class);
            if (fieldAnnotation != null && StringUtils.isNotBlank(fieldAnnotation.value())) {
                fieldNames.add(fieldAnnotation.value());
            } else if(column != null && StringUtils.isNotBlank(column.name())){
                fieldNames.add(column.name());
            }  else {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }

    private String getGenricClassTableName() {
        DBTableNameAnnotation dbTableNameAnnotation = genricClass.getAnnotation(DBTableNameAnnotation.class);
        Table table = genricClass.getAnnotation(Table.class);

        String tableName;
        if (dbTableNameAnnotation != null && StringUtils.isNotBlank(dbTableNameAnnotation.value())) {
            tableName = dbTableNameAnnotation.value();
        }else if(table != null && StringUtils.isNotBlank(table.name())){
            tableName = table.name();
        }else {
            tableName = genricClass.getSimpleName();
        }
        return tableName;
    }

    private List<Field> getGenricClassDeclaredFields() {
        List<Field> list = new ArrayList<>();
        Field[] fields = genricClass.getDeclaredFields();
        for (Field field : fields) {
            //跳过静态和final字段
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            NotDBFieldAnnotation notDBFieldAnnotation = field.getAnnotation(NotDBFieldAnnotation.class);
            if (notDBFieldAnnotation != null) {
                continue;
            }

            field.setAccessible(true);
            list.add(field);
        }
        return list;
    }

    /**
     * 添加where条件
     * 方法内部会自动判断是否已经添加where
     *
     * @param sqlRaw 原始的SQL
     * @param key    字段
     * @param value  字段值
     * @return
     */
    public static String addWhereCondition(String sqlRaw, String key, Object value) {
        if (StringUtils.isNoneEmpty(key, value)) {
            StringBuffer sql = new StringBuffer(sqlRaw);
            if (sqlRaw.matches("(?i)[\\s\\S]+?\\swhere\\s[\\s\\S]+?")) {
                sql.append(" and ").append(key).append(" = '").append(value).append("'");
            } else {
                sql.append(" where ").append(key).append(" = '").append(value).append("'");
            }
            return sql.toString();
        } else {
            return sqlRaw;
        }
    }

    /**
     * 添加where条件
     * 方法内部会自动判断是否已经添加where
     *
     * @param sqlRaw 原始的SQL
     * @param kv     字段和值，支持多个
     * @return
     */
    public static String addWhereConditions(String sqlRaw, Map<String, Object> kv) {
        if (kv != null && kv.size() != 0) {
            StringBuilder sql = new StringBuilder(sqlRaw);
            if (!sqlRaw.matches("(?i)[\\s\\S]+?\\swhere\\s[\\s\\S]+?")) {
                sql.append(" where 1=1");
            }

            for (Map.Entry<String, Object> entry : kv.entrySet()) {
                sql.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
            }
            return sql.toString();
        } else {
            return sqlRaw;
        }
    }

    /**
     * 返回第一个
     * @param list
     * @return
     */
    protected T first(List<T> list){
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
