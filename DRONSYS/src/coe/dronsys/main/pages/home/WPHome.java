package coe.dronsys.main.pages.home;

import coe.dronsys.main.pages.base.WPBase;

/**
 * User: jrubio
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class WPHome extends WPBase {

    public WPHome() {
    }

    @Override
    public String getPageTitle() {
        return "Home";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.home;
    }
}
