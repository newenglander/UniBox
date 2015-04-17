package de.unibox.model.database.objects;

import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.user.AbstractUser;
import de.unibox.model.user.AdministratorUser;

/**
 * The Class PasswordUpdate.
 */
public class AdminStatement extends DatabaseAction<Integer> {

    /** The user. */
    private AbstractUser user = null;

    /**
     * Instantiates a new admin statement.
     *
     * @param thisUser
     *            the this user
     * @param thisStatement
     *            the this statement
     */
    public AdminStatement(final AbstractUser thisUser,
            final String thisStatement) {
        super(thisStatement);
        this.user = thisUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unibox.model.database.DatabaseAction#attach(de.unibox.model.database
     * .DatabaseQuery)
     */
    @Override
    public void attach(final DatabaseQuery transaction) throws SQLException {
        // not needed here
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.unibox.model.database.DatabaseAction#execute()
     */
    @Override
    public Integer execute() throws SQLException, IllegalAccessError {
        if (this.user != null) {
            if (this.user instanceof AdministratorUser) {
                return super.executeUpdate();
            } else {
                this.noPrivilegs("no admin privilegs for: "
                        + super.getStatement());
            }
        } else {
            this.noPrivilegs("user is NULL for: " + super.getStatement());
        }
        return null;
    }

    /**
     * No privilegs.
     *
     * @param errorMessage
     *            the error message
     */
    private void noPrivilegs(final String errorMessage) {
        throw new IllegalAccessError(errorMessage);
    }

}