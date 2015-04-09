package de.unibox.core.network.object;

/**
 * The Interface Messenger.
 */
public interface Messenger {

    /**
     * Receive.
     *
     * @return the communicator message
     */
    public CommunicatorMessage receive();

    /**
     * Send.
     *
     * @param message
     *            the message
     */
    public void send(CommunicatorMessage message);

}
