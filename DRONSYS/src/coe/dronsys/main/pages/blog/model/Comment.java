package coe.dronsys.main.pages.blog.model;

import coe.dronsys.main.app.User;
import coe.wuti.jpa.BasicEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jesus on 1/8/16.
 */

@NamedQueries(value = {
        @NamedQuery(name = "Comment.byEntry", query = "SELECT c FROM Comment c WHERE c.entry = :entry"),
})

@Entity
public class Comment extends BasicEntity {
    @OneToOne
    private Entry entry ;

    @OneToOne
    private User user ;

    @Basic
    @Column(nullable = false, length = 1000)
    private String text ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_FIELD")
    private Date when ;

    public Comment() {
    }

    public Comment(Entry entry, User user, String text, Date when) {
        this.entry = entry;
        this.user = user;
        this.text = text;
        this.when = when;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
}
