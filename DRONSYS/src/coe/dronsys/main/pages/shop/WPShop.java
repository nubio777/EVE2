package coe.dronsys.main.pages.shop;

import coe.dronsys.main.app.DronsysSession;
import coe.dronsys.main.pages.base.WPBase;
import coe.dronsys.main.pages.shop.model.Cart;
import coe.dronsys.main.pages.shop.model.Item;
import coe.dronsys.main.pages.shop.model.Product;
import coe.wuti.jpa.DAO;
import coe.wuti.jpa.Insert;
import coe.wuti.jpa.Update;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.DynamicImageResource;

import java.util.List;

/**
 * User: jrubio
 */
public class WPShop extends WPBase {

    public WPShop() {
        add(getListViewProducts());
    }

    private ListView<Product> getListViewProducts() {
        List<Product> allProducts = DAO.get().getList(Product.class);

        return new ListView<Product>("element", allProducts) {
            @Override
            protected void populateItem(ListItem<Product> listItem) {
                Product product = listItem.getModelObject();

                listItem.add(new Image("productPhoto", new DynamicImageResource() {
                    @Override
                    protected byte[] getImageData(Attributes attributes) {
                        return product.getPicture();
                    }
                }));

                listItem.add(new Label("productName", product.getName()));
                listItem.add(new Label("productPrice", product.getPriceInEuro()));

                listItem.add(new AjaxLink<Product>("addToCart") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        performDatabaseOperations(product, target);
                    }
                });
            }
        };
    }

    private void performDatabaseOperations(Product product, AjaxRequestTarget target) {
        DronsysSession dronsysSession = getDronsysSession();

        if (!dronsysSession.isAutenticated()) {
            getPopup().showMessage("Please, log in or sign up before you add items to your cart", "Info", target);
            return;
        }

        Cart cart = DAO.get().getObject(Cart.class, "Cart.byUser", "user", dronsysSession.getUser());
        if (cart == null) cart = new Cart(dronsysSession.getUser());

        Item item = null;
        if (cart.getItems() != null)
            for (Item i : cart.getItems())
                if (i != null && i.getProduct() != null && i.getProduct().equals(product)) {
                    item = i;
                    i.setQuantity(i.getQuantity() + 1);
                    break;
                }
        if (item == null) {
            item = new Item(product, 1);
            cart.addItem(item);
        }

        try {
            DAO.get().execute(item.getId() == 0L ? new Insert(item) : new Update(item));
            DAO.get().execute(cart.getId() == 0L ? new Insert(cart) : new Update(cart));
            getPopup().showMessage("The product has been added to your shopping cart.", "Great news", target);
        } catch (Exception e) {
            getPopup().showMessage("The item could not be updated to the database: " + e.getMessage(), "Info", target);
        }
    }

    @Override
    public String getPageTitle() {
        return "Shop";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.shop;
    }
}
