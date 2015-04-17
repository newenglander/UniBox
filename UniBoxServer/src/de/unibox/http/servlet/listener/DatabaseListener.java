package de.unibox.http.servlet.listener;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.unibox.config.InternalConfig;
import de.unibox.model.database.DatabaseQuery;

/**
 * The listener interface for receiving database events. The class that is
 * interested in processing a database event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addDatabaseListener<code> method. When
 * the database event occurs, that object's appropriate
 * method is invoked. This class is listening to all jsps able to redirect any
 * loggings to several targets using log4j.
 *
 * @see DatabaseEvent
 */
public class DatabaseListener implements ServletContextListener {

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
            InternalConfig.log.info(DatabaseListener.class.getSimpleName()
                    + ": Database initialization..");
            DatabaseQuery.init();
        } catch (final SQLException e) {
            if (InternalConfig.LOG_DATABASE) {
                InternalConfig.log.warn(DatabaseListener.class.getSimpleName()
                        + ": Database initialization failed!");
            }
            e.printStackTrace();
        }

    }

}