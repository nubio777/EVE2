package coe.dronsys.main.pages.notFound;

import coe.dronsys.main.pages.base.WPBase;

/**
 * User: jrubio
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class WP404 extends WPBase {

    public WP404() {
    }

    @Override
    public String getPageTitle() {
        return "Not found";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return null;
    }
}
