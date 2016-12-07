package coe.dronsys.main.app;

import coe.dronsys.main.pages.about.WPAbout;
import coe.dronsys.main.pages.blog.WPBlogEntry;
import coe.dronsys.main.pages.contact.WPContact;
import coe.dronsys.main.pages.etc.WPAdmin;
import coe.dronsys.main.pages.help.WPHelp;
import coe.dronsys.main.pages.home.WPHome;
import coe.dronsys.main.pages.login.WPLogin;
import coe.dronsys.main.pages.login.WPLogout;
import coe.dronsys.main.pages.notFound.WP404;
import coe.dronsys.main.pages.shop.WPShop;
import coe.dronsys.main.pages.shop.WPShoppingCart;
import coe.dronsys.main.pages.user.WPSignup;
import coe.dronsys.test.var.WPTestAdmin;
import coe.wuti.http.WebXML;
import coe.wuti.wicket.core.MyRequestCycleListener;
import coe.wuti.wicket.core.MySessionListener;
import coe.wuti.wicket.core.NoSerializationPageManagerProvider;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.devutils.stateless.StatelessChecker;
import org.apache.wicket.protocol.http.CsrfPreventionRequestCycleListener;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.RequestCycleSettings;

import java.io.Serializable;

/**
 * User: jrubio
 * Date: 2/06/14
 */
public class App extends WebApplication implements Serializable {

    @Override
    protected void init() {
        super.init();

        // Access denied page
        getApplicationSettings().setAccessDeniedPage(WPLogin.class);

        // Pages will be UTF-8 encoded
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");

        // Markup settings
        if (getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT) {
            getMarkupSettings().setStripComments(true);
            getMarkupSettings().setStripWicketTags(true);
            getMarkupSettings().setCompressWhitespace(true);
        }

        // Pages: no page serialization and one page rendering
        setPageManagerProvider(new NoSerializationPageManagerProvider(this));
        getRequestCycleSettings().setRenderStrategy(RequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);

        //Internal errors
        if (getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT) {
            getApplicationSettings().setAccessDeniedPage(getHomePage());
            getApplicationSettings().setInternalErrorPage(getHomePage());
            getApplicationSettings().setPageExpiredErrorPage(getHomePage());
        }

        // Ajax debug
        getDebugSettings().setAjaxDebugModeEnabled(WebXML.<Boolean>readParameter("ajaxWicketDebug"));

        // General protection against CSRF (via Origin field)
        getRequestCycleListeners().add(new CsrfPreventionRequestCycleListener());

        // Mounted pages
        mountPage("/home", getHomePage());
        mountPage("/shop", WPShop.class);
        mountPage("/shop/cart", WPShoppingCart.class);
        mountPage("/blog", WPBlogEntry.class);
        mountPage("/contact", WPContact.class);
        mountPage("/about", WPAbout.class);
        mountPage("/admin", WPAdmin.class);
        mountPage("/login", WPLogin.class);
        mountPage("/logout", WPLogout.class);
        mountPage("/signup", WPSignup.class);
        mountPage("/test/admin", WPTestAdmin.class);
        mountPage("/help", WPHelp.class);
        mountPage("/notFound", WP404.class);

        // Development utils: Check stateless (works in combination with @StatelessComponent), listen to request cycle, session, etc...
        if (getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
            getComponentPostOnBeforeRenderListeners().add(new StatelessChecker());
            getRequestCycleListeners().add(new MyRequestCycleListener());
            getSessionListeners().add(new MySessionListener());
        }
    }


    @Override
    public Session newSession(Request request, Response response) {
        return new DronsysSession(request);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return WPHome.class;
    }
}

