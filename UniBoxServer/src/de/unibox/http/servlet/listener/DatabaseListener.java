package de.unibox.http.servlet.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

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
    public final void contextDestroyed(ServletContextEvent sce) {
        // deregister JDBC drivers in this context's ClassLoader:
        // Get the Context ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    log.info(this.getClass().getSimpleName()+ ": Deregistering JDBC driver {}" + driver);
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    log.error(this.getClass().getSimpleName()+ ": Error deregistering JDBC driver {}" + driver);
                    e.printStackTrace();
                }
            } else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
                log.trace(this.getClass().getSimpleName()+ ": Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader: " + driver);
            }
        }
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