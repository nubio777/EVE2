package coe.dronsys.main.pages.user;

import coe.dronsys.main.pages.base.WPBase;

/**
 * User: jrubio
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class WPSignup extends WPBase {

    public WPSignup() {
        // User form
        add (new PUser("userForm", getPopup())) ;
    }

    @Override
    public String getPageTitle() {
        return "Singup";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.signup;
    }
}
