package de.unibox.model.game;

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
public class GamePool extends InternalConfig {

    /** The instance. */
    private static GamePool instance;

    public static GamePool getInstance() {
        if (GamePool.instance == null) {
            GamePool.instance = new GamePool();
        }
        return GamePool.instance;
    }

    /** The game list. */
    private final ArrayList<Game> gameList;

    /**
     * Instantiates a new game pool.
     */
    private GamePool() {
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

    public ArrayList<Game> getGameList() {
        return this.gameList;
    }
    
    public void update() {
        this.initialize();
    }

    /**
     * Initialize.
     */
    private void initialize() {

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
                final String gameTitle = resultSet.getString("Gametitle");
                final int numberOfPlayers = resultSet.getInt("NumberOfPlayers");
                this.addGameToPool(new Game(gameId, gameName, gameTitle,
                        numberOfPlayers));
            }
            transaction.commit();

        } catch (final SQLException e) {
            if (InternalConfig.LOG_DATABASE) {
                InternalConfig.log.debug(this.getClass().getSimpleName()
                        + ": Could not query game table.");
            }
            e.printStackTrace();
        } finally {
            if (InternalConfig.LOG_DATABASE) {
                InternalConfig.log.debug(this.getClass().getSimpleName()
                        + ": game pool loaded..");
            }
        }

        if (InternalConfig.LOG_GAMEPOOL) {
            InternalConfig.log.debug(this.getClass().getSimpleName()
                    + ": available!");
        }

    }

}
