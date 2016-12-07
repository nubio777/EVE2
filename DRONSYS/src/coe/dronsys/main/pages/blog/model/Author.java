package coe.dronsys.main.pages.blog.model;


import coe.wuti.jpa.BasicEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by jesus on 1/8/16.
 */
@Entity
public class Author extends BasicEntity {
    @Basic
    @Column(nullable = false, length = 50)
    private String nombre ;

    @Basic
    @Column(nullable = false, length = 1000)
    private String about ;

    public Author() {
    }

    public Author(String nombre, String about) {
        this.nombre = nombre;
        this.about = about;
    }

    public String getName() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
