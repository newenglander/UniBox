package de.unibox.http.servlet.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.unibox.config.InternalConfig;

/**
 * The LogListener Class is able to provide logging features even in JSPs.
 *
 */
public class LogListener implements ServletContextListener {

    /** The log. */
    protected Logger log = Logger.getLogger("UniBoxLogger");

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
     * .ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        
    	final ServletContext context = event.getServletContext();

        BasicConfigurator.configure();

        /* Use log4j instead of java.utility.logger */
        System.setProperty("rootPath", context.getRealPath("/"));
        final String prefix = context.getRealPath("/");
        final String file = "WEB-INF" + System.getProperty("file.separator")
                + "classes" + System.getProperty("file.separator")
                + "log4j.properties";

        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
            if (InternalConfig.isLogAuthentification()) {
                this.log.info(LogListener.class.getSimpleName()
                        + ": Logging started for application: " + prefix + file);
            }
        }

    }

}