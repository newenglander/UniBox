package de.unibox.model.game;

import java.util.ArrayList;

import de.unibox.config.InternalConfig;
import de.unibox.model.user.AbstractUser;

/**
 * The Class Game.
 */
public class Game {

    /** The game id. */
    private final int gameId;

    /** The name. */
    private final String gameName;

    /** The game title. */
    private final String gameTitle;

    /** The number of players. */
    private final int numberOfPlayers;

    /** The player list. */
    private final ArrayList<AbstractUser> playerList;

    /**
     * Instantiates a new game.
     *
     * @param gameId
     *            the game id
     * @param name
     *            the name
     * @param gameTitle
     *            the game title
     * @param numberOfPlayers
     *            the number of players
     */
    public Game(final int gameId, final String name, final String gameTitle,
            final int numberOfPlayers) {
        super();
        this.gameId = gameId;
        this.gameName = name;
        this.gameTitle = gameTitle;
        this.numberOfPlayers = numberOfPlayers;
        this.playerList = new ArrayList<AbstractUser>();
    }

    /**
     * Adds the player.
     *
     * @param player
     *            the player
     * @return true, if successful
     */
    public boolean addPlayer(final AbstractUser player) {
        boolean returnThis = false;
        if (!this.playerList.contains(player)) {
            if (this.playerList.size() < this.numberOfPlayers) {
                if (InternalConfig.LOG_GAMEPOOL) {
                    InternalConfig.log.debug(this.getClass().getSimpleName()
                            + ": " + player.getName() + " joining game: "
                            + this);
                }
                this.playerList.add(player);
                returnThis = true;
            }
        }
        return returnThis;
    }

    /**
     * Gets the game id.
     *
     * @return the game id
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     * Gets the game title.
     *
     * @return the game title
     */
    public String getGameTitle() {
        return this.gameTitle;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.gameName;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    /**
     * Gets the player list.
     *
     * @return the player list
     */
    public ArrayList<AbstractUser> getPlayerList() {
        return this.playerList;
    }

    /**
     * Player to string.
     *
     * @return the string
     */
    public String playerToString() {
        final StringBuilder returnThis = new StringBuilder("");
        if (this.playerList.size() == 0) {
            returnThis.append("none");
        } else {
            int i = 1;
            for (final AbstractUser user : this.playerList) {
                if (i == this.playerList.size()) {
                    returnThis.append(user.getName());
                } else {
                    returnThis.append(user.getName() + ", ");
                }
                i++;
            }
        }
        return returnThis.toString();
    }

    /**
     * Removes the player.
     *
     * @param player
     *            the player
     * @return true, if successful
     */
    public boolean removePlayer(final AbstractUser player) {
        if (InternalConfig.LOG_GAMEPOOL) {
            InternalConfig.log.debug(this.getClass().getSimpleName() + ": "
                    + player.getName() + " leaving game: " + this);
        }
        return this.playerList.remove(player);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Game [gameId=" + this.gameId + ", gameTitle=" + this.gameTitle
                + ", gameName=" + this.gameName + ", numberOfPlayers="
                + this.numberOfPlayers + ", playerList=" + this.playerList
                + "]";
    }

}
