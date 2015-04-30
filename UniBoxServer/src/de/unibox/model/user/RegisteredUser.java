package de.unibox.model.user;

/**
 * The Class RegisteredUser.
 */
public class RegisteredUser extends AbstractUser {

	/**
	 * Instantiates a new registered user.
	 *
	 * @param sessionId
	 *            the session id
	 */
	public RegisteredUser(final String sessionId) {
		super(UserType.REGISTERED, sessionId);
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
