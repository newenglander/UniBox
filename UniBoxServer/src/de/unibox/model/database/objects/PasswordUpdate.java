package de.unibox.model.database.objects;

import java.sql.SQLException;

import de.unibox.core.provider.Helper;
import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class PasswordUpdate.
 */
public class PasswordUpdate extends DatabaseAction<Integer> {

    /** The input password confirm m d5. */
    private String inputPasswordConfirmMD5 = null;

    /** The input password m d5. */
    private String inputPasswordMD5 = null;

    /** The old password m d5. */
    private String oldPasswordMD5 = null;

    /** The player id. */
    private Integer playerId = null;

    /**
     * Instantiates a new password update.
     *
     * @param thisNick
     *            the this nick
     * @param thisOldPassword
     *            the this old password
     * @param thisInputPassword
     *            the this input password
     * @param thisInputPasswordConfirm
     *            the this input password confirm
     */
    public PasswordUpdate(final Integer thisNick, final String thisOldPassword,
            final String thisInputPassword,
            final String thisInputPasswordConfirm) {
        super("UPDATE player SET Password=? WHERE PlayerID=? AND Password=?");
        this.playerId = thisNick;
        this.oldPasswordMD5 = Helper.md5(Helper.decodeBase64(thisOldPassword));
        this.inputPasswordMD5 = Helper.md5(Helper
                .decodeBase64(thisInputPassword));
        this.inputPasswordConfirmMD5 = Helper.md5(Helper
                .decodeBase64(thisInputPasswordConfirm));
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
        System.out.println(this);
        if (this.inputPasswordMD5.equals(this.inputPasswordConfirmMD5)) {
            super.getStatement().setString(1, this.inputPasswordMD5);
            super.getStatement().setInt(2, this.playerId);
            super.getStatement().setString(3, this.oldPasswordMD5);
        }
        return super.executeUpdate();
    }

    public String getInputPasswordConfirmMD5() {
        return this.inputPasswordConfirmMD5;
    }

    public String getInputPasswordMD5() {
        return this.inputPasswordMD5;
    }

    public String getOldPasswordMD5() {
        return this.oldPasswordMD5;
    }

    public Integer getPlayerId() {
        return this.playerId;
    }

    public void setInputPasswordConfirmMD5(final String inputPasswordConfirmMD5) {
        this.inputPasswordConfirmMD5 = inputPasswordConfirmMD5;
    }

    public void setInputPasswordMD5(final String inputPasswordMD5) {
        this.inputPasswordMD5 = inputPasswordMD5;
    }

    public void setOldPasswordMD5(final String oldPasswordMD5) {
        this.oldPasswordMD5 = oldPasswordMD5;
    }

    public void setPlayerId(final Integer playerId) {
        this.playerId = playerId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PasswordUpdate [playerId=" + this.playerId
                + ", oldPasswordMD5=" + this.oldPasswordMD5
                + ", inputPasswordMD5=" + this.inputPasswordMD5
                + ", inputPasswordConfirmMD5=" + this.inputPasswordConfirmMD5
                + "]";
    }

}