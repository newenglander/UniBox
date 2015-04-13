package de.unibox.model.database.objects;

import java.sql.SQLException;

import de.unibox.core.provider.Helper;
import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class PasswordUpdate.
 */
public class PasswordUpdate extends DatabaseAction<Integer> {

    private Integer playerId = null;

    private String oldPasswordMD5 = null;
    private String inputPasswordMD5 = null;
    private String inputPasswordConfirmMD5 = null;

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

    @Override
    public String toString() {
        return "PasswordUpdate [playerId=" + playerId + ", oldPasswordMD5="
                + oldPasswordMD5 + ", inputPasswordMD5=" + inputPasswordMD5
                + ", inputPasswordConfirmMD5=" + inputPasswordConfirmMD5 + "]";
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
        if (inputPasswordMD5.equals(inputPasswordConfirmMD5)) {
            super.getStatement().setString(1, inputPasswordMD5);
            super.getStatement().setInt(2, this.playerId);
            super.getStatement().setString(3, oldPasswordMD5);
        }
        return super.executeUpdate();
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getOldPasswordMD5() {
        return oldPasswordMD5;
    }

    public void setOldPasswordMD5(String oldPasswordMD5) {
        this.oldPasswordMD5 = oldPasswordMD5;
    }

    public String getInputPasswordMD5() {
        return inputPasswordMD5;
    }

    public void setInputPasswordMD5(String inputPasswordMD5) {
        this.inputPasswordMD5 = inputPasswordMD5;
    }

    public String getInputPasswordConfirmMD5() {
        return inputPasswordConfirmMD5;
    }

    public void setInputPasswordConfirmMD5(String inputPasswordConfirmMD5) {
        this.inputPasswordConfirmMD5 = inputPasswordConfirmMD5;
    }

}