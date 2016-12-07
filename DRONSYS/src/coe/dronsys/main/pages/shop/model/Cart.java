package coe.dronsys.main.pages.shop.model;

import coe.dronsys.main.app.User;
import coe.wuti.jpa.BasicEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesus on 1/8/16.
 */

@NamedQueries(value = {
        @NamedQuery(name = "Cart.byUser", query = "SELECT c FROM Cart c WHERE c.user = :user"),
})

@Entity
public class Cart extends BasicEntity {
    @OneToOne
    private coe.dronsys.main.app.User user;

    @OneToMany
    private List<Item> items;

    public Cart() {
    }

    public Cart(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items == null || items.size() == 0;
    }

    public void addItem(Item item) {
        if (item == null) return;

        if (items == null) items = new ArrayList<>();
        items.add(item);
    }

    public void removeItem(Item item) {
        if (item != null && items != null && items.contains(item))
            items.remove(item);
    }
}


