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
import de.unibox.core.provider.Helper;
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
        case "changePassword":
            if (InternalConfig.LOG_AUTHENTIFICATION) {
                this.log.debug(this.getClass().getSimpleName()
                        + ": Change password for " + user.getName());
            }

            // TODO change password in database

            String nick = user.getName();
            // String passwordBase64old = request.getParameter("oldPassword");
            String passwordBase64 = request.getParameter("inputPassword");
            // String passwordBase64confirm = request
            // .getParameter("inputPasswordConfirm");

            DatabaseAction<Integer> query = null;

            if (!nick.isEmpty() && !passwordBase64.isEmpty()) {
                final String passwordPlain = Helper
                        .decodeBase64(passwordBase64);
                final String password = Helper.md5(passwordPlain);
                System.out.println("CHANGE_PW:" + password);
                query = new PasswordUpdate(nick, password);
            }

            int affectedRows = 0;
            response.setContentType("text/html");
            final PrintWriter out = response.getWriter();

            try {

                final DatabaseQuery transaction = new DatabaseQuery();
                transaction.connect();
                query.attach(transaction);
                affectedRows = query.execute();

                if (affectedRows == 1) {
                    out.print("success");
                    transaction.commit();
                } else {
                    out.print("failed");
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
        this.doGet(request, response);
    }

}
