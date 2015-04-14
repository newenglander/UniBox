package de.unibox.http.servlet.type;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.core.provider.Helper;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.user.AbstractUser;
import de.unibox.model.user.AdministratorUser;
import de.unibox.model.user.RegisteredUser;
import de.unibox.model.user.UserFactory;
import de.unibox.model.user.UserType;

/**
 * The Class ProtectedHttpServlet.
 */
public class ProtectedHttpServlet extends HttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7033488871704480169L;

    /** The log. */
    protected Logger log = Logger.getLogger("UniBoxLogger");

    /**
     * Auth.
     *
     * @param request
     *            the request
     * @return true, if successful
     */
    protected boolean auth(final HttpServletRequest request) {

        // get session instance
        final HttpSession session = request.getSession();

        // no session is authed by default
        boolean isAuthed = false;

        // create an abstract user object
        AbstractUser user;
        if (InternalConfig.LOG_AUTHENTIFICATION) {
            this.log.debug(ProtectedHttpServlet.class.getSimpleName()
                    + ": session: " + session.getId() + " - login.object: "
                    + session.getAttribute("login.object"));
        }

        try {

            // retrieve user from session
            user = (AbstractUser) session.getAttribute("login.object");
            if ((user instanceof RegisteredUser)
                    || (user instanceof AdministratorUser)) {
                isAuthed = true;
                if (InternalConfig.LOG_AUTHENTIFICATION) {
                    this.log.debug(ProtectedHttpServlet.class.getSimpleName()
                            + ": access granted for " + user.toString());
                }
            } else {

                // retrieve user from database with given dataset
                final String nick = request.getParameter("nick");
                final String passwordBase64 = request.getParameter("password");

                if ((nick != null) && (passwordBase64 != null)) {

                    try {

                        if (!nick.isEmpty() && !passwordBase64.isEmpty()) {

                            final String passwordPlain = Helper
                                    .decodeBase64(passwordBase64);
                            final String password = Helper.md5(passwordPlain);

                            // high critical logging!!! log will contain
                            // password data..
                            if (InternalConfig.LOG_AUTHENTIFICATION) {
                                this.log.debug(ProtectedHttpServlet.class
                                        .getSimpleName()
                                        + ": Try to match Nick: '"
                                        + nick
                                        + "' MD5: '"
                                        + password
                                        + "' Base64: '"
                                        + passwordBase64 + "'");
                            }

                            final DatabaseQuery query = new DatabaseQuery();
                            query.connect();

                            final PreparedStatement statement = query
                                    .getQuery("SELECT * FROM player WHERE Name = ? AND Password = ?;");

                            statement.setString(1, nick);
                            statement.setString(2, password);

                            final ResultSet result = statement.executeQuery();

                            query.commit();

                            if (result.next()) {

                                final int isAdmin = result
                                        .getInt("AdminRights");
                                UserType thisUserType = null;
                                if (isAdmin == 1) {
                                    thisUserType = UserType.ADMINISTRATOR;
                                } else {
                                    thisUserType = UserType.REGISTERED;
                                }

                                user = UserFactory.createUser(thisUserType,
                                        session.getId());
                                user.setName(result.getString("Name"));
                                user.setPlayerId(result.getInt("PlayerID"));
                                user.setSessionId(session.getId());

                                if (InternalConfig.LOG_AUTHENTIFICATION) {
                                    this.log.debug(ProtectedHttpServlet.class
                                            .getSimpleName()
                                            + ": auth request for  "
                                            + user.getName()
                                            + " - ID: "
                                            + user.getSessionId()
                                            + " - Object: " + user);
                                }

                                session.setAttribute("login.object", user);
                                isAuthed = true;

                            } else {
                                if (InternalConfig.LOG_AUTHENTIFICATION) {
                                    this.log.debug(ProtectedHttpServlet.class
                                            .getSimpleName()
                                            + ": invalid credentials. could not auth request.");
                                }
                            }
                        } else {
                            if (InternalConfig.LOG_AUTHENTIFICATION) {
                                this.log.debug(ProtectedHttpServlet.class
                                        .getSimpleName()
                                        + ": invalid request detected. incomplete user/pw? ");
                            }
                        }

                    } catch (final NullPointerException e) {
                        if (InternalConfig.LOG_AUTHENTIFICATION) {
                            this.log.debug(ProtectedHttpServlet.class
                                    .getSimpleName()
                                    + ": Error. mySQL offline?");
                        }
                        e.printStackTrace();
                    }
                }
            }
        } catch (final Exception e) {
            if (InternalConfig.LOG_AUTHENTIFICATION) {
                this.log.debug(ProtectedHttpServlet.class.getSimpleName()
                        + ": transaction error. could not auth request.");
            }
            e.printStackTrace();
        }
        return isAuthed;
    }

    /**
     * Do service.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void doService(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        super.service(request, response);
    }

    /**
     * Invalid request.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void invalidRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("BAD_REQUEST");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse)
     */
    @Override
    public void service(final ServletRequest req, final ServletResponse res)
            throws ServletException, IOException {
        HttpServletRequest request;
        HttpServletResponse response;
        try {
            request = (HttpServletRequest) req;

            if (InternalConfig.LOG_REQUEST_HEADER) {
                final Enumeration<String> headerNames = request
                        .getHeaderNames();
                this.log.debug("RequestHeader:");
                while (headerNames.hasMoreElements()) {
                    final String headerName = headerNames.nextElement();
                    this.log.debug(headerName + ": "
                            + request.getHeader(headerName));
                }
            }

            response = (HttpServletResponse) res;

        } catch (final ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }
        if (this.auth(request)) {
            this.doService(request, response);
        } else {
            this.serviceDenied(request, response);
        }
    }

    /**
     * Service denied.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void serviceDenied(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        try {
            request.getRequestDispatcher("/login.html").include(request,
                    response);
        } catch (final ServletException e) {
            response.getWriter().write("Access forbidden!");
            e.printStackTrace();
        }
    }
}
