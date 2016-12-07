package coe.dronsys.main.pages.base;

import coe.dronsys.main.app.DronsysSession;
import coe.dronsys.main.app.User;
import coe.dronsys.main.pages.blog.WPBlogEntry;
import coe.dronsys.main.pages.contact.WPContact;
import coe.dronsys.main.pages.about.WPAbout;
import coe.dronsys.main.pages.etc.WPAdmin;
import coe.dronsys.main.pages.help.WPHelp;
import coe.dronsys.main.pages.home.WPHome;
import coe.dronsys.main.pages.shop.WPShop;
import coe.dronsys.main.pages.shop.WPShoppingCart;
import coe.dronsys.main.pages.login.WPLogin;
import coe.dronsys.main.pages.login.WPLogout;
import coe.dronsys.main.pages.user.WPMyAccount;
import coe.dronsys.main.pages.user.WPSignup;
import coe.wuti.wicket.popup.Popup;
import coe.wuti.wicket.core.LWebPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * User: jrubio
 */
public abstract class WPBase extends LWebPage {

    // Menu items and their corresponding handling pages
    public static enum MenuItem {

        home(WPHome.class), shop(WPShop.class), blog(WPBlogEntry.class), contact(WPContact.class), about(WPAbout.class), admin(WPAdmin.class),
        login(WPLogin.class), logout(WPLogout.class), signup(WPSignup.class), myAccount(WPMyAccount.class), myCart(WPShoppingCart.class);

        private Class pageClass;

        MenuItem(Class pageClass) {
            this.pageClass = pageClass;
        }

        WebMarkupContainer getContainer() {
            return new WebMarkupContainer(name());
        }

        BookmarkablePageLink getLink() {
            return new BookmarkablePageLink(name() + "Link", pageClass);
        }
    }

    // Popup available for all pages
    private Popup popup;

    public WPBase() {

        // General: don't keep serveral versions of the same page
        setVersioned(false);

        // Title of the page
        add(new Label("title", "Dronsys "));

        // The session
        DronsysSession session = getDronsysSession();

        // Menu, set vissible and invisible items
        MenuItem highlightedItem = getAssociatedMeuItem();
        for (MenuItem menuItem : MenuItem.values()) {
            WebMarkupContainer item = menuItem.getContainer();
            add(item);

            Link ln = menuItem.getLink();
            item.add(ln);

            if (menuItem.equals(highlightedItem))
                item.add(new AttributeModifier("class", "active"));
            else
                item.add(new AttributeModifier("class", ""));

            // Disable login and signup links in case the user is authenticated
            if (menuItem == MenuItem.login || menuItem == MenuItem.signup)
                ln.setVisible(!session.isAutenticated());

            // Enable logout and myAccount page in case the user is logged in
            if (menuItem == MenuItem.logout || menuItem == MenuItem.myAccount || menuItem == MenuItem.myCart || menuItem == MenuItem.admin)
                ln.setVisible(session.isAutenticated());
        }

        // The wellcome message
        WebRequest req = (WebRequest) RequestCycle.get().getRequest();
        Cookie cookie = req.getCookie("greeting");
        String u = null;
        if (cookie != null) {
            try {
                u = User.get(URLDecoder.decode(cookie.getValue(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (session.isAutenticated()) {
            try {
                User user = session.getUser();
                u = user.getName();
                WebResponse resp = (WebResponse) RequestCycle.get().getResponse();
                Cookie newCookie = new Cookie("greeting", URLEncoder.encode(u, "UTF-8"));
                resp.addCookie(newCookie);
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            }
        }
        add(new Label("greeting", u != null && session.isAutenticated() ? "Wellcome, " + u : ""));


        // Footer
        add(new BookmarkablePageLink("help", WPHelp.class));

        // Popup available for all pages
        popup = new Popup("popup");
        add(popup);

        // Debug add (new DebugBar("debug").setVisible(App.get().getConfigurationType().equals(RuntimeConfigurationType.DEVELOPMENT))) ;

        // End base constructor
//        System.out.println("End base constructor");
    }

    protected Popup getPopup() {
        return popup;
    }

    protected DronsysSession getDronsysSession () {
        return (DronsysSession) Session.get() ;
    }

    public abstract String getPageTitle();

    public abstract MenuItem getAssociatedMeuItem();
}

