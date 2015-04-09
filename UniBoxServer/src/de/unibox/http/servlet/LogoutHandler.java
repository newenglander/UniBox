package de.unibox.http.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.unibox.config.InternalConfig;
import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.user.AbstractUser;

/**
 * The Class LogoutHandler.
 */
@WebServlet("/Logout")
public class LogoutHandler extends ProtectedHttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1482513223695465763L;

    /**
     * Instantiates a new logout handler.
     */
    public LogoutHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        final RequestDispatcher rd = request
                .getRequestDispatcher("/login.html");
        final HttpSession session = request.getSession();
        final AbstractUser user = (AbstractUser) session
                .getAttribute("login.object");

        if (InternalConfig.LOG_AUTHENTIFICATION) {
            this.log.debug("LogoutHandler: Invalidate session for "
                    + user.getName());
        }

        session.invalidate();
        rd.forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        this.doGet(request, response);
    }

}
