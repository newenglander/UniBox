package de.unibox.client.example;

import javafx.event.EventHandler;
import de.unibox.client.events.CommunicationEvent;
import de.unibox.core.network.object.CommunicatorMessage;

/**
 * The Class MessageListenerExample.
 */
public class MessageListenerExample implements EventHandler<CommunicationEvent> {

    /*
     * (non-Javadoc)
     * 
     * @see javafx.event.EventHandler#handle(javafx.event.Event)
     */
    @Override
    public void handle(final CommunicationEvent event) {

        if (event.getEventType() == CommunicationEvent.INCOMING_MESSAGE) {
            final CommunicatorMessage cMessage = event.getMessageObject();
            System.out.println("MessageListenerExample: " + cMessage.getName()
                    + ": " + cMessage.getMessage());
        }

    }

}
