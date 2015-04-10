package de.unibox.model.database.objects;

import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class CategoryInsert.
 */
public class CategoryInsert extends DatabaseAction<Integer> {

    /** The game title. */
    private String gameTitle = null;

    /** The number of players. */
    private Integer numberOfPlayers = null;

    /**
     * Instantiates a new category insert.
     *
     * @param thisGametitle
     *            the this gametitle
     * @param thisNumberOfPlayers
     *            the this number of players
     */
    public CategoryInsert(final String thisGametitle,
            final int thisNumberOfPlayers) {
        super(
                "INSERT INTO `unibox`.`Category` (`Gametitle`, `NumberOfPlayers`) VALUES (?, ?);");
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
        super.getStatement().setString(1, this.gameTitle);
        super.getStatement().setInt(2, this.numberOfPlayers);
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