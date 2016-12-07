package coe.dronsys.main.app;

import coe.wuti.jpa.BasicEntity;
import coe.wuti.jpa.DAO;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by jesus on 1/8/16.
 */

@NamedQueries(value = {
        @NamedQuery(name = "User.byMail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.byMailandPasswd", query = "SELECT u FROM User u WHERE u.email = :email and u.passwd = :passwd"),
})

@Entity
public class User extends BasicEntity {
    @Basic
    @Column(nullable = false, length = 50)
    private String name;

    @Basic
    @Column(nullable = false, length = 25)
    private String email;

    @Basic
    @Column(nullable = false, length = 25)
    private String passwd;

    @Basic
    @Column(nullable = false, length = 25)
    private String creditCardNr;

    @Lob
    @Column(nullable = true)
    private byte[] picture;

    @Basic
    @Column(nullable = false, length = 20)
    private String pictureType = "";


    public User() {
    }

    public User(String name, String email, String passwd, String creditCardNr) {
        this.name = name;
        this.email = email;
        this.passwd = passwd;
        this.creditCardNr = creditCardNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCreditCardNr() {
        return creditCardNr;
    }

    public void setCreditCardNr(String creditCardNr) {
        this.creditCardNr = creditCardNr;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureFormat() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public static String get(String s) throws Exception {
        Connection conn = DAO.get().getCt();
        String preparedQuery = "select name from User where name='" + s + "'";
        Statement stmt = conn.createStatement();
        boolean hasResults = stmt.execute(preparedQuery);
        if (!conn.getAutoCommit()) conn.commit();
        if (hasResults) {
            ResultSet rs = stmt.getResultSet();
            return rs.next() ? rs.getString(1) : null;
        } else
            return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                ", creditCardNr='" + creditCardNr + '\'' +
                ", picture? '" + (picture != null) + '\'' +
                ", pictureType = '" + pictureType + '\'' +
                '}';
    }
}
