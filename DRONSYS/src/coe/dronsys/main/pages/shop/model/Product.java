package coe.dronsys.main.pages.shop.model;

import coe.wuti.jpa.BasicEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Created by jesus on 1/8/16.
 */
@Entity
public class Product extends BasicEntity {
    @Basic
    @Column(nullable = false, length = 50)
    private String name;

    @Basic
    @Column(nullable = false)
    private double priceInEuro;

    @Lob
    @Column(nullable = true)
    private byte[] picture;

    @Basic
    @Column(nullable = false, length = 20)
    private String pictureType = "";

    public Product() {
    }

    public Product(String name, double priceInEuro, byte[] picture, String pictureType) {
        this.name = name;
        this.priceInEuro = priceInEuro;
        this.picture = picture;
        this.pictureType = pictureType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceInEuro() {
        return priceInEuro;
    }

    public void setPriceInEuro(double priceInEuro) {
        this.priceInEuro = priceInEuro;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }


}
