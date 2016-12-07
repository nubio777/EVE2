package coe.dronsys.main.pages.login;

import coe.dronsys.main.app.DronsysSession;
import coe.dronsys.main.pages.base.WPBase;
import coe.dronsys.main.pages.home.WPHome;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;

/**
 * User: jrubio
 */
public class WPLogout extends WPBase {

    public WPLogout() {

        // The session
        DronsysSession session = (DronsysSession) Session.get();
        session.setAutenticated(false);
        session.setUser(null);

        throw new RestartResponseException(WPHome.class);
    }

    @Override
    public String getPageTitle() {
        return "Login";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.login;
    }

}
