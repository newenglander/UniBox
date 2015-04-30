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
	private String configFile = "/WEB-INF/config/server.properties";

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

		// init configuration
		InternalConfig.load(context.getResourceAsStream(configFile));
		this.log.info(ConfigListener.class.getSimpleName()
				+ ": Configuration loaded from: " + configFile);
	}

}