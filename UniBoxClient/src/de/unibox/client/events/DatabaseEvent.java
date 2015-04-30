package de.unibox.client.events;

/**
 * The Class DatabaseEvent.
 */
public class DatabaseEvent {

	/** The parameter. */
	private String parameter = null;

	/** The request type. */
	private RequestType requestType = null;

	/**
	 * Instantiates a new database event.
	 *
	 * @param requestType
	 *            the request type
	 * @param parameter
	 *            the parameter
	 */
	public DatabaseEvent(final RequestType requestType, final String parameter) {
		this.requestType = requestType;
		this.parameter = parameter;
	}

	/**
	 * Gets the parameter.
	 *
	 * @return the parameter
	 */
	public String getParameter() {
		return this.parameter;
	}

	/**
	 * Gets the request type.
	 *
	 * @return the request type
	 */
	public RequestType getRequestType() {
		return this.requestType;
	}

	/**
	 * Sets the parameter.
	 *
	 * @param parameter
	 *            the new parameter
	 */
	public void setParameter(final String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Sets the request type.
	 *
	 * @param requestType
	 *            the new request type
	 */
	public void setRequestType(final RequestType requestType) {
		this.requestType = requestType;
	}

}
