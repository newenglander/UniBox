package de.unibox.http.servlet.listener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.model.game.GamePool;

/**
 * The Class GamePoolListener is a Listener which init() the GamePool
 * functionality on startup.
 *
 */
public class GamePoolListener implements ServletContextListener {

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

		// initialize game pool singleton
		try {
			this.log.info(GamePoolListener.class.getSimpleName()
					+ ": GamePool initialization..");
			GamePool.getInstance();
		} catch (final IOException e) {
			if (InternalConfig.isLogDatabase()) {
				this.log.warn(GamePoolListener.class.getSimpleName()
						+ ": GamePool initialization failed!");
			}
			e.printStackTrace();
		}

	}

}