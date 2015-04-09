package de.unibox.model.database.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class GameUpdate.
 */
public class GameUpdate extends DatabaseAction<Integer> {

    /** The cat id. */
    private Integer catID = null;

    /** The game name. */
    private String gameName = null;

    /**
     * Instantiates a new game update.
     *
     * @param thisStatement
     *            the this statement
     * @param thisGameName
     *            the this game name
     * @param thisCatID
     *            the this cat id
     */
    public GameUpdate(final PreparedStatement thisStatement,
            final String thisGameName, final int thisCatID) {
        super(thisStatement);
        this.gameName = thisGameName;
        this.catID = thisCatID;
    }

    /**
     * Instantiates a new game update.
     *
     * @param thisSqlString
     *            the this sql string
     * @param thisGameName
     *            the this game name
     * @param thisCatID
     *            the this cat id
     */
    public GameUpdate(final String thisSqlString, final String thisGameName,
            final int thisCatID) {
        super(thisSqlString);
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
