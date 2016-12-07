package coe.dronsys.main.pages.shop;

import coe.dronsys.main.pages.base.WPBase;
import coe.dronsys.main.pages.shop.model.Cart;
import coe.dronsys.main.pages.shop.model.Item;
import coe.wuti.jpa.DAO;
import coe.wuti.jpa.Update;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 * User: jrubio
 */
public class WPShoppingCart extends WPBase {
    private Cart cart;
    private boolean emptyCart;
    private WebMarkupContainer mainContent;
    private Calculus calculus ;

    public WPShoppingCart() {
        // The cart
        cart = DAO.get().getObject(Cart.class, "Cart.byUser", "user", getDronsysSession().getUser());
        emptyCart = cart == null || cart.isEmpty();

        // Empty cart ?
        add(new Label("cartEmpty", "Your cart is currently empty. ").setVisible(emptyCart));

        // Items list
        mainContent = new WebMarkupContainer("tableOfItems");
        mainContent.add(getListViewPItems());
        mainContent.setVisible(!emptyCart);
        mainContent.setOutputMarkupId(true);
        add(mainContent);

        // Price summary
        calculus = new Calculus() ;
        calculus.update();
        mainContent.add(new Label("subTotal", new PropertyModel<Double>(calculus, "subTotal")));
        mainContent.add(new Label("vat", new PropertyModel<Double>(calculus, "vat")));
        mainContent.add(new Label("totalPrice", new PropertyModel<Double>(calculus, "total")));

        // Links to other pages
        add(new BookmarkablePageLink("moreShopping", WPShop.class));
        AjaxLink alCheckOut = new AjaxLink("checkout") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getPopup().showMessage("Due to overwhelming demand we cannot ship any product at this moment.<br> Please, try it later.", "Information", target);
            }
        };
        alCheckOut.setVisible(!emptyCart);
        add(alCheckOut);
    }

    private ListView<Item> getListViewPItems() {
        List<Item> items = emptyCart ? null : cart.getItems();

        return new ListView<Item>("element", items) {
            @Override
            protected void populateItem(ListItem<Item> listItem) {
                final Item item = listItem.getModelObject();
                listItem.add(new Label("productName", item.getProduct().getName()));
                listItem.add(new Label("quantity", item.getQuantity()));
                listItem.add(new Label("unitPrice", item.getProduct().getPriceInEuro()));
                listItem.add(new Label("combinedPrice", item.getCombinedPrice()));
                listItem.add(new AddOrRemoveLink("addItem", item, true)) ;
                listItem.add(new AddOrRemoveLink("removeItem", item, false)) ;
            }
        };
    }

    private class Calculus {
        double subTotal, vat, total;

        Calculus () {
            update();
        }

        void update() {
            subTotal = 0.0;
            if (!emptyCart)
                for (Item item : cart.getItems())
                    subTotal += item.getCombinedPrice();

            vat = subTotal * .20;
            total = subTotal + vat;
        }

        public double getSubTotal() {
            return subTotal;
        }

        public double getVat() {
            return vat;
        }

        public double getTotal() {
            return total;
        }
    }

    private class AddOrRemoveLink extends AjaxLink {
        Item item;
        boolean add;

        private AddOrRemoveLink(String id, Item item, boolean add) {
            super(id);
            this.add = add;
            this.item = item;
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            if (add && item.getQuantity() >= 99) return;

            try {
                if (add) {
                    item.setQuantity(item.getQuantity() + 1);
                    DAO.get().execute(new Update(item));
                } else if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    DAO.get().execute(new Update(item));
                } else if (item.getQuantity() == 1) {
                    cart.removeItem(item);
                    DAO.get().execute(new Update(cart));
                }

                calculus.update();
                target.add(mainContent);
            } catch (Exception e) {
                getPopup().showMessage("Could not performed operation", "Information", target);
            }
        }


    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.shop;
    }

    @Override
    public String getPageTitle() {
        return "Cart";
    }
}
