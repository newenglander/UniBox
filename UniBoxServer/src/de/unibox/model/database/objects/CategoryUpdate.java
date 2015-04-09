package de.unibox.model.database.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class CategoryUpdate.
 */
public class CategoryUpdate extends DatabaseAction<Integer> {

    /** The game title. */
    private String gameTitle = null;

    /** The number of players. */
    private Integer numberOfPlayers = null;

    /**
     * Instantiates a new category update.
     *
     * @param thisStatement
     *            the this statement
     * @param thisGametitle
     *            the this gametitle
     * @param thisNumberOfPlayers
     *            the this number of players
     */
    public CategoryUpdate(final PreparedStatement thisStatement,
            final String thisGametitle, final int thisNumberOfPlayers) {
        super(thisStatement);
        this.gameTitle = thisGametitle;
        this.numberOfPlayers = thisNumberOfPlayers;
    }

    /**
     * Instantiates a new category update.
     *
     * @param thisSqlString
     *            the this sql string
     * @param thisGametitle
     *            the this gametitle
     * @param thisNumberOfPlayers
     *            the this number of players
     */
    public CategoryUpdate(final String thisSqlString,
            final String thisGametitle, final int thisNumberOfPlayers) {
        super(thisSqlString);
        this.gameTitle = thisGametitle;
        this.numberOfPlayers = thisNumberOfPlayers;
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

    public final String getGameTitle() {
        return this.gameTitle;
    }

    public final Integer getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public final void setGameTitle(final String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public final void setNumberOfPlayers(final Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

}