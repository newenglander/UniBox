package de.unibox.http.servlet.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.http.servlet.comet.ClientType;
import de.unibox.model.user.AbstractUser;

/**
 * The Class HttpSessionCollector is a Listener which is able to register every
 * available session.
 */
public class HttpSessionCollector implements HttpSessionListener {

	/** The log. */
	protected static Logger log = Logger.getLogger("UniBoxLogger");

	/** The Constant sessions. */
	private static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

	/**
	 * This Function is not working.. ClientTypes are not (!) stored in
	 * session..
	 *
	 * @param clientType
	 *            the client type
	 * @return the array list
	 */
	@Deprecated
	public static ArrayList<HttpSession> findByClientType(
			final ClientType clientType) {
		if (InternalConfig.isLogAuthentification()) {
			HttpSessionCollector.log.debug(HttpSessionCollector.class
					.getSimpleName()
					+ ": tries to find session by ClientType: " + clientType);
		}
		final ArrayList<HttpSession> returnThis = new ArrayList<HttpSession>();
		/*
		 * much faster then iterating using iterator or a foreach on entrySet().
		 */
		for (final HttpSession session : HttpSessionCollector.sessions.values()) {

			final ClientType thisClientType = (ClientType) session
					.getAttribute("format");
			if (clientType == thisClientType) {
				returnThis.add(session);
			}
		}
		return returnThis;
	}

	/**
	 * Find by id.
	 *
	 * @param sessionId
	 *            the session id
	 * @return the http session
	 */
	public static HttpSession findById(final String sessionId) {
		if (InternalConfig.isLogAuthentification()) {
			HttpSessionCollector.log
					.debug("HttpSessionListener tries to find session by id: "
							+ sessionId);
		}
		return HttpSessionCollector.sessions.get(sessionId);
	}

	/**
	 * Find by user name.
	 *
	 * @param name
	 *            the name
	 * @return the http session
	 */
	public static HttpSession findByUserName(final String name) {
		if (InternalConfig.isLogAuthentification()) {
			HttpSessionCollector.log
					.debug("HttpSessionListener tries to find session of user: "
							+ name);
		}
		HttpSession returnThis = null;
		/*
		 * much faster then iterating using iterator or a foreach on entrySet().
		 */
		for (final HttpSession session : HttpSessionCollector.sessions.values()) {
			final AbstractUser user = (AbstractUser) session
					.getAttribute("login.object");
			if (user.getName().equalsIgnoreCase(name)) {
				returnThis = session;
			}
		}
		return returnThis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http
	 * .HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(final HttpSessionEvent event) {
		if (InternalConfig.isLogAuthentification()) {
			HttpSessionCollector.log
					.debug("HttpSessionListener creating new Session");
		}
		final HttpSession session = event.getSession();
		HttpSessionCollector.sessions.put(session.getId(), session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet
	 * .http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(final HttpSessionEvent event) {
		if (InternalConfig.isLogAuthentification()) {
			HttpSessionCollector.log
					.debug("HttpSessionListener destroying Session");
		}
		HttpSessionCollector.sessions.remove(event.getSession().getId());
	}
}