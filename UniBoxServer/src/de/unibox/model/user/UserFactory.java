package de.unibox.model.user;

import java.util.ArrayList;

/**
 * A factory for creating User objects.
 */
public class UserFactory {

    /** The user list. */
    private static ArrayList<AbstractUser> userList = new ArrayList<AbstractUser>();

    /**
     * Creates a new User object.
     *
     * @param thisType
     *            the this type
     * @param thisSessionId
     *            the this session id
     * @return the abstract user
     */
    public static AbstractUser createUser(final UserType thisType,
            final String thisSessionId) {

        AbstractUser user = null;
        switch (thisType) {
        case REGISTERED:
            user = new RegisteredUser(thisSessionId);
            UserFactory.userList.add(user);
            break;
        case ADMINISTRATOR:
            user = new AdministratorUser(thisSessionId);
            UserFactory.userList.add(user);
            break;
        default:
            break;
        }

        return user;
    }

    /**
     * Gets the user by name.
     *
     * @param name
     *            the name
     * @return the user by name
     */
    public static AbstractUser getUserByName(final String name) {
        AbstractUser user = null;
        for (final AbstractUser userObj : UserFactory.userList) {
            if (userObj.getName().equals(name)) {
                user = userObj;
            }
        }
        return user;
    }

    public static ArrayList<AbstractUser> getUserList() {
        return UserFactory.userList;
    }
}
