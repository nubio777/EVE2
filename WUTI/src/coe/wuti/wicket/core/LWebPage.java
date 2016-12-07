package coe.wuti.wicket.core;

import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.html.WebPage;

import java.util.logging.Logger;

/**
 * Created by jesus on 3/16/16.
 */
public class LWebPage extends WebPage {
    private static final Logger log = Logger.getGlobal() ;

    @Override
    protected void onInitialize() {
        super.onInitialize();
//        Logger.getGlobal().info("Page " + getPath() + " has been initialize");
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
//        Logger.getGlobal().info("Page " + getPath() + " has been configured");
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
//        Logger.getGlobal().info("Page " + getPath() + " is going to be rendered");

    }

    @Override
    protected void onRender() {
        super.onRender();
//        Logger.getGlobal().info("Page " + getPath() + " has been rendered");
    }
}
