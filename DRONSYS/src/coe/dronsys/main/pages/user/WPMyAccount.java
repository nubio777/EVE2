package coe.dronsys.main.pages.user;

import coe.dronsys.main.pages.base.WPBase;

/**
 * User: jrubio
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class WPMyAccount extends WPBase {

    public WPMyAccount() {
        // User form
        add (new PUser("userForm", getPopup())) ;
    }

    @Override
    public String getPageTitle() {
        return "My account";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.myAccount;
    }

}
