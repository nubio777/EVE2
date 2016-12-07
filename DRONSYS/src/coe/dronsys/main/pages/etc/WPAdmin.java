package coe.dronsys.main.pages.etc;

import coe.dronsys.main.pages.base.WPBase;

/**
 * User: jrubio
 */
public class WPAdmin extends WPBase {

    public WPAdmin() {
    }

    @Override
    public String getPageTitle() {
        return "Admin";
    }


    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.admin ;
    }
}
