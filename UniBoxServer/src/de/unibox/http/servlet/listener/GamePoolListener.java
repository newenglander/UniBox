package de.unibox.http.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.unibox.model.game.GamePool;

/**
 * The listener interface for receiving gamePool events. The class that is
 * interested in processing a gamePool event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addGamePoolListener<code> method. When
 * the gamePool event occurs, that object's appropriate
 * method is invoked.
 *
 * @see GamePoolEvent
 */
public class GamePoolListener implements ServletContextListener {

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
        GamePool.getInstance();

    }

}