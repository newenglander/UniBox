package de.unibox.client.events;

/**
 * The Enum RequestType.
 */
public enum RequestType {

	/** The method get. */
	METHOD_GET("GET"),

	/** The method post. */
	METHOD_POST("POST");

	/** The text. */
	private final String text;

	/**
	 * Instantiates a new request type.
	 *
	 * @param text
	 *            the text
	 */
	private RequestType(final String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.text;
	}
}