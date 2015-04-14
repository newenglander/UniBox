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

    private AbstractUser user = null;

    public AdminStatement(AbstractUser thisUser, String thisStatement) {
        super(thisStatement);
        this.user = thisUser;
    }

    @Override
    public void attach(DatabaseQuery transaction) throws SQLException {
        // not needed here
    }

    @Override
    public Integer execute() throws SQLException, IllegalAccessError {
        if (user != null) {
            if (user instanceof AdministratorUser) {
                return super.executeUpdate();
            } else {
                noPrivilegs("no admin privilegs for: " + super.getStatement());
            }
        } else {
            noPrivilegs("user is NULL for: " + super.getStatement());
        }
        return null;
    }

    private void noPrivilegs(String errorMessage) {
        throw new IllegalAccessError(errorMessage);
    }

}