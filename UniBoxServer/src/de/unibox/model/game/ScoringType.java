package de.unibox.model.game;

/**
 * The Enum ScoringType.
 */
public enum ScoringType {

    /** The draw. */
    DRAW(0), /** The lose. */
    LOSE(-1), /** The win. */
    WIN(1);

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
        return this.score;
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