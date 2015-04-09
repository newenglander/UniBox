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
import javax.servlet.http.HttpSession;

import de.unibox.config.InternalConfig;
import de.unibox.model.user.AbstractUser;

/**
 * The Class SerialCommunicator.
 */
@WebServlet(urlPatterns = { "/Communicator/Serial" }, asyncSupported = true)
public class SerialCommunicator extends Communicator {

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
        res.setContentType("text/html");
        res.setHeader("Cache-Control", "private");
        res.setHeader("Pragma", "no-cache");

        req.setAttribute("format", ClientType.SERIAL);

        final HttpSession session = req.getSession();
        final AbstractUser user = (AbstractUser) session
                .getAttribute("login.object");
        user.setSessionId(session.getId());

        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug("SerialCommunicator: Get detected: " + user);
        }

        final PrintWriter writer = res.getWriter();
        writer.flush();

        final AsyncContext ac = req.startAsync();
        ac.setTimeout(10 * 60 * 1000);
        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    SerialCommunicator.this.log
                            .debug("SerialCommunicator onComplete()");
                }
                Communicator.asyncContextQueue.remove(ac);
            }

            @Override
            public void onError(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    SerialCommunicator.this.log
                            .debug("SerialCommunicator onError()");
                }
                Communicator.asyncContextQueue.remove(ac);
            }

            @Override
            public void onStartAsync(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    SerialCommunicator.this.log
                            .debug("SerialCommunicator onStartAsync()");
                }
            }

            @Override
            public void onTimeout(final AsyncEvent event) throws IOException {
                if (InternalConfig.LOG_ASYNC_SESSIONS) {
                    SerialCommunicator.this.log
                            .debug("SerialCommunicator onTimeout()");
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

        res.setContentType("text/plain");
        res.setHeader("Cache-Control", "private");
        res.setHeader("Pragma", "no-cache");
        req.setAttribute("format", ClientType.SERIAL);
        req.setCharacterEncoding("UTF-8");

        final String action = req.getParameter("action");
        final HttpSession session = req.getSession();
        final AbstractUser user = (AbstractUser) session
                .getAttribute("login.object");

        if (InternalConfig.LOG_COMMUNICATION) {
            this.log.debug("SerialCommunicator: Post detected: " + user);
        }

        super.switchAction(req, res, user, action);
    }
}
