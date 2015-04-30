package de.unibox.model.user;

/**
 * The Class AdministratorUser.
 */
public class AdministratorUser extends AbstractUser {

	/**
	 * Instantiates a new administrator user.
	 *
	 * @param sessionId
	 *            the session id
	 */
	public AdministratorUser(final String sessionId) {
		super(UserType.ADMINISTRATOR, sessionId);
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public UserType getRole() {
		return null;
	}

}
