package de.unibox.http.servlet.comet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unibox.config.InternalConfig;

/**
 * The Class JavaScriptCommunicator.
 */
@WebServlet(urlPatterns = { "/Communicator/JavaScript" }, asyncSupported = true)
public class JavaScriptCommunicator extends Communicator {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1968748523068438047L;

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

        req.setAttribute("format", ClientType.JAVASCRIPT);

        super.thisUser.setSessionId(super.thisSession.getId());

        if (InternalConfig.LOG_AUTHENTIFICATION) {
            this.log.debug(JavaScriptCommunicator.class.getSimpleName()
                    + ": Get detected: " + super.thisUser);
        }

        final PrintWriter writer = res.getWriter();
        writer.flush();

        final AsyncContext ac = req.startAsync();
        ac.setTimeout(10 * 60 * 1000);
        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    JavaScriptCommunicator.this.log
                            .debug(JavaScriptCommunicator.class.getSimpleName()
                                    + " onComplete()");
                }
                Communicator.asyncContextQueue.remove(ac);
            }

            @Override
            public void onError(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    JavaScriptCommunicator.this.log
                            .debug(JavaScriptCommunicator.class.getSimpleName()
                                    + " onError()");
                }
                Communicator.asyncContextQueue.remove(ac);
            }

            @Override
            public void onStartAsync(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    JavaScriptCommunicator.this.log
                            .debug(JavaScriptCommunicator.class.getSimpleName()
                                    + " onStartAsync()");
                }
            }

            @Override
            public void onTimeout(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    JavaScriptCommunicator.this.log
                            .debug(JavaScriptCommunicator.class.getSimpleName()
                                    + " onTimeout()");
                }
                Communicator.asyncContextQueue.remove(ac);
            }
        });
        super.addContext(ac);
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

        req.setAttribute("format", ClientType.JAVASCRIPT);
        req.setCharacterEncoding("UTF-8");

        final String action = req.getParameter("action");

        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug(JavaScriptCommunicator.class.getSimpleName()
                    + ": Post detected: " + super.thisUser);
        }

        super.switchAction(req, res, action);
    }

}
