package de.unibox.model.database.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class PlayerUpdate.
 */
public class PlayerUpdate extends DatabaseAction<Integer> {

    /** The admin rights. */
    private Integer adminRights = null;

    /** The name. */
    private String name = null;

    /** The password. */
    private String password = null;

    /**
     * Instantiates a new player update.
     *
     * @param thisStatement
     *            the this statement
     * @param thisAdminRights
     *            the this admin rights
     * @param thisName
     *            the this name
     * @param thisPassword
     *            the this password
     */
    public PlayerUpdate(final PreparedStatement thisStatement,
            final Integer thisAdminRights, final String thisName,
            final String thisPassword) {
        super(thisStatement);
        this.adminRights = thisAdminRights;
        this.name = thisName;
        this.password = thisPassword;
    }

    /**
     * Instantiates a new player update.
     *
     * @param thisSqlString
     *            the this sql string
     * @param thisAdminRights
     *            the this admin rights
     * @param thisName
     *            the this name
     * @param thisPassword
     *            the this password
     */
    public PlayerUpdate(final String thisSqlString,
            final Integer thisAdminRights, final String thisName,
            final String thisPassword) {
        super(thisSqlString);
        this.adminRights = thisAdminRights;
        this.name = thisName;
        this.password = thisPassword;
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
        super.attachUpdate(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.unibox.model.database.DatabaseAction#execute()
     */
    @Override
    public Integer execute() throws SQLException {
        return super.executeUpdate();
    }

    public final Integer getAdminRights() {
        return this.adminRights;
    }

    public final String getName() {
        return this.name;
    }

    public final String getPassword() {
        return this.password;
    }

    public final void setAdminRights(final Integer adminRights) {
        this.adminRights = adminRights;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final void setPassword(final String password) {
        this.password = password;
    }

}
