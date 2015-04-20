package de.unibox.http.servlet.comet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.media.sound.InvalidFormatException;

import de.unibox.config.InternalConfig;
import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.network.object.CommunicatorMessage.MessageType;
import de.unibox.core.provider.Helper;
import de.unibox.core.provider.ObjectSerializerImpl;
import de.unibox.http.servlet.type.ProtectedHttpServlet;

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

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2919167206889576860L;

    /**
     * Gets the Constant queue.
     *
     * @return the Constant queue
     */
    public static Queue<AsyncContext> getAsyncContextQueue() {
        return Communicator.asyncContextQueue;
    }

    public static BlockingQueue<CommunicatorMessage> getMessagequeue() {
        return Communicator.messageQueue;
    }

    /** The keep alive service. */
    protected ScheduledExecutorService keepAliveService = null;

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
            this.log.debug(this.getClass().getSimpleName() + ": destroyed");
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

        req.setAttribute("format", ClientType.PLAIN);
        super.thisUser.setSessionId(super.thisSession.getId());

        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug(Communicator.class.getSimpleName()
                    + ": Get detected: " + super.thisUser);
        }

        final PrintWriter writer = res.getWriter();
        writer.flush();

        final AsyncContext ac = req.startAsync();
        ac.setTimeout(50);
        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    Communicator.this.log.debug(Communicator.class
                            .getSimpleName() + " onComplete()");
                }
                Communicator.asyncContextQueue.remove(ac);
                ac.complete();
            }

            @Override
            public void onError(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    Communicator.this.log.debug(Communicator.class
                            .getSimpleName() + " onError()");
                }
                Communicator.asyncContextQueue.remove(ac);
                ac.complete();
            }

            @Override
            public void onStartAsync(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    Communicator.this.log.debug(Communicator.class
                            .getSimpleName() + " onStartAsync()");
                }
            }

            @Override
            public void onTimeout(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    Communicator.this.log.debug(Communicator.class
                            .getSimpleName() + " onTimeout()");
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
        req.setAttribute("format", ClientType.PLAIN);
        req.setCharacterEncoding("UTF-8");

        final String action = req.getParameter("action");

        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug(Communicator.class.getSimpleName()
                    + ": Detected: POST, Action: " + action + ", User: "
                    + super.thisUser);
        }

        final StringBuilder buffer = new StringBuilder();
        final BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        this.switchAction(req, res, action);
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
            this.log.debug(Communicator.class.getSimpleName()
                    + ": deploy new thread");
        }

        this.notifierThread = new Thread(new RunnableNotifier());
        this.notifierThread.start();

        this.keepAliveService = Executors.newSingleThreadScheduledExecutor();
        this.keepAliveService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Communicator.getMessagequeue().add(
                        new CommunicatorMessage(MessageType.PING, "ALL", "."));
            }
        }, 0, 60, TimeUnit.SECONDS);

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
     * @param action
     *            the action
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void switchAction(final HttpServletRequest req,
            final HttpServletResponse res, final String action)
            throws ServletException, IOException {

        if ("connect".equals(action)) {

            final CommunicatorMessage notifyMessage = this.generateCommand(
                    MessageType.SYSTEM,
                    "SYSTEM",
                    Helper.encodeBase64(super.thisUser.getName()
                            + " has joined."));
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
                    this.log.debug(Communicator.class.getSimpleName()
                            + " switchAction: " + clientType);
                }

                switch (clientType) {
                case JAVASCRIPT:
                    cMessage = this.generateCommand(MessageType.CHAT,
                            super.thisUser.getName(), messageString);
                    break;
                case SERIAL:
                    cMessage = ObjectSerializerImpl.stringToObject(
                            messageString, CommunicatorMessage.class);
                    break;
                case PLAIN:
                    if (InternalConfig.LOG_COMMUNICATION) {
                        this.log.warn(Communicator.class.getSimpleName()
                                + ": UNHANDLED PLAIN MESSAGE: "
                                + super.thisUser.getName() + ": "
                                + messageString);
                    }
                    break;
                default:
                    if (InternalConfig.LOG_COMMUNICATION) {
                        this.log.warn(Communicator.class.getSimpleName()
                                + " suppressing default message: "
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
                    this.log.debug(Communicator.class.getSimpleName()
                            + ": Recieving: " + cMessage);
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