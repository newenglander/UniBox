package de.unibox.http.servlet.listener;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class DatabaseListener is a Listener which init() the Database
 * functionality on startup.
 *
 */
public class DatabaseListener implements ServletContextListener {

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

        try {
            this.log.info(DatabaseListener.class.getSimpleName()
                    + ": Database initialization..");
            DatabaseQuery.init();
        } catch (final SQLException e) {
            if (InternalConfig.isLogDatabase()) {
                this.log.warn(DatabaseListener.class.getSimpleName()
                        + ": Database initialization failed!");
            }
            e.printStackTrace();
        }

    }

}