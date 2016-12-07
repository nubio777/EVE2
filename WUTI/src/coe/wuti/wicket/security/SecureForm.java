package coe.wuti.wicket.security;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.string.StringValue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by jesus on 2/10/16.
 */
public class SecureForm<T> extends StatelessForm<T> {
    private final String COOKIE_NAME = "csrfCookie";
    private static final String HIDDEN_FIELD_NAME = "csrfField";

    private String hiddenFieldId = "";

    private CookieManager cookieManager = new CookieManager();

    public SecureForm(String id) {
        super(id);
        init() ;
    }

    public SecureForm(String id, IModel<T> model) {
        super(id, model);
        init() ;
    }

    private static int idNumber = 1 ;
    private void init () {
        hiddenFieldId = "csrfField_" + idNumber ;
        idNumber ++ ;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        cookieManager.setCookie();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // For IE6
        response.render(StringHeaderItem.forString("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />"));

        // Appends javascript so that secure value is transferred from cookie to hidden field.
        // Important: requeries js function getCookie (defined in cookieHandling.js)
        String valueTransfer = "document.getElementById('" + hiddenFieldId + "').value=getCookie('" + COOKIE_NAME + "'); ";
        response.render(OnDomReadyHeaderItem.forScript(valueTransfer));
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        super.onComponentTagBody(markupStream, openTag);

        // Appends hidden field to this form
        if (isRootForm()) {
            Response response = getRequestCycle().getResponse();
            response.write("<div style=\"display:none\"><input type=\"hidden\" id=\"" + hiddenFieldId + "\"  name=\"" + HIDDEN_FIELD_NAME + "\" /></div>");
        }
    }

    @Override
    protected void onValidate() {

        // Key point: check the token in the hidden field value. This guards against CSRF attacks.
        StringValue hiddenFieldValue = getRequest().getPostParameters().getParameterValue(HIDDEN_FIELD_NAME);
        if (!cookieManager.isValid(hiddenFieldValue.toString()))
            error("Attempted unauthorized form submission");

        // Validate any field AFTER checking for CSRF
        super.onValidate();
    }

    private class CookieManager {
        private String value;

        void setCookie() {
            // First check wether there is already a given cookie
            WebRequest webRequest = (WebRequest) RequestCycle.get().getRequest();
            Cookie cookie = webRequest.getCookie(COOKIE_NAME);

            // If no cookie, then set it
            value = getSecureValue();
            cookie = new Cookie(COOKIE_NAME, value);
            cookie.setPath("/");

            // Decide wether the cookie should be secure
            HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getContainerRequest();
            cookie.setSecure(httpServletRequest.isSecure());

            // Add the cookie to the response
            WebResponse webResponse = (WebResponse) RequestCycle.get().getResponse();
            webResponse.addCookie(cookie);
        }

        boolean isValid(String s) {
            return s != null && s.equals(value);
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
}


