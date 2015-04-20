package de.unibox.model.game;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.unibox.config.InternalConfig;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.database.objects.SelectionQuery;
import de.unibox.model.user.AbstractUser;

/**
 * The Class GameQueue.
 */
public class GamePool {

    /**
     * The Enum ScoringType.
     */
    public enum ScoringType {

        /** The win. */
        WIN(1), /** The draw. */
        DRAW(0), /** The lose. */
        LOSE(-1);

        /** The score. */
        private final int score;

        /**
         * Instantiates a new scoring type.
         *
         * @param thisScore
         *            the this score
         */
        private ScoringType(final int thisScore) {
            this.score = thisScore;
        }

        /**
         * Gets the text.
         *
         * @return the text
         */
        public int getScore() {
            return score;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return this.score + "";
        }
    }

    /** The instance. */
    private static GamePool instance;

    /**
     * Gets the instance.
     *
     * @return the instance
     */
    public static GamePool getInstance() throws IOException {
        if (GamePool.instance == null) {
            GamePool.instance = new GamePool();
        }
        return GamePool.instance;
    }

    /** The game list. */
    private final ArrayList<Game> gameList;

    /**
     * Instantiates a new game pool.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private GamePool() throws IOException {
        if (InternalConfig.LOG_GAMEPOOL) {
            InternalConfig.log.debug(this.getClass().getSimpleName()
                    + ": constructor()");
        }
        this.gameList = new ArrayList<Game>();
        this.initialize();
    }

    /**
     * Adds the game to pool.
     *
     * @param game
     *            the game
     */
    private void addGameToPool(final Game game) {
        this.gameList.add(game);
    }

    /**
     * Are members.
     *
     * @param sender
     *            the sender
     * @param receiver
     *            the receiver
     * @return true, if successful
     */
    public boolean areMembers(final AbstractUser sender,
            final AbstractUser receiver) {
        boolean returnThis = false;
        for (final Game game : this.gameList) {
            final ArrayList<AbstractUser> playerList = game.getPlayerList();
            if (!playerList.isEmpty()) {
                if (playerList.contains(sender)
                        && playerList.contains(receiver)) {
                    returnThis = true;
                    break;
                } else {
                    continue;
                }
            }
        }
        return returnThis;
    }

    /**
     * Gets the game.
     *
     * @param gameId
     *            the game id
     * @return the game
     */
    public Game getGame(final int gameId) {
        Game returnThis = null;
        for (final Game game : this.gameList) {
            if (game.getGameId() == gameId) {
                returnThis = game;
                break;
            } else {
                continue;
            }
        }
        return returnThis;
    }

    /**
     * Gets the game by player.
     *
     * @param player
     *            the player
     * @return the game by player
     */
    public Game getGameByPlayer(final AbstractUser player) {
        Game returnThis = null;
        for (final Game game : this.gameList) {
            final ArrayList<AbstractUser> playerList = game.getPlayerList();
            if (!playerList.isEmpty()) {
                if (playerList.contains(player)) {
                    returnThis = game;
                    break;
                } else {
                    continue;
                }
            }
        }
        return returnThis;
    }

    /**
     * Gets the game list.
     *
     * @return the game list
     */
    public ArrayList<Game> getGameList() {
        return this.gameList;
    }

    /**
     * Initialize.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void initialize() throws IOException {

        if (InternalConfig.LOG_GAMEPOOL) {
            InternalConfig.log.debug(this.getClass().getSimpleName()
                    + ": initializing..");
        }

        if (InternalConfig.LOG_DATABASE) {
            InternalConfig.log.debug(this.getClass().getSimpleName()
                    + ": select game table..");
        }

        final SelectionQuery query = new SelectionQuery(
                "SELECT GameID, GameName, Gametitle, NumberOfPlayers FROM game INNER JOIN category WHERE game.CatID=category.CatID;");

        try {

            final DatabaseQuery transaction = new DatabaseQuery();
            transaction.connect();
            query.attach(transaction);

            final ResultSet resultSet = query.execute();
            while (resultSet.next()) {
                final int gameId = resultSet.getInt("GameID");
                final String gameName = resultSet.getString("GameName");
                final String gameTitle = resultSet.getString("GameTitle");
                final int numberOfPlayers = resultSet.getInt("NumberOfPlayers");
                this.addGameToPool(new Game(gameId, gameName, gameTitle,
                        numberOfPlayers));
            }
            transaction.commit();

            if (InternalConfig.LOG_DATABASE) {
                InternalConfig.log.debug(this.getClass().getSimpleName()
                        + ": game pool loaded and available!");
            }

        } catch (final SQLException e) {
            if (InternalConfig.LOG_DATABASE) {
                InternalConfig.log.debug(this.getClass().getSimpleName()
                        + ": Could not query game table.");
            }
            e.printStackTrace();
            throw new IOException("Could not connect to Database!");
        }

    }

    /**
     * Update.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void update() throws IOException {
        this.initialize();
    }

}
