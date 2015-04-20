package de.unibox.model.database.objects;

import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class GameInsert.
 */
public class GameInsert extends DatabaseAction<Integer> {

    /** The cat id. */
    private Integer catID = null;

    /** The game name. */
    private String gameName = null;

    /**
     * Instantiates a new game insert.
     *
     * @param thisGameName
     *            the this game name
     * @param thisCatID
     *            the this cat id
     */
    public GameInsert(final String thisGameName, final int thisCatID) {
        super("INSERT INTO game (GameName, CatID) VALUES (?, ?);");
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

    /**
     * Gets the cat id.
     *
     * @return the cat id
     */
    public final Integer getCatID() {
        return this.catID;
    }

    /**
     * Gets the game name.
     *
     * @return the game name
     */
    public final String getGameName() {
        return this.gameName;
    }

    /**
     * Sets the cat id.
     *
     * @param catID
     *            the new cat id
     */
    public final void setCatID(final Integer catID) {
        this.catID = catID;
    }

    /**
     * Sets the game name.
     *
     * @param gameName
     *            the new game name
     */
    public final void setGameName(final String gameName) {
        this.gameName = gameName;
    }

}
