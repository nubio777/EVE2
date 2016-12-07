package coe.dronsys.test.var;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;

import javax.servlet.http.Cookie;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by jesus on 2/5/16.
 */
public class SecureBehavior extends Behavior {
    private static String COOKIE = "secure";

    @Override
    public void afterRender(Component component) {
        super.afterRender(component);
        if (!secureCookieIsSet()) setSecureCookie();
    }

    @Override
    public void onEvent(Component component, IEvent<?> event) {
        super.onEvent(component, event);
    }

    private boolean secureCookieIsSet() {
        WebRequest webRequest = (WebRequest) RequestCycle.get().getRequest();
        Cookie secureCookie = webRequest.getCookie(COOKIE);
        return secureCookie != null && secureCookie.getValue() != null && secureCookie.getValue().trim().length() > 1;
    }

    private void setSecureCookie() {
        WebRequest webRequest = (WebRequest) RequestCycle.get().getRequest();
        Cookie secureCookie = webRequest.getCookie(COOKIE);
        if (secureCookie == null) secureCookie = new Cookie(COOKIE, getSecureValue());
        WebResponse webResponse = (WebResponse) RequestCycle.get().getResponse();
        webResponse.addCookie(secureCookie);
    }

    /**
     * Idea taken from http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     * <p>
     * This works by choosing 130 bits from a cryptographically secure random bit generator, and encoding them in base-32.
     * 128 bits is considered to be cryptographically strong, but each digit in a base 32 number can encode 5 bits,
     * so 128 is rounded up to the next multiple of 5.
     */
    private String getSecureValue() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
