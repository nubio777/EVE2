package coe.dronsys.main.app;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * Created by jesus on 1/29/16.
 */
public class DronsysSession extends WebSession {

    private boolean autenticated;
    private User user;

    public DronsysSession(Request request) {
        super(request);
    }

    public boolean isAutenticated() {
        return autenticated;
    }

    public void setAutenticated(boolean autenticated) {
        this.autenticated = autenticated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
