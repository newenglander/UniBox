package de.unibox.client.thread.worker;

import de.unibox.client.api.ClientProvider;
import de.unibox.client.events.CommunicationEvent;
import de.unibox.client.thread.implementation.ThreadTaskImpl;
import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.provider.Helper;

/**
 * The Class RunnableMessageMediator is a Worker to handle incoming messages
 * queued in the ClientProvider.
 */
public class RunnableMessageMediator extends ThreadTaskImpl {

    /** The enabled. */
    public boolean enabled = true;

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public synchronized final boolean isEnabled() {
        return this.enabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.unibox.client.thread.implementation.ThreadTaskImpl#process()
     */
    @Override
    protected void process() {
        try {
            ThreadTaskImpl.log.debug(RunnableMessageMediator.class
                    .getSimpleName() + ": running..");
            while (true) {
                ThreadTaskImpl.log.debug(RunnableMessageMediator.class
                        .getSimpleName() + ": take()");
                final CommunicatorMessage cMessage = ClientProvider
                        .getNextIncomingMessage();
                // decode message first
                ThreadTaskImpl.log.debug(RunnableMessageMediator.class
                        .getSimpleName()
                        + ": encoded message: "
                        + cMessage.getMessage());
                cMessage.setMessage(Helper.decodeBase64(cMessage.getMessage()));
                ThreadTaskImpl.log.debug(RunnableMessageMediator.class
                        .getSimpleName()
                        + ": decoded message: "
                        + cMessage.getMessage());
                // promote
                CommunicationEvent.promote(cMessage);
            }
        } catch (final InterruptedException e) {
            ThreadTaskImpl.log.warn(RunnableMessageMediator.class
                    .getSimpleName() + ": fatal error!");
            e.printStackTrace();
        } finally {
            ThreadTaskImpl.log.debug(RunnableMessageMediator.class
                    .getSimpleName() + ": done. Rebooting..");
            this.process();
        }
    }

    /**
     * Sets the enabled.
     *
     * @param enabled
     *            the new enabled
     */
    public synchronized final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

}
