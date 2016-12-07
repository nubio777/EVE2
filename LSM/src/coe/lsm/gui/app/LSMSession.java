package coe.lsm.gui.app;

import coe.lsm.main.model.Status;
import coe.lsm.main.model.Team;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * Created by jesus on 1/29/16.
 */
public class LSMSession extends WebSession {

    private boolean authenticated;
    private Team loginTeam;
    private Status lastSelectedStatus;

    public static LSMSession get() {
        return (LSMSession) WebSession.get();
    }

    public LSMSession(Request request) {
        super(request);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        if (!authenticated) {
            loginTeam = null;
            lastSelectedStatus = null;
        }
    }

    public Team getLoginTeam() {
        return loginTeam;
    }

    public void setLoginTeam(Team loginTeam) {
        this.loginTeam = loginTeam;
    }

    public Status getLastSelectedStatus() {
        return lastSelectedStatus;
    }

    public void setLastSelectedStatus(Status lastSelectedStatus) {
        this.lastSelectedStatus = lastSelectedStatus;
    }
}
