package coe.dronsys.main.pages.contact;


import coe.wuti.jpa.BasicEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by jesus on 1/8/16.
 */
@Entity
public class Contact extends BasicEntity {
    @Basic
    @Column(nullable = false, length = 25)
    private String name ;

    @Basic
    @Column(nullable = false, length = 25)
    private String email ;

    @Basic
    @Column(nullable = false, length = 500)
    private String message ;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
