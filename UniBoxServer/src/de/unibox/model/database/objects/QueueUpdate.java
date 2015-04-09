package de.unibox.model.database.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class QueueUpdate.
 */
public class QueueUpdate extends DatabaseAction<Integer> {

    /** The game id. */
    private Integer gameID = null;

    /** The player id. */
    private Integer playerID = null;

    /**
     * Instantiates a new queue update.
     *
     * @param thisStatement
     *            the this statement
     * @param thisPlayerID
     *            the this player id
     * @param thisGameID
     *            the this game id
     */
    public QueueUpdate(final PreparedStatement thisStatement,
            final Integer thisPlayerID, final Integer thisGameID) {
        super(thisStatement);
        this.playerID = thisPlayerID;
        this.gameID = thisGameID;
    }

    /**
     * Instantiates a new queue update.
     *
     * @param thisSqlString
     *            the this sql string
     * @param thisPlayerID
     *            the this player id
     * @param thisGameID
     *            the this game id
     */
    public QueueUpdate(final String thisSqlString, final Integer thisPlayerID,
            final Integer thisGameID) {
        super(thisSqlString);
        this.playerID = thisPlayerID;
        this.gameID = thisGameID;
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

    public final void setGameID(final Integer gameID) {
        this.gameID = gameID;
    }

    public final void setPlayerID(final Integer playerID) {
        this.playerID = playerID;
    }

}
