package de.unibox.http.servlet.comet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import de.unibox.config.InternalConfig;
import de.unibox.core.provider.Helper;
import de.unibox.core.provider.Reversed;

/**
 * The Class AsyncContextParser.
 */
public class AsyncContextParser {

    /** The list. */
    public static List<AsyncContextParser> list = null;

    /**
     * Inits the.
     */
    public static void init() {
        if (InternalConfig.LOG_ASYNC_SESSIONS) {
            InternalConfig.log.debug(AsyncContextParser.class.getSimpleName()
                    + ": init()");
        }
        AsyncContextParser.list = new ArrayList<AsyncContextParser>();
    }

    /**
     * Verify.
     */
    public static void verify() {

        final HashMap<String, Long> asyncSessions = new HashMap<String, Long>();

        for (final AsyncContextParser comSession : Reversed
                .reversed(AsyncContextParser.list)) {

            final AsyncContext ac = comSession.getContext();

            final String id = comSession.getId();
            final Long time = comSession.getSession().getCreationTime();

            if (asyncSessions.containsKey(id)
                    && asyncSessions.get(id).equals(time)) {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    InternalConfig.log.debug(AsyncContextParser.class
                            .getSimpleName()
                            + ": removing id="
                            + comSession.getId()
                            + ", creationTime="
                            + comSession.getCreationTimeString()
                            + ", ac="
                            + comSession.getContext());
                }
                Communicator.asyncContextQueue.remove(ac);
                ac.complete();
            } else {
                asyncSessions.put(id, time);
            }
        }
    }

    /** The context. */
    private AsyncContext context = null;

    /** The creation time. */
    private long creationTime = 0;

    /** The creation time string. */
    private String creationTimeString = null;

    /** The id. */
    private String id = null;

    /** The session. */
    private HttpSession session = null;

    /**
     * Instantiates a new async context parser.
     *
     * @param thisContext
     *            the this context
     */
    public AsyncContextParser(final AsyncContext thisContext) {
        super();
        this.context = thisContext;
        HttpServletRequest req = null;
        try {
            req = (HttpServletRequest) thisContext.getRequest();
        } catch (IllegalStateException e) {
            if (InternalConfig.LOG_ASYNC_SESSIONS) {
                e.printStackTrace();
            }
        }

        if (null != req) {
            this.session = req.getSession();
            this.id = this.session.getId();
            this.creationTime = this.session.getCreationTime();
            this.creationTimeString = Helper.longToDate(this.session
                    .getCreationTime());
            if (InternalConfig.LOG_ASYNC_SESSIONS) {
                InternalConfig.log.debug(AsyncContextParser.class
                        .getSimpleName() + ": add Context: " + thisContext);
                InternalConfig.log.debug(AsyncContextParser.class
                        .getSimpleName()
                        + ": adding id="
                        + this.id
                        + ", creationTime="
                        + this.creationTimeString
                        + ", ac="
                        + this.context);
            }
            AsyncContextParser.list.add(this);
        } else {
            if (InternalConfig.LOG_ASYNC_SESSIONS) {
                InternalConfig.log.debug(AsyncContextParser.class
                        .getSimpleName()
                        + ": illegal state for context detected: "
                        + thisContext);
            }
        }
    }

    public AsyncContext getContext() {
        return this.context;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public String getCreationTimeString() {
        return this.creationTimeString;
    }

    public String getId() {
        return this.id;
    }

    public HttpSession getSession() {
        return this.session;
    }

}
