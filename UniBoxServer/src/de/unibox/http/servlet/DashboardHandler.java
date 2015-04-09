package de.unibox.http.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unibox.http.servlet.beans.AdminBean;
import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.user.AbstractUser;
import de.unibox.model.user.AdministratorUser;

/**
 * The Class DashboardHandler.
 */
@WebServlet("/Dashboard")
public class DashboardHandler extends ProtectedHttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3278754155030900080L;

    /**
     * Instantiates a new dashboard handler.
     */
    public DashboardHandler() {
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

        response.setContentType("text/html");

        final AbstractUser user = (AbstractUser) request.getSession()
                .getAttribute("login.object");

        if (user instanceof AdministratorUser) {
            Object adminObj = request.getSession().getAttribute("admin");
            AdminBean adminBean = null;
            if (adminObj != null && adminObj instanceof AdminBean) {
                adminBean = (AdminBean) adminBean;
            } else {
                adminBean = new AdminBean();
                request.getSession().setAttribute("admin", adminBean);
                adminBean.setAdminMenu("<li><a class='whiteText' id='triggerAdmin' href='#'>Administration</a></li>");
            }
        }

        final RequestDispatcher rd = request
                .getRequestDispatcher("/dashboard.jsp");
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
