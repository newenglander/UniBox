package de.unibox.http.servlet.beans;

/**
 * The Class AdminBean.
 */
public class UserBean extends AdminBean {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -256128157289188446L;

	/** The session. */
	private String session = null;

	/** The username. */
	private String username = null;

	/**
	 * Instantiates a new user bean.
	 */
	public UserBean() {
		super();
	}

	/**
	 * Instantiates a new user bean.
	 *
	 * @param username
	 *            the username
	 * @param session
	 *            the session
	 * @param isAdmin
	 *            the is admin
	 */
	public UserBean(final String username, final String session,
			final boolean isAdmin) {
		super(isAdmin);
		this.username = username;
		this.session = session;
	}

	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public String getSession() {
		return this.session;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

}
