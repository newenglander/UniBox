package de.unibox.http.servlet.comet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.media.sound.InvalidFormatException;

import de.unibox.config.InternalConfig;
import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.network.object.CommunicatorMessage.MessageType;
import de.unibox.core.provider.Helper;
import de.unibox.core.provider.ObjectSerializerImpl;
import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.game.GamePool;
import de.unibox.model.user.AbstractUser;
import de.unibox.model.user.UserFactory;

/**
 * The Class Communicator.
 */
@WebServlet(urlPatterns = { "/Communicator" }, asyncSupported = true)
public class Communicator extends ProtectedHttpServlet {

    /**
     * The Enum ClientType.
     */
    public static enum ClientType {

        /** The javascript. */
        JAVASCRIPT,
        /** The plain. */
        PLAIN,
        /** The serial. */
        SERIAL
    }

    /** The Constant queue. */
    protected static final Queue<AsyncContext> asyncContextQueue = new ConcurrentLinkedQueue<AsyncContext>();

    /** The Constant messageQueue. */
    private static final BlockingQueue<CommunicatorMessage> messageQueue = new LinkedBlockingQueue<CommunicatorMessage>();

    public static BlockingQueue<CommunicatorMessage> getMessagequeue() {
        return messageQueue;
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2919167206889576860L;

    /** The notifier thread. */
    protected Thread notifierThread = null;

    /**
     * Adds the context.
     *
     * @param thisAc
     *            the this ac
     */
    protected void addContext(final AsyncContext thisAc) {
        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug(Communicator.class.getSimpleName()
                    + ": adding context: " + thisAc);
        }
        Communicator.asyncContextQueue.add(thisAc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy() {
        if (InternalConfig.LOG_THREADS) {
            this.log.debug("Communicator: destroyed");
        }
        Communicator.asyncContextQueue.clear();
        this.notifierThread.interrupt();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest req,
            final HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/html");
        res.setHeader("Cache-Control", "private");
        res.setHeader("Pragma", "no-cache");

        req.setAttribute("format", ClientType.PLAIN);

        final HttpSession session = req.getSession();
        final AbstractUser user = (AbstractUser) session
                .getAttribute("login.object");
        user.setSessionId(session.getId());

        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug("Communicator: Get detected: " + user);
        }

        final PrintWriter writer = res.getWriter();
        writer.flush();

        final AsyncContext ac = req.startAsync();
        ac.setTimeout(50);
        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    Communicator.this.log.debug("Communicator onComplete()");
                }
                Communicator.asyncContextQueue.remove(ac);
                ac.complete();
            }

