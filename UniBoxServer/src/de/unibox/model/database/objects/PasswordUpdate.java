package de.unibox.model.database.objects;

import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class PasswordUpdate.
 */
public class PasswordUpdate extends DatabaseAction<Integer> {

    /** The nick. */
    private String nick = null;

    /** The password. */
    private String password = null;

    /**
     * Instantiates a new password update.
     *
     * @param thisNick
     *            the this nick
     * @param thisPassword
     *            the this password
     */
    public PasswordUpdate(final String thisNick, final String thisPassword) {
        super("UPDATE player SET Password=? WHERE PlayerID=?");
        this.nick = thisNick;
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
        super.getStatement().setString(1, this.password);
        super.getStatement().setString(2, this.nick);
        return super.executeUpdate();
    }

    public String getNick() {
        return this.nick;
    }

    public String getPassword() {
        return this.password;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}