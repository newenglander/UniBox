package de.unibox.model.user;

import de.unibox.config.InternalConfig;

/**
 * The Class AbstractUser.
 */
public abstract class AbstractUser {

    /** The name. */
    private String name;

    /** The player id. */
    private int playerId;

    /** The session id. */
    private String sessionId;

    /** The type. */
    private UserType type;

    /**
     * Instantiates a new abstract user.
     *
     * @param thisType
     *            the this type
     * @param thisSessionId
     *            the this session id
     */
    public AbstractUser(final UserType thisType, final String thisSessionId) {
        if (InternalConfig.LOG_AUTHENTIFICATION) {
            InternalConfig.log.debug(this.getClass().getSimpleName() + ": AbstractUser Type: "
                    + thisType + " SessionID: " + this.sessionId);
        }
        this.sessionId = thisSessionId;
        this.type = thisType;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * Gets the session id.
     *
     * @return the session id
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public UserType getType() {
        return this.type;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    public void setPlayerId(final int playerId) {
        this.playerId = playerId;
    }

    /**
     * Sets the session id.
     *
     * @param thisSessionId
     *            the new session id
     */
    public void setSessionId(final String thisSessionId) {
        this.sessionId = thisSessionId;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(final UserType type) {
        this.type = type;
    }

}
