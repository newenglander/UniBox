package de.unibox.http.servlet.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The LogListener Class is able to provide logging features even in JSPs.
 *
 */
public class LogListener implements ServletContextListener {

	/** The config file. */
	private String configFile = "/WEB-INF/config/log4j.properties";

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

		PropertyConfigurator.configure(context.getResourceAsStream(configFile));
		this.log.info(LogListener.class.getSimpleName()
				+ ": Logging started for application: " + configFile);

	}

}