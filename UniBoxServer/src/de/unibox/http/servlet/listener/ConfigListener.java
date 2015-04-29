package de.unibox.http.servlet.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;

/**
 * The LogListener Class is able to provide logging features even in JSPs.
 *
 */
public class ConfigListener implements ServletContextListener {

	/** The log. */
	protected Logger log = Logger.getLogger("UniBoxLogger");

	/** The config file. */
	private String configFile = "server.properties";

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

		System.setProperty("rootPath", context.getRealPath("/"));
		final String prefix = context.getRealPath("/");
		final String file = "WEB-INF" + System.getProperty("file.separator")
				+ "classes" + System.getProperty("file.separator") + configFile;

		if (file != null) {
			
			// init configuration
			InternalConfig.load(prefix + file);
			this.log.info(ConfigListener.class.getSimpleName()
					+ ": Configuration loaded from: " + prefix + file);
		}

	}

}