package de.unibox.http.servlet.type;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.unibox.config.InternalConfig;
import de.unibox.model.user.AdministratorUser;

/**
 * The Class AdminHttpServlet is a Security Layer for Servlets which should
 * prevent these from non-administrative access.
 */
public class AdminHttpServlet extends ProtectedHttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8302172333071821716L;

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
    @Override
    protected void doService(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {

        if (null != super.thisUser) {

            if (InternalConfig.isLogAuthentification()) {
                this.log.debug(AdminHttpServlet.class.getSimpleName() + ": "
                        + super.thisUser.getName()
                        + " requesting AdminHttpServlet.");
            }

            if (super.thisUser instanceof AdministratorUser) {
                if (InternalConfig.isLogAuthentification()) {
                    this.log.debug(AdminHttpServlet.class.getSimpleName()
                            + ": Access granted for "
                            + super.thisUser.getName());
                }
                this.service(request, response);
            } else {
                if (InternalConfig.isLogAuthentification()) {
                    this.log.debug(AdminHttpServlet.class.getSimpleName()
                            + ": Access denied for " + super.thisUser.getName()
                            + ". Invalidate related session..");
                }
                final HttpSession session = request.getSession();
                session.invalidate();
                this.serviceDenied(request, response);
            }
        } else {
            if (InternalConfig.isLogAuthentification()) {
                this.log.debug(AdminHttpServlet.class.getSimpleName()
                        + ": Access denied, user object == null !");
            }
            this.serviceDenied(request, response);
        }
    }

}
