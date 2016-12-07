package coe.dronsys.main.pages.help;


import coe.wuti.jpa.BasicEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by jesus on 1/8/16.
 */
@Entity
public class Help extends BasicEntity {
    @Basic
    @Column(nullable = false, length = 500)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Help{" +
                "text='" + text + '\'' +
                '}';
    }
}
