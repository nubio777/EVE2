package coe.lsm.servlets;

import coe.lsm.main.Configuration;
import coe.lsm.main.read.FileReader;
import coe.lsm.main.read.Reading;
import coe.lsm.main.store.Store;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by jesus on 5/5/16.
 */
public class Init implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Configuration configuration = Configuration.get() ;
        Store store = Store.get() ;

        FileReader fileReader;
        try {
            fileReader = new FileReader(configuration);
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
        fileReader.readContinuous(listener, configuration.getReadEverySeconds());

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
