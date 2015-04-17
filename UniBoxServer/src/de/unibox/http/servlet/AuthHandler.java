package de.unibox.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unibox.config.InternalConfig;
import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.database.objects.PasswordUpdate;

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
        
        final String action = request.getParameter("action");

        switch (action) {
        case "logout":
            if (InternalConfig.LOG_AUTHENTIFICATION) {
                this.log.debug(this.getClass().getSimpleName()
                        + ": Invalidate session for "
                        + super.thisUser.getName());
            }
            final RequestDispatcher rd = request
                    .getRequestDispatcher("/login.html");

            request.getSession().invalidate();

            response.setStatus(HttpServletResponse.SC_OK);
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

        final String action = request.getParameter("action");

        switch (action) {
        case "changePassword":
            if (InternalConfig.LOG_AUTHENTIFICATION) {
                this.log.debug(this.getClass().getSimpleName()
                        + ": Change password for " + super.thisUser.getName());
            }

            final Integer userId = super.thisUser.getPlayerId();
            final String oldPassword64 = request.getParameter("oldPassword");
            final String inputPassword64 = request
                    .getParameter("inputPassword");
            final String inputPasswordConfirm64 = request
                    .getParameter("inputPasswordConfirm");

            if ((userId != null) && (oldPassword64 != null)
                    && (inputPassword64 != null)
                    && (inputPasswordConfirm64 != null)) {

                DatabaseAction<Integer> query = null;

                query = new PasswordUpdate(userId, oldPassword64,
                        inputPassword64, inputPasswordConfirm64);

                int affectedRows = 0;
                response.setContentType("text/html");
                final PrintWriter out = response.getWriter();

                try {

                    final DatabaseQuery transaction = new DatabaseQuery();
                    transaction.connect();
                    query.attach(transaction);
                    affectedRows = query.execute();

                    if (affectedRows == 1) {
                        if (InternalConfig.LOG_AUTHENTIFICATION) {
                            this.log.debug(this.getClass().getSimpleName()
                                    + " change password succesfull. Affected rows: "
                                    + affectedRows);
                        }
                        out.print("success");
                        response.setStatus(HttpServletResponse.SC_OK);
                        transaction.commit();
                    } else {
                        if (InternalConfig.LOG_AUTHENTIFICATION) {
                            this.log.debug(this.getClass().getSimpleName()
                                    + " change password failed. Affected rows: "
                                    + affectedRows);
                        }
                        out.print("failed");
                        response.setStatus(HttpServletResponse.SC_OK);
                        transaction.rollback();
                    }

                } catch (final SQLException e) {

                    if (InternalConfig.LOG_DATABASE) {
                        this.log.debug(this.getClass().getSimpleName() + ": Could not update database: "
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
