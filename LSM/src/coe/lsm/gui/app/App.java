package coe.lsm.gui.app;

import coe.lsm.gui.pages.base.WPTester;
import coe.lsm.gui.pages.login.WPLogin;
import coe.lsm.gui.pages.main.WPMain;
import coe.lsm.gui.pages.main.map.resources.RMapLoad;
import coe.lsm.gui.pages.main.map.resources.RMapServicesUp;
import coe.lsm.gui.pages.main.map.resources.RMapValue;
import coe.lsm.main.Configuration;
import coe.lsm.main.read.FileReader;
import coe.lsm.main.read.Reading;
import coe.lsm.main.store.Store;
import coe.wuti.http.WebXML;
import coe.wuti.wicket.core.MyRequestCycleListener;
import coe.wuti.wicket.core.MySessionListener;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.devutils.stateless.StatelessChecker;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

import java.io.Serializable;

/**
 * User: jrubio
 * Date: 2/06/14
 */
public class App extends WebApplication implements Serializable {
    private Configuration configuration ;
    private Store store ;

    public static App get() {
        return (App) WebApplication.get();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Store getStore() {
        return store;
    }

    public LSMSession getLSMSession() {
        return LSMSession.get();
    }

    @Override
    protected void init() {
        super.init();

        // Access denied page
        getApplicationSettings().setAccessDeniedPage(WPLogin.class);

        // Pages will be UTF-8 encoded
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");

        // Markup settings
        if (isDeployment()) {
            getMarkupSettings().setStripComments(true);
            getMarkupSettings().setStripWicketTags(true);
            getMarkupSettings().setCompressWhitespace(true);
        }

        // Pages: no page serialization and one page rendering
//        setPageManagerProvider(new NoSerializationPageManagerProvider(this));
//        getStoreSettings().setMaxSizePerSession(Bytes.bytes(1)) ;
//        getPageSettings().setVersionPagesByDefault(false) ;
//        getRequestCycleSettings().setRenderStrategy(RequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);

        // Internal errors
        if (isDeployment()) {
            getApplicationSettings().setAccessDeniedPage(getHomePage());
            getApplicationSettings().setInternalErrorPage(getHomePage());
            getApplicationSettings().setPageExpiredErrorPage(getHomePage());
        }

        // Ajax debug
        getDebugSettings().setAjaxDebugModeEnabled(WebXML.<Boolean>readParameter("ajaxWicketDebug"));

        // General protection against CSRF (via origin field)
//        getRequestCycleListeners().add(new CsrfPreventionRequestCycleListener());

        // Mounted pages
        mountPage("/login", WPLogin.class);
        mountPage("/main", WPMain.class);
        if (getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT)
            mountPage("/test", WPTester.class);

        // Mount resources
        mountResource("/servicesUp/${any}", new ResourceReference("servicesUp") {
            @Override
            public IResource getResource() {
                return new RMapServicesUp();
            }
        });
        mountResource("/value/${teamNumber}/${minute}/${serviceNumber}", new ResourceReference("value") {
            @Override
            public IResource getResource() {
                return new RMapValue();
            }
        });

        // Development utils: Check stateless (works in combination with @StatelessComponent), listen to request cycle, session, etc...
        if (getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
            getComponentPostOnBeforeRenderListeners().add(new StatelessChecker());
            getRequestCycleListeners().add(new MyRequestCycleListener());
            getSessionListeners().add(new MySessionListener());
        }

        // Tests
        mountResource("/pru/${teamNumber}/${lastMinuteAvailable}", new ResourceReference("pru") {
            @Override
            public IResource getResource() {
                return new RMapLoad();
            }
        });

        // Logging
//        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
//        loggers.add(LogManager.getRootLogger());
//        for (Logger logger : loggers) {
//            logger.setLevel(Level.OFF);
//        }

        // Init reading
        initReading();
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new LSMSession(request);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return WPLogin.class;
    }

    public boolean isDevelopment() {
        return getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT;
    }

    public boolean isDeployment() {
        return getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT;
    }

    private void initReading() {
        FileReader fileReader;
        try {
            fileReader = new FileReader(getConfiguration());
        } catch (Exception e) {
            Logger.getLogger("App").error(e.getMessage());
            return;
        }

        // Listening to the readings and update renderers
        FileReader.Listener listener = new FileReader.Listener() {
            @Override
            public void newLine(Reading reading) {
                store.addReading(reading);
            }

            @Override
            public void error(String msg) {
                Logger.getLogger("App").info("error reading: " + msg);
            }

            @Override
            public void end(String msg) {
                Logger.getLogger("App").info("CRITICAL ERROR. Reading data process ends: " + msg);
            }

            @Override
            public void idle(int seconds) {
                Logger.getLogger("App").info("idle reading...");
            }
        };

        // The game is on...
        fileReader.readContinuous(listener, getConfiguration().getReadEverySeconds());
    }
}