            @Override
            public void onError(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    Communicator.this.log.debug("Communicator onError()");
                }
                Communicator.asyncContextQueue.remove(ac);
                ac.complete();
            }

            @Override
            public void onStartAsync(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    Communicator.this.log.debug("Communicator onStartAsync()");
                }
            }

            @Override
            public void onTimeout(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    Communicator.this.log.debug("Communicator onTimeout()");
                }
                Communicator.asyncContextQueue.remove(ac);
                ac.complete();
            }
        });
        this.addContext(ac);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest req,
            final HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/plain");
        res.setHeader("Cache-Control", "private");
        res.setHeader("Pragma", "no-cache");
        req.setAttribute("format", ClientType.PLAIN);
        req.setCharacterEncoding("UTF-8");

        final String action = req.getParameter("action");
        final HttpSession session = req.getSession();
        final AbstractUser user = (AbstractUser) session
                .getAttribute("login.object");

        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug("Communicator: Detected: POST, Action: " + action
                    + ", User: " + user);
        }

        final StringBuilder buffer = new StringBuilder();
        final BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        this.switchAction(req, res, user, action);
    }

    /**
     * Generate command.
     *
     * @param type
     *            the type
     * @param name
     *            the name
     * @param message
     *            the message
     * @return the communicator message
     */
    protected CommunicatorMessage generateCommand(final MessageType type,
            final String name, final String message) {
        return new CommunicatorMessage(type, name, message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        if (InternalConfig.LOG_THREADS) {
            this.log.debug("Communicator: deploy new thread");
        }

        final Runnable notifierRunnable = new Runnable() {
            @Override
            public void run() {

                boolean done = false;

                while (!done) {

                    CommunicatorMessage cMessage = null;

                    /**
                     * This is a simple helper class to find and remove unused
                     * contexts from the productive queue e.g. verify the used
                     * contexts.
                     */
                    AsyncContextParser.init();
                    for (final AsyncContext ac : Communicator.asyncContextQueue) {
                        new AsyncContextParser(ac);
                    }
                    AsyncContextParser.verify();

                    try {

                        cMessage = Communicator.messageQueue.take();

                        if (InternalConfig.LOG_ASYNC_SESSIONS) {
                            for (final AsyncContext ac : Communicator.asyncContextQueue) {
                                Communicator.this.log.debug(Communicator.class
                                        .getSimpleName() + ": " + ac);
                            }
                        }

                        for (final AsyncContext ac : Communicator.asyncContextQueue) {

                            try {

                                final HttpServletRequest req = (HttpServletRequest) ac
                                        .getRequest();

                                final ClientType remoteType = (ClientType) req
                                        .getAttribute("format");
                                final MessageType messageType = cMessage
                                        .getType();
                                final String sessionId = req.getSession()
                                        .getId();
                                final AbstractUser receiver = (AbstractUser) req
                                        .getSession().getAttribute(
                                                "login.object");

                                final AbstractUser sender = UserFactory
                                        .getUserByName(cMessage.getName());

                                final PrintWriter acWriter = ac.getResponse()
                                        .getWriter();

                                if (InternalConfig.LOG_COMMUNICATION) {
                                    Communicator.this.log
                                            .debug("Communicator run(): ClientType="
                                                    + remoteType
                                                    + ", MessageType="
                                                    + messageType
                                                    + ", session=" + sessionId);
                                }

                                switch (remoteType) {
                                case JAVASCRIPT:
                                    // ignore GAME messages for webinterface
                                    if (messageType == MessageType.JS_Command) {
                                        if (receiver.getName().equals(
                                                cMessage.getName())) {
                                            if (InternalConfig.LOG_COMMUNICATION) {
                                                Communicator.this.log
                                                        .debug("Communicator run(): NOT REFLECTING JS_COMMAND: ClientType="
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
                                        this.send(acWriter,
                                                cMessage.toJavaScript());
                                    }
                                    break;
                                case SERIAL:
                                    // ignore CHAT messages for java clients
                                    if (messageType != MessageType.CHAT) {

                                        // decide if message is relevant for
                                        // this client
                                        final boolean isParticipant = GamePool
                                                .getInstance().areMembers(
                                                        sender, receiver);

                                        // send message
                                        if (isParticipant) {
                                            this.send(
                                                    acWriter,
                                                    ObjectSerializerImpl
                                                            .objectToString(cMessage));
                                        } else {
                                            // message belongs NOT to this
                                            // context
                                            if (InternalConfig.LOG_COMMUNICATION) {
                                                Communicator.this.log
                                                        .debug("Communicator skipped message from "
                                                                + cMessage
                                                                        .getName()
                                                                + " for "
                                                                + receiver
                                                                        .getName());
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
                                        Communicator.this.log
                                                .warn("Typeless message detected: "
                                                        + cMessage.toString());
                                        Communicator.this.log
                                                .debug("Communicator: Typeless message detected: "
                                                        + cMessage.toString());
                                    }
                                    break;
                                }

                            } catch (final IOException e1) {
                                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                                    Communicator.this.log
                                            .debug("aCommunicator: response already committed. removing: "
                                                    + ac);
                                }
                                Communicator.asyncContextQueue.remove(ac);
                                e1.printStackTrace();
                            } catch (final IllegalStateException e2) {
                                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                                    Communicator.this.log
                                            .debug("Communicator: catched IllegalStateException. Response already committed. removing: "
                                                    + ac);
                                }
                                Communicator.asyncContextQueue.remove(ac);
                                // e2.printStackTrace();
                            }
                        }
                    } catch (final InterruptedException e2) {
                        done = true;
                        if (InternalConfig.LOG_THREADS) {
                            Communicator.this.log
                                    .warn("Communicator shutdown..");
                        }
                        e2.printStackTrace();
                    }
                }
            }

            private void send(final PrintWriter acWriter,
                    final String concreteMessage) {
                if (InternalConfig.LOG_COMMUNICATION) {
                    Communicator.this.log.debug("Communicator says: "
                            + concreteMessage);
                }
                acWriter.println(concreteMessage);
                acWriter.flush();
            }
        };
        this.notifierThread = new Thread(notifierRunnable);
        this.notifierThread.start();
    }

    /**
     * Notify.
     *
     * @param cMessage
     *            the c message
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void notify(final CommunicatorMessage cMessage)
            throws IOException {
        try {
            Communicator.messageQueue.put(cMessage);
        } catch (final Exception ex) {
            final IOException t = new IOException();
            t.initCause(ex);
            throw t;
        }
    }

    /**
     * Switch action.
     *
     * @param req
     *            the req
     * @param res
     *            the res
     * @param user
     *            the user
     * @param action
     *            the action
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void switchAction(final HttpServletRequest req,
            final HttpServletResponse res, final AbstractUser user,
            final String action) throws ServletException, IOException {

        if ("connect".equals(action)) {

            final CommunicatorMessage notifyMessage = this.generateCommand(
                    MessageType.SYSTEM, this.getClass().getSimpleName(),
                    Helper.encodeBase64(user.getName() + " has joined."));
            this.notify(notifyMessage);

            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().println("success");

        } else if ("post".equals(action)) {

            final String messageString = req.getParameter("message");
            CommunicatorMessage cMessage = null;

            try {

                final ClientType clientType = (ClientType) req
                        .getAttribute("format");

                if (InternalConfig.LOG_COMMUNICATION) {
                    this.log.debug("Communicator switchAction: " + clientType);
                }

                switch (clientType) {
                case JAVASCRIPT:
                    cMessage = this.generateCommand(MessageType.CHAT,
                            user.getName(), messageString);
                    break;
                case SERIAL:
                    cMessage = ObjectSerializerImpl.stringToObject(
                            messageString, CommunicatorMessage.class);
                    break;
                case PLAIN:
                    if (InternalConfig.LOG_COMMUNICATION) {
                        this.log.warn("UNHANDLED PLAIN MESSAGE: "
                                + user.getName() + ": " + messageString);
                    }
                    break;
                default:
                    if (InternalConfig.LOG_COMMUNICATION) {
                        this.log.warn("Communicator suppressing default message: "
                                + messageString);
                    }
                    throw new InvalidFormatException(
                            "incoming message switched to default type!");
                }

            } catch (final Exception e) {
                res.sendError(422, "Unprocessable Entity");
                e.printStackTrace();
            } finally {
                if (InternalConfig.LOG_COMMUNICATION) {
                    this.log.debug("Recieving: " + cMessage);
                }
                this.notify(cMessage);
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().println("success");
            }

        } else {

            res.sendError(422, "Unprocessable Entity");

        }
    }
}