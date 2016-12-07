package coe.dronsys.main.pages.about;

import coe.dronsys.main.pages.base.WPBase;
import coe.dronsys.main.pages.contact.WPContact;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * User: jrubio
 */
public class WPAbout extends WPBase {

    public WPAbout() {
        add (new BookmarkablePageLink("goToContact", WPContact.class)) ;
    }

    @Override
    public String getPageTitle() {
        return "About";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.about;
    }
}
