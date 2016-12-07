package coe.wuti.wicket.core;

import org.apache.wicket.Application;
import org.apache.wicket.ISessionListener;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;

import java.util.logging.Logger;

/**
 * Created by jesus on 3/16/16.
 */
public class MySessionListener implements ISessionListener {
    @Override
    public void onCreated(Session session) {
        Logger.getGlobal().info("** Session created **");
    }

    @Override
    public void onUnbound(String sessionId) {
        Logger.getGlobal().info("** Session unbound **");
    }
}
