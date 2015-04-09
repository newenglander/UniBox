package de.alextape.tictactoe;

import java.util.Arrays;

/**
 * The Class Model.
 */
public class Model {

    /** The move counter. */
    private int moveCounter;

    /** The player1. */
    private String player1;

    /** The player1color. */
    private String player1color;

    /** The player2. */
    private String player2;

    /** The player2color. */
    private String player2color;

    /** The player colors. */
    private String[] playerColors = { "green", "blue", "grey", "black", "red" };

    /** The title. */
    private String title;

    /** The win combinations. */
    private String[] winCombinations = {
            // horizontal lines
            "x00x00x00", "0x00x00x0", "00x00x00x",
            // vertical lines
            "xxx000000", "000xxx000", "000000xxx",
            // diagonal lines
            "x00x00x00", "x000x000x", "00x0x0x00" };

    /**
     * Gets the move counter.
     *
     * @return the move counter
     */
    public final int getMoveCounter() {
        return this.moveCounter;
    }

    /**
     * Gets the player1.
     *
     * @return the player1
     */
    public final String getPlayer1() {
        return this.player1;
    }

    /**
     * Gets the player1color.
     *
     * @return the player1color
     */
    public final String getPlayer1color() {
        return this.player1color;
    }

    /**
     * Gets the player2.
     *
     * @return the player2
     */
    public final String getPlayer2() {
        return this.player2;
    }

    /**
     * Gets the player2color.
     *
     * @return the player2color
     */
    public final String getPlayer2color() {
        return this.player2color;
    }

    /**
     * Gets the player colors.
     *
     * @return the player colors
     */
    public final String[] getPlayerColors() {
        return this.playerColors;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public final String getTitle() {
        return this.title;
    }

    /**
     * Gets the win combinations.
     *
     * @return the win combinations
     */
    public final String[] getWinCombinations() {
        return this.winCombinations;
    }

    /**
     * Checks if is draw.
     *
     * @param newMatrix
     *            the new matrix
     * @return true, if is draw
     */
    public final boolean isDraw(final String newMatrix) {
        String matrix = newMatrix;
        matrix = matrix.replace("1", "x");
        matrix = matrix.replace("2", "x");
        return matrix.indexOf("0") == -1;
    }

    /**
     * Checks if is win.
     *
     * @param matrix
     *            the matrix
     * @return the boolean[]
     */
    public final boolean[] isWin(final String matrix) {
        final boolean user1win = (Arrays.asList(this.getWinCombinations())
                .contains(matrix.replace("1", "x").replace("2", "0")));
        final boolean user2win = (Arrays.asList(this.getWinCombinations())
                .contains(matrix.replace("2", "x").replace("1", "0")));
        return new boolean[] { user1win, user2win };
    }

    /**
     * Sets the move counter.
     *
     * @param newMoveCounter
     *            the new move counter
     */
    public final void setMoveCounter(final int newMoveCounter) {
        this.moveCounter = newMoveCounter;
    }

    /**
     * Sets the player1.
     *
     * @param newPlayer1
     *            the new player1
     */
    public final void setPlayer1(final String newPlayer1) {
        this.player1 = newPlayer1;
    }

    /**
     * Sets the player1color.
     *
     * @param newPlayer1Color
     *            the new player1color
     */
    public final void setPlayer1color(final String newPlayer1Color) {
        this.player1color = newPlayer1Color;
    }

    /**
     * Sets the player2.
     *
     * @param newPlayer2
     *            the new player2
     */
    public final void setPlayer2(final String newPlayer2) {
        this.player2 = newPlayer2;
    }

    /**
     * Sets the player2color.
     *
     * @param newPlayer2Color
     *            the new player2color
     */
    public final void setPlayer2color(final String newPlayer2Color) {
        this.player2color = newPlayer2Color;
    }

    /**
     * Sets the player colors.
     *
     * @param newPlayerColors
     *            the new player colors
     */
    public final void setPlayerColors(final String[] newPlayerColors) {
        this.playerColors = newPlayerColors;
    }

    /**
     * Sets the title.
     *
     * @param newTitle
     *            the new title
     */
    public final void setTitle(final String newTitle) {
        this.title = newTitle;
    }

    /**
     * Sets the win combinations.
     *
     * @param newWinCombinations
     *            the new win combinations
     */
    public final void setWinCombinations(final String[] newWinCombinations) {
        this.winCombinations = newWinCombinations;
    }

}
