package de.unibox.http.servlet.comet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.network.object.CommunicatorMessage.MessageType;
import de.unibox.core.provider.ObjectSerializerImpl;
import de.unibox.http.servlet.comet.Communicator.ClientType;
import de.unibox.model.game.GamePool;
import de.unibox.model.user.AbstractUser;
import de.unibox.model.user.UserFactory;

/**
 * The Class RunnableNotifier.
 */
public class RunnableNotifier implements Runnable {

    /** The log. */
    protected Logger log = Logger.getLogger("UniBoxLogger");

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        boolean done = false;

        while (!done) {

            CommunicatorMessage cMessage = null;

            /**
             * This is a simple helper class to find and remove unused contexts
             * from the productive queue e.g. verify the used contexts.
             */
            AsyncContextParser.init();
            for (final AsyncContext ac : Communicator.asyncContextQueue) {
                new AsyncContextParser(ac);
            }
            AsyncContextParser.verify();

            try {

                cMessage = Communicator.getMessagequeue().take();

                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    for (final AsyncContext ac : Communicator.asyncContextQueue) {
                        this.log.debug(Communicator.class.getSimpleName()
                                + ": " + ac);
                    }
                }

                for (final AsyncContext ac : Communicator.asyncContextQueue) {

                    try {

                        final HttpServletRequest req = (HttpServletRequest) ac
                                .getRequest();

                        final ClientType remoteType = (ClientType) req
                                .getAttribute("format");
                        final MessageType messageType = cMessage.getType();
                        final String sessionId = req.getSession().getId();
                        final AbstractUser receiver = (AbstractUser) req
                                .getSession().getAttribute("login.object");

                        final AbstractUser sender = UserFactory
                                .getUserByName(cMessage.getName());

                        final PrintWriter acWriter = ac.getResponse()
                                .getWriter();

                        if (InternalConfig.LOG_COMMUNICATION) {
                            this.log.debug(RunnableNotifier.class
                                    .getSimpleName()
                                    + " run(): ClientType="
                                    + remoteType
                                    + ", MessageType="
                                    + messageType + ", session=" + sessionId);
                        }

                        switch (remoteType) {
                        case JAVASCRIPT:
                            // ignore GAME messages for webinterface
                            if (messageType == MessageType.JS_COMMAND) {
                                if (receiver.getName().equals(
                                        cMessage.getName())) {
                                    if (InternalConfig.LOG_COMMUNICATION) {
                                        RunnableNotifier.this.log
                                                .debug(Communicator.class
                                                        .getSimpleName()
                                                        + " run(): NOT REFLECTING JS_COMMAND: ClientType="
                                                        + remoteType
                                                        + ", MessageType="
                                                        + messageType
                                                        + ", session="
                                                        + sessionId);
                                    }
                                    break;
                                }
                            }
                            if (messageType != MessageType.GAME) {
                                this.send(acWriter, cMessage.toJavaScript());
                            }
                            break;
                        case SERIAL:
                            // ignore CHAT messages for java clients
                            if (messageType != MessageType.CHAT) {

                                // decide if message is relevant for
                                // this client
                                final boolean isParticipant = GamePool
                                        .getInstance().areMembers(sender,
                                                receiver);

                                // send message
                                if (isParticipant) {
                                    this.send(acWriter, ObjectSerializerImpl
                                            .objectToString(cMessage));
                                } else {
                                    // message belongs NOT to this
                                    // context
                                    if (InternalConfig.LOG_COMMUNICATION) {
                                        this.log.debug(Communicator.class
                                                .getSimpleName()
                                                + " skipped message from "
                                                + cMessage.getName()
                                                + " for "
                                                + receiver.getName());
                                    }
                                    continue;
                                }
                            }
                            break;
                        case PLAIN:
                            // broadcast plain messages strictly
                            this.send(acWriter, cMessage.toString());
                            break;
                        default:
                            if (InternalConfig.LOG_COMMUNICATION) {
                                RunnableNotifier.this.log
                                        .warn(Communicator.class
                                                .getSimpleName()
                                                + "Typeless message detected: "
                                                + cMessage.toString());
                                RunnableNotifier.this.log
                                        .debug(Communicator.class
                                                .getSimpleName()
                                                + ": Typeless message detected: "
                                                + cMessage.toString());
                            }
                            break;
                        }

                    } catch (final IOException e1) {
                        if (InternalConfig.LOG_ASYNC_SESSIONS) {
                            RunnableNotifier.this.log
                                    .debug(Communicator.class.getSimpleName()
                                            + ": response already committed. removing: "
                                            + ac);
                        }
                        Communicator.asyncContextQueue.remove(ac);
                        e1.printStackTrace();
                    } catch (final IllegalStateException e2) {
                        if (InternalConfig.LOG_ASYNC_SESSIONS) {
                            RunnableNotifier.this.log
                                    .debug(Communicator.class.getSimpleName()
                                            + ": catched IllegalStateException. Response already committed. removing: "
                                            + ac);
                        }
                        Communicator.getAsyncContextQueue().remove(ac);
                        // e2.printStackTrace();
                    }
                }
            } catch (final InterruptedException e2) {
                done = true;
                if (InternalConfig.LOG_THREADS) {
                    this.log.warn(Communicator.class.getSimpleName()
                            + " shutdown and rebooting..");
                    e2.printStackTrace();
                }
                this.run();
            }
        }
    }

    /**
     * Send.
     *
     * @param acWriter
     *            the ac writer
     * @param concreteMessage
     *            the concrete message
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void send(final PrintWriter acWriter, final String concreteMessage)
            throws IOException {
        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug(Communicator.class.getSimpleName() + " says: "
                    + concreteMessage);
        }
        acWriter.println(concreteMessage);
        /**
         * if you got a SocketException traced to this point after logout and
         * login in a short period of time, read this:
         * https://bz.apache.org/bugzilla/show_bug.cgi?id=57683
         *
         * It depends on your Tomcat version and can be ignored.
         */
        acWriter.flush();
    }

}
