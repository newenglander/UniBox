package de.unibox.client.example;

import de.unibox.client.api.ClientProvider;

/**
 * The Class MessageSenderExample.
 */
public class MessageSenderExample {

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {

        ClientProvider.setUsername("user");
        ClientProvider.setPassword("user");
        ClientProvider.setUrl("http://localhost:7637/UniBox");

        ClientProvider.login();
        ClientProvider.connect();

        ClientProvider.sendChatMessage("chat message test");
        ClientProvider.sendGameMessage("game message test");
        ClientProvider.sendSystemMessage("system message test");
        ClientProvider.sendErrorMessage("error message test");

    }

}
