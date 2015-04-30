package de.unibox.http.servlet.filter;

import de.unibox.config.InternalConfig;

/**
 * The Enum SecurityLevel. Essential enum to identify and relate userRoles to
 * privilegs of the db connections they get out of the relevant pool.
 */
public enum SecurityLevel {

	/** The administrator. ATM not implemented properly. */
	ADMINISTRATOR(null, "", 5, SecurityWrapper.ADMINISTRATOR_LEVEL),

	/** The user. */
	USER(InternalConfig.getDbUser(), "", 15, SecurityWrapper.USER_LEVEL);

	/** The connection count. */
	private final int connectionCount;

	/** The descriptor. */
	private final String descriptor;

	/** The password. */
	private final String password;

	/** The user. */
	private final String user;

	/**
	 * Instantiates a new security level.
	 *
	 * @param user
	 *            the user
	 * @param password
	 *            the password
	 * @param connectionCount
	 *            the connection count
	 * @param descriptor
	 *            the descriptor
	 */
	private SecurityLevel(final String user, final String password,
			final int connectionCount, final String descriptor) {
		this.user = user;
		this.password = password;
		this.connectionCount = connectionCount;
		this.descriptor = descriptor;
	}

	/**
	 * Gets the connection count.
	 *
	 * @return the connection count
	 */
	synchronized public int getConnectionCount() {
		return this.connectionCount;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	synchronized public String getPassword() {
		return this.password;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	synchronized public String getUser() {
		return this.user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	synchronized public String toString() {
		return this.descriptor;
	}
}