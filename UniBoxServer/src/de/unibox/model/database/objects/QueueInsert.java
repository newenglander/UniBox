package de.unibox.model.database.objects;

import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class QueueInsert.
 */
public class QueueInsert extends DatabaseAction<Integer> {

    /** The game id. */
    private Integer gameID = null;

    /** The player id. */
    private Integer playerID = null;

    /**
     * Instantiates a new queue insert.
     *
     * @param thisPlayerID
     *            the this player id
     * @param thisGameID
     *            the this game id
     */
    public QueueInsert(final Integer thisPlayerID, final Integer thisGameID) {
        super(
                "INSERT INTO `unibox`.`Game` (`GameName`, `CatID`) VALUES (?, ?);");
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
        super.getStatement().setInt(1, this.playerID);
        super.getStatement().setInt(2, this.gameID);
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
