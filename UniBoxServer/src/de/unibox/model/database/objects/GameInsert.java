package de.unibox.model.database.objects;

import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

public class GameInsert extends DatabaseAction<Integer> {

    /** The cat id. */
    private Integer catID = null;

    /** The game name. */
    private String gameName = null;

    public GameInsert(final String thisGameName, final int thisCatID) {
        super(
                "INSERT INTO game (GameName, CatID) VALUES (?, ?);");
        this.gameName = thisGameName;
        this.catID = thisCatID;
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
        super.getStatement().setString(1, this.gameName);
        super.getStatement().setInt(2, this.catID);
        return super.executeUpdate();
    }

    public final Integer getCatID() {
        return this.catID;
    }

    public final String getGameName() {
        return this.gameName;
    }

    public final void setCatID(final Integer catID) {
        this.catID = catID;
    }

    public final void setGameName(final String gameName) {
        this.gameName = gameName;
    }

}
