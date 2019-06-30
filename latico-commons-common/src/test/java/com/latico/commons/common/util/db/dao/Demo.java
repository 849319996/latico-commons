package com.latico.commons.common.util.db.dao;

/**
 * <PRE>
 * Table Name : Demo
 * Class Name : Demo
 * </PRE>
 * <B>project:</B> KTJ2SE Development Platform
 * <B>company:</B> GD CATTSOFT(c) 2019
 * @version   1.0.0.0 2019-06-18 15:06:46
 * @author    {Chinese Name}:{who}@qq.com
 * @since     jdk version : jdk 1.6
 */
public class Demo {
    /** id */
    private Integer id;

    /** username */
    private String username;

    /** password */
    private String password;

    /** administrator */
    private Integer administrator;

    /**
     * getId
     * @return Integer
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * setId
     * @param id id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * getUsername
     * @return String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * setUsername
     * @param username username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getPassword
     * @return String
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * setPassword
     * @param password password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getAdministrator
     * @return Integer
     */
    public Integer getAdministrator() {
        return this.administrator;
    }

    /**
     * setAdministrator
     * @param administrator administrator to set
     */
    public void setAdministrator(Integer administrator) {
        this.administrator = administrator;
    }


    /**
     * get table name
     * @return String
     */
    public static String getTableName() {
        return "Demo";
    }

    /**
     * get class name
     * @return String
     */
    public static String getClassName() {
        return "Demo";
    }
    
    /**
     * toString
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Demo/Demo: {\r\n");
        sb.append("\tid").append(" = ").append(this.getId()).append("\r\n");
        sb.append("\tusername").append(" = ").append(this.getUsername()).append("\r\n");
        sb.append("\tpassword").append(" = ").append(this.getPassword()).append("\r\n");
        sb.append("\tadministrator").append(" = ").append(this.getAdministrator()).append("\r\n");
        sb.append("}\r\n");
        return sb.toString();
    }
}
