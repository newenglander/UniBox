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
public class AsyncContextParser extends InternalConfig {

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
    private final AsyncContext context;

    /** The creation time. */
    private final long creationTime;

    /** The creation time string. */
    private final String creationTimeString;

    /** The id. */
    private final String id;

    /** The session. */
    private final HttpSession session;

    /**
     * Instantiates a new async context parser.
     *
     * @param thisContext
     *            the this context
     */
    public AsyncContextParser(final AsyncContext thisContext) {
        super();
        this.context = thisContext;
        final HttpServletRequest req = (HttpServletRequest) thisContext
                .getRequest();
        this.session = req.getSession();
        this.id = this.session.getId();
        this.creationTime = this.session.getCreationTime();
        this.creationTimeString = Helper.longToDate(this.session
                .getCreationTime());
        if (InternalConfig.LOG_ASYNC_SESSIONS) {
            InternalConfig.log.debug(AsyncContextParser.class.getSimpleName()
                    + ": add Context: " + thisContext);
            InternalConfig.log.debug(AsyncContextParser.class.getSimpleName()
                    + ": adding id=" + this.id + ", creationTime="
                    + this.creationTimeString + ", ac=" + this.context);
        }
        AsyncContextParser.list.add(this);
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
