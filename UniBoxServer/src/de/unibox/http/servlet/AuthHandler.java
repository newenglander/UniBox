package de.unibox.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.unibox.config.InternalConfig;
import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.database.objects.PasswordUpdate;
import de.unibox.model.user.AbstractUser;

/**
 * The Class LogoutHandler.
 */
@WebServlet("/Auth")
public class AuthHandler extends ProtectedHttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1482513223695465763L;

    /**
     * Instantiates a new auth handler.
     */
    public AuthHandler() {
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

        final HttpSession session = request.getSession();
        final AbstractUser user = (AbstractUser) session
                .getAttribute("login.object");
        final String action = request.getParameter("action");

        switch (action) {
        case "logout":
            if (InternalConfig.LOG_AUTHENTIFICATION) {
                this.log.debug(this.getClass().getSimpleName()
                        + ": Invalidate session for " + user.getName());
            }
            final RequestDispatcher rd = request
                    .getRequestDispatcher("/login.html");

            session.invalidate();

            rd.forward(request, response);
            break;
        default:
            break;
        }

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

        final HttpSession session = request.getSession();
        final AbstractUser user = (AbstractUser) session
                .getAttribute("login.object");
        final String action = request.getParameter("action");

        switch (action) {
        case "changePassword":
            if (InternalConfig.LOG_AUTHENTIFICATION) {
                this.log.debug(this.getClass().getSimpleName()
                        + ": Change password for " + user.getName());
            }

            final Integer userId = user.getPlayerId();
            final String oldPassword = request.getParameter("oldPassword");
            final String inputPassword = request.getParameter("inputPassword");
            final String inputPasswordConfirm = request
                    .getParameter("inputPasswordConfirm");
            
            if (userId != null && oldPassword != null && inputPassword != null
                    && inputPasswordConfirm != null) {

                DatabaseAction<Integer> query = null;

                query = new PasswordUpdate(userId, oldPassword, inputPassword,
                        inputPasswordConfirm);

                int affectedRows = 0;
                response.setContentType("text/html");
                final PrintWriter out = response.getWriter();

                try {

                    final DatabaseQuery transaction = new DatabaseQuery();
                    transaction.connect();
                    query.attach(transaction);
                    affectedRows = query.execute();

                    System.out.println("AFF:" + affectedRows);

                    if (affectedRows == 1) {
                        out.print("success");
                        response.setStatus(HttpServletResponse.SC_OK);
                        transaction.commit();
                    } else {
                        out.print("failed");
                        response.setStatus(HttpServletResponse.SC_OK);
                        transaction.rollback();
                    }

                } catch (final SQLException e) {

                    if (InternalConfig.LOG_DATABASE) {
                        this.log.debug("GameHandler: Could not update database: "
                                + query.getSqlString());
                    }
                    e.printStackTrace();
                }

                out.flush();
                out.close();

            } else {
                super.invalidRequest(request, response);
            }
            break;
        default:
            break;
        }

    }

}
