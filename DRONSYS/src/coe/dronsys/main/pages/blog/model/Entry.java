package coe.dronsys.main.pages.blog.model;


import coe.wuti.jpa.BasicEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by jesus on 1/8/16.
 */
@Entity
public class Entry extends BasicEntity {
    @Basic
    @Column(nullable = false, length = 50)
    private String title;

    @Basic
    @Column(nullable = false, length = 50)
    private String subTitle;

    @Basic
    @Column(nullable = false, length = 5000)
    private String text;

    @OneToOne
    private Author author;

    public Entry() {
    }

    public Entry(String title, String subTitle, String text, Author author) {
        this.title = title;
        this.subTitle = subTitle;
        this.text = text;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
