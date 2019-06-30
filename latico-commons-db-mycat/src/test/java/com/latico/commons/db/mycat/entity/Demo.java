package com.latico.commons.db.mycat.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author landingdong
 * @since 2019-02-24
 */
@Entity
@Table(name = "Demo")
public class Demo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private Integer administrator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Integer administrator) {
        this.administrator = administrator;
    }


    @Override
    public String toString() {
        return "Demo{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", administrator=" + administrator +
        "}";
    }
}
