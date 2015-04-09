package de.unibox.http.servlet.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.pattern.LogEvent;

import de.unibox.config.InternalConfig;

/*
 * this class is listening to all jsps able to redirect any loggings to several targets using log4j
 */
/**
 * The listener interface for receiving log events. The class that is interested
 * in processing a log event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addLogListener<code> method. When
 * the log event occurs, that object's appropriate
 * method is invoked.
 *
 * @see LogEvent
 */
public class LogListener extends InternalConfig implements
        ServletContextListener {

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
            if (InternalConfig.LOG_AUTHENTIFICATION) {
                InternalConfig.log
                        .debug("LogListener: Logging started for application: "
                                + prefix + file);
            }
        }

    }

}