package coe.dronsys.main.pages.shop.model;


import coe.wuti.jpa.BasicEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by jesus on 1/8/16
 * .
 */
@Entity
public class Item extends BasicEntity {

    @OneToOne
    private Product product;

    @Basic
    private int quantity;

    public Item() {
    }

    public Item(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCombinedPrice() {
        return quantity * getProduct().getPriceInEuro();
    }
}
