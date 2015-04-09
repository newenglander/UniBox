package de.unibox.model.database.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class ResultUpdate.
 */
public class ResultUpdate extends DatabaseAction<Integer> {

    /** The game id. */
    private Integer gameID = null;

    /** The player id. */
    private Integer playerID = null;

    /** The scoring. */
    private Integer scoring = null;

    /**
     * Instantiates a new result update.
     *
     * @param thisStatement
     *            the this statement
     * @param thisGameID
     *            the this game id
     * @param thisPlayerID
     *            the this player id
     * @param thisScoring
     *            the this scoring
     */
    public ResultUpdate(final PreparedStatement thisStatement,
            final Integer thisGameID, final Integer thisPlayerID,
            final Integer thisScoring) {
        super(thisStatement);
        this.gameID = thisGameID;
        this.playerID = thisPlayerID;
        this.scoring = thisScoring;
    }

    /**
     * Instantiates a new result update.
     *
     * @param thisSqlString
     *            the this sql string
     * @param thisGameID
     *            the this game id
     * @param thisPlayerID
     *            the this player id
     * @param thisScoring
     *            the this scoring
     */
    public ResultUpdate(final String thisSqlString, final Integer thisGameID,
            final Integer thisPlayerID, final Integer thisScoring) {
        super(thisSqlString);
        this.gameID = thisGameID;
        this.playerID = thisPlayerID;
        this.scoring = thisScoring;
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

    public final Integer getGameID() {
        return this.gameID;
    }

    public final Integer getPlayerID() {
        return this.playerID;
    }

    public final Integer getScoring() {
        return this.scoring;
    }

    public final void setGameID(final Integer gameID) {
        this.gameID = gameID;
    }

    public final void setPlayerID(final Integer playerID) {
        this.playerID = playerID;
    }

    public final void setScoring(final Integer scoring) {
        this.scoring = scoring;
    }

}
