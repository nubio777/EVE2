package coe.lsm.gui.pages.base;

import coe.wuti.wicket.core.LWebPage;
import org.apache.wicket.markup.html.basic.Label;


/**
 * User: jrubio
 */
public abstract class WPBase extends LWebPage {
    public WPBase() {

        // General: don't keep several versions of the same page
//        setVersioned(false);

        // Title of the page
        add(new Label("title", getPageTitle()));
    }

    public String getPageTitle() {
        return "LS 2016";
    }
}

